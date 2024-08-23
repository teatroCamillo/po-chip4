package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main{
	public static void main(String[] args) throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException{

		Simulation simulation;

		simulation = new Simulation();

		int chipIn1 = simulation.createInputPinHeader(3);
		int chip7400 = simulation.createChip(7400);
		int chip7402 = simulation.createChip(7402);
		int chip7404 = simulation.createChip(7404);
		int chipOut1 = simulation.createOutputPinHeader(2);

		simulation.connect(chipIn1, 1, chip7400, 1);
		simulation.connect(chipIn1, 2, chip7400, 2);
		simulation.connect(chipIn1, 3, chip7402, 9);

		simulation.connect(chip7400, 3, chip7402, 8);
		simulation.connect(chip7400, 3, chipOut1, 1);

		simulation.connect(chip7402, 10, chip7404, 3);

		simulation.connect(chip7404, 4, chipOut1, 2);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 2, PinState.LOW));
		states.add(new ComponentPinState(chipIn1, 3, PinState.HIGH));

		simulation.stationaryState(states);

		Set<ComponentPinState> states0 = new HashSet<>();
		states0.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn1, 2, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn1, 3, PinState.LOW));

		Map<Integer, Set<ComponentPinState>> result = simulation.simulation(states0, 4);

		PinState pin1 = result.get(1).stream()
				.filter(state -> state.pinId() == 1)
				.peek(System.out::println)
				.findFirst().orElseThrow().state();

		PinState pin2 = result.get(1).stream()
				.filter(state -> state.pinId() == 2)
				.peek(System.out::println)
				.findFirst().orElseThrow().state();
	}
}
