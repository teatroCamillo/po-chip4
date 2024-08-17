package edu.uj.po.simulation.model.pin;

import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.model.Pin;

public class PinOut implements Pin {

	private PinState state;

	public PinOut(){
		this.state = PinState.UNKNOWN;
	}

	@Override
	public void setPinState(PinState pinState){
		this.state = pinState;
	}

	@Override
	public PinState getPinState(){
		return state;
	}

	@Override
	public String toString(){
		return "PinOut{" + "state=" + state + '}';
	}

	@Override
	public PinOut clone(){
		try{
			return (PinOut) super.clone();
		} catch(CloneNotSupportedException e){
			throw new AssertionError();
		}
	}
}