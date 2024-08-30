package edu.model;

import edu.uj.po.simulation.interfaces.PinState;

public interface Pin extends Cloneable {
	void setPinState(PinState pinState);
	PinState getPinState();
}
