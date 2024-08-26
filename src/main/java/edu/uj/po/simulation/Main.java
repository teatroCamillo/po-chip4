package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main{
	public static void main(String[] args) throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException{

		Simulation simulation;

		simulation = new Simulation();

		int chipIn1 = simulation.createInputPinHeader(2);
		int chip7400 = simulation.createChip(7400);
		int chipOut1 = simulation.createOutputPinHeader(1);

		simulation.connect(chipIn1, 1, chip7400, 1);
		simulation.connect(chipIn1, 2, chip7400, 2);

		simulation.connect(chip7400, 3, chipOut1, 1);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 2, PinState.LOW));

		simulation.stationaryState(states);

		Set<ComponentPinState> states0 = new HashSet<>();
		states0.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn1, 2, PinState.HIGH));

		Set<Integer> result = simulation.optimize(states0, 4);
		System.out.println("RESULT");
		result.forEach(System.out::println);
	}
}
