package edu.model.chip;

import edu.model.Chip;
import edu.model.pin.PinIn;

public class HeaderOut extends Chip{

	public HeaderOut(){}
	public HeaderOut(Chip target, int size){
		super(target);
		for(int i = 1; i <= size; i++)
			this.putToPinMap(i, new PinIn());
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
