package edu;

import edu.uj.po.simulation.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;
import java.util.Map;
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

		Assertions.assertEquals(PinState.LOW, simulation.getChips().get(chipId1).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId1).getPinMap().get(2).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId1).getPinMap().get(3).getPinState());
	}

	//TODO: do rozważenia
	// Pin jest w stanie UNKONWN ale nie jest podłączony - nie rzuca wyjatku UnknownStateException
	@Disabled
	@Test
	void testStationaryStateNOTThrowsUnknownStateExceptionWhenPinHasUNKNOWNButIsNotConnected() throws UnknownChip,
			UnknownPin, ShortCircuitException, UnknownComponent {
		int chipId0 = simulation.createInputPinHeader(2);
		int chipId1 = simulation.createChip(7400);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId0, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId0, 2, PinState.UNKNOWN));

		simulation.connect(chipId0, 1, chipId1, 1);
		// simulation.connect(chipId2, 2, chipId1, 2);

		assertDoesNotThrow(() -> simulation.stationaryState(states),
						   "Should not throw UnknownStateException for unknown pin state.");
	}

	@Test
	void testStationaryStateThrowsUnknownStateExceptionWhenPinStateIsUKNOWNAndIsConnectedToo() throws UnknownChip,
			UnknownPin,
			ShortCircuitException,
			UnknownComponent{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createInputPinHeader(2);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId2, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId2, 2, PinState.UNKNOWN));

		simulation.connect(chipId2, 1, chipId1, 1);
		simulation.connect(chipId2, 2, chipId1, 2);

		assertThrows(UnknownStateException.class, () -> simulation.stationaryState(states),
					 "Should throw UnknownStateException for unknown pin state which is connected also.");
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

		Assertions.assertEquals(PinState.LOW, simulation.getChips().get(chipId3).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.LOW, simulation.getChips().get(chipId3).getPinMap().get(2).getPinState());
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

		Assertions.assertEquals(PinState.LOW, simulation.getChips().get(chipId0).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.LOW, simulation.getChips().get(chipId0).getPinMap().get(2).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId1).getPinMap().get(2).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId1).getPinMap().get(4).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId2).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId2).getPinMap().get(2).getPinState());

		Assertions.assertEquals(PinState.UNKNOWN, simulation.getChips().get(chipId1).getPinMap().get(5).getPinState());
		Assertions.assertEquals(PinState.UNKNOWN, simulation.getChips().get(chipId1).getPinMap().get(6).getPinState());
		Assertions.assertEquals(PinState.UNKNOWN, simulation.getChips().get(chipId1).getPinMap().get(8).getPinState());
		Assertions.assertEquals(PinState.UNKNOWN, simulation.getChips().get(chipId1).getPinMap().get(9).getPinState());
		Assertions.assertEquals(PinState.UNKNOWN, simulation.getChips().get(chipId1).getPinMap().get(10).getPinState());
		Assertions.assertEquals(PinState.UNKNOWN, simulation.getChips().get(chipId1).getPinMap().get(11).getPinState());
		Assertions.assertEquals(PinState.UNKNOWN, simulation.getChips().get(chipId1).getPinMap().get(12).getPinState());
		Assertions.assertEquals(PinState.UNKNOWN, simulation.getChips().get(chipId1).getPinMap().get(13).getPinState());
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

		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId3).getPinMap().get(1).getPinState());
	}


	@Test
	void testSetMomentZero(){
		int chipId1 = simulation.createInputPinHeader(2);

		Set<ComponentPinState> states0 = new HashSet<>();
		states0.add(new ComponentPinState(chipId1, 1, PinState.HIGH));
		states0.add(new ComponentPinState(chipId1, 2, PinState.LOW));

		simulation.simulationManager.setMomentZero(states0);

		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId1).getPinMap().get(1).getPinState());
	}

	@Test
	void testStationaryStateWithMultipleInputHeaders() throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent {
		int chipId1 = simulation.createInputPinHeader(2);
		int chipId2 = simulation.createInputPinHeader(2);
		int chipId3 = simulation.createChip(7400);
		int chipId4 = simulation.createOutputPinHeader(1);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId1, 2, PinState.HIGH));
		states.add(new ComponentPinState(chipId2, 1, PinState.LOW));
		states.add(new ComponentPinState(chipId2, 2, PinState.LOW));

		simulation.connect(chipId1, 1, chipId3, 1);
		simulation.connect(chipId2, 2, chipId3, 2);
		simulation.connect(chipId3, 3, chipId4, 1);

		simulation.stationaryState(states);

		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId4).getPinMap().get(1).getPinState());
	}

	@Test
	void testStationaryStateWithMultipleOutputHeaders() throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent {
		int chipId1 = simulation.createInputPinHeader(2);
		int chipId2 = simulation.createChip(7400);
		int chipId3 = simulation.createOutputPinHeader(1);
		int chipId4 = simulation.createOutputPinHeader(1);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId1, 2, PinState.LOW));

		simulation.connect(chipId1, 1, chipId2, 1);
		simulation.connect(chipId1, 2, chipId2, 2);
		simulation.connect(chipId2, 3, chipId3, 1);
		simulation.connect(chipId2, 3, chipId4, 1);

		simulation.stationaryState(states);

		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId3).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId4).getPinMap().get(1).getPinState());
	}

	@Test
	void testNoThrowExceptionExampleFromDoUkladu17_simulation01() throws UnknownChip,
			UnknownComponent, UnknownPin, ShortCircuitException, UnknownStateException{
		int chipId0 = simulation.createInputPinHeader(2);
		int chipId1 = simulation.createChip(7431);
		int chipId2 = simulation.createChip(7404);
		int chipId3 = simulation.createOutputPinHeader(4);


		simulation.connect(chipId0, 1, chipId1, 11);
		simulation.connect(chipId0, 2, chipId1, 10);

		simulation.connect(chipId1, 9, chipId3, 1);
		simulation.connect(chipId1, 9, chipId2, 13);

		simulation.connect(chipId2, 12, chipId2, 11);

		simulation.connect(chipId2, 11, chipId3, 2);

		simulation.connect(chipId2, 10, chipId3, 3);
		simulation.connect(chipId2, 10, chipId1, 1);

		simulation.connect(chipId1, 2, chipId3, 4);


		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipId0, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipId0, 2, PinState.HIGH));

		simulation.stationaryState(states);

		Assertions.assertEquals(PinState.LOW, simulation.getChips().get(chipId3).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId3).getPinMap().get(2).getPinState());
		Assertions.assertEquals(PinState.LOW, simulation.getChips().get(chipId3).getPinMap().get(3).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipId3).getPinMap().get(4).getPinState());
	}

	// T2
	@Test
	void testSimulationComplexCircuit() throws UnknownChip,
			UnknownStateException,	UnknownPin,
			ShortCircuitException, UnknownComponent {

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

		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipOut1).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipOut1).getPinMap().get(2).getPinState());
	}

	//T3
	@Test
	void testSimulationMaxComplexCircuit()
			throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent {
		int chipIn0 = simulation.createInputPinHeader(1);
		int chipIn1 = simulation.createInputPinHeader(2);
		int chipIn2 = simulation.createInputPinHeader(3);
		int chip7400 = simulation.createChip(7400);
		int chip7402 = simulation.createChip(7402);
		int chip7404 = simulation.createChip(7404);
		int chip7408 = simulation.createChip(7408);
		int chip7410 = simulation.createChip(7410);
		int chipOut0 = simulation.createOutputPinHeader(1);
		int chipOut1 = simulation.createOutputPinHeader(2);

		simulation.connect(chipIn0, 1, chip7410, 9);

		simulation.connect(chipIn1, 1, chip7410, 10);
		simulation.connect(chipIn1, 2, chip7410, 11);

		simulation.connect(chipIn2, 1, chip7408, 5);
		simulation.connect(chipIn2, 2, chip7402, 2);
		simulation.connect(chipIn2, 3, chip7402, 3);

		simulation.connect(chip7410, 8, chip7408, 4);

		simulation.connect(chip7408, 6, chip7404, 13);
		simulation.connect(chip7408, 6, chipOut1, 2);

		simulation.connect(chip7404, 12, chip7400, 4);

		simulation.connect(chip7402, 1, chip7400, 5);

		simulation.connect(chip7400, 6, chipOut0, 1);
		simulation.connect(chip7400, 6, chipOut1, 1);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipIn0, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 2, PinState.LOW));
		states.add(new ComponentPinState(chipIn2, 1, PinState.LOW));
		states.add(new ComponentPinState(chipIn2, 2, PinState.LOW));
		states.add(new ComponentPinState(chipIn2, 3, PinState.HIGH));

		simulation.stationaryState(states);

		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipOut0).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.HIGH, simulation.getChips().get(chipOut1).getPinMap().get(1).getPinState());
		Assertions.assertEquals(PinState.LOW, simulation.getChips().get(chipOut1).getPinMap().get(2).getPinState());
	}
}
