package edu.model.chip;

import edu.model.pin.PinOut;

public class HeaderIn extends Chip {

	public HeaderIn(){}
	public HeaderIn(Chip target, int size){
		super(target);
		for(int i = 1; i <= size; i++)
			this.putToPinMap(i, new PinOut());
	}

	@Override
	public Chip clone(int size){
		return new HeaderIn(this, size);
	}

	@Override
	public String toString(){
		return "HeaderIn{\npinMap=" + this.getPinMap() + "}\n";
	}
}
