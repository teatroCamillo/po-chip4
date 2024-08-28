package edu.model.chip;

import edu.logic.ChipLogicCalculation;
import edu.model.Chip;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;

public class Chip7442 extends Chip {

	public Chip7442(){}
	public Chip7442(Chip target){
		super(target);
		this.putToPinMap(1, new PinOut());
		this.putToPinMap(2, new PinOut());
		this.putToPinMap(3, new PinOut());
		this.putToPinMap(4, new PinOut());
		this.putToPinMap(5, new PinOut());
		this.putToPinMap(6, new PinOut());
		this.putToPinMap(7, new PinOut());
		this.putToPinMap(9, new PinOut());
		this.putToPinMap(10, new PinOut());
		this.putToPinMap(11, new PinOut());
		this.putToPinMap(12, new PinIn());
		this.putToPinMap(13, new PinIn());
		this.putToPinMap(14, new PinIn());
		this.putToPinMap(15, new PinIn());
	}

	@Override
	public void simulate(){
		if(isOn) ChipLogicCalculation.chip7442Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip7442(this);
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{ id=" + this.chipId + "\npinMap=" + this.getPinMap() + "}\n";
	}
}
