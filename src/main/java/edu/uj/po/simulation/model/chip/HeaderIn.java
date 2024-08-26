package edu.uj.po.simulation.model.chip;

import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.pin.PinOut;


// TODO: aby zgaddzała się numeracja pinów na listwach
// potraktuj je jako:
// - w HeaderIn jako same PinOut
// - w HeaderOut jako PinIn
// Lub drugi pomysł:
// mapowanie pinów czyli np. jak on poda na listwe wejściową że pin 1 łączy się z układem C1 pin 3
// to ja mapuje połączenie pin 1 na swój pin wyjściowy w listwe czyli np. 2 i łącze C1 pin 3

public class HeaderIn extends Chip{

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
