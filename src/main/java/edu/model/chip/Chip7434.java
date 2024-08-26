package edu.model.chip;

import edu.logic.ChipLogicCalculation;
import edu.model.Chip;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;

public class Chip7434 extends Chip{

	public Chip7434(){}
	public Chip7434(Chip target){
		super(target);
		this.putToPinMap(1, new PinIn());
		this.putToPinMap(2, new PinIn());
		this.putToPinMap(3, new PinOut());
		this.putToPinMap(4, new PinIn());
		this.putToPinMap(5, new PinIn());
		this.putToPinMap(6, new PinOut());
		this.putToPinMap(8, new PinOut());
		this.putToPinMap(9, new PinIn());
		this.putToPinMap(10, new PinIn());
		this.putToPinMap(11, new PinOut());
		this.putToPinMap(12, new PinIn());
		this.putToPinMap(13, new PinIn());
	}

	@Override
	public void simulate(){
		ChipLogicCalculation.chip7400Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip7434(this);
	}

	@Override
	public String toString(){
		return "Chip7400{\npinMap=" + this.getPinMap() + "}\n";
	}
}
