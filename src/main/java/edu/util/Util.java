package edu.util;

import edu.model.chip.HeaderOut;
import edu.uj.po.simulation.interfaces.ComponentPinState;
import edu.model.Chip;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class Util{

	private static Set<ComponentPinState> saveCircuitStateInternal(Map<Integer, Chip> chips, Predicate<Chip> chipFilter) {
		Set<ComponentPinState> currentState = new HashSet<>();
		chips.entrySet()
				.stream()
				.filter(entry -> chipFilter.test(entry.getValue()))
				.forEach(entry ->
								 entry.getValue()
										 .getPinMap()
										 .forEach((pinId, pin) ->
														  currentState.add(new ComponentPinState(entry.getKey(), pinId, pin.getPinState()))
										 )
				);
		return currentState;
	}

	public static Set<ComponentPinState> saveCircuitHeaderOutState(Map<Integer, Chip> chips) {
		return saveCircuitStateInternal(chips, chip -> chip instanceof HeaderOut);
	}

	public static Set<ComponentPinState> saveCircuitState(Map<Integer, Chip> chips) {
		return saveCircuitStateInternal(chips, chip -> true);
	}
}
