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
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimulationManager implements SimulationAndOptimization {

	private final CircuitManager circuitManager;

	public SimulationManager(CircuitManager circuitManager) {
		this.circuitManager = circuitManager;
	}

	@Override
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException {
		setHeadersInPins(states);
		validateHeadersIn();
		circuitManager.propagateSignal();

		Set<ComponentPinState> previousState;
		Set<ComponentPinState> currentState = Util.saveCircuitState(circuitManager.chips);

		do {
			previousState = new HashSet<>(currentState);
			circuitManager.chips.values().forEach(Chip::simulate);
			circuitManager.propagateSignal();
			currentState = Util.saveCircuitState(circuitManager.chips);
		} while(!previousState.equals(currentState));

		if(circuitManager.chips.values().stream().anyMatch(chip -> chip instanceof HeaderOut)) validateHeadersOut();
	}

	public void setHeadersInPins(Set<ComponentPinState> states) {
		states.forEach(state -> {
			Chip chip = circuitManager.chips.get(state.componentId());
			chip.getPinMap().get(state.pinId()).setPinState(state.state());
		});
	}

	private void validateHeadersIn() throws UnknownStateException {
		Map<Integer, Chip> headerChips = getHeaderChipsByPredicate(chip -> chip instanceof HeaderIn);

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
		Map<Integer, Chip> headerChips = getHeaderChipsByPredicate(chip -> chip instanceof HeaderOut);

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

	private Map<Integer, Chip> getHeaderChipsByPredicate(Predicate<Chip> predicate) {
		return circuitManager.chips.entrySet().stream()
				.filter(entry -> predicate.test(entry.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private boolean isPinInConnected(AbstractPin pin) {
		if (pin instanceof PinOut) throw new RuntimeException("Expected PinIn, but found PinOut.");
		return circuitManager.isPinConnected(pin);
	}

	private boolean isPinOutConnected(AbstractPin pin) {
		if (pin instanceof PinIn) throw new RuntimeException("Expected PinOut, but found PinIn.");
		return circuitManager.isPinConnected(pin);
	}

	@Override
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0, int ticks) throws UnknownStateException {
		setHeadersInPins(states0);
		circuitManager.propagateSignal();

		Map<Integer, Set<ComponentPinState>> resultMap = new HashMap<>();
		resultMap.put(0, Util.saveCircuitHeaderOutState(circuitManager.chips));

		for (int i = 1; i <= ticks; i++) {
			circuitManager.chips.values().forEach(Chip::simulate);
			circuitManager.propagateSignal();
			resultMap.put(i, Util.saveCircuitHeaderOutState(circuitManager.chips));
		}
		return resultMap;
	}

	@Override
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException {
		Set<Integer> componentsToRemove = new HashSet<>();
		Set<ComponentPinState> stationaryInput = circuitManager.chips.values().stream()
				.filter(chip -> chip instanceof HeaderIn)
				.flatMap(chip -> chip.getPinMap().entrySet().stream()
						.map(pinEntry -> new ComponentPinState(chip.getChipId(), pinEntry.getKey(), pinEntry.getValue().getPinState())))
				.collect(Collectors.toSet());

		Map<Integer, Set<ComponentPinState>> normalSimulationResult = simulation(states0, ticks);
		stationaryState(stationaryInput);

		for (Chip component : circuitManager.chips.values()) {
			if (!(component instanceof HeaderIn || component instanceof HeaderOut)) {
				component.setOn(false);
				Map<Integer, Set<ComponentPinState>> modifiedSimulation = simulation(states0, ticks);

				if (normalSimulationResult.get(ticks).equals(modifiedSimulation.get(ticks)))
					componentsToRemove.add(component.getChipId());

				component.setOn(true);
				stationaryState(stationaryInput);
			}
		}
		return componentsToRemove;
	}
}
