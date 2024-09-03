package edu;

import edu.manager.CircuitManager;
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

	private CircuitManager circuitManager;

	@BeforeEach
	void setUp() {
		circuitManager = new CircuitManager();
	}

	@Test
	void testConnect() throws UnknownComponent, UnknownPin, ShortCircuitException, UnknownChip{
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createOutputPinHeader(1);

		circuitManager.connect(chipId1, 3, chipId2, 1);

		assertTrue(circuitManager.getDirectConnections().stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 1),
				   "Chip1 should be connected to Chip2.");
	}

	@Test
	void testConnectThrowsUnknownComponent() {
		assertThrows(UnknownComponent.class, () -> {
			circuitManager.connect(100, 1, 101, 1);
		}, "Should throw UnknownComponent if component does not exist.");
	}

	@Test
	void testConnectThrowsUnknownPin() throws UnknownChip{
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createOutputPinHeader(1);

		assertThrows(UnknownPin.class, () -> {
			circuitManager.connect(chipId1, 7, chipId2, 1);
		}, "Should throw UnknownPin if pin does not exist.");
	}

	@Test
	@Description("Can again provide the same connection and exception won't be thrown and in directConnections" +
			" will be only one record.")
	void testConnectSecondAttemptAddTheSameConnection() throws UnknownComponent, UnknownPin, ShortCircuitException,
			UnknownChip{
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createOutputPinHeader(1);

		circuitManager.connect(chipId1, 3, chipId2, 1);
		circuitManager.connect(chipId1, 3, chipId2, 1);

		assertEquals(1, circuitManager.getDirectConnections().size());
	}

	@Test
	@Description("Two same connect() given size 1 form directConnection.")
	void testConnect2SameConnectGiven1DirectConnection() throws UnknownComponent, UnknownPin,
			ShortCircuitException,
			UnknownChip{
		int chipId1 = circuitManager.createChip(7402);
		int chipId2 = circuitManager.createOutputPinHeader(1);

		circuitManager.connect(chipId1, 3, chipId2, 1);
		circuitManager.connect(chipId1, 3, chipId2, 1);

		assertEquals(1, circuitManager.getDirectConnections().size());
	}

	//CON 0
	@Test
	void testConnectThrowsShortCircuitExceptionOnMultipleOutputsToSameInput() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7402);

		circuitManager.connect(chipId1, 3, chipId1, 1);
																					//1      1        0        1
		assertThrows(ShortCircuitException.class, () -> { circuitManager.connect(chipId2, 1, chipId1, 1); },
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
	}

	//shortCircutInDirectConnectionManyChips - to jest dobry test ale spróbuj pokombinować z kolejnością podpinania
	@Test
	void testConnectThrowsShortCircuitExceptionOnMultipleOutputsToSameInputV2() throws UnknownChip, UnknownComponent,
			UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(7);
		int chipId1 = circuitManager.createChip(7402);
		int chipId2 = circuitManager.createChip(74152);
		int chipId3 = circuitManager.createChip(74138);
		int chipId4 = circuitManager.createChip(7482);
		int chipId5 = circuitManager.createChip(7444);

		circuitManager.connect(chipId0, 3, chipId1, 2);

		assertThrows(ShortCircuitException.class, () -> { circuitManager.connect(chipId0, 5, chipId1, 2); },
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
		assertThrows(ShortCircuitException.class, () -> { circuitManager.connect(chipId1, 2, chipId0, 5); },
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");

		assertThrows(ShortCircuitException.class, () -> { circuitManager.connect(chipId2, 6, chipId1, 2); },
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
		assertThrows(ShortCircuitException.class, () -> { circuitManager.connect(chipId3, 12, chipId1, 2); },
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
		assertThrows(ShortCircuitException.class, () -> { circuitManager.connect(chipId4, 1, chipId1, 2); },
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
		assertThrows(ShortCircuitException.class, () -> { circuitManager.connect(chipId5, 4, chipId1, 2); },
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
	}

	@Test
	void testConnectThrowsShortCircuitExceptionOnConnectingToHeaderIn() throws UnknownChip, UnknownComponent, UnknownPin {
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createInputPinHeader(1);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId1, 3, chipId2, 1),
					 "Should throw ShortCircuitException when connecting output to HeaderIn input.");
	}

	@Test
	void testConnectThrowsShortCircuitExceptionOnConnectingOutputToOutput() throws UnknownChip, UnknownComponent, UnknownPin {
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7404);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId1, 3, chipId2, 2),
					 "Should throw ShortCircuitException when connecting two output pins.");
	}

	@Test
	void testConnectChip7402ToHeaderOut() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = circuitManager.createChip(7402);
		int chipId2 = circuitManager.createOutputPinHeader(1);

		circuitManager.connect(chipId1, 1, chipId2, 1);

		assertTrue(circuitManager.getDirectConnections().stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 1),
				   "Chip7402 should be connected to HeaderOut.");
	}

	@Test
	void testConnectChip7404ToChip7402() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = circuitManager.createChip(7404);
		int chipId2 = circuitManager.createChip(7402);

		circuitManager.connect(chipId1, 2, chipId2, 3);

		assertTrue(circuitManager.getDirectConnections().stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 3),
				   "Chip7404 should be connected to Chip7402.");
	}

	@Test
	void testConnectChip7408ToChip7410() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = circuitManager.createChip(7408);
		int chipId2 = circuitManager.createChip(7410);

		circuitManager.connect(chipId1, 3, chipId2, 13);

		assertTrue(circuitManager.getDirectConnections().stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 13),
				   "Chip7408 should be connected to Chip7410.");
	}

	@Test
	void testConnectChip7404ToHeaderInThrowsException() throws UnknownChip, UnknownComponent, UnknownPin {
		int chipId1 = circuitManager.createChip(7404);
		int chipId2 = circuitManager.createInputPinHeader(1);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId1, 2, chipId2, 1),
					 "Should throw ShortCircuitException when connecting Chip7404 output to HeaderIn.");
	}

	@Test
	void testConnectChip7410ToHeaderOut() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = circuitManager.createChip(7410);
		int chipId2 = circuitManager.createOutputPinHeader(1);

		circuitManager.connect(chipId1, 12, chipId2, 1);

		assertTrue(circuitManager.getDirectConnections().stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 1),
				   "Chip7410 should be connected to HeaderOut.");
	}

	@Test
	void testConnectMultipleChipsThrowsShortCircuitException() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException {
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7402);
		int chipId3 = circuitManager.createChip(7404);

		circuitManager.connect(chipId1, 3, chipId2, 11);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 2, chipId2, 11),
					 "Should throw ShortCircuitException when multiple outputs connect to the same input.");
	}

	@Test
	void testConnectChipToItselfThrowsException() throws UnknownChip, UnknownComponent, UnknownPin {
		int chipId = circuitManager.createChip(7408);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId, 3, chipId, 3),
					 "Should throw ShortCircuitException when connecting a chip's output to its own output.");
	}

	@Test
	void testValidConnectionWhenInputIsConnectedToOtherInput() throws UnknownChip, UnknownComponent, UnknownPin,
			ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);

		circuitManager.connect(chipId0, 1, chipId1, 1);
		circuitManager.connect(chipId0, 2, chipId1, 2);

		assertDoesNotThrow(() -> circuitManager.connect(chipId1, 1, chipId2, 1));
		assertDoesNotThrow(() -> circuitManager.connect(chipId1, 2, chipId2, 2));
	}

	//SCiE 0
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOtherInputAndThe2ndInputIsConnectedToOutput() throws UnknownChip,	UnknownComponent,
			UnknownPin,	ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);

		circuitManager.connect(chipId0, 1, chipId1, 1);
		circuitManager.connect(chipId0, 2, chipId1, 2);

		circuitManager.connect(chipId1, 1, chipId2, 1);
		circuitManager.connect(chipId1, 2, chipId2, 2);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId2, 3, chipId2, 2),
					 "");

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId2, 2, chipId2, 3),
					 "");
	}

	//SCiE 1
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOutputAndWhenThatInputHasSignalFromOtherOutputMoreComplex() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);
		int chipId3 = circuitManager.createChip(7400);

		circuitManager.connect(chipId0, 1, chipId1, 1);
		circuitManager.connect(chipId0, 2, chipId1, 2);

		circuitManager.connect(chipId1, 1, chipId2, 4);
		circuitManager.connect(chipId1, 2, chipId2, 5);

		circuitManager.connect(chipId2, 4, chipId3, 10);
		circuitManager.connect(chipId2, 5, chipId3, 9);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 8, chipId3, 10),
					 "");

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 10, chipId3, 8),
					 "");
	}

	//SCiE 2 - odwórć kolejność łączeń z //SCiE 1
	//@Disabled
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOutputAndWhenThatInputHasSignalFromOtherOutputMoreComplexV0() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);
		int chipId3 = circuitManager.createChip(7400);

		// inverted
		circuitManager.connect(chipId1, 1, chipId0, 1); // kolejność PinIn, PinOut
		circuitManager.connect(chipId1, 2, chipId0, 2);

		circuitManager.connect(chipId1, 1, chipId2, 4);
		circuitManager.connect(chipId1, 2, chipId2, 5);

		// inverted
		circuitManager.connect(chipId3, 10, chipId2, 4); // kolejność PinIn, PinIn
		circuitManager.connect(chipId3, 9, chipId2, 5);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 8, chipId3, 10),
					 "");

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 10, chipId3, 8),
					 "");
	}

	//SCiE 3
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOutputAndWhenThatInputHasSignalFromOtherOutputMoreComplexV1() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);
		int chipId3 = circuitManager.createChip(7400);

		circuitManager.connect(chipId0, 1, chipId1, 1);
		circuitManager.connect(chipId0, 2, chipId1, 2);

		circuitManager.connect(chipId1, 1, chipId2, 4);
		circuitManager.connect(chipId1, 2, chipId2, 5);

		circuitManager.connect(chipId2, 4, chipId3, 10);
		circuitManager.connect(chipId2, 5, chipId3, 9);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 8, chipId2, 4),
					 "");

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId2, 4, chipId3, 8),
					 "");
	}

	//SCiE 4 - zamieniona kolejność połączenia w stosunku do //SCiE 3 - najpierw out potem in
	// ten przypadek ze zmiana kolejności łączenia trudno spełnić i nie mam pewności ze on jest wymagany
	// odpuszczam póki co i rozważam inny przypadek
	//@Disabled
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOutputAndWhenThatInputHasSignalFromOtherOutputMoreComplexV2() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);
		int chipId3 = circuitManager.createChip(7400);

		//łaczenie chip 0 z 1
		circuitManager.connect(chipId0, 1, chipId1, 1);
		circuitManager.connect(chipId0, 2, chipId1, 2);

		//łaczenie chip 2 z 3
		circuitManager.connect(chipId2, 4, chipId3, 10);
		circuitManager.connect(chipId2, 5, chipId3, 9);

		circuitManager.connect(chipId3, 8, chipId2, 4);
		//componentManager.connect(chipId2, 4, chipId3, 8);

		//w tej kolejności łaczenie chip 1 z 2 - tu powinien być wyjątek
		circuitManager.connect(chipId1, 2, chipId2, 5);


		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId1, 1, chipId2, 4),
					 "");

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId2, 4, chipId1, 1),
					 "");

	}

	//SCiE 5
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOutputAndWhenThatInputHasSignalFromOtherOutputMoreComplexV3() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(1);
		int chipId1 = circuitManager.createChip(7404);
		int chipId2 = circuitManager.createChip(7404);
		int chipId3 = circuitManager.createChip(7404);
		int chipId4 = circuitManager.createChip(7404);
		int chipId5 = circuitManager.createChip(7404);

		circuitManager.connect(chipId0, 1, chipId1, 1);

		circuitManager.connect(chipId1, 1, chipId2, 3);

		circuitManager.connect(chipId2, 3, chipId3, 5);
		circuitManager.connect(chipId2, 3, chipId4, 9);

		circuitManager.connect(chipId4, 9, chipId5, 11);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 6, chipId5, 11),
					 "");
		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId5, 11, chipId3, 6),
					 "");

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId1, 2, chipId5, 11),
					 "");
		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId5, 11, chipId1, 2),
					 "");
	}

	//SCiE 6
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOutputAndWhenThatInputHasSignalFromOtherOutputMoreComplexV4() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);
		int chipId3 = circuitManager.createChip(7400);

		circuitManager.connect(chipId0, 1, chipId1, 1);
		circuitManager.connect(chipId0, 2, chipId1, 2);

		circuitManager.connect(chipId1, 3, chipId2, 4);
		circuitManager.connect(chipId1, 3, chipId2, 5);
		circuitManager.connect(chipId1, 3, chipId3, 10);

		circuitManager.connect(chipId2, 5, chipId3, 9);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId1, 6, chipId2, 5),
					 "");
		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId2, 5, chipId1, 6),
					 "");

	}

	//SCiE 7 - wariacja SCiE 6
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOutputAndWhenThatInputHasSignalFromOtherOutputMoreComplexV5() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);
		int chipId3 = circuitManager.createChip(7400);

		circuitManager.connect(chipId0, 1, chipId1, 1);
		circuitManager.connect(chipId0, 2, chipId1, 2);

		circuitManager.connect(chipId1, 3, chipId2, 4);
		circuitManager.connect(chipId1, 3, chipId2, 5);
		circuitManager.connect(chipId1, 3, chipId3, 10);

		circuitManager.connect(chipId2, 5, chipId3, 9);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId2, 6, chipId2, 5),
					 "");
		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId2, 5, chipId2, 6),
					 "");

	}

	//SCiE 8 - wariacja SCiE 6
	@Test
	void testThrowsShortCircuitExceptionWhenInputIsConnectedToOutputAndWhenThatInputHasSignalFromOtherOutputMoreComplexV6() throws UnknownChip, UnknownComponent, UnknownPin, ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7400);
		int chipId2 = circuitManager.createChip(7400);
		int chipId3 = circuitManager.createChip(7400);

		circuitManager.connect(chipId0, 1, chipId1, 1);
		circuitManager.connect(chipId0, 2, chipId1, 2);

		circuitManager.connect(chipId1, 3, chipId2, 4);
		circuitManager.connect(chipId1, 3, chipId2, 5);
		circuitManager.connect(chipId1, 3, chipId3, 10);

		circuitManager.connect(chipId2, 5, chipId3, 9);

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 8, chipId2, 5),
					 "");
		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId2, 5, chipId3, 8),
					 "");

		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 8, chipId3, 9),
					 "");
		assertThrows(ShortCircuitException.class, () -> circuitManager.connect(chipId3, 9, chipId3, 8),
					 "");

	}

	//SCiE 9 - układ z img doUkładu17.jpg
	@Test
	void testNoThrowExceptionExampleFromDoUkladu17_simulation01() throws UnknownChip, UnknownComponent, UnknownPin,
			ShortCircuitException{
		int chipId0 = circuitManager.createInputPinHeader(2);
		int chipId1 = circuitManager.createChip(7431);
		int chipId2 = circuitManager.createChip(7404);
		int chipId3 = circuitManager.createOutputPinHeader(4);


		circuitManager.connect(chipId0, 1, chipId1, 11);
		circuitManager.connect(chipId0, 2, chipId1, 10);

		circuitManager.connect(chipId1, 9, chipId3, 1);
		circuitManager.connect(chipId1, 9, chipId2, 13);

		//componentManager.connect(chipId2, 12, chipId2, 11);

		//componentManager.connect(chipId2, 11, chipId3, 2);

		circuitManager.connect(chipId2, 10, chipId3, 3);
		circuitManager.connect(chipId2, 10, chipId1, 1);

		circuitManager.connect(chipId1, 2, chipId3, 4);
		assertDoesNotThrow(() -> circuitManager.connect(chipId2, 12, chipId2, 11));
		assertDoesNotThrow(() -> circuitManager.connect(chipId2, 11, chipId3, 2));
	}
}
