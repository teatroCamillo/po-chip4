package edu.model.chip;


import edu.Simulation;
import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.interfaces.UnknownChip;
import edu.model.pin.Pin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChipTest {

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	@Test
	void testInitialPinState() throws UnknownChip{
		int headerIn = simulation.createInputPinHeader(2);
		int headerOut = simulation.createOutputPinHeader(1);
		int chip7400 = simulation.createChip(7400);

		assertEquals(PinState.UNKNOWN, simulation.getChips().get(headerIn).getPinMap().get(1).getPinState(),
					 "Initial state of PinOut in HeaderIn should be UNKNOWN.");
		assertEquals(PinState.UNKNOWN, simulation.getChips().get(headerOut).getPinMap().get(1).getPinState(),
					 "Initial state of PinIn in HeaderOut should be UNKNOWN.");
		assertEquals(PinState.UNKNOWN, simulation.getChips().get(chip7400).getPinMap().get(1).getPinState(),
					 "Initial state of PinIn in Chip7400 should be UNKNOWN.");
	}

	@Test
	void testExecuteHeaderIn() {
		int headerIn = simulation.createInputPinHeader(2);

		Pin out = simulation.getChips().get(headerIn).getPinMap().get(1);
		out.setPinState(PinState.HIGH);

		assertEquals(PinState.HIGH, out.getPinState(),
					 "PinOut powinien mieć stan HIGH po wywołaniu execute w HeaderIn.");
	}

	@Test
	void testExecuteHeaderOut() {
		int headerOut = simulation.createInputPinHeader(2);

		Pin in = simulation.getChips().get(headerOut).getPinMap().get(1);
		in.setPinState(PinState.HIGH);

		assertEquals(PinState.HIGH, in.getPinState(),
					 "PinIn powinien mieć stan LOW po wywołaniu execute w HeaderOut.");
	}

	@Test
	void testExecuteChip7400() throws UnknownChip{
		int chip7400Id = simulation.createChip(7400);
		Chip chip7400 = simulation.getChips().get(chip7400Id);

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

		chip7400.simulate();

		assertEquals(PinState.HIGH, chip7400.getPinMap().get(3).getPinState(),
					 "PinOut (3) powinien mieć stan HIGH po wywołaniu execute w Chip7400 dla LL.");

		assertEquals(PinState.HIGH, chip7400.getPinMap().get(6).getPinState(),
					 "PinOut (6) powinien mieć stan HIGH po wywołaniu execute w Chip7400 dla LH.");

		assertEquals(PinState.HIGH, chip7400.getPinMap().get(8).getPinState(),
					 "PinOut (8) powinien mieć stan HIGH po wywołaniu execute w Chip7400 dla HL.");

		assertEquals(PinState.LOW, chip7400.getPinMap().get(11).getPinState(),
					 "PinOut (11) powinien mieć stan LOW po wywołaniu execute w Chip7400 dla HH.");
	}

	@Test
	void testExecuteChip7400WithUNKONOWNState() throws UnknownChip{
		int chip7400Id = simulation.createChip(7400);
		Chip chip7400 = simulation.getChips().get(chip7400Id);

		// LU
		chip7400.getPinMap().get(1).setPinState(PinState.LOW);
		chip7400.getPinMap().get(2).setPinState(PinState.UNKNOWN);
		// UH
		chip7400.getPinMap().get(4).setPinState(PinState.UNKNOWN);
		chip7400.getPinMap().get(5).setPinState(PinState.HIGH);
		// HU
		chip7400.getPinMap().get(9).setPinState(PinState.HIGH);
		chip7400.getPinMap().get(10).setPinState(PinState.UNKNOWN);
		// UH
		chip7400.getPinMap().get(12).setPinState(PinState.UNKNOWN);
		chip7400.getPinMap().get(13).setPinState(PinState.LOW);

		chip7400.simulate();

		assertEquals(PinState.HIGH, chip7400.getPinMap().get(3).getPinState(),
					 "PinOut (3) powinien mieć stan HIGH po wywołaniu execute w Chip7400.");

		assertEquals(PinState.UNKNOWN, chip7400.getPinMap().get(6).getPinState(),
					 "PinOut (6) powinien mieć stan HIGH po wywołaniu execute w Chip7400.");

		assertEquals(PinState.UNKNOWN, chip7400.getPinMap().get(8).getPinState(),
					 "PinOut (8) powinien mieć stan HIGH po wywołaniu execute w Chip7400.");

		assertEquals(PinState.HIGH, chip7400.getPinMap().get(11).getPinState(),
					 "PinOut (11) powinien mieć stan HIGH po wywołaniu execute w Chip7400.");
	}
}
