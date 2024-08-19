package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.ShortCircuitException;
import edu.uj.po.simulation.interfaces.UnknownChip;
import edu.uj.po.simulation.interfaces.UnknownComponent;
import edu.uj.po.simulation.interfaces.UnknownPin;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectTest{

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	@Test
	void testConnect() throws UnknownComponent, UnknownPin, ShortCircuitException, UnknownChip{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createOutputPinHeader(1);

		simulation.connect(chipId1, 3, chipId2, 1);

		assertTrue(simulation.directConnections.stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 1),
				   "Chip1 should be connected to Chip2.");
	}

	@Test
	void testConnectThrowsUnknownComponent() {
		assertThrows(UnknownComponent.class, () -> {
			simulation.connect(100, 1, 101, 1);
		}, "Should throw UnknownComponent if component does not exist.");
	}

	@Test
	void testConnectThrowsUnknownPin() throws UnknownChip{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createOutputPinHeader(1);

		assertThrows(UnknownPin.class, () -> {
			simulation.connect(chipId1, 7, chipId2, 1);
		}, "Should throw UnknownPin if pin does not exist.");
	}

	@Test
	@Description("Can again provide the same connection and exception won't be thrown and in directConnections" +
			" will be only one record.")
	void testConnectSecondAttemptAddTheSameConnection() throws UnknownComponent, UnknownPin, ShortCircuitException,
			UnknownChip{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createOutputPinHeader(1);

		simulation.connect(chipId1, 3, chipId2, 1);
		simulation.connect(chipId1, 3, chipId2, 1);

		assertEquals(1, simulation.directConnections.size());
	}

	@Test
	@Description("Two same connect() given size 1 form directConnection.")
	void testConnect2SameConnectGiven1DirectConnection() throws UnknownComponent, UnknownPin,
			ShortCircuitException,
			UnknownChip{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createOutputPinHeader(1);

		simulation.connect(chipId1, 3, chipId2, 1);
		simulation.connect(chipId1, 3, chipId2, 1);

		assertEquals(1, simulation.directConnections.size());
	}

	@Test
	void testConnectThrowsShortCircuitExceptionOnMultipleOutputsToSameInput() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createChip(7400);

		simulation.connect(chipId1, 3, chipId1, 1);

		assertThrows(ShortCircuitException.class, () -> { simulation.connect(chipId2, 3, chipId1, 1); },
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
	}

	@Test
	void testConnectThrowsShortCircuitExceptionOnConnectingToHeaderIn() throws UnknownChip, UnknownComponent, UnknownPin {
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createInputPinHeader(1);

		assertThrows(ShortCircuitException.class, () -> simulation.connect(chipId1, 3, chipId2, 1),
					 "Should throw ShortCircuitException when connecting output to HeaderIn input.");
	}

	@Test
	void testConnectThrowsShortCircuitExceptionOnConnectingOutputToOutput() throws UnknownChip, UnknownComponent, UnknownPin {
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createChip(7400);

		assertThrows(ShortCircuitException.class, () -> simulation.connect(chipId1, 3, chipId2, 3),
					 "Should throw ShortCircuitException when connecting two output pins.");
	}

	// TODO nowe testy
	@Test
	void testConnectChip7402ToHeaderOut() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = simulation.createChip(7402);
		int chipId2 = simulation.createOutputPinHeader(1);

		simulation.connect(chipId1, 1, chipId2, 1);

		assertTrue(simulation.directConnections.stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 1),
				   "Chip7402 should be connected to HeaderOut.");
	}

	@Test
	void testConnectChip7404ToChip7402() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = simulation.createChip(7404);
		int chipId2 = simulation.createChip(7402);

		simulation.connect(chipId1, 2, chipId2, 3);

		assertTrue(simulation.directConnections.stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 3),
				   "Chip7404 should be connected to Chip7402.");
	}

	@Test
	void testConnectChip7408ToChip7410() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = simulation.createChip(7408);
		int chipId2 = simulation.createChip(7410);

		simulation.connect(chipId1, 3, chipId2, 13);

		assertTrue(simulation.directConnections.stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 13),
				   "Chip7408 should be connected to Chip7410.");
	}

	@Test
	void testConnectChip7404ToHeaderInThrowsException() throws UnknownChip, UnknownComponent, UnknownPin {
		int chipId1 = simulation.createChip(7404);
		int chipId2 = simulation.createInputPinHeader(1);

		assertThrows(ShortCircuitException.class, () -> simulation.connect(chipId1, 2, chipId2, 1),
					 "Should throw ShortCircuitException when connecting Chip7404 output to HeaderIn input.");
	}

	@Test
	void testConnectChip7410ToHeaderOut() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = simulation.createChip(7410);
		int chipId2 = simulation.createOutputPinHeader(1);

		simulation.connect(chipId1, 12, chipId2, 1);

		assertTrue(simulation.directConnections.stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 1),
				   "Chip7410 should be connected to HeaderOut.");
	}

	@Test
	void testConnectMultipleChipsThrowsShortCircuitException() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createChip(7402);
		int chipId3 = simulation.createChip(7404);

		simulation.connect(chipId1, 3, chipId2, 11);

		assertThrows(ShortCircuitException.class, () -> simulation.connect(chipId3, 2, chipId2, 11),
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
	}

	@Test
	void testConnectChipToItselfThrowsException() throws UnknownChip, UnknownComponent, UnknownPin {
		int chipId = simulation.createChip(7408);

		assertThrows(ShortCircuitException.class, () -> simulation.connect(chipId, 3, chipId, 3),
					 "Should throw ShortCircuitException when connecting a chip's output to its own output.");
	}
}
