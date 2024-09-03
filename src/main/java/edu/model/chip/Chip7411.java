package edu.model.chip;

import edu.logic.ChipLogicCalculation;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;

public class Chip7411 extends Chip {

	public Chip7411(){}
	public Chip7411(Chip target){
		super(target);
		this.putToPinMap(1, new PinIn());
		this.putToPinMap(2, new PinIn());
		this.putToPinMap(3, new PinIn());
		this.putToPinMap(4, new PinIn());
		this.putToPinMap(5, new PinIn());
		this.putToPinMap(6, new PinOut());
		this.putToPinMap(8, new PinOut());
		this.putToPinMap(9, new PinIn());
		this.putToPinMap(10, new PinIn());
		this.putToPinMap(11, new PinIn());
		this.putToPinMap(12, new PinOut());
		this.putToPinMap(13, new PinIn());
	}

	@Override
	public void simulate(){
		if(isOn) ChipLogicCalculation.chip7411Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip7411(this);
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{ id=" + this.chipId + "\npinMap=" + this.getPinMap() + "}\n";
	}
}
