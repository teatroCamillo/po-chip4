package edu.manager;

import edu.model.pin.AbstractPin;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;
import edu.uj.po.simulation.interfaces.*;
import edu.model.Chip;
import edu.model.Connection;
import edu.model.Creator;
import edu.model.creator.ChipCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComponentManager implements Component, CircuitDesign {

	final Map<Integer, Chip> chips;
	final Set<Connection> directConnections;
	private final Creator creator;

	public ComponentManager(){
		this.chips = new HashMap<>();
		this.directConnections = new HashSet<>();
		this.creator = new ChipCreator();
	}

	protected void propagateSignal(){
		chips.values().forEach(chip -> {
			chip.getPinMap().values()
					.forEach(AbstractPin::notifySubscribers);
		});
	}

	// Connection to Connection nie ma znaczenie musi być tylko jeden wpis
	// *********** TO JEST BARDZO WAŻNE- TEST IT ******
	// Ważniejsze jest właściwy kierunek subskrypcji aby sygnał był poprawnie przesyłany
	// ****************************************************
	// z poziomu managera ustawiam subskrypcję miedzy pinami
	private void setSubscribe(int component1,
							  int pin1,
							  int component2,
							  int pin2){
		Chip chip1 = chips.get(component1);
		Chip chip2 = chips.get(component2);
		AbstractPin p1 = chip1.getPinMap().get(pin1);
		AbstractPin p2 = chip2.getPinMap().get(pin2);

		//1. jeśli any is PinOut - to on jest publiszerem
		//2. jeśli component1 i component2 są PinIn ?

		// kierunek propagacji - subskrybentami sa tylko PinIn a PinOut są Publisherami
		if(p1 instanceof PinOut) chip1.getPinMap().get(pin1).subscribe(chip2.getPinMap().get(pin2));
		else if(p2 instanceof PinOut) chip2.getPinMap().get(pin2).subscribe(chip1.getPinMap().get(pin1));
		else {
			// tu powinien być subskrybentem ten co już pisiada połączenie z PinOut
			//System.out.println("******************************* Both are PinIn class " +
									//   "********************************");

			// lub isPinConnectedToPinOut || isConnectedToPinOutRecursive
			//v0
			//if(isPinConnected(p1)) chip1.getPinMap().get(pin1).subscribe(chip2.getPinMap().get(pin2));
			//chip1.getPinMap().get(pin1).subscribe(chip2.getPinMap().get(pin2));
			//brak tego nie wpływa na wynik - bez tego mam 9 błędów
			//else chip2.getPinMap().get(pin2).subscribe(chip1.getPinMap().get(pin1));

			//v1 - jest ok na tym wszystko przechodzi
			if(canReachOutputThroughAnotherInput(chip1.getChipId(), p1.getId())){
				chip1.getPinMap().get(pin1).subscribe(chip2.getPinMap().get(pin2));
			}
			else if(canReachOutputThroughAnotherInput(chip2.getChipId(), p2.getId()))
				chip2.getPinMap().get(pin2).subscribe(chip1.getPinMap().get(pin1));

			else chip1.getPinMap().get(pin1).subscribe(chip2.getPinMap().get(pin2));
			// to z canReach... nie pomaga
			//else chip2.getPinMap().get(pin2).subscribe(chip1.getPinMap().get(pin1));
		}
	}

	// Connection to Connection nie ma znaczenie musi być tylko jeden wpis
	// *********** TO JEST BARDZO WAŻNE- TEST IT ******
	// Ważniejsze jest właściwy kierunek subskrypcji aby sygnał był poprawnie przesyłany
	// ****************************************************
	// należy zadbać zdublowanie w obie strony aby było jednoznaczne że jest połączenie między dwoma pinami
	// przed utworzeniem połączenia zidentyfikuj który Pin jest In a który Out
	public void addNewConnection(int sourceChipId, int sourcePinId, int targetChipId, int targetPinId) {
		//if(isOutputPin(sourceChipId, sourcePinId))
		// zestawienie subskrypcji
		System.out.println("Sprawdzam i ustawiam subskrybcję... oraz dodaję połączenie");
		setSubscribe(sourceChipId, sourcePinId, targetChipId, targetPinId);
		this.directConnections.add(new Connection(sourceChipId,sourcePinId, targetChipId, targetPinId));
		//this.directConnections.add(new Connection(targetChipId, targetPinId, sourceChipId, sourcePinId));

		// przetestowano warianty
		//1. z dwoma wpisami i zmienioną kolejnością - brak większego wpływu
		//this.directConnections.add(new Connection(sourceChipId,sourcePinId, targetChipId, targetPinId));
		//this.directConnections.add(new Connection(targetChipId, targetPinId, sourceChipId, sourcePinId));

	}

	private int putToChipsMap(Chip newChip){
		int newChipId = newChip.getChipId();
		chips.put(newChipId, newChip);
		return newChipId;
	}

	@Override
	public void simulate() {
		System.out.println("Simulate() from ComponentManager");
	}

	public Map<Integer, Chip> getChips(){
		return chips;
	}
	public Set<Connection> getDirectConnections(){
		return directConnections;
	}

	@Override
	public int createChip(int code) throws UnknownChip {
		return putToChipsMap(creator.create(code));
	}

	@Override
	public int createInputPinHeader(int size){
		return putToChipsMap(creator.createHeaderIn(size));
	}

	@Override
	public int createOutputPinHeader(int size){
		return putToChipsMap(creator.createHeaderOut(size));
	}

	// Wskazówka gdy waliduje połączenie to czy waliduję na wszystkich chipach wszystkie piny? Upewnij się
	// TODO: błąd który mam z obliczeniami może być spowodowany niewłaściwym kierunkiem połączenia a w efekcie
	// TODO: kierunkiem przesyłania sygnału gdy połączone są np. 2 lub więcej PinIn'ów ze sobą - sprawdź to ! -
	// TODO: Napisałem wskazówkę w setSubscribe()
	// TODO: Czy dodanie 2 wpisów Connection do dircectCOnnections to błąd? - na chwilę obecną wydaje się że to nie bład
	// również przy takim ukłądzie Wyjście ---- Wejście ----- Wejście ---- Wyjście, upewnij się że prąd leci we
	// właściwym kierunku aby właściwie rzucić wyjatek.
	// Może warto zaimplementować wykrywanie zwarcia w całym układzie - ale najpierw popraw propagację bo powinna pomóc

	//Kolejność spięcia Pinów - ważne:
	//Q(id:2257)> Czy ma znaczenie w jakiej kolejności podajemy piny w metodzie connect()? Co ma się
	//wydarzyć jeśli jako pierwszy argument podam pin wejściowy a jako kolejny pin wyjściowy? Czy w
	// implementacji mamy "domyślić się", że chodzi nam o przekazanie stanu z wyjścia na wejście czy ma w
	// takiej sytuacji zostać rzucony wyjątek bo przekazanie stanu z wejścia na wyjście nie ma sensu?
	//A(id:2259)> Kabelki nie mają kierunku, czyli to program musi ustalić co z czym jest łączone.
	// Przy czym można sobie wyobrazić połączenie dwóch wejść ze sobą. Niekoniecznie jeden pin musi być
	// wejściem a drugi wyjściem.

	//Dozwolone połączenia:
	// 1. Out z In/ManyIns, In z In/ManyIn + pośrednio też
	// 2. Połącznie wielu HederOut ze sobą

	//Niedozwolone połaczenia:
	// 1. Out z Out (bezpośrednio lub pośrednio)
	// 2. Out z Out/In HeaderIn - tak ma podobno nie robić
	// 3. Połącznei wielu HederIn ze sobą

	// Jaka jest różnica między  UnknownComponent a UnknownChip : chyba Chip to implementacje poszczególnych ukłądów
	// scalonych a Component to wytworzone w oparciu o Chip elementy które biorą udział w pracy
	//Q(id:2223)> 1. Co się stanie jeżeli chcę wpiąć się w pin który jest zajęty?
	// Czy po prostu wypinam to co bylo i wpinam nowe?
	//A(id:2230)> 1. Generalnie, dozwolone jest połączenie np. wyjścia z wieloma wejściami.
	// Mogę też do wejścia podłączyć inne wejście, do tego kolejne wejście itd.
	// Jedyne ograniczenie to połączenie ze sobą (bezpośrednio, lub pośrednio) dwóch wyjść, lecz to prowadzi
	// do wyjątku i nie powinno skutkować zmianą w układzie połączeń. Poprzez pośrednie zwarcie wyjść mam na
	// myśli np. taki układ połączeń:
	// Wyjście ---- Wejście ----- Wejście ---- Wyjście
	// TODO: odwrotny spodób połączń tak żeby można było łączyć w ten sam sposó A z B jak i B z A

	//Q(id:2254)> 1. Czy w sytuacji w której połączone jest kilka wejść, a sygnał zostanie przekazany tylko na
	// jedno z nich to następuje natychmiastowa propagacja tego sygnału na wszystkie połączone wejścia?
	//A(id:2261)> Tak. Kable przenoszą sygnał natychmiast.

	//Q(id:2256)> Czy jeśli zabronione jest łączenie wyjścia układów z wejściami użytkownika to czy
	// łączenie wyjść użytkownika z wejściem użytkownika też jest zabronione? Czy można połączyć ze
	// sobą dwie listwy wejściowe? Czy można połączyć ze sobą dwie listy wyjściowe?
	//A(id:2262)> Zdrowy rozsądek faktycznie nakazuje, aby wyjść układów nie podpinać do pinów listw wejściowych
	// i tak robić nie będę. Połączenie dwóch list wejściowych (od mojej strony wejścia, od Państwa strony to są
	// jednak wyjścia) zabrania spięcia ze sobą pinów listw wejściowych. Samo z siebie spięcie list wyjściowych
	// (dla mnie wyjście, dla Państwa to wejścia) nie jest problemem, po prostu na połączonych pinach będzie ten sam stan.

	//Q(id:2257)> Czy ma znaczenie w jakiej kolejności podajemy piny w metodzie connect()? Co ma się
	//wydarzyć jeśli jako pierwszy argument podam pin wejściowy a jako kolejny pin wyjściowy? Czy w
	// implementacji mamy "domyślić się", że chodzi nam o przekazanie stanu z wyjścia na wejście czy ma w
	// takiej sytuacji zostać rzucony wyjątek bo przekazanie stanu z wejścia na wyjście nie ma sensu?
	//A(id:2259)> Kabelki nie mają kierunku, czyli to program musi ustalić co z czym jest łączone.
	// Przy czym można sobie wyobrazić połączenie dwóch wejść ze sobą. Niekoniecznie jeden pin musi być
	// wejściem a drugi wyjściem.
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

		// Sprawdź, czy takie połączenie już istnieje
		if (isConnectionExist(component1, pin1, component2, pin2)) {
			return;
		}

		//warunek gdy z chip łączy się spowrotem do HederIn

		// Sprawdzenie, czy oba piny to wyjścia
		if (isOutputPin(component1, pin1) && isOutputPin(component2, pin2)) {
			throw new ShortCircuitException();
		}

		// Sprawdzenie, czy nie tworzy się pośrednie zwarcie
		if (isCreatingIndirectShortCircuit(component1, pin1, component2, pin2)) {
			throw new ShortCircuitException();
		}

		addNewConnection(component1, pin1, component2, pin2);
	}

	public boolean isConnectionExist(int component1,
									 int pin1,
									 int component2,
									 int pin2){
		return directConnections
				.stream()
				.anyMatch(connection ->
								  (connection.sourceChipId() == component1 &&
										  connection.sourcePinId() == pin1 &&
										  connection.targetChipId() == component2 &&
										  connection.targetPinId() == pin2) ||
										  (connection.sourceChipId() == component2 &&
												  connection.sourcePinId() == pin2 &&
												  connection.targetChipId() == component1 &&
												  connection.targetPinId() == pin1));
	}

	public boolean isPinConnected(AbstractPin pin){
		return directConnections
				.stream()
				.anyMatch(connection ->
								  (connection.sourceChipId() == pin.getChipId() &&
										  connection.sourcePinId() == pin.getId()) ||
										  (connection.targetChipId() == pin.getChipId() &&
												  connection.targetPinId() == pin.getId())
				);
	}

	// dla przykładu: 1, 1, 0, 1
	private boolean isCreatingIndirectShortCircuit(int component1, int pin1, int component2, int pin2) {
		// isOutputPin() - jest chyba bez sensu bo jak łączone są 2 PinIn to jak to się ma?
//		return (isOutputPin(component1, pin1) && canReachOutputThroughAnotherInput(component2, pin2)) ||
//				(isOutputPin(component2, pin2) && canReachOutputThroughAnotherInput(component1, pin1));

		// 0. to nie jest dobre rozwiązanie
//		return (canReachOutputThroughAnotherInput(component2, pin2)) ||
//				(canReachOutputThroughAnotherInput(component1, pin1));
		// 1.
		if(isOutputPin(component1, pin1)) return canReachOutputThroughAnotherInput(component2, pin2);
		if(isOutputPin(component2, pin2)) return canReachOutputThroughAnotherInput(component1, pin1);
		return canReachOutputThroughAnotherInput(component1, pin1) &&
				canReachOutputThroughAnotherInput(component2,pin2);
	}

	private boolean canReachOutputThroughAnotherInput(int chipId, int pinId) {
		Set<String> visited = new HashSet<>();
		return isConnectedToOutputRecursive(chipId, pinId, visited);
	}

	private boolean isConnectedToOutputRecursive(int chipId, int pinId, Set<String> visited) {
		String key = chipId + ":" + pinId; // 3 : 10
		if (visited.contains(key)) {
			return false;
		}

		visited.add(key);

		// Przeszukaj połączenia w poszukiwaniu pośredniego połączenia z wyjściem
		for (Connection connection : directConnections) {
			if (connection.sourceChipId() == chipId && connection.sourcePinId() == pinId) {
				if (isOutputPin(connection.targetChipId(), connection.targetPinId())) {
					return true;
				}
				if (isInputPin(connection.targetChipId(), connection.targetPinId())) {
					if (isConnectedToOutputRecursive(connection.targetChipId(), connection.targetPinId(), visited)) {
						return true;
					}
				}
			}
			else if(connection.targetChipId() == chipId && connection.targetPinId() == pinId) {
				if (isOutputPin(connection.sourceChipId(), connection.sourcePinId())) {
					return true;
				}
				if (isInputPin(connection.sourceChipId(), connection.sourcePinId())) {
					if (isConnectedToOutputRecursive(connection.sourceChipId(), connection.sourcePinId(), visited)) {
						return true;
					}
				}
			}

		}

		return false;
	}

	private boolean isOutputPin(int chipId, int pinId) {
		Chip chip = chips.get(chipId);
		//return chip.getPinMap().get(pin).getClass().getSimpleName().equals(Util.PIN_OUT);
		return chip.getPinMap().get(pinId) instanceof PinOut;
	}

	private boolean isInputPin(int chipId, int pinId) {
		Chip chip = chips.get(chipId);
		//return chip.getPinMap().get(pin).getClass().getSimpleName().equals(Util.PIN_IN);
		return chip.getPinMap().get(pinId) instanceof PinIn;
	}
}
