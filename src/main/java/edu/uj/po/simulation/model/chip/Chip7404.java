package edu.uj.po.simulation.model.chip;

import edu.uj.po.simulation.logic.ChipLogicCalculation;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.pin.PinIn;
import edu.uj.po.simulation.model.pin.PinOut;

public class Chip7404 extends Chip {

	public Chip7404(){}
	public Chip7404(Chip target){
		super(target);
		this.putToPinMap(1, new PinIn());
		this.putToPinMap(2, new PinOut());
		this.putToPinMap(3, new PinIn());
		this.putToPinMap(4, new PinOut());
		this.putToPinMap(5, new PinIn());
		this.putToPinMap(6, new PinOut());
		this.putToPinMap(8, new PinOut());
		this.putToPinMap(9, new PinIn());
		this.putToPinMap(10, new PinOut());
		this.putToPinMap(11, new PinIn());
		this.putToPinMap(12, new PinOut());
		this.putToPinMap(13, new PinIn());
	}

	@Override
	public void execute(){
		ChipLogicCalculation.chip7404Calculation(this);
	}

	@Override
	public Chip clone(){
		return new Chip7404(this);
	}

	@Override
	public Chip clone(int size){
		return null;
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{\npinMap=" + this.getPinMap() + ",\ndirectConnections=" + this.getDirectConnections() + "}\n";
	}
}
