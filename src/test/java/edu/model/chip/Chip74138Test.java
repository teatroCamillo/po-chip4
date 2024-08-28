package edu.model.chip;

import edu.Simulation;
import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.interfaces.UnknownChip;
import edu.model.Chip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Chip74138Test {

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	@Test
	void testInitialPinState74138() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Sprawdzenie początkowego stanu wszystkich pinów
		for (int i = 1; i <= 15; i++) {
			if(i==8) continue;
			assertEquals(PinState.UNKNOWN, chip74138.getPinMap().get(i).getPinState(),
						 "Initial state of Pin " + i + " should be UNKNOWN.");
		}
	}

	@Test
	void testDisableOutputsWhenG1IsLow() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// G1 = LOW, G2A i G2B = don't care (X)
		chip74138.getPinMap().get(6).setPinState(PinState.LOW); // G1

		chip74138.getPinMap().get(4).setPinState(PinState.HIGH); // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.HIGH); // G2B
		chip74138.getPinMap().get(1).setPinState(PinState.HIGH); // A
		chip74138.getPinMap().get(2).setPinState(PinState.HIGH); // B
		chip74138.getPinMap().get(3).setPinState(PinState.HIGH); // C

		chip74138.simulate();

		// Wszystkie wyjścia powinny być HIGH
		for (int i = 7; i <= 15; i++) {
			if(i==8) continue;
			assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
						 "Pin " + i + " (output) should be HIGH when G1 is LOW.");
		}
	}

	@Test
	void testDisableOutputsWhenG2IsHigh() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// G1 = don't care (X), G2A lub G2B = HIGH
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1

		chip74138.getPinMap().get(4).setPinState(PinState.HIGH); // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW); // G2B
		chip74138.getPinMap().get(1).setPinState(PinState.LOW); // A
		chip74138.getPinMap().get(2).setPinState(PinState.HIGH); // B
		chip74138.getPinMap().get(3).setPinState(PinState.LOW); // C

		chip74138.simulate();

		// Wszystkie wyjścia powinny być HIGH
		for (int i = 7; i <= 15; i++) {
			if(i==8) continue;
			assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
						 "Pin " + i + " (output) should be HIGH when G2 is HIGH.");
		}
	}

	@Test
	void testY0IsLowForAddress000() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Włącz układ (G1 na HIGH, G2A i G2B na LOW)
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1
		chip74138.getPinMap().get(4).setPinState(PinState.LOW);  // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW);  // G2B

		// Ustawienie adresu na A=LOW, B=LOW, C=LOW
		chip74138.getPinMap().get(1).setPinState(PinState.LOW);  // A
		chip74138.getPinMap().get(2).setPinState(PinState.LOW);  // B
		chip74138.getPinMap().get(3).setPinState(PinState.LOW);  // C

		chip74138.simulate();

		// Y0 powinno być LOW, pozostałe wyjścia HIGH
		assertEquals(PinState.LOW, chip74138.getPinMap().get(15).getPinState(),
					 "Pin 15 (Y0) should be LOW when address is 000.");
		for (int i = 7; i <= 14; i++) {
			if(i==8) continue;
			assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
							 "Pin " + i + " (output) should be HIGH when address is 000.");

		}
	}

	@Test
	void testY1IsLowForAddress001() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Włącz układ (G1 na HIGH, G2A i G2B na LOW)
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1
		chip74138.getPinMap().get(4).setPinState(PinState.LOW);  // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW);  // G2B

		// Ustawienie adresu na A=HIGH, B=LOW, C=LOW
		chip74138.getPinMap().get(1).setPinState(PinState.HIGH); // A
		chip74138.getPinMap().get(2).setPinState(PinState.LOW);  // B
		chip74138.getPinMap().get(3).setPinState(PinState.LOW);  // C

		chip74138.simulate();

		// Y1 powinno być LOW, pozostałe wyjścia HIGH
		assertEquals(PinState.LOW, chip74138.getPinMap().get(14).getPinState(),
					 "Pin 14 (Y1) should be LOW when address is 001.");
		for (int i = 7; i <= 15; i++) {
			if(i==8) continue;
			if (i != 14) {
				assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
							 "Pin " + i + " (output) should be HIGH when address is 001.");
			}
		}
	}

	@Test
	void testY2IsLowForAddress010() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Włącz układ (G1 na HIGH, G2A i G2B na LOW)
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1
		chip74138.getPinMap().get(4).setPinState(PinState.LOW);  // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW);  // G2B

		// Ustawienie adresu na A=LOW, B=HIGH, C=LOW
		chip74138.getPinMap().get(1).setPinState(PinState.LOW);  // A
		chip74138.getPinMap().get(2).setPinState(PinState.HIGH); // B
		chip74138.getPinMap().get(3).setPinState(PinState.LOW);  // C

		chip74138.simulate();

		// Y2 powinno być LOW, pozostałe wyjścia HIGH
		assertEquals(PinState.LOW, chip74138.getPinMap().get(13).getPinState(),
					 "Pin 13 (Y2) should be LOW when address is 010.");
		for (int i = 7; i <= 15; i++) {
			if(i==8) continue;
			if (i != 13) {
				assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
							 "Pin " + i + " (output) should be HIGH when address is 010.");
			}
		}
	}

	@Test
	void testY3IsLowForAddress011() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Włącz układ (G1 na HIGH, G2A i G2B na LOW)
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1
		chip74138.getPinMap().get(4).setPinState(PinState.LOW);  // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW);  // G2B

		// Ustawienie adresu na A=HIGH, B=HIGH, C=LOW
		chip74138.getPinMap().get(1).setPinState(PinState.HIGH); // A
		chip74138.getPinMap().get(2).setPinState(PinState.HIGH); // B
		chip74138.getPinMap().get(3).setPinState(PinState.LOW);  // C

		chip74138.simulate();

		// Y3 powinno być LOW, pozostałe wyjścia HIGH
		assertEquals(PinState.LOW, chip74138.getPinMap().get(12).getPinState(),
					 "Pin 12 (Y3) should be LOW when address is 011.");
		for (int i = 7; i <= 15; i++) {
			if(i==8) continue;
			if (i != 12) {
				assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
							 "Pin " + i + " (output) should be HIGH when address is 011.");
			}
		}
	}

	@Test
	void testY4IsLowForAddress100() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Włącz układ (G1 na HIGH, G2A i G2B na LOW)
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1
		chip74138.getPinMap().get(4).setPinState(PinState.LOW);  // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW);  // G2B

		// Ustawienie adresu na A=LOW, B=LOW, C=HIGH
		chip74138.getPinMap().get(1).setPinState(PinState.LOW);  // A
		chip74138.getPinMap().get(2).setPinState(PinState.LOW);  // B
		chip74138.getPinMap().get(3).setPinState(PinState.HIGH); // C

		chip74138.simulate();

		// Y4 powinno być LOW, pozostałe wyjścia HIGH
		assertEquals(PinState.LOW, chip74138.getPinMap().get(11).getPinState(),
					 "Pin 11 (Y4) should be LOW when address is 100.");
		for (int i = 7; i <= 15; i++) {
			if(i==8) continue;
			if (i != 11) {
				assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
							 "Pin " + i + " (output) should be HIGH when address is 100.");
			}
		}
	}

	@Test
	void testY5IsLowForAddress101() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Włącz układ (G1 na HIGH, G2A i G2B na LOW)
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1
		chip74138.getPinMap().get(4).setPinState(PinState.LOW);  // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW);  // G2B

		// Ustawienie adresu na A=HIGH, B=LOW, C=HIGH
		chip74138.getPinMap().get(1).setPinState(PinState.HIGH); // A
		chip74138.getPinMap().get(2).setPinState(PinState.LOW);  // B
		chip74138.getPinMap().get(3).setPinState(PinState.HIGH); // C

		chip74138.simulate();

		// Y5 powinno być LOW, pozostałe wyjścia HIGH
		assertEquals(PinState.LOW, chip74138.getPinMap().get(10).getPinState(),
					 "Pin 10 (Y5) should be LOW when address is 101.");
		for (int i = 7; i <= 15; i++) {
			if(i==8) continue;
			if (i != 10) {
				assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
							 "Pin " + i + " (output) should be HIGH when address is 101.");
			}
		}
	}

	@Test
	void testY6IsLowForAddress110() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Włącz układ (G1 na HIGH, G2A i G2B na LOW)
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1
		chip74138.getPinMap().get(4).setPinState(PinState.LOW);  // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW);  // G2B

		// Ustawienie adresu na A=LOW, B=HIGH, C=HIGH
		chip74138.getPinMap().get(1).setPinState(PinState.LOW);  // A
		chip74138.getPinMap().get(2).setPinState(PinState.HIGH); // B
		chip74138.getPinMap().get(3).setPinState(PinState.HIGH); // C

		chip74138.simulate();

		// Y6 powinno być LOW, pozostałe wyjścia HIGH
		assertEquals(PinState.LOW, chip74138.getPinMap().get(9).getPinState(),
					 "Pin 9 (Y6) should be LOW when address is 110.");
		for (int i = 7; i <= 15; i++) {
			if(i==8) continue;
			if (i != 9) {
				assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
							 "Pin " + i + " (output) should be HIGH when address is 110.");
			}
		}
	}

	@Test
	void testY7IsLowForAddress111() throws UnknownChip {
		int chip74138Id = simulation.createChip(74138);
		Chip chip74138 = simulation.getChips().get(chip74138Id);

		// Włącz układ (G1 na HIGH, G2A i G2B na LOW)
		chip74138.getPinMap().get(6).setPinState(PinState.HIGH); // G1
		chip74138.getPinMap().get(4).setPinState(PinState.LOW);  // G2A
		chip74138.getPinMap().get(5).setPinState(PinState.LOW);  // G2B

		// Ustawienie adresu na A=HIGH, B=HIGH, C=HIGH
		chip74138.getPinMap().get(1).setPinState(PinState.HIGH); // A
		chip74138.getPinMap().get(2).setPinState(PinState.HIGH); // B
		chip74138.getPinMap().get(3).setPinState(PinState.HIGH); // C

		chip74138.simulate();

		// Y7 powinno być LOW, pozostałe wyjścia HIGH
		assertEquals(PinState.LOW, chip74138.getPinMap().get(7).getPinState(),
					 "Pin 7 (Y7) should be LOW when address is 111.");
		for (int i = 9; i <= 15; i++) {
			assertEquals(PinState.HIGH, chip74138.getPinMap().get(i).getPinState(),
						 "Pin " + i + " (output) should be HIGH when address is 111.");
		}
	}
}
