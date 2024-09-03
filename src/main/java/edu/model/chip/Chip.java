package edu.model.chip;

import edu.model.pin.AbstractPin;
import edu.uj.po.simulation.interfaces.PinState;

import java.util.HashMap;
import java.util.Map;

public abstract class Chip {

	protected Integer chipId;
	protected boolean isOn;
	protected Map<Integer, AbstractPin> pinMap;

	public Chip() {
		this.chipId = -1;
		this.pinMap = new HashMap<>();
		this.isOn = true;
	}

	public Chip(Chip target){
		if (target != null) {
			this.chipId = -1;
			this.pinMap = new HashMap<>();
			this.isOn = true;
		}
	}

	public void simulate(){}

	public Map<Integer, AbstractPin> getPinMap() {
		return pinMap;
	}

	public void putToPinMap(Integer id, AbstractPin pin) {
		pin.setId(id);
		pinMap.put(id, pin);
	}

	public Chip clone(){
		throw new UnsupportedOperationException("Method not implemented");
	}

	public Chip clone(int size){
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public String toString(){
		return "Chip{" + "chipId=" + chipId + ", isOn=" + isOn + ", pinMap=" + pinMap + '}';
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
