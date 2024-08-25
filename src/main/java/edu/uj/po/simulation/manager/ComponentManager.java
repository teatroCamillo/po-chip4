package edu.uj.po.simulation.manager;

import edu.uj.po.simulation.Component;
import edu.uj.po.simulation.interfaces.*;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.Connection;
import edu.uj.po.simulation.model.Creator;
import edu.uj.po.simulation.model.Pin;
import edu.uj.po.simulation.model.creator.ChipCreator;
import edu.uj.po.simulation.util.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComponentManager implements Component, CircuitDesign {

	// chips i componenets referują do tego samego ? Częściowo tak bo są to m.in chipy ale mogą być tez inne obiekty
	private final Set<Component> components;
	final Map<Integer, Chip> chips;

	private final Set<Integer> availableChipCodes;
	final Set<Connection> directConnections;
	private final Creator creator;

	public ComponentManager(){
		this.components = new HashSet<>();
		this.chips = new HashMap<>();
		this.availableChipCodes = new HashSet<>();
		availableChipCodes.add(7400);
		availableChipCodes.add(7402);
		availableChipCodes.add(7404);
		availableChipCodes.add(7408);
		availableChipCodes.add(7410);

		this.directConnections = new HashSet<>();
		this.creator = new ChipCreator();
	}

	public void addComponent(Component component) {
		components.add(component);
	}

	public void removeComponent(Component component) {
		components.remove(component);
	}

	@Override
	public int createChip(int code) throws UnknownChip {
		if(!availableChipCodes.contains(code)) throw new UnknownChip();
		return putToChipsMap(creator.create(code));
	}

	// TODO: WAŻNE!
	// Q5: Rozumiem, że metoda createInputPinHeader() tworzy listwe kołkową na którą użytkownik podaje sygnały
	//	i ona jest "punktem startowym propagacji" dla całej symulacji. A wywołanie createOutputPinHeader()
	//	tworzy listwę kołkowa, na której będą obecne sygnały już po przejściu przez cała symulacje? Czyli
	//	simulation() zwraca wynik tylko tych listw kołkowych, stworzonych za pomocą createOutputPinHeader()?
	//	Pytam ponieważ w pytaniach pojęcia wejście/wyjście jest trochę inaczej zdefiniowane, z innej perspektywy
	//	i krótko mówiąc się pogubiłem.
	//A5. Generalnie TAK. Uruchamiając obliczenia używam wyłącznie listw wejściowych.
	// W wyniku oczekuję wyłącznie informacji z listw wyjściowych.

	// milczące założenie że size=0 się nie trafi - bo interfejs tego nie określ
	@Override
	public int createInputPinHeader(int size){
		return putToChipsMap(creator.createHeaderIn(size));
	}

	// Określone przez prowadzącego założenie że size=0 się nie trafi - bo interfejs tego nie określ
	@Override
	public int createOutputPinHeader(int size){
		return putToChipsMap(creator.createHeaderOut(size));
	}

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

		// TODO: przypadki zwarc
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
		// Naiwnie dodana na chwilę obecną do obecnej klasy: directConnections, addNewConnection i propagateSignal
		// to rozwiązuje problem:
		// 		- Set - unikatowe rekordy - czyli nie trzeba sprawdzać warunku nr.1
		//		- wszystkie połączenia w jednym miejscu - łatwy dostęp, przeszukiwanie, dodawanie etc
		//		- nie ma porblemu bi-/uni- directional na Chip'ach
		// -
		// Inny problem:
		// Czy recordy Connection(1,1,2,2) i Connection(2,2,1,1) to to samo połączenie?
		// Czy powinienem ustlaić że gdy dodaję pierwszy rekord do setu to dodaję też drugi żeby było jasne?

		// TODO: OK - przetestowane
		// 2. Sprawdzenie, czy połączenie nie powoduje zwarcia (wiele wyjścia do jednego wejścia)
		if (directConnections
				.stream()
				.anyMatch(connection -> connection.targetChipId() == component2
						&& connection.targetPinId() == pin2
						&& (connection.sourceChipId() != component1 || connection.sourcePinId() != pin1)
				)) {
			//System.out.println("Cannot connect this pin: " + pin2 + " in component "
			//						   + component2 + ". It is already connected.");
			throw new ShortCircuitException();
		}

		// TODO: OK - przetestowane
		// 3. Sprawdzenie, czy nie ma połączenia wyjścia układu z wejściami użytkownika (HeaderIn)
		if (chip2.getClass().getSimpleName().equals(Util.HEADER_IN) &&
				chip1.getPinMap().get(pin1).getClass().getSimpleName().equals(Util.PIN_OUT)) {
			//System.out.println("Cannot connect an output pin to a HeaderIn input pin: " + pin2 + " in component " +
			// component2);
			throw new ShortCircuitException();
		}

		// TODO: OK - przetestowane
		// 4. Sprawdz czy nie ma połączenia wyjście do wyjścia.
		if (chip1.getPinMap().get(pin1).getClass().getSimpleName().equals(Util.PIN_OUT) &&
				chip2.getPinMap().get(pin2).getClass().getSimpleName().equals(Util.PIN_OUT)) {
			//System.out.println("Cannot connect two output pins!");
			throw new ShortCircuitException();
		}

		addNewConnection(component1, pin1, component2, pin2);
	}

	public void addNewConnection(int sourceChipId, int sourcePinId, int targetChipId, int targetPinId) {
		this.directConnections.add(new Connection(sourceChipId, sourcePinId, targetChipId, targetPinId));
	}

	private int putToChipsMap(Chip newChip){
		int newChipId = newChip.getChipId();
		chips.put(newChipId, newChip);
		return newChipId;
	}

	// to powinno być zrobione według wzorca Obserwator
	// to jest naiwan implementacja póki co
	// do poprawy na jakiś wzorzec
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
	public void simulate() {
		System.out.println("Simulate() from ComponentManager");
	}

	public Map<Integer, Chip> getChips(){
		return chips;
	}
	public Set<Connection> getDirectConnections(){
		return directConnections;
	}
}
