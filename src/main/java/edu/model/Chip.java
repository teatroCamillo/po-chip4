package edu.model;

import edu.manager.Component;
import edu.model.pin.AbstractPin;
import edu.uj.po.simulation.interfaces.PinState;

import java.util.HashMap;
import java.util.Map;

public abstract class Chip implements Component {

	protected Integer chipId;
	protected boolean isOn;
	protected Map<Integer, AbstractPin> pinMap;
	protected Map<Integer, AbstractPin> previousPinMap;

	public Chip() {
		this.chipId = -1;
		this.pinMap = new HashMap<>();
		this.isOn = true;
		this.previousPinMap = new HashMap<>();
	}

	public Chip(Chip target){
		if (target != null) {
			this.chipId = -1;
			this.pinMap = new HashMap<>();
			this.isOn = true;
			this.previousPinMap = new HashMap<>();
		}
	}

	public Map<Integer, AbstractPin> getPinMap() {
		return pinMap;
	}

	public void putToPinMap(Integer id, AbstractPin pin) {
		pin.setId(id);
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

	protected Map<Integer, AbstractPin> clonePinMap(){
		Map<Integer, AbstractPin> newPinMap = new HashMap<>();
		for (Map.Entry<Integer, AbstractPin> entry : this.pinMap.entrySet()) {
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
		pinMap.values().forEach(pin -> pin.setChipId(chipId));
	}

	public int getChipId(){
		return this.chipId;
	}

	public void setOn(boolean isOn){
		pinMap.values().forEach(pin -> pin.setPinState(PinState.UNKNOWN));
		this.isOn = isOn;
	}
}
