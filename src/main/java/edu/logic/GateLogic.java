package edu.logic;

import edu.model.chip.Chip;
import edu.model.pin.AbstractPin;
import edu.model.pin.Pin;
import edu.uj.po.simulation.interfaces.PinState;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.uj.po.simulation.interfaces.PinState.*;

public class GateLogic{

	static PinState invertState(PinState pinState) {
		return switch (pinState) {
			case HIGH -> LOW;
			case LOW -> HIGH;
			default -> UNKNOWN;
		};
	}

	static void nandGateLogic(Chip chip, Set<Integer> inputPinIds, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Set<Pin> inputPins = inputPinIds.stream()
				.map(id -> pins.get(id))
				.collect(Collectors.toSet());

		Pin outputPin = pins.get(outputPinId);

		if (inputPins.stream().allMatch(pin -> pin.getPinState() == HIGH)) outputPin.setPinState(LOW);
		else if (inputPins.stream().anyMatch(pin -> pin.getPinState() == LOW)) outputPin.setPinState(HIGH);
		else outputPin.setPinState(UNKNOWN);
	}

	static void norGateLogic(Chip chip, Set<Integer> inputPinIds, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Set<Pin> inputPins = inputPinIds.stream()
				.map(id -> pins.get(id))
				.collect(Collectors.toSet());

		Pin outputPin = pins.get(outputPinId);

		if (inputPins.stream().allMatch(pin -> pin.getPinState() == LOW)) outputPin.setPinState(HIGH);
		else if (inputPins.stream().anyMatch(pin -> pin.getPinState() == HIGH)) outputPin.setPinState(LOW);
		else outputPin.setPinState(UNKNOWN);
	}

	static void andGateLogic(Chip chip, Set<Integer> inputPinIds, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Set<Pin> inputPins = inputPinIds.stream()
				.map(id -> pins.get(id))
				.collect(Collectors.toSet());

		Pin outputPin = pins.get(outputPinId);

		if (inputPins.stream().allMatch(pin -> pin.getPinState() == HIGH)) outputPin.setPinState(HIGH);
		else if (inputPins.stream().anyMatch(pin -> pin.getPinState() == LOW)) outputPin.setPinState(LOW);
		else outputPin.setPinState(UNKNOWN);
	}

	static void orGateLogic(Chip chip, Set<Integer> inputPinIds, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Set<Pin> inputPins = inputPinIds.stream()
				.map(id -> pins.get(id))
				.collect(Collectors.toSet());

		Pin outputPin = pins.get(outputPinId);

		if (inputPins.stream().allMatch(pin -> pin.getPinState() == LOW)) outputPin.setPinState(LOW);
		else if (inputPins.stream().anyMatch(pin -> pin.getPinState() == HIGH)) outputPin.setPinState(HIGH);
		else outputPin.setPinState(UNKNOWN);

	}

	static void notGateLogic(Chip chip, int inputPinId, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Pin inputPin = pins.get(inputPinId);
		Pin outputPin = pins.get(outputPinId);

		if (inputPin.getPinState() == LOW) outputPin.setPinState(HIGH);
		else if (inputPin.getPinState() == HIGH) outputPin.setPinState(LOW);
		else outputPin.setPinState(UNKNOWN);
	}

	static void identityGateLogic(Chip chip, int inputPinId, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Pin inputPin = pins.get(inputPinId);
		Pin outputPin = pins.get(outputPinId);

		outputPin.setPinState(inputPin.getPinState());
	}

	static void bcdDecoderLogic(Chip chip, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Pin A = pins.get(15);
		Pin B = pins.get(14);
		Pin C = pins.get(13);
		Pin D = pins.get(12);

		Pin outputPin = pins.get(outputPinId);

		PinState aState = A.getPinState();
		PinState bState = B.getPinState();
		PinState cState = C.getPinState();
		PinState dState = D.getPinState();

		if (aState == UNKNOWN || bState == UNKNOWN || cState == UNKNOWN || dState == UNKNOWN) return;

		int selector = (aState == HIGH ? 1 : 0) |
				(bState == HIGH ? 2 : 0) |
				(cState == HIGH ? 4 : 0) |
				(dState == HIGH ? 8 : 0);

		boolean outputLow = false;

		switch (outputPinId) {
			case 1 -> outputLow = (selector == 0b0000); // 0
			case 2 -> outputLow = (selector == 0b0001); // 1
			case 3 -> outputLow = (selector == 0b0010); // 2
			case 4 -> outputLow = (selector == 0b0011); // 3
			case 5 -> outputLow = (selector == 0b0100); // 4
			case 6 -> outputLow = (selector == 0b0101); // 5
			case 7 -> outputLow = (selector == 0b0110); // 6
			case 9 -> outputLow = (selector == 0b0111); // 7
			case 10 -> outputLow = (selector == 0b1000); // 8
			case 11 -> outputLow = (selector == 0b1001); // 9
		}
		outputPin.setPinState(outputLow ? LOW : HIGH);
	}


