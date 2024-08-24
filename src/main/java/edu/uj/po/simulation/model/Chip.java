package edu.uj.po.simulation.model;

import edu.uj.po.simulation.model.pin.PinOut;

import java.util.HashMap;
import java.util.Map;

public abstract class Chip {

	protected Map<Integer, Pin> pinMap;
	protected Map<Integer, Pin> previousPinMap;
	protected Publisher publisher;
	private PinComparator pinComparator;

	public Chip() {
		this.pinMap = new HashMap<>();
		this.previousPinMap = new HashMap<>();
		this.publisher = new ChipPublisher();
		this.pinComparator = new PinOutComparator();
	}

	public Chip(Chip target) {
		if (target != null) {
			this.pinMap = new HashMap<>();
			this.previousPinMap = new HashMap<>();
			for (Map.Entry<Integer, Pin> entry : target.pinMap.entrySet()) {
				this.pinMap.put(entry.getKey(), entry.getValue().clone());
			}
			this.publisher = new ChipPublisher();
			this.pinComparator = new PinOutComparator();
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
	}

	public Chip clone(int size){
		throw new UnsupportedOperationException("Method not implemented");
	}

	public void subscribe(Subscriber subscriber){
		publisher.subscribe(subscriber);
	}

	public Integer report(){
		if (hasPinOutNotChanged()) return publisher.report(this);
		return null;
	}

	protected Map<Integer, Pin> clonePinMap(){
		Map<Integer, Pin> newPinMap = new HashMap<>();
		for (Map.Entry<Integer, Pin> entry : this.pinMap.entrySet()) {
			newPinMap.put(entry.getKey(), entry.getValue().clone());
		}
		return newPinMap;
	}

	public boolean hasPinOutNotChanged() {
		return pinMap.entrySet().stream()
				.filter(entry -> entry.getValue() instanceof PinOut) // dodja  żeby były tylko podłączone
				// !!! piny
				.anyMatch(entry -> {
					Integer pinId = entry.getKey();
					Pin currentPin = entry.getValue();
					Pin previousPin = previousPinMap.get(pinId);
					return previousPin != null && pinComparator.compare(currentPin, previousPin);
				});
	}

	@Override
	public String toString() {
		return "Chip{ pinMap=" + pinMap + "}\n";
	}
}
