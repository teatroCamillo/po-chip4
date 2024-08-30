package edu.util;

import edu.uj.po.simulation.interfaces.ComponentPinState;
import edu.model.Chip;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Util{

	public static final String PIN_IN = "PinIn";
	public static final String PIN_OUT = "PinOut";
	public static final String HEADER_IN = "HeaderIn";
	public static final String HEADER_OUT = "HeaderOut";

	//to TURN ON/OFF between methods propagateSignal() and observer
	// propagate = true
	// observer = false
	public static final boolean SWITCH_BETWEEN_PO = false;

	// zbiera stan listew wyjściowych
	public static Set<ComponentPinState> saveCircuitHeaderOutState(Map<Integer, Chip> chips){
		Set<ComponentPinState> currentState = new HashSet<>();
		chips.entrySet().stream()
				.filter(entry -> entry.getValue().getClass().getSimpleName().equals(HEADER_OUT))
				.forEach(entry -> {
					entry.getValue().getPinMap().forEach((pinId, pin) -> {
						currentState.add(new ComponentPinState(entry.getKey(), pinId, pin.getPinState()));
					});
		});
		return currentState;
	}

	// zbiera cały stan układu
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