	static void grayDecoderLogic(Chip chip, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Pin A = pins.get(15);
		Pin B = pins.get(14);
		Pin C = pins.get(13);
		Pin D = pins.get(12);

		Pin outputPin = pins.get(outputPinId);

		if (Set.of(A, B, C, D).stream().anyMatch(pin -> pin.getPinState() == UNKNOWN)) {
			outputPin.setPinState(UNKNOWN);
			return;
		}

		switch (outputPinId) {
			// 0
			case 1:
				if (Set.of(A, B, D).stream().allMatch(p -> p.getPinState() == LOW) && C.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 1
			case 2:
				if (Set.of(A, D).stream().allMatch(p -> p.getPinState() == LOW) &&
						Set.of(B, C).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 2
			case 3:
				if (A.getPinState() == LOW && Set.of(B, C, D).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 3
			case 4:
				if (Set.of(A, C).stream().allMatch(p -> p.getPinState() == LOW) &&
						Set.of(B, D).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 4
			case 5:
				if (Set.of(A, C, D).stream().allMatch(p -> p.getPinState() == LOW) && B.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 5
			case 6:
				if (Set.of(C, D).stream().allMatch(p -> p.getPinState() == LOW) &&
						Set.of(A, B).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 6
			case 7:
				if (C.getPinState() == LOW && Set.of(A, B, D).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 7
			case 9:
				if (Set.of(A, B, C, D).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 8
			case 10:
				if (D.getPinState() == LOW && Set.of(A, B, C).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 9
			case 11:
				if (Set.of(B, D).stream().allMatch(p -> p.getPinState() == LOW) &&
						Set.of(A, C).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
		}
	}

	static void _74138DecoderLogic(Chip chip, int outputPinId) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Pin A = pins.get(1);
		Pin B = pins.get(2);
		Pin C = pins.get(3);
		Pin G2A = pins.get(4);
		Pin G2B = pins.get(5);
		Pin G1 = pins.get(6);

		Pin outputPin = pins.get(outputPinId);

		if (Set.of(A, B, C, G2A, G2B, G1).stream().anyMatch(pin -> pin.getPinState() == UNKNOWN)) {
			outputPin.setPinState(HIGH);
			return;
		}

		boolean G2 = G2A.getPinState() == HIGH || G2B.getPinState() == HIGH;

		if (G1.getPinState() == LOW || G2) {
			outputPin.setPinState(HIGH);
			return;
		}

		boolean A_High = A.getPinState() == HIGH;
		boolean B_High = B.getPinState() == HIGH;
		boolean C_High = C.getPinState() == HIGH;

		switch (outputPinId) {
			case 15:
				outputPin.setPinState((!A_High && !B_High && !C_High) ? LOW : HIGH);
				break;
			case 14:
				outputPin.setPinState((A_High && !B_High && !C_High) ? LOW : HIGH);
				break;
			case 13:
				outputPin.setPinState((!A_High && B_High && !C_High) ? LOW : HIGH);
				break;
			case 12:
				outputPin.setPinState((A_High && B_High && !C_High) ? LOW : HIGH);
				break;
			case 11:
				outputPin.setPinState((!A_High && !B_High && C_High) ? LOW : HIGH);
				break;
			case 10:
				outputPin.setPinState((A_High && !B_High && C_High) ? LOW : HIGH);
				break;
			case 9:
				outputPin.setPinState((!A_High && B_High && C_High) ? LOW : HIGH);
				break;
			case 7:
				outputPin.setPinState((A_High && B_High && C_High) ? LOW : HIGH);
				break;
		}
	}
}
