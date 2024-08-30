package edu.manager;

import edu.model.pin.AbstractPin;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;
import edu.uj.po.simulation.interfaces.ComponentPinState;
import edu.uj.po.simulation.interfaces.PinState;
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

	//Wykrycie stanu UNKNOWN i zgłoszenie stosownego wyjątki (UnknownStateException)
	// ograniczone zostało wyłącznie do metody stationaryState.
	// W trakcie pracy simulation/optimization stan taki się nie pojawi.
	// Z dystansem:
	// 1. Wyjątek powinien pojawić się np. wtedy, gdy obserwowane/używane jest wyjście bramki,
	// której jedno z wejść nie jest do niczego podłączone.

	// 2.Natomiast, pojawienie się tego stanu na elementach biorących aktywny udział w symulacji
	// (są włączone do obwodu) traktowane jest jako błąd i prowadzi do pojawianie się stosownego wyjątku.
	// Wyjątek powinien pojawić się np. wtedy, gdy obserwowane/używane jest wyjście bramki, której jedno z
	// wejść nie jest do niczego podłączone.

	// 3. Wyjątek UnknownStateException zarezerwowany jest dla sytuacji, gdy używana bramka:
	//	- posiada co najmniej jedno z wejść niepodłączone do listwy wejściowej
	//  - jest wprawdzie podłączona do listwy wejściowej, ale stan co najmniej jednego z potrzebnych pinów listwy nie
	// został przez użytkownika podany.
	@Override
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException {
		setHeadersInPins(states);

		validateHeadersIn();
		propagateSignal();

		Set<ComponentPinState> previousState;
		Set<ComponentPinState> currentState;
		currentState = Util.saveCircuitState(componentManager.chips);
		//currentState = Util.saveCircuitHeaderOutState(componentManager.chips);
		do {
			//System.out.println("\n");
			previousState = new HashSet<>(currentState);

			componentManager.chips.values().forEach(Chip::simulate);
			propagateSignal();

			currentState.clear();
			currentState = Util.saveCircuitState(componentManager.chips);
			//currentState = Util.saveCircuitHeaderOutState(componentManager.chips);

		} while (!previousState.equals(currentState));

		boolean isHeaderOut = componentManager.chips.values()
				.stream()
				.anyMatch(chip -> chip.getClass().getSimpleName().equals(Util.HEADER_OUT));
		//System.out.println("Sprawdzam stan układu przed validacja HeaderOur");
		//currentState.forEach(System.out::println);
		if(isHeaderOut) validateHeadersOut();
	}

	public void setHeadersInPins(Set<ComponentPinState> states) {
		// if chip/pin == null: throw
		for (ComponentPinState state : states) {
			Chip chip = componentManager.chips.get(state.componentId());
			//if(chip == null) throw new UnknownStateException(state);

			AbstractPin pin = chip.getPinMap().get(state.pinId());
			//if(pin == null) throw new UnknownStateException(state);

			pin.setPinState(state.state());
		}
	}

	// K Kiedy rzucać UnknownStateException?
	// 1. gdy PinOutpu for Header(In or Out) pinState UNKNOWN
	// ale chip musi istnić! w przeciwnym razie jest null i leci Runtime
	// 2. Dla InputHeaders: pinState == UNKNOWN i
	// hasAnyConnection() i musi być PinOut
	// ale chip musi istnić! w przeciwnym razie jest null i leci Runtime

	// me Kiedy rzucać UnknownStateException?
	// dla HederIn i Out gdy istnieją i:
