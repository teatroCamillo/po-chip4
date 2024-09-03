package edu.model.chip;

import edu.logic.ChipLogic;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;

public class Chip7431 extends Chip {

	public Chip7431(){}
	public Chip7431(Chip target){
		super(target);
		this.putToPinMap(1, new PinIn());
		this.putToPinMap(2, new PinOut());
		this.putToPinMap(3, new PinIn());
		this.putToPinMap(4, new PinOut());
		this.putToPinMap(5, new PinIn());
		this.putToPinMap(6, new PinIn());
		this.putToPinMap(7, new PinOut());
		this.putToPinMap(9, new PinOut());
		this.putToPinMap(10, new PinIn());
		this.putToPinMap(11, new PinIn());
		this.putToPinMap(12, new PinOut());
		this.putToPinMap(13, new PinIn());
		this.putToPinMap(14, new PinOut());
		this.putToPinMap(15, new PinIn());
	}

	@Override
	public void simulate(){
		if(isOn) ChipLogic.chip7431Logic(this);
	}

	@Override
	public Chip clone(){
		return new Chip7431(this);
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{ id=" + this.chipId + "\npinMap=" + this.getPinMap() + "}\n";
	}
}
