package edu.uj.po.simulation.model.chip;

import edu.uj.po.simulation.logic.ChipLogicCalculation;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.pin.PinIn;
import edu.uj.po.simulation.model.pin.PinOut;

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
	public void execute(){
		ChipLogicCalculation.chip7402Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip7402(this);
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{\npinMap=" + this.getPinMap() + "}\n";
	}
}
