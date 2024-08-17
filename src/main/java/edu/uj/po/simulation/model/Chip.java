package edu.uj.po.simulation.model;

import edu.uj.po.simulation.interfaces.PinState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Chip {

	private Map<Integer, Pin> pinMap;
	private Set<Connection> directConnections;

	public Chip() {
		this.pinMap = new HashMap<>();
		this.directConnections = new HashSet<>();
	}

	public Chip(Chip target) {
		if (target != null) {
			// Głęboka kopia mapy pinMap
			this.pinMap = new HashMap<>();
			for (Map.Entry<Integer, Pin> entry : target.pinMap.entrySet()) {
				this.pinMap.put(entry.getKey(), entry.getValue().clone());
			}

			// Głęboka kopia zbioru directConnections
			this.directConnections = new HashSet<>();
			for (Connection connection : target.directConnections) {
				addNewConnection(connection.pinId(), connection.targetChipId(), connection.targetPinId());
			}
		}
	}

	// Zwracamy kopię mapy, aby uniknąć wycieku referencji
	public Map<Integer, Pin> getPinMap() {
		return pinMap;
	}

	// Zwracamy kopię zbioru directConnections, aby uniknąć wycieku referencji
	public Set<Connection> getDirectConnections() {
		return directConnections;
	}

	public void addNewConnection(int pinId, int targetChipId, int targetPinId) {
		// Tworzymy nowe połączenie i dodajemy je do zbioru directConnections
		this.directConnections.add(new Connection(pinId, targetChipId, targetPinId));
	}

	public void putToPinMap(Integer id, Pin pin) {
		pinMap.put(id, pin);
	}

	// to powinno być zrobione według wzorca Obserwator
	// to jest naiwan implementacja póki co
	// do poprawy na jakiś wzorzec
	public void propagateSignal(Map<Integer, Chip> chipMap){
		// 1. przechodze po wszystkich połączeniach
		// 2. mapuje stan pinu docelowego na źródłowy

		directConnections.forEach(connection -> {
			int sourceId = connection.pinId();
			int targetChipId = connection.targetChipId();
			int targetPinId = connection.targetPinId();

			// Pobieramy stan pinu źródłowego
			PinState pinState = getPinMap().get(sourceId).getPinState();
			// 0. sprawdź czy outputPin biezącego componentu jest jest w odpowiednim stanie - != UNKNOWN
			if(pinState != PinState.UNKNOWN)
				chipMap.get(targetChipId).getPinMap().get(targetPinId).setPinState(pinState);
		});
	}

	public abstract void execute();

	public abstract Chip clone();

	public abstract Chip clone(int size);

	@Override
	public String toString() {
		return "Chip{ pinMap=" + pinMap + ", directConnections=" + directConnections + "}\n";
	}
}
