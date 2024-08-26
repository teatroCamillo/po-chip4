package edu.uj.po.simulation.model;

import edu.uj.po.simulation.interfaces.PinState;

public interface Subscriber {
	void update(PinState state);
}
