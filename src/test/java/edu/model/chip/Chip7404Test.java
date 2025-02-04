package edu.model.chip;

import edu.Simulation;
import edu.uj.po.simulation.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class Chip7404Test {

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	@Test
	void testInitialPinState7404() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(1).getPinState(),
					 "Initial state of Pin A (input) should be UNKNOWN.");
		assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(2).getPinState(),
					 "Initial state of Pin Y (output) should be UNKNOWN.");
	}

	@Test
	void testExecuteChip7404WithNoConnectedOut2ConnectedPins() throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException{
		int in = simulation.createInputPinHeader(1);
		int chip7404Id = simulation.createChip(7404);
		int out = simulation.createOutputPinHeader(2);

		simulation.connect(in, 1, chip7404Id, 11);
		simulation.connect(chip7404Id, 11, chip7404Id, 9);
		simulation.connect(chip7404Id, 10, out, 1);
		simulation.connect(chip7404Id, 8, out, 2);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(in, 1, PinState.LOW));

		simulation.stationaryState(states);

		assertEquals(PinState.HIGH, simulation.getChips().get(out).getPinMap().get(1).getPinState());
		assertEquals(PinState.HIGH, simulation.getChips().get(out).getPinMap().get(2).getPinState());

	}

	@Test
	void testExecuteChip7404LowInput() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		chip7404.getPinMap().get(1).setPinState(PinState.LOW);

		chip7404.simulate();

		assertEquals(PinState.HIGH, chip7404.getPinMap().get(2).getPinState(),
					 "Pin Y (output) should be HIGH after simulation when input is LOW.");
	}

	@Test
	void testExecuteChip7404HighInput() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		chip7404.getPinMap().get(1).setPinState(PinState.HIGH);

		chip7404.simulate();

		assertEquals(PinState.LOW, chip7404.getPinMap().get(2).getPinState(),
					 "Pin Y (output) should be LOW after simulation when input is HIGH.");
	}

	@Test
	void testExecuteChip7404UnknownInput() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		chip7404.getPinMap().get(1).setPinState(PinState.UNKNOWN);  // A (input)

		chip7404.simulate();

		assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(2).getPinState(),
					 "Pin Y (output) should be UNKNOWN after simulation when input is UNKNOWN.");
	}

	@Test
	void testInitialPinStates7404() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		for (int i = 1; i <= 5; i += 2) {
			assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(i).getPinState(),
						 "Initial state of Pin " + i + " (input) should be UNKNOWN.");
			assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(i + 1).getPinState(),
						 "Initial state of Pin " + (i + 1) + " (output) should be UNKNOWN.");
		}

		for (int i = 9; i <= 13; i += 2) {
			assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(i).getPinState(),
						 "Initial state of Pin " + i + " (input) should be UNKNOWN.");
			assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(i - 1).getPinState(),
						 "Initial state of Pin " + (i - 1) + " (output) should be UNKNOWN.");
		}
	}

	@Test
	void testExecuteChip7404LowInputs() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		for (int i = 1; i <= 13; i += 2) {
			if(i == 7) continue;
			chip7404.getPinMap().get(i).setPinState(PinState.LOW);  // Set input to LOW
		}

		chip7404.simulate();

		for (int i = 2; i <= 12; i += 2) {
			assertEquals(PinState.HIGH, chip7404.getPinMap().get(i).getPinState(),
						 "Pin " + (i) + " (output) should be HIGH after simulation when input is LOW.");
		}
	}

	@Test
	void testExecuteChip7404HighInputs() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		for (int i = 1; i <= 13; i += 2) {
			if(i == 7) continue;
			chip7404.getPinMap().get(i).setPinState(PinState.HIGH);  // Set input to HIGH
		}

		chip7404.simulate();

		for (int i = 2; i <= 12; i += 2) {
			assertEquals(PinState.LOW, chip7404.getPinMap().get(i).getPinState(),
						 "Pin " + (i) + " (output) should be LOW after simulation when input is HIGH.");
		}
	}

	@Test
	void testExecuteChip7404UnknownInputs() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		chip7404.simulate();

		for (int i = 1; i <= 5; i += 2) {
			assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(i + 1).getPinState(),
						 "Pin " + (i + 1) + " (output) should be UNKNOWN after simulation when input is UNKNOWN.");
		}
		for (int i = 9; i <= 13; i += 2) {
			assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(i - 1).getPinState(),
						 "Pin " + (i - 1) + " (output) should be UNKNOWN after simulation when input is UNKNOWN.");
		}
	}

	@Test
	void testMixedInputs7404() throws UnknownChip {
		int chip7404Id = simulation.createChip(7404);
		Chip chip7404 = simulation.getChips().get(chip7404Id);

		// Set inputs: LOW, HIGH, UNKNOWN, LOW, HIGH, UNKNOWN
		chip7404.getPinMap().get(1).setPinState(PinState.LOW);
		chip7404.getPinMap().get(3).setPinState(PinState.HIGH);
		chip7404.getPinMap().get(5).setPinState(PinState.UNKNOWN);
		chip7404.getPinMap().get(9).setPinState(PinState.LOW);
		chip7404.getPinMap().get(11).setPinState(PinState.HIGH);
		chip7404.getPinMap().get(13).setPinState(PinState.UNKNOWN);

		chip7404.simulate();

		assertEquals(PinState.HIGH, chip7404.getPinMap().get(2).getPinState(),
					 "Pin 2 (output) should be HIGH after simulation.");
		assertEquals(PinState.LOW, chip7404.getPinMap().get(4).getPinState(),
					 "Pin 4 (output) should be LOW after simulation.");
		assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(6).getPinState(),
					 "Pin 6 (output) should be UNKNOWN after simulation.");
		assertEquals(PinState.LOW, chip7404.getPinMap().get(10).getPinState(),
					 "Pin 10 (output) should be HIGH after simulation.");
		assertEquals(PinState.UNKNOWN, chip7404.getPinMap().get(12).getPinState(),
					 "Pin 12 (output) should be LOW after simulation.");
	}
}

