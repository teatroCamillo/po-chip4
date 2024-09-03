package edu.model.chip;

import edu.logic.ChipLogicCalculation;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;

public class Chip74138 extends Chip {

	public Chip74138(){}
	public Chip74138(Chip target){
		super(target);
		this.putToPinMap(1, new PinIn());
		this.putToPinMap(2, new PinIn());
		this.putToPinMap(3, new PinIn());
		this.putToPinMap(4, new PinIn());
		this.putToPinMap(5, new PinIn());
		this.putToPinMap(6, new PinIn());
		this.putToPinMap(7, new PinOut());
		this.putToPinMap(9, new PinOut());
		this.putToPinMap(10, new PinOut());
		this.putToPinMap(11, new PinOut());
		this.putToPinMap(12, new PinOut());
		this.putToPinMap(13, new PinOut());
		this.putToPinMap(14, new PinOut());
		this.putToPinMap(15, new PinOut());
	}

	@Override
	public void simulate(){
		if(isOn) ChipLogicCalculation.chip74138Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip74138(this);
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{ id=" + this.chipId + "\npinMap=" + this.getPinMap() + "}\n";
	}
}
