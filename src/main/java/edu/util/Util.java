package edu.util;

import edu.model.chip.HeaderOut;
import edu.uj.po.simulation.interfaces.ComponentPinState;
import edu.model.Chip;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Util{

	public static Set<ComponentPinState> saveCircuitHeaderOutState(Map<Integer, Chip> chips){
		Set<ComponentPinState> currentState = new HashSet<>();
		chips.entrySet()
				.stream()
				.filter(entry -> entry.getValue() instanceof HeaderOut)
				.forEach(entry ->
					entry.getValue()
							.getPinMap()
							.forEach((pinId, pin) ->
								currentState.add(new ComponentPinState(entry.getKey(), pinId, pin.getPinState()))
							)
				);
		return currentState;
	}

	public static Set<ComponentPinState> saveCircuitState(Map<Integer, Chip> chips){
		Set<ComponentPinState> currentState = new HashSet<>();
		chips.forEach((componentId, chip) ->
			chip.getPinMap()
					.forEach((pinId, pin) ->
						currentState.add(new ComponentPinState(componentId, pinId, pin.getPinState()))
					)
		);
		return currentState;
	}
}
