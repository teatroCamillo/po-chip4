package edu.uj.po.simulation.util;

import edu.uj.po.simulation.interfaces.ComponentPinState;
import edu.uj.po.simulation.model.Chip;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Util{

	// Constants
	public static final String PIN_IN = "PinIn";
	public static final String PIN_OUT = "PinOut";
	public static final String HEADER_IN = "HeaderIn";
	public static final String HEADER_OUT = "HeaderOut";

	public static Set<ComponentPinState> saveCircuitState(Map<Integer, Chip> chips){
		Set<ComponentPinState> currentState = new HashSet<>();
		chips.forEach((componentId, chip) -> {
			chip.getPinMap().forEach((pinId, pin) -> {
				currentState.add(new ComponentPinState(componentId, pinId, pin.getPinState()));
			});
		});
		return currentState;
	}

}
