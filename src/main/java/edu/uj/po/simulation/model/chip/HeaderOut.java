package edu.uj.po.simulation.model.chip;

import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.pin.PinIn;

public class HeaderOut extends Chip{

	public HeaderOut(){}
	public HeaderOut(Chip target, int size){
		super(target);
		for(int i = 1; i <= size; i++)
			this.putToPinMap(i, new PinIn());
	}

	@Override
	public Chip clone(){
		return null;
	}

	@Override
	public Chip clone(int size){
		return new HeaderOut(this, size);
	}

	@Override
	public String toString(){
		return "HeaderOut{\npinMap=" + this.getPinMap() + "}\n";
	}
}
