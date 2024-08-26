package edu.uj.po.simulation.model;

import edu.uj.po.simulation.Component;
import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.model.pin.PinOut;

import java.util.HashMap;
import java.util.Map;

public abstract class Chip implements Component {

	protected Integer chipId;
	protected boolean isOn;
	protected Map<Integer, Pin> pinMap;
	protected Map<Integer, Pin> previousPinMap;
	//protected Publisher publisher;

	public Chip() {
		this.pinMap = new HashMap<>();
		this.isOn = true;
		this.previousPinMap = new HashMap<>();
		//this.publisher = new ChipPublisher();
	}

	public Chip(Chip target) {
		if (target != null) {
			this.pinMap = new HashMap<>();
			this.isOn = true;
			this.previousPinMap = new HashMap<>();
			for (Map.Entry<Integer, Pin> entry : target.pinMap.entrySet()) {
				this.pinMap.put(entry.getKey(), entry.getValue().clone());
			}
			//this.publisher = new ChipPublisher();
		}
	}

	public Map<Integer, Pin> getPinMap() {
		return pinMap;
	}

	public void putToPinMap(Integer id, Pin pin) {
		pinMap.put(id, pin);
	}

	@Override
	public void simulate(){}

	public Chip clone(){
		throw new UnsupportedOperationException("Method not implemented");
	}

	public Chip clone(int size){
		throw new UnsupportedOperationException("Method not implemented");
	}

//	public void subscribe(Subscriber subscriber){
//		publisher.subscribe(subscriber);
//	}
//
//	public Integer report(){
//		if (hasPinOutNotChanged()) return publisher.report(this);
//		return null;
//	}

	protected Map<Integer, Pin> clonePinMap(){
		Map<Integer, Pin> newPinMap = new HashMap<>();
		for (Map.Entry<Integer, Pin> entry : this.pinMap.entrySet()) {
			newPinMap.put(entry.getKey(), entry.getValue().clone());
		}
		return newPinMap;
	}

//	public boolean hasPinOutNotChanged() {
//		return pinMap.entrySet().stream()
//				.filter(entry -> entry.getValue() instanceof PinOut) // dodja  żeby były tylko podłączone
//				// !!! piny
//				.anyMatch(entry -> {
//					Integer pinId = entry.getKey();
//					Pin currentPin = entry.getValue();
//					Pin previousPin = previousPinMap.get(pinId);
//					return previousPin != null && pinComparator.compare(currentPin, previousPin);
//				});
//	}

	@Override
	public String toString() {
		return "Chip{ pinMap=" + pinMap + "}\n";
	}

	public void setChipId(Integer uniqueChipId){
		this.chipId = uniqueChipId;
	}

	public int getChipId(){
		return this.chipId;
	}
	public void setOn(boolean isOn){
		pinMap.values().forEach(pin -> pin.setPinState(PinState.UNKNOWN));
		this.isOn = isOn;
	}
}
