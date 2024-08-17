package edu.uj.po.simulation.model.pin;

import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.model.Pin;

public class PinIn implements Pin {

	private PinState state;

	public PinIn(){
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
		return "PinIn{" + "state=" + state + '}';
	}

	@Override
	public PinIn clone(){
		try{
			return (PinIn) super.clone();
		} catch(CloneNotSupportedException e){
			throw new AssertionError();
		}
	}
}
