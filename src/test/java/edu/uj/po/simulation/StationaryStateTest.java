package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StationaryStateTest {

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	@Test
	void testStationaryStateWithValidInput() throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createInputPinHeader(2);
		int chipId3 = simulation.createOutputPinHeader(1);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId2, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId2, 2, PinState.LOW));

		simulation.connect(chipId2, 2, chipId1, 1);
		simulation.connect(chipId2, 1, chipId1, 2);
		simulation.connect(chipId1, 3, chipId3, 1);

		simulation.stationaryState(states);

		assertEquals(PinState.LOW, simulation.chips.get(chipId1).getPinMap().get(1).getPinState());
		assertEquals(PinState.HIGH, simulation.chips.get(chipId1).getPinMap().get(2).getPinState());
		assertEquals(PinState.HIGH, simulation.chips.get(chipId1).getPinMap().get(3).getPinState());
	}

	@Test
	void testStationaryStateThrowsUnknownStateException() throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createInputPinHeader(2);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId2, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId2, 2, PinState.UNKNOWN));

		simulation.connect(chipId2, 1, chipId1, 1);
		simulation.connect(chipId2, 2, chipId1, 2);

		assertThrows(UnknownStateException.class, () -> simulation.stationaryState(states),
					 "Should throw UnknownStateException for unknown pin state.");
	}

	@Test
	void testStationaryStateWithMultipleChips() throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent{
		int chipId0 = simulation.createInputPinHeader(2);
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createChip(7402);
		int chipId3 = simulation.createOutputPinHeader(2);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId0, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId0, 2, PinState.HIGH));

		simulation.connect(chipId0, 1, chipId1, 4);
		simulation.connect(chipId0, 2, chipId1, 5);

		simulation.connect(chipId0, 1, chipId2, 8);
		simulation.connect(chipId0, 2, chipId2, 9);

		simulation.connect(chipId1, 6, chipId3, 1);
		simulation.connect(chipId2, 10, chipId3, 2);

		simulation.stationaryState(states);

		assertEquals(PinState.LOW, simulation.chips.get(chipId3).getPinMap().get(1).getPinState());
		assertEquals(PinState.LOW, simulation.chips.get(chipId3).getPinMap().get(2).getPinState());
	}

	@Test
	void testStationaryStateWithNoStateChangeOnChip7404() throws UnknownChip, UnknownStateException, UnknownPin,
			ShortCircuitException, UnknownComponent{
		int chipId0 = simulation.createInputPinHeader(2);
		int chipId1 = simulation.createChip(7404);
		int chipId2 = simulation.createOutputPinHeader(2);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId0, 1, PinState.LOW));
		states.add(new ComponentPinState(chipId0, 2, PinState.LOW));

		simulation.connect(chipId0, 1, chipId1, 1);
		simulation.connect(chipId0, 2, chipId1, 3);
		simulation.connect(chipId1, 2, chipId2, 1);
		simulation.connect(chipId1, 4, chipId2, 2);

		simulation.stationaryState(states);

		assertEquals(PinState.LOW, simulation.chips.get(chipId0).getPinMap().get(1).getPinState());
		assertEquals(PinState.LOW, simulation.chips.get(chipId0).getPinMap().get(2).getPinState());
		assertEquals(PinState.HIGH, simulation.chips.get(chipId1).getPinMap().get(2).getPinState());
		assertEquals(PinState.HIGH, simulation.chips.get(chipId1).getPinMap().get(4).getPinState());
		assertEquals(PinState.HIGH, simulation.chips.get(chipId2).getPinMap().get(1).getPinState());
		assertEquals(PinState.HIGH, simulation.chips.get(chipId2).getPinMap().get(2).getPinState());

		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(5).getPinState());
		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(6).getPinState());
		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(8).getPinState());
		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(9).getPinState());
		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(10).getPinState());
		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(11).getPinState());
		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(12).getPinState());
		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(13).getPinState());
	}

	@Test
	void testStationaryStateWithHeaderInAndOut() throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent{
		int chipId1 = simulation.createInputPinHeader(2);
		int chipId2 = simulation.createChip(7410);
		int chipId3 = simulation.createOutputPinHeader(1);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId1, 2, PinState.LOW));

		simulation.connect(chipId1, 1, chipId2, 1);
		simulation.connect(chipId1, 2, chipId2, 2);
		simulation.connect(chipId1, 2, chipId2, 13);

		simulation.connect(chipId2, 12, chipId3, 1);

		simulation.stationaryState(states);

		assertEquals(PinState.HIGH, simulation.chips.get(chipId3).getPinMap().get(1).getPinState());
	}
}
