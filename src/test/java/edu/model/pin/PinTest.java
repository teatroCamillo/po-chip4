package edu.model.pin;

import edu.uj.po.simulation.interfaces.PinState;
import edu.model.Pin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PinTest {

	private AbstractPin pinIn;
	private AbstractPin pinOut;

	@BeforeEach
	void setUp() {
		pinIn = new PinIn();
		pinOut = new PinOut();
	}

	@Test
	void testPinInInitialState() {
		assertEquals(PinState.UNKNOWN, pinIn.getPinState(), "Initial state of PinIn should be UNKNOWN.");
	}

	@Test
	void testPinOutInitialState() {
		assertEquals(PinState.UNKNOWN, pinOut.getPinState(), "Initial state of PinOut should be UNKNOWN.");
	}

	@Test
	void testSetPinState() {
		pinIn.setPinState(PinState.HIGH);
		pinOut.setPinState(PinState.LOW);

		assertEquals(PinState.HIGH, pinIn.getPinState(), "PinIn state should be set to HIGH.");
		assertEquals(PinState.LOW, pinOut.getPinState(), "PinOut state should be set to LOW.");
	}
}
