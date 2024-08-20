package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.*;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.Pin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StationaryStateTest {

//	private Simulation simulation;
//
//	@BeforeEach
//	void setUp() {
//		simulation = new Simulation();
//	}
//
//	@Test
//	void testStationaryStateWithValidInput() throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent{
//		int chipId1 = simulation.createChip(7400);
//		int chipId2 = simulation.createInputPinHeader(2);
//		int chipId3 = simulation.createOutputPinHeader(1);
//
//		Set<ComponentPinState> states = new HashSet<>();
//		states.add(new ComponentPinState(chipId2, 1, PinState.HIGH));
//		states.add(new ComponentPinState(chipId2, 3, PinState.LOW));
//
//		simulation.connect(chipId2, 2, chipId1, 1);
//		simulation.connect(chipId2, 2, chipId1, 2);
//		simulation.connect(chipId1, 3, chipId3, 1);
//
//		simulation.stationaryState(states);
//
//		assertEquals(PinState.HIGH, simulation.chips.get(chipId1).getPinMap().get(1).getPinState());
//		assertEquals(PinState.LOW, simulation.chips.get(chipId1).getPinMap().get(2).getPinState());
//		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId1).getPinMap().get(3).getPinState());
//	}
//
//	@Test
//	void testStationaryStateThrowsUnknownStateException() throws UnknownChip {
//		int chipId1 = simulation.createChip(7400);
//		int chipId2 = simulation.createInputPinHeader(2);
//
//		Chip chip1 = simulation.chips.get(chipId1);
//		chip1.putToPinMap(1, new Pin());
//		chip1.putToPinMap(2, new Pin());
//		chip1.putToPinMap(3, new Pin());
//
//		Set<ComponentPinState> states = new HashSet<>();
//		states.add(new ComponentPinState(chipId2, 1, PinState.HIGH));
//		states.add(new ComponentPinState(chipId2, 2, PinState.UNKNOWN));
//
//		simulation.connect(chipId2, 1, chipId1, 1);
//		simulation.connect(chipId2, 2, chipId1, 2);
//
//		assertThrows(UnknownStateException.class, () -> simulation.stationaryState(states),
//					 "Should throw UnknownStateException for unknown pin state.");
//	}
//
//	@Test
//	void testStationaryStateWithMultipleChips() throws UnknownChip, UnknownStateException {
//		int chipId1 = simulation.createChip(7400);
//		int chipId2 = simulation.createChip(7402);
//		int chipId3 = simulation.createOutputPinHeader(2);
//
//		Chip chip1 = simulation.chips.get(chipId1);
//		Chip chip2 = simulation.chips.get(chipId2);
//		chip1.putToPinMap(1, new Pin());
//		chip1.putToPinMap(2, new Pin());
//		chip1.putToPinMap(3, new Pin());
//
//		chip2.putToPinMap(1, new Pin());
//		chip2.putToPinMap(2, new Pin());
//		chip2.putToPinMap(3, new Pin());
//
//		Set<ComponentPinState> states = new HashSet<>();
//		states.add(new ComponentPinState(chipId1, 1, PinState.HIGH));
//		states.add(new ComponentPinState(chipId1, 2, PinState.LOW));
//		states.add(new ComponentPinState(chipId2, 1, PinState.LOW));
//		states.add(new ComponentPinState(chipId2, 2, PinState.HIGH));
//
//		simulation.connect(chipId1, 3, chipId3, 1);
//		simulation.connect(chipId2, 3, chipId3, 2);
//
//		simulation.stationaryState(states);
//
//		assertEquals(PinState.HIGH, simulation.chips.get(chipId3).getPinMap().get(1).getPinState());
//		assertEquals(PinState.LOW, simulation.chips.get(chipId3).getPinMap().get(2).getPinState());
//	}
//
//	@Test
//	void testStationaryStateWithNoStateChange() throws UnknownChip, UnknownStateException {
//		int chipId1 = simulation.createChip(7404);
//		int chipId2 = simulation.createOutputPinHeader(1);
//
//		Chip chip1 = simulation.chips.get(chipId1);
//		chip1.putToPinMap(1, new Pin());
//		chip1.putToPinMap(2, new Pin());
//
//		Set<ComponentPinState> states = new HashSet<>();
//		states.add(new ComponentPinState(chipId1, 1, PinState.LOW));
//		states.add(new ComponentPinState(chipId1, 2, PinState.LOW));
//
//		simulation.connect(chipId1, 1, chipId2, 1);
//
//		simulation.stationaryState(states);
//
//		assertEquals(PinState.LOW, simulation.chips.get(chipId1).getPinMap().get(1).getPinState());
//		assertEquals(PinState.LOW, simulation.chips.get(chipId1).getPinMap().get(2).getPinState());
//		assertEquals(PinState.UNKNOWN, simulation.chips.get(chipId2).getPinMap().get(1).getPinState());
//	}
//
//	@Test
//	void testStationaryStateWithHeaderInAndOut() throws UnknownChip, UnknownStateException {
//		int chipId1 = simulation.createInputPinHeader(2);
//		int chipId2 = simulation.createChip(7410);
//		int chipId3 = simulation.createOutputPinHeader(2);
//
//		Chip headerIn = simulation.chips.get(chipId1);
//		headerIn.putToPinMap(1, new Pin());
//		headerIn.putToPinMap(2, new Pin());
//
//		Chip chip = simulation.chips.get(chipId2);
//		chip.putToPinMap(1, new Pin());
//		chip.putToPinMap(2, new Pin());
//
//		Set<ComponentPinState> states = new HashSet<>();
//		states.add(new ComponentPinState(chipId1, 1, PinState.HIGH));
//		states.add(new ComponentPinState(chipId1, 2, PinState.LOW));
//
//		simulation.connect(chipId1, 1, chipId2, 1);
//		simulation.connect(chipId1, 2, chipId2, 2);
//		simulation.connect(chipId2, 1, chipId3, 1);
//		simulation.connect(chipId2, 2, chipId3, 2);
//
//		simulation.stationaryState(states);
//
//		assertEquals(PinState.HIGH, simulation.chips.get(chipId3).getPinMap().get(1).getPinState());
//		assertEquals(PinState.LOW, simulation.chips.get(chipId3).getPinMap().get(2).getPinState());
//	}
}
