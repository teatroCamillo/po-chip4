package edu.uj.po.simulation.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Chip {

	private Map<Integer, Pin> pinMap;

	public Chip() {
		this.pinMap = new HashMap<>();
	}

	public Chip(Chip target) {
		if (target != null) {
			// Głęboka kopia mapy pinMap
			this.pinMap = new HashMap<>();
			for (Map.Entry<Integer, Pin> entry : target.pinMap.entrySet()) {
				this.pinMap.put(entry.getKey(), entry.getValue().clone());
			}
		}
	}

	public Map<Integer, Pin> getPinMap() {
		return pinMap;
	}

	public void putToPinMap(Integer id, Pin pin) {
		pinMap.put(id, pin);
	}

	public void execute(){}

	public Chip clone(){
		throw new UnsupportedOperationException("Method not implemented");
	};

	public Chip clone(int size){
		throw new UnsupportedOperationException("Method not implemented");
	};

	@Override
	public String toString() {
		return "Chip{ pinMap=" + pinMap + "}\n";
	}
}
