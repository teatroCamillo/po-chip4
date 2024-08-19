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
	final Map<Integer, Chip> chips;
	final Set<Connection> directConnections;
	private final Creator creator;
	private Integer uniqueChipIdGenerator;

	public Simulation(){
		this.availableChipCodes = new HashSet<>();
		availableChipCodes.add(7400);
		availableChipCodes.add(7402);
		availableChipCodes.add(7404);
		availableChipCodes.add(7408);
		availableChipCodes.add(7410);

		this.chips = new HashMap<>();
		this.directConnections = new HashSet<>();
		this.creator = new ChipCreator();
		this.uniqueChipIdGenerator = 0;
	}

	public void getInfo(Set<Integer> chipIds) {
		System.out.println("Chips & headers:");
		chips.entrySet().stream()
				.filter(entry -> chipIds.contains(entry.getKey()))
				.forEach(entry -> System.out.println("ID: " + entry.getKey() + ", Chip: " + entry.getValue().toString()));
	}

	//TODO: pomyśl o innym miejscu dla tej metody
	public void addNewConnection(int sourceChipId, int sourcePinId, int targetChipId, int targetPinId) {
		this.directConnections.add(new Connection(sourceChipId, sourcePinId, targetChipId, targetPinId));
	}

	// to powinno być zrobione według wzorca Obserwator
	// to jest naiwan implementacja póki co
	// do poprawy na jakiś wzorzec
	//TODO: pomyśl o innym miejscu dla tej metody
	public void propagateSignal(){
		// 1. przechodze po wszystkich połączeniach
		// 2. mapuje stan pinu docelowego na źródłowy

		directConnections.forEach(connection -> {
			int sourceChipId = connection.sourceChipId();
			int sourceId = connection.sourcePinId();
			int targetChipId = connection.targetChipId();
			int targetPinId = connection.targetPinId();

			Pin sourcePin = chips.get(sourceChipId).getPinMap().get(sourceId);
			// 0. sprawdź czy outputPin biezącego componentu jest w odpowiednim stanie - != UNKNOWN
			if(sourcePin.getPinState() != PinState.UNKNOWN)
				chips.get(targetChipId).getPinMap().get(targetPinId).setPinState(sourcePin.getPinState());
		});
	}

	@Override
	public int createChip(int code) throws UnknownChip {
		if(!availableChipCodes.contains(code)) throw new UnknownChip();
		int id = uniqueChipIdGenerator++;
		chips.put(id, creator.create(code));
		return id;
	}

	// milczące założenie że size=0 się nie trafi - bo interfejs tego nie określ
	@Override
	public int createInputPinHeader(int size){
		int id = uniqueChipIdGenerator++;
		chips.put(id, creator.createHeaderIn(size));
		return id;
	}

	// milczące założenie że size=0 się nie trafi - bo interfejs tego nie określ
	@Override
	public int createOutputPinHeader(int size){
		int id = uniqueChipIdGenerator++;
		chips.put(id, creator.createHeaderOut(size));
		return id;
	}

	// Jaka jest różnica między  UnknownComponent a UnknownChip : chyba Chip to implementacje poszczególnych ukłądów
	// scalonych a Component to wytworzone w oparciu o Chip elementy które biorą udział w pracy
	@Override
	public void connect(int component1,
						int pin1,
						int component2,
						int pin2) throws UnknownComponent, UnknownPin, ShortCircuitException {

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

		//TODO: przypadki zwarc
		// 1. Dwa piny nie mogą być połączone ze sobą więcej niż raz - czyli dwa lub więcej takich samych rekordów
		// 2. Sprawdzenie, czy połączenie nie powoduje zwarcia (wiele wyjścia do jednego wejścia)
		// 3. Sprawdzenie, czy nie ma połączenia wyjścia układu z wejściami użytkownika (HeaderIn)
		// 4. Sprawdz czy nie ma połączenia wyjście do wyjścia.
		// -
		// Note:
		// Czy lepszym sposobme byłoby dodanie stanu na każdy Pin np. CONNECTED żeby to określić, niż sprawdzać w
		// pętli. Co więcje teraz directConnections są unidirectional wiec tylko jedna strona ma info o połączeniu.
		// To generuje problem że gdy PinOut z C1 jest aktualnie połączony z PinIn z C2, to dane zapisane są u C1. Wiec
		// pojawia się błąd gdy PinOut z C3 będzie próbować połączyć się z tym samym PinIn z C2.
		// - Gdzie przechowywać dane o połączeniach żeby łatwo było tworzyć nowe, usówać, przeszukiwać i wykrywać błędy?

		//TODO: do zastanowienia ten for
		// 1. Dwa piny nie mogą być połączone ze sobą więcej niż raz) - czyli dwa lub więcjet takich same rekordów
//		Set<Connection> chip1Connections = chip1.getDirectConnections();
//		for (Connection connection : chip1Connections) {
//			// 1. Sprawdzenie, czy połączenie już istnieje
//			if (connection.targetChipId() == component2 && connection.targetPinId() == pin2) {
//				System.out.println("Pin " + pin1 + " in component " + component1 +
//										   " is already connected to pin " + pin2 +
//										   " in component " + component2);
//				return; // Nic nie zmienia, ponieważ połączenie już istnieje
//			}
//		}

		//TODO:
		// 2. Sprawdzenie, czy połączenie nie powoduje zwarcia (wiele wyjścia do jednego wejścia)
		if (chip1.getPinMap().get(pin1).getClass().getSimpleName().equals(Util.PIN_OUT) &&
				chip2.getPinMap().get(pin2).getClass().getSimpleName().equals(Util.PIN_OUT)) {
			System.out.println("Cannot connect two outputs: " + pin1 + " in component " + component1 + " and " + pin2 + " in component " + component2);
			throw new ShortCircuitException();
		}

		//TODO:
		// 2
//		for (Connection connection : chip2.getDirectConnections()) {
//			if (connection.targetPinId() == pin2 && chip2.getPinMap().get(pin2).getClass().getSimpleName().equals(Util.PIN_IN)) {
//				System.out.println("Multiple outputs cannot be connected to the same input: " + pin2 + " in component " + component2);
//				throw new ShortCircuitException();
//			}
//		}

		//TODO:
		// 3. Sprawdzenie, czy nie ma połączenia wyjścia układu z wejściami użytkownika (HeaderIn)
		if (chip2.getClass().getSimpleName().equals(Util.HEADER_IN) &&
				chip1.getPinMap().get(pin1).getClass().getSimpleName().equals(Util.PIN_OUT)) {
			System.out.println("Cannot connect an output pin to a HeaderIn input pin: " + pin2 + " in component " + component2);
			throw new ShortCircuitException();
		}

		//TODO: OK
		// 4. Sprawdz czy nie ma połączenia wyjście do wyjścia.

		addNewConnection(component1, pin1, component2, pin2);
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
					System.out.println("UnknownStateException: chipId: " + chipId + ", pinId: " + pinId + ", pinState" +
											   ": " + pin.getPinState());
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
			propagateSignal();

			// 3.2 Zapis aktualnych stanów pinów
			currentState.clear();
			currentState = Util.saveCircuitState(chips);
			++tick;
		} while (!previousState.equals(currentState));  // 3.3 Porównanie stanu bieżącego z poprzednim

		//4.0. Wyciągnięcie listw wejściowych
		System.out.println("stationaryState: 4. walidacja stanów pinów listw wyjściowych");
		Map<Integer,Chip> headerOutChips = chips.entrySet().stream()
				.filter(entry -> entry.getValue().getClass().getSimpleName().equals(Util.HEADER_OUT))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		System.out.println("headerOutChips:");
		headerOutChips.forEach((key, value) -> System.out.println(key + " : " + value));

		// 4.1 Walidacja stanu końcowego na pinach wyjściowych
		// tu walidować piny wejściowe czy wyjsciowe czy wszystkie?
		for (Map.Entry<Integer, Chip> entry : headerOutChips.entrySet()) {
			int chipId = entry.getKey();
			Chip chip = entry.getValue();

			for (Map.Entry<Integer, Pin> entryPin : chip.getPinMap().entrySet()) {
				int pinId = entryPin.getKey();
				Pin pin = entryPin.getValue();
				if (pin.getClass().getCanonicalName().equals(Util.PIN_OUT) && pin.getPinState() == PinState.UNKNOWN) {
					System.out.println("UnknownStateException: chipId: " + chipId + ", pinId: " + pinId + ", pinState" +
											   ": " + pin.getPinState());
					throw new UnknownStateException(new ComponentPinState(chipId, pinId, PinState.UNKNOWN));
				}
			}
		}
	}

	@Override
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0,
														   int ticks) throws UnknownStateException{
		System.out.println("simulation: 1. ustawianie pinów na listwach w stan w chwili 0");
		states0.stream()
				.filter(state -> chips.containsKey(state.componentId()))
				.forEach(state -> {
					Chip chip = chips.get(state.componentId());
					Pin pin = chip.getPinMap().get(state.pinId());
					if (pin != null) {
						pin.setPinState(state.state());
					}
				});
		// czy powninienem spropagować syganł na listwach z PinIn na PinOut w tym miejscu?

		System.out.println("simulation: 2. deklaracja zasobów i zapis w czasie 0");

		Map<Integer, Set<ComponentPinState>> resultMap = new HashMap<>();
		Set<ComponentPinState> currentState;
		currentState = Util.saveCircuitState(chips);
		System.out.println("STATE: 0");
		currentState.forEach(System.out::println);
		resultMap.put(0, new HashSet<>(currentState));

		System.out.println("simulation: 3. petla symulacji");
		for(int i=1; i<=ticks; i++){
			chips.values().forEach(Chip::execute);
			propagateSignal();

			currentState.clear();
			currentState = Util.saveCircuitState(chips);
			resultMap.put(i, new HashSet<>(currentState));
			System.out.println("TICK: " + i);
			currentState.forEach(System.out::println);
		}
		System.out.println("simulation: 4. return resultMap");
		return resultMap;
	}

	@Override
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException{
		Set<Integer> result = new HashSet<>();
		result.add(1);


		return result;
	}
}
