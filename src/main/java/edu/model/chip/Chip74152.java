package edu.model.chip;

import edu.logic.ChipLogicCalculation;
import edu.model.Chip;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;

public class Chip74152 extends Chip {

	public Chip74152(){}
	public Chip74152(Chip target){
		super(target);
		this.putToPinMap(1, new PinIn());
		this.putToPinMap(2, new PinIn());
		this.putToPinMap(3, new PinIn());
		this.putToPinMap(4, new PinIn());
		this.putToPinMap(5, new PinIn());
		this.putToPinMap(6, new PinOut());
		this.putToPinMap(8, new PinIn());
		this.putToPinMap(9, new PinIn());
		this.putToPinMap(10, new PinIn());
		this.putToPinMap(11, new PinIn());
		this.putToPinMap(12, new PinIn());
		this.putToPinMap(13, new PinIn());
	}

	@Override
	public void simulate(){
		if(isOn) ChipLogicCalculation.chip74152Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip74152(this);
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{ id=" + this.chipId + "\npinMap=" + this.getPinMap() + "}\n";
	}
}
