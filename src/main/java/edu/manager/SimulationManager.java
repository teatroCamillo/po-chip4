package edu.manager;

import edu.model.pin.AbstractPin;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;
import edu.uj.po.simulation.interfaces.ComponentPinState;
import edu.uj.po.simulation.interfaces.SimulationAndOptimization;
import edu.uj.po.simulation.interfaces.UnknownStateException;
import edu.model.Chip;
import edu.model.chip.HeaderIn;
import edu.model.chip.HeaderOut;
import edu.util.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimulationManager implements Component, SimulationAndOptimization {

	private final ComponentManager componentManager;

	public SimulationManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	@Override
	public void simulate() {
		System.out.println("simulate from SimulationManager");
	}

	@Override
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException {
		setHeadersInPins(states);

		validateHeadersIn();
		componentManager.propagateSignal();

		Set<ComponentPinState> previousState;
		Set<ComponentPinState> currentState;
		currentState = Util.saveCircuitState(componentManager.chips);
		do {
			previousState = new HashSet<>(currentState);

			componentManager.chips.values().forEach(Chip::simulate);
			componentManager.propagateSignal();

			currentState.clear();
			currentState = Util.saveCircuitState(componentManager.chips);
		} while (!previousState.equals(currentState));

		boolean isHeaderOut = componentManager.chips.values()
				.stream()
				.anyMatch(chip -> chip instanceof HeaderOut);
		if(isHeaderOut) validateHeadersOut();
	}

	public void setHeadersInPins(Set<ComponentPinState> states) {
		for (ComponentPinState state : states) {
			Chip chip = componentManager.chips.get(state.componentId());
			AbstractPin pin = chip.getPinMap().get(state.pinId());
			pin.setPinState(state.state());
		}
	}

	private void validateHeadersIn() throws UnknownStateException {
		Map<Integer, Chip> headerChips = componentManager.chips.entrySet().stream()
				.filter(entry -> entry.getValue() instanceof HeaderIn)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		for (Map.Entry<Integer, Chip> entry : headerChips.entrySet()) {
			int chipId = entry.getKey();
			Chip chip = entry.getValue();

			for (Map.Entry<Integer, AbstractPin> entryPin : chip.getPinMap().entrySet()) {
				int pinId = entryPin.getKey();
				AbstractPin pin = entryPin.getValue();
				if (!isPinOutConnected(pin))
					throw new UnknownStateException(new ComponentPinState(chipId, pinId, pin.getPinState()));
			}
		}
	}

	private void validateHeadersOut() throws UnknownStateException {
		Map<Integer, Chip> headerChips = componentManager.chips.entrySet().stream()
				.filter(entry -> entry.getValue().getClass().getSimpleName().equals(Util.HEADER_OUT))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		for (Map.Entry<Integer, Chip> entry : headerChips.entrySet()) {
			int chipId = entry.getKey();
			Chip chip = entry.getValue();

			for (Map.Entry<Integer, AbstractPin> entryPin : chip.getPinMap().entrySet()) {
				int pinId = entryPin.getKey();
				AbstractPin pin = entryPin.getValue();
				if (!isPinInConnected(pin))
					throw new UnknownStateException(new ComponentPinState(chipId, pinId, pin.getPinState()));
			}
		}
	}

	private boolean isPinInConnected(AbstractPin pin) {
		if(pin instanceof PinOut) throw new RuntimeException();
		return componentManager.isPinConnected(pin);
	}

	private boolean isPinOutConnected(AbstractPin pin) {
		if(pin instanceof PinIn) throw new RuntimeException();
		return componentManager.isPinConnected(pin);
	}

	@Override
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0,
														   int ticks) throws UnknownStateException{
		setHeadersInPins(states0);
		componentManager.propagateSignal();

		Map<Integer, Set<ComponentPinState>> resultMap = new HashMap<>();
		Set<ComponentPinState> currentState;
		currentState = Util.saveCircuitHeaderOutState(componentManager.chips);
		resultMap.put(0, new HashSet<>(currentState));

		for(int i=1; i<=ticks; i++){
			componentManager.chips.values().forEach(Chip::simulate);
			componentManager.propagateSignal();

			currentState.clear();
			currentState = Util.saveCircuitHeaderOutState(componentManager.chips);
			resultMap.put(i, new HashSet<>(currentState));
		}
		return resultMap;
	}

	@Override
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException {
		Set<Integer> componentsToRemove = new HashSet<>();
		Set<ComponentPinState> stationaryInput = componentManager.chips.entrySet().stream()
				.filter(entry -> entry.getValue() instanceof HeaderIn)
				.flatMap(entry -> entry.getValue().getPinMap().entrySet().stream()
						.map(pinEntry -> new ComponentPinState(entry.getKey(), pinEntry.getKey(), pinEntry.getValue().getPinState()))
				)
				.collect(Collectors.toSet());

		Map<Integer, Set<ComponentPinState>> normalSimulationResul = simulation(states0, ticks);
		stationaryState(stationaryInput);

		for (Chip component :
				componentManager.chips.values().stream().filter(chip -> !(chip instanceof HeaderIn || chip instanceof HeaderOut)).collect(
						Collectors.toSet())) {
			component.setOn(false);

			Map<Integer, Set<ComponentPinState>> modifiedSimulation = simulation(states0, ticks);

			if (normalSimulationResul.get(ticks).equals(modifiedSimulation.get(ticks)))
				componentsToRemove.add(component.getChipId());

			component.setOn(true);
			stationaryState(stationaryInput);
		}
		return componentsToRemove;
	}
}