//	if (pin.getPinState() == PinState.UNKNOWN && isOutputPinConnected(pin)) {
//		throw new UnknownStateException(new ComponentPinState(chipId, pinId, pin.getPinState()));



	// 1. wariant ze sprawdzeniem HeaderIN oraz Out pokrywa wszystkie sytuacje ale tez
	// te które nie pownny być pokryte
	// 2. wartiant z samym HeaderIn pokrywa większość poza 3 sytuacjami - jakie to sytuacje? Wime że są po stronie
	// HeaderOut
	private void validateHeadersIn() throws UnknownStateException {
		Map<Integer, Chip> headerChips = componentManager.chips.entrySet().stream()
				.filter(entry -> entry.getValue().getClass().getSimpleName().equals(Util.HEADER_IN))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		for (Map.Entry<Integer, Chip> entry : headerChips.entrySet()) {
			int chipId = entry.getKey();
			Chip chip = entry.getValue();

			for (Map.Entry<Integer, AbstractPin> entryPin : chip.getPinMap().entrySet()) {
				int pinId = entryPin.getKey();
				AbstractPin pin = entryPin.getValue();

				//pin.getPinState() == PinState.UNKNOWN && isPinOutConnected(pin) - całkiem ok
				// !isPinOutConnected(pin) - better !
				//isPinInConnected(pin) - nie tutaj bo rzuca Runtimy
				if (!isPinOutConnected(pin)) {
					throw new UnknownStateException(new ComponentPinState(chipId, pinId, pin.getPinState()));
				}

				// sam ten warunek jest za dużo generuje extra USEx ale pokrywa te które pojawiają się przy
				// połączeniu z  && isPinOutConnected(pin)
//				if (pin.getPinState() == PinState.UNKNOWN) {
//					throw new UnknownStateException(new ComponentPinState(chipId, pinId, pin.getPinState()));
//				}
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

				//pin.getPinState() == PinState.UNKNOWN || isPinInConnected(pin) - wykluczone, ściana USEx
				//pin.getPinState() == PinState.UNKNOWN && isPinInConnected(pin) pojawiają się extra UnSEx
				// pin.getPinState() == PinState.UNKNOWN też pojawiają się extra UnSEx
				//!isPinInConnected(pin) - najlepszy wariant
				if (!isPinInConnected(pin)) {
					throw new UnknownStateException(new ComponentPinState(chipId, pinId, pin.getPinState()));
				}
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

	private void propagateSignal(){
		componentManager.chips.values().forEach(chip -> {
			chip.getPinMap().values().forEach(AbstractPin::notifySubscribers);
		});
	}

	@Override
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0,
														   int ticks) throws UnknownStateException{
		setHeadersInPins(states0);
		propagateSignal();

		Map<Integer, Set<ComponentPinState>> resultMap = new HashMap<>();
		Set<ComponentPinState> currentState;
		currentState = Util.saveCircuitHeaderOutState(componentManager.chips);
		//currentState = Util.saveCircuitState(componentManager.chips);
		resultMap.put(0, new HashSet<>(currentState));

		for(int i=1; i<=ticks; i++){
			componentManager.chips.values().forEach(Chip::simulate);
			propagateSignal();

			currentState.clear();
			currentState = Util.saveCircuitHeaderOutState(componentManager.chips);
			//currentState = Util.saveCircuitState(componentManager.chips);
			resultMap.put(i, new HashSet<>(currentState));
		}
		return resultMap;
	}

	@Override
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException{

		Set<Integer> componentsToRemove = new HashSet<>();

		Set<ComponentPinState> stationaryInput = componentManager.chips.entrySet().stream()
				.filter(entry -> entry.getValue() instanceof HeaderIn)
				.flatMap(entry -> entry.getValue().getPinMap().entrySet().stream()
						.map(pinEntry -> new ComponentPinState(entry.getKey(), pinEntry.getKey(), pinEntry.getValue().getPinState()))
				)
				.collect(Collectors.toSet());

		Map<Integer, Set<ComponentPinState>> normalSimulationResul = simulation(states0, ticks);
//		System.out.println("normalSimulationResul");
//		normalSimulationResul.forEach((key, value) -> {
//			System.out.println("Tick: " + key + ", set: ");
//			for(ComponentPinState c : value) System.out.println(c);
//		});

		// TODO: przyda się reset układu ze wczytanym na nowo stanemStacjonarnym i dopiero wtedy wyłączenie chipa
		// K1. RESET stanStacjonarny - dla całego układu, wyszystkie chipy włączone - tak jak on robi przed
		// wywołaniem optimize
		stationaryState(stationaryInput);

		// Przechodzimy przez wszystkie komponenty, aby sprawdzić, które można usunąć
		for (Chip component :
				componentManager.chips.values().stream().filter(chip -> !(chip instanceof HeaderIn || chip instanceof HeaderOut)).collect(
						Collectors.toSet())) {
			// K2. Wyłącznie Chipu - to znaczy ze Chip nie przetwarza sygnałów - natomiast propagacja sygnału
			// działa dalej bo piny mają ustawione stany. Tak więc dodaję ze wyłączenie również ustawia piny
			// w stan UNKNOWN
			System.out.println("Chip is turned OFF: ");
			System.out.println(component);
			component.setOn(false);


			// Run the simulation without this component
			Map<Integer, Set<ComponentPinState>> modifiedSimulation = simulation(states0, ticks);
//			System.out.println("modifiedSimulation");
//			modifiedSimulation.forEach((key, value) -> {
//				System.out.println("Tick: " + key + ", set: ");
//				for(ComponentPinState c : value) System.out.println(c);
//			});

			// Compare the baseline and modified simulations
			if (normalSimulationResul.get(ticks).equals(modifiedSimulation.get(ticks))) {
				System.out.println("ADDED!");
				// If the results are the same, this component can be removed
				componentsToRemove.add(component.getChipId());
			}

			// Restore the component to the system
			//System.out.println("Chip is turned ON");
			component.setOn(true);
			stationaryState(stationaryInput);
		}
		return componentsToRemove;
	}
}
