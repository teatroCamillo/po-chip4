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

	public Map<Integer, Chip> getChips(){
		return chips;
	}

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
		int id = uniqueChipIdGenerator++;
		chips.put(id, creator.createHeaderIn(size));
		return id;
	}

	// Określone przez prowadzącego założenie że size=0 się nie trafi - bo interfejs tego nie określ
	@Override
	public int createOutputPinHeader(int size){
		int id = uniqueChipIdGenerator++;
		chips.put(id, creator.createHeaderOut(size));
		return id;
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

	//Wykrycie stanu UNKNOWN i zgłoszenie stosownego wyjątki (UnknownStateException)
	// ograniczone zostało wyłącznie do metody stationaryState.
	// W trakcie pracy simulation/optimization stan taki się nie pojawi.
	// Z dystansem:
	// Wyjątek powinien pojawić się np. wtedy, gdy obserwowane/używane jest wyjście bramki,
	// której jedno z wejść nie jest do niczego podłączone.
	@Override
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException {
		// 1. Ustawienie stanów początkowych na pinach wejściowych komponentów
		//System.out.println("stationaryState: 1. ustawianie pinów na listwach");
		setMomentZero(states);

		// 2. Walidacja: Sprawdzenie, czy wszystkie piny wejściowe mają poprawnie ustawiony stan
		// tu walidować piny wejściowe czy wyjsciowe czy wszystkie?
		//System.out.println("\nstationaryState: 2. walidacja stanów pinów listw wejściowych");

		validateHeaders(Util.HEADER_IN);

		// 3. Propagacja sygnału do pinów wyjściowych do osiągnięcia stanu stacjonarnego
		//System.out.println("stationaryState: 3. propagacja syganłu z HeaderIn na piny wejściowe układów");
		propagateSignal();

		// 3.0 zapis stanu układu i deklaracja TICK'a
		Set<ComponentPinState> previousState;
		Set<ComponentPinState> currentState;
		currentState = Util.saveCircuitState(chips);
		int tick = 0;
		//System.out.println("stationaryState: 3. propagacja syganłu w pętli");
		do {
			previousState = new HashSet<>(currentState);
			//System.out.println("TICK: " + tick);
			//currentState.forEach(System.out::println);

			// 3.1 Wykonanie kroku symulacji - uruchomienie wszystkich chipów (bramek logicznych)
			chips.values().forEach(Chip::execute);
			// 3.1.1 Przekazanie z wyjść na wejścia połącoznych componetów - cz moze to zrobic w exec?
			propagateSignal();

			// 3.2 Zapis aktualnych stanów pinów
			currentState.clear();
			currentState = Util.saveCircuitState(chips);
			++tick;
		} while (!previousState.equals(currentState));  // 3.3 Porównanie stanu bieżącego z poprzednim

		boolean isHeaderOut = chips.values()
				.stream()
				.anyMatch(chip -> chip.getClass().getSimpleName().equals(Util.HEADER_OUT));
		//System.out.println("validateHeaders: isHeaderOut: " + isHeaderOut);
		if(isHeaderOut) validateHeaders(Util.HEADER_OUT);
	}

	public void setMomentZero(Set<ComponentPinState> states){
		// 1. Ustawienie stanów początkowych na pinach wejściowych komponentów
		states.forEach(state -> {
					Chip chip = chips.get(state.componentId());
					Pin pin = chip.getPinMap().get(state.pinId());
					if (pin != null) {
						pin.setPinState(state.state());
						//System.out.println("SET: chip: " + state.componentId() + " and its pin: " + state.pinId() +
						//						   " with status: " + pin.getPinState());
					}
				});
	}

	private void validateHeaders(String headerClassName) throws UnknownStateException{
		//System.out.println("validateHeaders: 1 pull out correct header");
		Map<Integer,Chip> headerChips = chips.entrySet().stream()
				.filter(entry -> entry.getValue().getClass().getSimpleName().equals(headerClassName))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		//System.out.println("Class: " + headerClassName + " headerChips:");
		//headerChips.forEach((key, value) -> System.out.println(key + " : " + value));

		//System.out.println("validateHeaders: 2 validating");
		for (Map.Entry<Integer, Chip> entry : headerChips.entrySet()) {
			int chipId = entry.getKey();
			Chip chip = entry.getValue();

			for (Map.Entry<Integer, Pin> entryPin : chip.getPinMap().entrySet()) {
				int pinId = entryPin.getKey();
				Pin pin = entryPin.getValue();
				if (pin.getPinState() == PinState.UNKNOWN) {
//					System.out.println("UnknownStateException: chipId: " + chipId + ", pinId: " + pinId + ", pinState" +
//											   ": " + pin.getPinState());
					throw new UnknownStateException(new ComponentPinState(chipId, pinId, PinState.UNKNOWN));
				}
			}
		}
	}

	//todo: ważne też do optimize()
	//Q(id:2264)> 1. Czy zakładamy, że stany podawane w metodzie stationaryState, simulation i optimize są poprawne?
	// Tzn. czy obiekty ComponentPinState zawierają id komponentów, które faktycznie zostały utworzone i nie
	// musimy sprawdzać, czy istnieją? 2. Czy w symulacji dozwolone są stany UNKNOWN, gdy wiadomo jaki będzie wynik,
	// bo możemy go wywnioskować po mniejszej ilości wejść? Np. w przypadku dwuwejściowej bramki AND: stan na pierwszym
	// pinie wynosi 0, a na drugim UNKNOWN - wiemy, że wynik będzie 0. Czy w takim wypadku należy zgłosić wyjątek?
	//AD.2 W dokumentacji jest coś takiego: "UnknownStateException - pin używany w symulacji jest w stanie
	// nieokreślonym". Czyli, pojawienie się stanu UNKNOWN na pinie, który stosowany jest w symulacji skutkuje
	// stosownym wyjątkiem. Czyli, użytkownik powinien tak zestawić połączenia w układzie i dostarczyć taki
	// zestaw danych początkowych, aby każdy użyty w symulacji pin był w stanie określonym. Wyjątek stanowi proces
	// optymalizacji, gdzie może pojawić się stan UNKNOWN, ale tylko na skutek usunięcia niepotrzebnego układu.

	//Q(id:2270)> Czy pinem użytym w symulacji jest tylko ten pin, który został (bezpośrednio lub pośrednio)
	// podłączony do listy wejściowej i wyjściowej? Mam na myśli sytuacje gdy: 1. Wyjście, na które nie przychodzi
	// żaden sygnał jest połączone z wejściem, które po przejściu przez bramkę daje sygnał na wyjście, które nie jest
	// z niczym połączone. 2. Wejście jest połączone (dostaje sygnał z listy wejściowej), ale wyjście nie. 3.
	// Niepodłączone z żadnym wyjściem wejście listy wyjściowej jest połączone z inną listwą wyjściową. Jak
	// traktować takie przypadki?
	//A(id:2271)> Ustalmy najprostszą wersję: jeśli użytkownik z jakiegoś elementu logicznego skorzystał
	// (podłączył go), to jego stan musi dać się ustalić "wprost". Użytkownik musi zadbać o jego stany wejściowe.
	// [EDIT] Pisząc "wprost" mam na myśli, że stany wejść da się ustalić, bo albo są podane wprost, albo zostaną
	// ustalone jako wynik pracy układu w szczególności w trakcie wyznaczania stanu stacjonarnego.
	//Jeśli z 7400 użyję jednej bramki i podłącze do niej kabelek, to jej stan wejściowy mam obowiązek ustalić.
	// Pozostałe 3 bramki nie stanowią problemu jak długo ich nie podłącze do czegokolwiek.
	//1. Zgłaszamy wyjątek, bo stanu wejściowego brak.
	//2. Nie zgłaszamy, bo stan wejść jest określony. Brak obserwacji wyjścia to nie problem.
	//3. Listwy nie zostaną ze sobą spięte.


	//Standardowe flow:
	//1.budowa componentów
	//2.połączenia
	//3.stan stacjonarny
	//4.symulacja lub optymalizacja

	//Q9: Czy moglibyśmy dostać przykładowy przypadek/scenariusz gdy wywołanie metody stationaryState() nie
	// zwróci wyjątku UnknownStateException ale wywołanie metody simulation() już wróci ten wyjątek? Jak do
	// tego doprowadzić? W założeniach było, że użytkownik nie może usunąć istniejących już połaczeń, ale zakładamy
	// że może odpiąć kabelek od listwy i tym samym podać na jakiś ukłąd stan UNKOWN tak? W takim przypadku to ten
	// scenariusz jest możliwy i prosty do wywołania, ale wolałem dopytać, czy usunięcie połaczeń =/= usunięcie stanu
	// z danego pinu.
	//A(id:2283)> A9. Po chwili zastanowienia stwierdzam, że w sumie, to taki scenariusz jest bardzo prosty.
	// Potrzebujemy czterech stanów (symulacja używa tylko jednego układu 7400 i dwóch bramek). Wystarczy wszystkie
	// potrzebne stany w stationaryState podać, a przy okazji simulation podesłać informację tylko o dwóch.
	// stationaryState zadziała, simulation nie, bo dwóch stanów brak.
	@Override
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0,
														   int ticks) throws UnknownStateException{
		//System.out.println("simulation: 1. ustawianie pinów na listwach w stan w chwili 0");
		setMomentZero(states0);
		propagateSignal();

		//System.out.println("simulation: 2. deklaracja zasobów i zapis w czasie 0");

		Map<Integer, Set<ComponentPinState>> resultMap = new HashMap<>();
		Set<ComponentPinState> currentState;
		currentState = Util.saveCircuitHeaderOutState(chips);
		//System.out.println("STATE: 0");
		//currentState.forEach(System.out::println);
		resultMap.put(0, new HashSet<>(currentState));

		//System.out.println("simulation: 3. petla symulacji");
		for(int i=1; i<=ticks; i++){
			chips.values().forEach(Chip::execute);
			propagateSignal();

			currentState.clear();
			currentState = Util.saveCircuitHeaderOutState(chips);
			resultMap.put(i, new HashSet<>(currentState));
			//System.out.println("TICK: " + i);
			//currentState.forEach(System.out::println);
		}
		//System.out.println("simulation: 4. return resultMap");
		return resultMap;
	}

	// TODO: WAŻNE!
	// Q6: Do metody optimize() podajemy ilość ticków, to rozumiem, że symulacja po usunięciu komponentów,
	// które wydają się zbędne, powinna przez określoną ilość ticków być stabilna i nie wyrzucić UnknownStateException.
	// Ale czy scenariusz, gdy podamy mniejszą ilość ticków, niż jest potrzebna na wykrycie wyjątku powinien być
	// akceptowalny? Czyli scenariusz, gdy optimize() nie wyrzuci UnknownStateException bo nie dotarł jeszcze do
	// problematycznego pinu/komponentu i zwróci - według programu - poprawny wynik, tych komponentów, które mogą byc
	// usunięte mimo że w kolejnych tickach, wyjątek już by się pojawił?
	// A6. Wyrzucenie UnknownStateException powinno nastąpić natychmiast, albo wcale. Proszę zauważyć, że
	// operacje na wszystkich układach wykonywane są w tym samym czasie. Są takie, których stan bezpośrednio
	// określa użytkownik, ale są i takie, które rozpoczynają pracę, od stanu ustalonego w trakcie stationaryState
	// - po to on jest.

	// Q(id:2242)> W przykładzie z usuwaniem komponentów demonstruje Pan algorytm sprawdzający czy komponent ma
	// wpływ na wynik. Natomiast metoda `optimize przyjmuje liczbę kroków symulacji dla jakiej należy to sprawdzić.
	// Czy zatem na przykład podłączając szeregowo 6 bramek NOT, po 5 krokach, sygnał nie spropaguje się do listwy
	// wyjściowej więc należy te bramki zwrócić jako nie mające wpływu na wynik?
	//A(id:2244)> W takiej sytuacji trudno mówić o wyniku, skoro go nie ma (sygnał nie dotarł na czas). Liczba
	// tick-ów zegara dla metody optimize zostanie tak dobrana, aby sygnał wejść na pewno pozwolił ustalić
	// stany pinów listwy wyjściowej.

	//Q(id:2249)> Czy optymalizacja obwodu ma odbywać się tylko na poziomie usuwania zbędnych komponentów
	// (jeśli istnieją), czy również na poziomie pojedynczych połączeń między pinami?
	//A(id:2250)> Metoda CircuitDesign.connect niczego nie zwraca. Użytkownik nie dysponuje informacją
	// o wewnętrznym sposobie realizacji połączeń pomiędzy pinami. Wynikowy zbiór identyfikatorów usuwanych
	// elementów może zawierać wyłącznie te identyfikatory, które potrafi zidentyfikować użytkownik. W naszym
	// przypadku będą to identyfikatory układów scalonych (listwy możemy pominąć).

	//Q(id:2255)> Dlaczego usunięcie układu U1 w przykładzie o usuwaniu komponentów nie wpłynie na wyjście
	// 11 układu U3? Po usunięciu U1 na wejściu 13 U3 pojawi się stan UNKNOWN a na wejściu 12 ciągle
	// będziemy mieć stan "0". Zgodnie z opisem zadania w takiej sytuacji powinien zostać rzucony wyjątek
	// bo jedno z wejść bramki nie jest do niczego podłączone. Jeśli zostanie rzucony wyjątek to nie będzie nic
	// na wyjściu 11 U3, a więc uproszczenie schematu wpływa na wyjście. Wiem, że metoda optimize() zwraca tylko
	// identyfikatory możliwe do usunięcia, a nic nie usuwa ze schematu, jednak użytkownik może ponownie uruchomić
	// program i zbudować nowy schemat bez układów scalonych wskazanych wcześniej jako możliwe do usunięcia i wtedy
	// symulacja się nie powiedzie, bo dostaniemy wyjątek.
	//A(id:2263)> Bardzo dobrze, że pojawiło się to pytanie. Dziękuję.
	//Zacznijmy od tego, że idąc przedstawionym tu tropem każde usunięcie układu scalonego wpiętego w
	// obwód kończyłoby symulację/optymalizację wyjątkiem o pojawieniu się stanu UNKNOWN. Usuwalne byłyby tylko
	// te układy, które do niczego nie zostałyby podłączone.
	//Wyjątek UnknownStateException dotyczy tylko sytuacji, w której to użytkownik nie zadbał od
	// odpowiednie skonfigurowanie stanu startowego, czyli w przekazywanym zbiorze stanów pinów brakuje
	// stanów dla pinów, które konieczne są do rozpoczęcia obliczeń. Pojawienie się później stanu UNKNOWN na
	// skutek usuwania układów nie ma już znaczenia i nie powinno generować wspomnianego wyjątku.

	//Q(id:2268)> A jeszcze w nawiązaniu do tego pytania,
	//czy to że po usunięciu układu U1 pojawia się stan UNKNOWN na wejściu 13 układu U2,
	//to znaczy że zakładamy tutaj domyślny stan bramek? Tj. że skoro na jednym wejściu bramki AND mamy LOW to nie
	// ważne jaki będzie drugie wejście to zawsze będzie LOW. Co to by było jasne dla takich bramek jak AND i OR,
	// ale co z NOT? Musielibyśmy założyć na potrzeby projektu jakiś jej domyślny stan. Czy tutaj raczej chodzi
	// o to że nie "czyścimy" wyjściowego sygnału bramki (pin 11 układu U2) i nawet jeśli oba jej wejścia będą
	// UNKNOWN to już w tym stanie początkowym pozostanie do końca symulacji? Co też może być kłopotliwe ponieważ
	// użytkownik widząc że może usunąć układ U1, przebuduje sobie schemat, uruchomi ponownie poszukiwanie stanu
	// stacjonarnego i dostanie UnknownStateException ponieważ na wejściu zabraknie odpowiedniego stanu, który sam
	// musiał podać. Też nie informujemy go o tym czemu ten układ został usunięty, więc zrzucamy na niego odpowiedzialność
	// odgadnięcia odpowiedniego sygnału dla odpowiedniego wejścia.
	//Właściwy obrazek to ten z trzema układami: U1, U2 i U2. Wszystkie to 4xAND. Tam usunięcie U1 jest możliwe,
	// bo stan wejścia 13 U2 nie ma znaczenia, bramka AND nie jest do niczego używana. Problem stanu UNKNOWN
	// dotyczy wyłącznie tych elementów, które są do czegokolwiek używane w symulacji. Czyli, są obserwowane,
	// albo są podłączone do innych układów i to te kolejne są obserwowane.

	//Standardowe flow:
	//1.budowa componentów
	//2.połączenia
	//3.stan stacjonarny
	//4.symulacja lub optymalizacja

	//Q6: Do metody optimize() podajemy ilość ticków, to rozumiem, że symulacja po usunięciu komponentów,
	// które wydają się zbędne, powinna przez określoną ilość ticków być stabilna i nie wyrzucić UnknownStateException.
	// Ale czy scenariusz, gdy podamy mniejszą ilość ticków, niż jest potrzebna na wykrycie wyjątku powinien być
	// akceptowalny? Czyli scenariusz, gdy optimize() nie wyrzuci UnknownStateException bo nie dotarł jeszcze do
	// problematycznego pinu/komponentu i zwróci - według programu - poprawny wynik, tych komponentów, które mogą
	// byc usunięte mimo że w kolejnych tickach, wyjątek już by się pojawił?
	//A(id:2280)> A6. Wyrzucenie UnknownStateException powinno nastąpić natychmiast, albo wcale. Proszę zauważyć,
	// że operacje na wszystkich układach wykonywane są w tym samym czasie. Są takie, których stan bezpośrednio określa
	// użytkownik, ale są i takie, które rozpoczynają pracę, od stanu ustalonego w trakcie stationaryState - po to on jest.

	//Q(id:2284)> Jeśli podczas optymalizacji schematu okaże się że można usunąć układ A lub układ B to czy zbiór
	// identyfikatorów elementów możliwych do usunięcia ma zawierać ID obu układów czy tylko jednego z nich?
	//A(id:2285)> Jeśli nie można usunąć jednocześnie obu układów, to w odpowiedzi podajemy A lub B. Obie
	// odpowiedzi traktowane są jako poprawne.
	@Override
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException{
		Set<Integer> result = new HashSet<>();
		result.add(1);


		return result;
	}
}
