package edu.model.chip;

import edu.logic.ChipLogicCalculation;
import edu.model.pin.PinOut;
import edu.model.pin.PinIn;

public class Chip7402 extends Chip {

	public Chip7402(){}
	public Chip7402(Chip target){
		super(target);
		this.putToPinMap(1, new PinOut());
		this.putToPinMap(2, new PinIn());
		this.putToPinMap(3, new PinIn());
		this.putToPinMap(4, new PinOut());
		this.putToPinMap(5, new PinIn());
		this.putToPinMap(6, new PinIn());
		this.putToPinMap(8, new PinIn());
		this.putToPinMap(9, new PinIn());
		this.putToPinMap(10, new PinOut());
		this.putToPinMap(11, new PinIn());
		this.putToPinMap(12, new PinIn());
		this.putToPinMap(13, new PinOut());
	}

	@Override
	public void simulate(){
		if(isOn) ChipLogicCalculation.chip7402Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip7402(this);
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{ id=" + this.chipId + "\npinMap=" + this.getPinMap() + "}\n";
	}
}
