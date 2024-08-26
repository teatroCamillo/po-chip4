package edu.uj.po.simulation.model;

public class PinOutComparator implements PinComparator {

	@Override
	public boolean compare(Pin pin1, Pin pin2) {
		return pin1.getPinState() == pin2.getPinState();
	}
}