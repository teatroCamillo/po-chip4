package edu.model.chip;

import Simulation;
import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.interfaces.UnknownChip;
import edu.model.Chip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//TODO: coś z logika jest nie tak testy Oramusa nie przechodza
class Chip7482Test {

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	@Test
	void testInitialPinState7482() throws UnknownChip {
		int chip7482Id = simulation.createChip(7482);
		Chip chip7482 = simulation.getChips().get(chip7482Id);

		assertEquals(PinState.UNKNOWN, chip7482.getPinMap().get(1).getPinState(),
					 "Initial state of Pin S1 should be UNKNOWN.");
		assertEquals(PinState.UNKNOWN, chip7482.getPinMap().get(12).getPinState(),
					 "Initial state of Pin S2 should be UNKNOWN.");
		assertEquals(PinState.UNKNOWN, chip7482.getPinMap().get(10).getPinState(),
					 "Initial state of Pin C2 should be UNKNOWN.");
	}

	@Test
	void testExecuteChip7482LowInputs() throws UnknownChip {
		int chip7482Id = simulation.createChip(7482);
		Chip chip7482 = simulation.getChips().get(chip7482Id);

		chip7482.getPinMap().get(2).setPinState(PinState.LOW);  // A1
		chip7482.getPinMap().get(14).setPinState(PinState.LOW); // A2
		chip7482.getPinMap().get(3).setPinState(PinState.LOW);  // B1
		chip7482.getPinMap().get(13).setPinState(PinState.LOW); // B2
		chip7482.getPinMap().get(5).setPinState(PinState.LOW);  // C0

		chip7482.simulate();

		assertEquals(PinState.LOW, chip7482.getPinMap().get(1).getPinState(),
					 "Pin S1 should be LOW after simulation.");
		assertEquals(PinState.LOW, chip7482.getPinMap().get(12).getPinState(),
					 "Pin S2 should be LOW after simulation.");
		assertEquals(PinState.LOW, chip7482.getPinMap().get(10).getPinState(),
					 "Pin C2 should be LOW after simulation.");
	}

	@Test
	void testExecuteChip7482MixedInputs() throws UnknownChip {
		int chip7482Id = simulation.createChip(7482);
		Chip chip7482 = simulation.getChips().get(chip7482Id);

		chip7482.getPinMap().get(2).setPinState(PinState.HIGH); // A1
		chip7482.getPinMap().get(14).setPinState(PinState.LOW);  // A2
		chip7482.getPinMap().get(3).setPinState(PinState.LOW);   // B1
		chip7482.getPinMap().get(13).setPinState(PinState.HIGH); // B2
		chip7482.getPinMap().get(5).setPinState(PinState.LOW);   // C0

		chip7482.simulate();

		assertEquals(PinState.HIGH, chip7482.getPinMap().get(1).getPinState(),
					 "Pin S1 should be HIGH after simulation.");
		assertEquals(PinState.HIGH, chip7482.getPinMap().get(12).getPinState(),
					 "Pin S2 should be HIGH after simulation.");
		assertEquals(PinState.LOW, chip7482.getPinMap().get(10).getPinState(),
					 "Pin C2 should be LOW after simulation.");
	}

	@Test
	void testExecuteChip7482HighInputs() throws UnknownChip {
		int chip7482Id = simulation.createChip(7482);
		Chip chip7482 = simulation.getChips().get(chip7482Id);

		chip7482.getPinMap().get(2).setPinState(PinState.HIGH);  // A1
		chip7482.getPinMap().get(14).setPinState(PinState.HIGH); // A2
		chip7482.getPinMap().get(3).setPinState(PinState.HIGH);  // B1
		chip7482.getPinMap().get(13).setPinState(PinState.HIGH); // B2
		chip7482.getPinMap().get(5).setPinState(PinState.HIGH);  // C0

		chip7482.simulate();

		assertEquals(PinState.HIGH, chip7482.getPinMap().get(1).getPinState(),
					 "Pin S1 should be HIGH after simulation.");
		assertEquals(PinState.HIGH, chip7482.getPinMap().get(12).getPinState(),
					 "Pin S2 should be HIGH after simulation.");
		assertEquals(PinState.HIGH, chip7482.getPinMap().get(10).getPinState(),
					 "Pin C2 should be HIGH after simulation.");
	}

	@Test
	void testExecuteChip7482WithUNKNOWNState() throws UnknownChip {
		int chip7482Id = simulation.createChip(7482);
		Chip chip7482 = simulation.getChips().get(chip7482Id);

		chip7482.getPinMap().get(2).setPinState(PinState.UNKNOWN); // A1
		chip7482.getPinMap().get(14).setPinState(PinState.HIGH);   // A2
		chip7482.getPinMap().get(3).setPinState(PinState.LOW);     // B1
		chip7482.getPinMap().get(13).setPinState(PinState.UNKNOWN); // B2
		chip7482.getPinMap().get(5).setPinState(PinState.HIGH);    // C0

		chip7482.simulate();

		assertEquals(PinState.UNKNOWN, chip7482.getPinMap().get(1).getPinState(),
					 "Pin S1 should be UNKNOWN after simulation.");
		assertEquals(PinState.UNKNOWN, chip7482.getPinMap().get(12).getPinState(),
					 "Pin S2 should be UNKNOWN after simulation.");
		assertEquals(PinState.UNKNOWN, chip7482.getPinMap().get(10).getPinState(),
					 "Pin C2 should be UNKNOWN after simulation.");
	}
}
