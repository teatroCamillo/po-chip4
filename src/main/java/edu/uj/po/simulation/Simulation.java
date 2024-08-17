package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.*;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.Connection;
import edu.uj.po.simulation.model.Creator;
import edu.uj.po.simulation.model.Pin;
import edu.uj.po.simulation.model.creator.ChipCreator;
import edu.uj.po.simulation.util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation implements UserInterface {

	private final Set<Integer> availableChipCodes;
	private final Map<Integer, Chip> chips;
	private final Creator creator;
	private Integer chipId;

	public Simulation(){
		this.availableChipCodes = new HashSet<>();
		availableChipCodes.add(7400);
		availableChipCodes.add(7402);
		availableChipCodes.add(7404);

		this.chips = new HashMap<>();
		this.creator = new ChipCreator();
		this.chipId = 0;
	}

	public void getInfo(Set<Integer> chipIds) {
		System.out.println("Chips & headers:");
		chips.entrySet().stream()
				.filter(entry -> chipIds.contains(entry.getKey()))
				.forEach(entry -> System.out.println("ID: " + entry.getKey() + ", Chip: " + entry.getValue().toString()));
	}
	@Override
	public int createChip(int code) throws UnknownChip {
		if(!availableChipCodes.contains(code)) throw new UnknownChip();
		int id = chipId++;
		chips.put(id, creator.create(code));
		return id;
	}

	// milczące założenie że size=0 się nie trafi - bo interfejs tego nie określ
	@Override
	public int createInputPinHeader(int size){
		int id = chipId++;
		chips.put(id, creator.createHeaderIn(size));
		return id;
	}

	// milczące założenie że size=0 się nie trafi - bo interfejs tego nie określ
	@Override
	public int createOutputPinHeader(int size){
		int id = chipId++;
		chips.put(id, creator.createHeaderOut(size));
		return id;
	}

	// Jaka jest różnica między  UnknownComponent a UnknownChip : chyba Chip to implementacje poszczególnych ukłądów
	// scalonych a Component to wytworzone w oparciu o Chip elementy które biorą udział w pracy
	@Override
	public void connect(int component1,
						int pin1,
						int component2,
						int pin2) throws UnknownComponent, UnknownPin, ShortCircuitException{

		// Sprawdź, czy komponenty istnieją
		if (!chips.containsKey(component1)) {
			throw new UnknownComponent(component1);
		}
		if (!chips.containsKey(component2)) {
			throw new UnknownComponent(component2);
		}

		// Sprawdź, czy piny istnieją w odpowiednich komponentach
		Chip chip1 = chips.get(component1);
		Chip chip2 = chips.get(component2);

		if (!chip1.getPinMap().containsKey(pin1)) {
			throw new UnknownPin(component1, pin1);
		}
		if (!chip2.getPinMap().containsKey(pin2)) {
			throw new UnknownPin(component2, pin2);
		}

		// Sprawdź, czy nie dochodzi do zwarcia (dwa piny nie mogą być połączone ze sobą więcej niż raz)
		Set<Connection> chip1Connections = chip1.getDirectConnections();
		for (Connection connection : chip1Connections) {
			// 1. połączenie wyjścia z tym samym wyjściem w tym samym komponecie
			if (connection.targetChipId()  == component2 && connection.targetPinId() == pin2) {
				System.out.println("Pin " + pin1 + " in component " + component1 + " is already connected to pin " + pin2 + " in component " + component2);
				throw new ShortCircuitException();
			}
			// 2. połączenie dwóch wyjść
		}

		// Połącz komponenty
		chip1.addNewConnection(pin1, component2, pin2);
	}

	//Wykrycie stanu UNKNOWN i zgłoszenie stosownego wyjątki (UnknownStateException)
	// ograniczone zostało wyłącznie do metody stationaryState.
	// W trakcie pracy simulation/optimization stan taki się nie pojawi.
	@Override
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException {
		// 1. Ustawienie stanów początkowych na pinach wejściowych komponentów
		System.out.println("stationaryState: 1. ustawianie pinów na listwach");
		states.stream()
				.filter(state -> chips.containsKey(state.componentId())) // Filtrujemy tylko istniejące komponenty
				.forEach(state -> {
					Chip chip = chips.get(state.componentId());
					Pin pin = chip.getPinMap().get(state.pinId());
					if (pin != null) {
						pin.setPinState(state.state());
					}
				});

		// 2. Walidacja: Sprawdzenie, czy wszystkie piny wejściowe mają poprawnie ustawiony stan
		// tu walidować piny wejściowe czy wyjsciowe czy wszystkie?
		System.out.println("stationaryState: 2. walidacja stanów pinów listw wejściowych");

		//2.0. Wyciągnięcie listw wejściowych
		Map<Integer,Chip> headerInChips = chips.entrySet().stream()
				.filter(entry -> entry.getValue().getClass().getSimpleName().equals(Util.HEADER_IN))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		System.out.println("headerInChips:");
		headerInChips.forEach((key, value) -> System.out.println(key + " : " + value));

		//2.1. Walidacja
		for (Map.Entry<Integer, Chip> entry : headerInChips.entrySet()) {

			int chipId = entry.getKey();
			Chip chip = entry.getValue();

			for (Map.Entry<Integer, Pin> entryPin : chip.getPinMap().entrySet()) {
				int pinId = entryPin.getKey();
				Pin pin = entryPin.getValue();
				if (pin.getClass().getSimpleName().equals(Util.PIN_IN) && pin.getPinState() == PinState.UNKNOWN) {
					System.out.println("UnknownStateException");
					throw new UnknownStateException(new ComponentPinState(chipId, pinId, PinState.UNKNOWN));
				}
			}
		}

		// 3. Propagacja sygnału do pinów wyjściowych do osiągnięcia stanu stacjonarnego
		System.out.println("stationaryState: 3. propagacja syganłu");

		// 3.0 zapis stanu układu i deklaracja TICK'a
		Set<ComponentPinState> previousState;
		Set<ComponentPinState> currentState;
		currentState = Util.saveCircuitState(chips);
		int tick = 0;

		do {
			previousState = new HashSet<>(currentState);
			System.out.println("TICK: " + tick);
			currentState.forEach(System.out::println);

			// 3.1 Wykonanie kroku symulacji - uruchomienie wszystkich chipów (bramek logicznych)
			chips.values().forEach(Chip::execute);
			// 3.1.1 Przekazanie z wyjść na wejścia połącoznych componetów - cz moze to zrobic w exec?
			chips.values().forEach(chip -> chip.propagateSignal(chips));

			// 3.2 Zapis aktualnych stanów pinów
			currentState.clear();
			currentState = Util.saveCircuitState(chips);
			++tick;
		} while (!previousState.equals(currentState));  // 3.3 Porównanie stanu bieżącego z poprzednim

		// 4. Walidacja stanu końcowego na pinach wyjściowych
		// tu walidować piny wejściowe czy wyjsciowe czy wszystkie?
		System.out.println("stationaryState: 4. walidacja stanów pinów listw wyjściowych");
		for (Map.Entry<Integer, Chip> entry : chips.entrySet()) {
			int chipId = entry.getKey();
			Chip chip = entry.getValue();

			for (Map.Entry<Integer, Pin> entryPin : chip.getPinMap().entrySet()) {
				int pinId = entryPin.getKey();
				Pin pin = entryPin.getValue();
				if (pin.getClass().getCanonicalName().equals(Util.PIN_OUT) && pin.getPinState() == PinState.UNKNOWN) {
					throw new UnknownStateException(new ComponentPinState(chipId, pinId, PinState.UNKNOWN));
				}
			}
		}
	}

	@Override
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0,
														   int ticks) throws UnknownStateException{
		// po każdym ticku stan na wysztkich komponentach i pinach jest wrzucany do mapy gdzie tick to klucz a Set<CPS>
		// to wartość na dany tic
		return null;
	}

	@Override
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException{
		return null;
	}
}
