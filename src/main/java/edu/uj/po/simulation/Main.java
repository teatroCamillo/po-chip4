package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.*;

import java.util.HashSet;
import java.util.Set;

public class Main{
	public static void main(String[] args) throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException{
		Simulation simulation = new Simulation();
		Set<Integer> idSet = new HashSet<>();

		int headerId0 = simulation.createInputPinHeader(2);
		idSet.add(headerId0);

		int headerId1 = simulation.createOutputPinHeader(1);
		idSet.add(headerId1);

		int chipId0 = simulation.createChip(7400);
		idSet.add(chipId0);

		simulation.connect(headerId0, 2, chipId0, 1);
		simulation.connect(headerId0, 4, chipId0, 2);

		simulation.connect(chipId0, 3, headerId1, 1);

		simulation.getInfo(idSet);

		//stan stacjonarny - 0
		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(headerId0, 1, PinState.HIGH));
		states.add(new ComponentPinState(headerId0, 3, PinState.LOW));

		simulation.stationaryState(states);

	}
}
