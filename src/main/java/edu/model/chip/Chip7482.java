package edu.model.chip;

import edu.logic.ChipLogicCalculation;
import edu.model.Chip;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;

public class Chip7482 extends Chip {

	public Chip7482(){}
	public Chip7482(Chip target){
		super(target);
		this.putToPinMap(1, new PinOut());
		this.putToPinMap(2, new PinIn());
		this.putToPinMap(3, new PinIn());
		this.putToPinMap(5, new PinIn());
		this.putToPinMap(10, new PinOut());
		this.putToPinMap(12, new PinOut());
		this.putToPinMap(13, new PinIn());
		this.putToPinMap(14, new PinIn());
	}

	@Override
	public void simulate(){
		if(isOn) ChipLogicCalculation.chip7482Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip7482(this);
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{ id=" + this.chipId + "\npinMap=" + this.getPinMap() + "}\n";
	}
}
