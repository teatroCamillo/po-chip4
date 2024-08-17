package edu.uj.po.simulation.model.chip;

import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.Connection;
import edu.uj.po.simulation.model.Pin;
import edu.uj.po.simulation.model.pin.PinIn;
import edu.uj.po.simulation.model.pin.PinOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChipTest {

	private Chip headerIn;
	private Chip headerOut;
	private Chip chip7400;

	@BeforeEach
	void setUp() {
		headerIn = new HeaderIn();
		headerOut = new HeaderOut();
		chip7400 = new Chip7400();
	}

	@Test
	void testInitialPinState() {
		headerIn.putToPinMap(1, new PinIn());
		headerOut.putToPinMap(1, new PinOut());
		chip7400.putToPinMap(1, new PinIn());

		assertEquals(PinState.UNKNOWN, headerIn.getPinMap().get(1).getPinState(), "Initial state of PinIn in HeaderIn should be UNKNOWN.");
		assertEquals(PinState.UNKNOWN, headerOut.getPinMap().get(1).getPinState(), "Initial state of PinOut in HeaderOut should be UNKNOWN.");
		assertEquals(PinState.UNKNOWN, chip7400.getPinMap().get(1).getPinState(), "Initial state of PinIn in Chip7400 should be UNKNOWN.");
	}

	@Test
	void testAddNewConnection() {
		headerIn.addNewConnection(1, 2, 1);
		Set<Connection> connections = headerIn.getDirectConnections();

		assertEquals(1, connections.size(), "There should be one connection in the HeaderIn.");
	}

	@Test
	void testPropagateSignal() {
		headerIn.putToPinMap(1, new PinIn());
		headerOut.putToPinMap(1, new PinOut());

		headerIn.getPinMap().get(1).setPinState(PinState.HIGH);
		headerIn.addNewConnection(1, 2, 1);

		Map<Integer, Chip> chipMap = new HashMap<>();
		chipMap.put(1, headerIn);
		chipMap.put(2, headerOut);

		headerIn.propagateSignal(chipMap);

		assertEquals(PinState.HIGH, headerOut.getPinMap().get(1).getPinState(), "Signal should propagate from HeaderIn to HeaderOut.");
	}

	@Test
	void testExecuteHeaderIn() {
		headerIn.putToPinMap(1, new PinIn());
		headerIn.putToPinMap(2, new PinOut());

		headerIn.getPinMap().get(1).setPinState(PinState.HIGH);
		headerIn.execute();

		// Weryfikacja logiki wykonania, specyficzna dla implementacji HeaderIn
		// Sprawdzenie, czy stan pinu wyjściowego (2) został ustawiony zgodnie z logiką headerCalculation (tożsamościową)
		assertEquals(PinState.HIGH, headerIn.getPinMap().get(2).getPinState(),
					 "PinOut powinien mieć stan HIGH po wywołaniu execute w HeaderIn.");
	}

	@Test
	void testExecuteHeaderOut() {
		headerOut.putToPinMap(1, new PinIn());
		headerOut.putToPinMap(2, new PinOut());

		headerOut.getPinMap().get(1).setPinState(PinState.LOW);
		headerOut.execute();

		// Weryfikacja logiki wykonania, specyficzna dla implementacji HeaderOut
		// Sprawdzenie, czy stan pinu wyjściowego (2) został ustawiony zgodnie z logiką headerCalculation (tożsamościową)
		assertEquals(PinState.LOW, headerOut.getPinMap().get(2).getPinState(),
					 "PinOut powinien mieć stan LOW po wywołaniu execute w HeaderOut.");
	}

	@Test
	void testExecuteChip7400() {
		chip7400.putToPinMap(1, new PinIn());
		chip7400.putToPinMap(2, new PinIn());
		chip7400.putToPinMap(3, new PinOut());

		chip7400.putToPinMap(4, new PinIn());
		chip7400.putToPinMap(5, new PinIn());
		chip7400.putToPinMap(6, new PinOut());

		chip7400.putToPinMap(8, new PinOut());
		chip7400.putToPinMap(9, new PinIn());
		chip7400.putToPinMap(10, new PinIn());

		chip7400.putToPinMap(11, new PinOut());
		chip7400.putToPinMap(12, new PinIn());
		chip7400.putToPinMap(13, new PinIn());

		// LL
		chip7400.getPinMap().get(1).setPinState(PinState.LOW);
		chip7400.getPinMap().get(2).setPinState(PinState.LOW);
		// LH
		chip7400.getPinMap().get(4).setPinState(PinState.LOW);
		chip7400.getPinMap().get(5).setPinState(PinState.HIGH);
		// HL
		chip7400.getPinMap().get(9).setPinState(PinState.HIGH);
		chip7400.getPinMap().get(10).setPinState(PinState.LOW);
		// HH
		chip7400.getPinMap().get(12).setPinState(PinState.HIGH);
		chip7400.getPinMap().get(13).setPinState(PinState.HIGH);

		chip7400.execute();

		assertEquals(PinState.HIGH, chip7400.getPinMap().get(3).getPinState(),
					 "PinOut (3) powinien mieć stan HIGH po wywołaniu execute w Chip7400 dla LL.");

		assertEquals(PinState.HIGH, chip7400.getPinMap().get(6).getPinState(),
					 "PinOut (6) powinien mieć stan HIGH po wywołaniu execute w Chip7400 dla LH.");

		assertEquals(PinState.HIGH, chip7400.getPinMap().get(8).getPinState(),
					 "PinOut (8) powinien mieć stan HIGH po wywołaniu execute w Chip7400 dla HL.");

		assertEquals(PinState.LOW, chip7400.getPinMap().get(11).getPinState(),
					 "PinOut (11) powinien mieć stan LOW po wywołaniu execute w Chip7400 dla HH.");
	}
}
