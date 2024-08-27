package edu.logic;

import edu.model.Chip;
import edu.model.Pin;
//import com.digitalcircuit.model.pin.InputPin; // dla wykomentowanego kodu
import edu.model.pin.PinIn;
import edu.uj.po.simulation.interfaces.PinState;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.uj.po.simulation.interfaces.PinState.*;

public class ChipLogicCalculation {

	public static void chip7400Calculation(Chip chip) {
		nandGateLogicFunction(chip, Set.of(1, 2), 3);
		nandGateLogicFunction(chip, Set.of(4, 5), 6);
		nandGateLogicFunction(chip, Set.of(9, 10), 8);
		nandGateLogicFunction(chip, Set.of(12, 13), 11);
	}

	public static void chip7402Calculation(Chip chip) {
		norGateLogicFunction(chip, Set.of(2, 3), 1);
		norGateLogicFunction(chip, Set.of(5, 6), 4);
		norGateLogicFunction(chip, Set.of(8, 9), 10);
		norGateLogicFunction(chip, Set.of(11, 12), 13);
	}

	public static void chip7404Calculation(Chip chip) {
		notGateLogicFunction(chip, 1, 2);
		notGateLogicFunction(chip, 3, 4);
		notGateLogicFunction(chip, 5, 6);
		notGateLogicFunction(chip, 9, 8);
		notGateLogicFunction(chip, 11, 10);
		notGateLogicFunction(chip, 13, 12);
	}

	public static void chip7408Calculation(Chip chip) {
		andGateLogicFunction(chip, Set.of(1, 2), 3);
		andGateLogicFunction(chip, Set.of(4, 5), 6);
		andGateLogicFunction(chip, Set.of(9, 10), 8);
		andGateLogicFunction(chip, Set.of(12, 13), 11);
	}

	public static void chip7410Calculation(Chip chip) {
		nandGateLogicFunction(chip, Set.of(1, 2, 13), 12);
		nandGateLogicFunction(chip, Set.of(3, 4, 5), 6);
		nandGateLogicFunction(chip, Set.of(9, 10, 11), 8);
	}

	public static void chip7411Calculation(Chip chip) {
		andGateLogicFunction(chip, Set.of(1, 2, 13), 12);
		andGateLogicFunction(chip, Set.of(3, 4, 5), 6);
		andGateLogicFunction(chip, Set.of(9, 10, 11), 8);
	}

	public static void chip7420Calculation(Chip chip) {
		nandGateLogicFunction(chip, Set.of(1, 2, 4, 5), 6);
		nandGateLogicFunction(chip, Set.of(9, 10, 12, 13), 8);
	}

	public static void chip7431Calculation(Chip chip) {
		notGateLogicFunction(chip, 1, 2);
		identityGateLogicFunction(chip, 3, 4);
		nandGateLogicFunction(chip, Set.of(5, 6), 7);
		nandGateLogicFunction(chip, Set.of(10, 11), 9);
		identityGateLogicFunction(chip, 13, 12);
		notGateLogicFunction(chip, 15, 14);
	}

	public static void chip7432Calculation(Chip chip) {
		orGateLogicFunction(chip, Set.of(1, 2), 3);
		orGateLogicFunction(chip, Set.of(4, 5), 6);
		orGateLogicFunction(chip, Set.of(9, 10), 8);
		orGateLogicFunction(chip, Set.of(12, 13), 11);
	}

	public static void chip7434Calculation(Chip chip) {
		identityGateLogicFunction(chip, 1, 2);
		identityGateLogicFunction(chip, 3, 4);
		identityGateLogicFunction(chip, 5, 6);
		identityGateLogicFunction(chip, 9, 8);
		identityGateLogicFunction(chip, 11, 10);
		identityGateLogicFunction(chip, 13, 12);
	}

	public static void chip7442Calculation(Chip chip) {
		bcdDecoderLogicFunction(chip, 1);
		bcdDecoderLogicFunction(chip, 2);
		bcdDecoderLogicFunction(chip, 3);
		bcdDecoderLogicFunction(chip, 4);
		bcdDecoderLogicFunction(chip, 5);
		bcdDecoderLogicFunction(chip, 6);
		bcdDecoderLogicFunction(chip, 7);
		bcdDecoderLogicFunction(chip, 9);
		bcdDecoderLogicFunction(chip, 10);
		bcdDecoderLogicFunction(chip, 11);
	}

	public static void chip7444Calculation(Chip chip) {
		grayDecoderLogicFunction(chip, 1);
		grayDecoderLogicFunction(chip, 2);
		grayDecoderLogicFunction(chip, 3);
		grayDecoderLogicFunction(chip, 4);
		grayDecoderLogicFunction(chip, 5);
		grayDecoderLogicFunction(chip, 6);
		grayDecoderLogicFunction(chip, 7);
		grayDecoderLogicFunction(chip, 9);
		grayDecoderLogicFunction(chip, 10);
		grayDecoderLogicFunction(chip, 11);
	}

	public static void chip7482Calculation(Chip chip) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Pin A1 = pins.get(2);
		Pin A2 = pins.get(14);
		Pin B1 = pins.get(3);
		Pin B2 = pins.get(13);
		Pin C0 = pins.get(5);

		Pin S1 = pins.get(1);
		Pin S2 = pins.get(12);
		Pin C2 = pins.get(10);

		if (Set.of(A1, A2, B1, B2, C0).stream().anyMatch(pin -> pin.getPinState() == PinState.UNKNOWN)) {
			S1.setPinState(PinState.UNKNOWN);
			S2.setPinState(PinState.UNKNOWN);
			C2.setPinState(PinState.UNKNOWN);
			return;
		}

		// Zamiana stanów na wartości logiczne 0 i 1
		int a1 = (A1.getPinState() == PinState.HIGH) ? 1 : 0;
		int a2 = (A2.getPinState() == PinState.HIGH) ? 1 : 0;
		int b1 = (B1.getPinState() == PinState.HIGH) ? 1 : 0;
		int b2 = (B2.getPinState() == PinState.HIGH) ? 1 : 0;
		int c0 = (C0.getPinState() == PinState.HIGH) ? 1 : 0;

		// Obliczenie sumy i przeniesienia
		int sum1 = a1 + b1 + c0;
		int sum2 = a2 + b2 + (sum1 >> 1); // przeniesienie z sum1 dodane do sumy sum2
		int carryOut = (sum2 >> 1); // ostateczne przeniesienie

		// Ustawienie wyjść
		S1.setPinState((sum1 & 1) == 1 ? PinState.HIGH : PinState.LOW);
		S2.setPinState((sum2 & 1) == 1 ? PinState.HIGH : PinState.LOW);
		C2.setPinState(carryOut == 1 ? PinState.HIGH : PinState.LOW);
	}
//	public static void chip7482Calculation(Chip chip) {
//		Map<Integer, Pin> pins = chip.getPinMap();
//
//		Pin A1 = pins.get(2);
//		Pin A2 = pins.get(14);
//		Pin B1 = pins.get(3);
//		Pin B2 = pins.get(13);
//		Pin C0 = pins.get(5);
//
//		Pin S1 = pins.get(1);
//		Pin S2 = pins.get(12);
//		Pin C2 = pins.get(10);
//
//		if (Set.of(A1, A2, B1, B2, C0).stream().anyMatch(pin -> pin.getPinState() == UNKNOWN)) {
//			S1.setPinState(UNKNOWN);
//			S2.setPinState(UNKNOWN);
//			C2.setPinState(UNKNOWN);
//			return;
//		}
//
//		if (Set.of(A1, B1, A2, B2, C0).stream().allMatch(pin -> pin.getPinState() == LOW)) {
//			S1.setPinState(LOW);
//			S2.setPinState(LOW);
//			C2.setPinState(LOW);
//		} else if (Set.of(B1, A2, B2, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(LOW);
//			C2.setPinState(LOW);
//		} else if (Set.of(A1, A2, B2, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(B1).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(LOW);
//			C2.setPinState(LOW);
//		} else if (Set.of(A2, B2, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, B1).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(A1, B1, B2, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A2).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(B1, B2, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, A2).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(A1, B2, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(B1, A2).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(B2, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, B1, A2).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(LOW);
//			C2.setPinState(HIGH);
//		} else if (Set.of(A1, B1, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A2, B2).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(B1, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, A2, B2).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(LOW);
//			C2.setPinState(HIGH);
//		} else if (Set.of(A1, C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(B1, A2, B2).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(LOW);
//			C2.setPinState(HIGH);
//		} else if (Set.of(C0).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, B1, A2, B2).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(HIGH);
//			C2.setPinState(HIGH);
//
//		} else if (Set.of(A1, B1, A2, B2).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(LOW);
//			C2.setPinState(LOW);
//		} else if (Set.of(B1, A2, B2).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(A1, A2, B2).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(B1, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(A2, B2).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, B1, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(A1, B1, B2).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A2, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(HIGH);
//			C2.setPinState(LOW);
//		} else if (Set.of(B1, B2).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, A2, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(LOW);
//			C2.setPinState(HIGH);
//		} else if (Set.of(A1, B2).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(B1, A2, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(LOW);
//			C2.setPinState(HIGH);
//		} else if (Set.of(B2).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, B1, A2, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(LOW);
//			C2.setPinState(HIGH);
//		} else if (Set.of(A1, B1).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A2, B2, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(LOW);
//			C2.setPinState(HIGH);
//		} else if (Set.of(B1).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(A1, A2, B2, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(HIGH);
//			C2.setPinState(HIGH);
//		} else if (Set.of(A1).stream().allMatch(pin -> pin.getPinState() == LOW)
//				&& Set.of(B1, A2, B2, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(LOW);
//			S2.setPinState(HIGH);
//			C2.setPinState(HIGH);
//		} else if (Set.of(A1, B1, A2, B2, C0).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
//			S1.setPinState(HIGH);
//			S2.setPinState(HIGH);
//			C2.setPinState(HIGH);
//		} else {
//			S1.setPinState(UNKNOWN);
//			S2.setPinState(UNKNOWN);
//			C2.setPinState(UNKNOWN);
//		}
//	}

	public static void chip74138Calculation(Chip chip) {
		_74138DecoderLogicFunction(chip, 7);
		_74138DecoderLogicFunction(chip, 9);
		_74138DecoderLogicFunction(chip, 10);
		_74138DecoderLogicFunction(chip, 11);
		_74138DecoderLogicFunction(chip, 12);
		_74138DecoderLogicFunction(chip, 13);
		_74138DecoderLogicFunction(chip, 14);
		_74138DecoderLogicFunction(chip, 15);
	}

	public static void chip74152Calculation(Chip chip) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Pin D0 = pins.get(5);
		Pin D1 = pins.get(4);
		Pin D2 = pins.get(3);
		Pin D3 = pins.get(2);
		Pin D4 = pins.get(1);
		Pin D5 = pins.get(13);
		Pin D6 = pins.get(12);
		Pin D7 = pins.get(11);
		Pin A = pins.get(10);
		Pin B = pins.get(9);
		Pin C = pins.get(8);

		Pin W = pins.get(6);

		if (Set.of(A, B, C).stream().allMatch(pin -> pin.getPinState() == LOW)) {
			W.setPinState(invertState(D0.getPinState()));
		} else if (Set.of(B, C).stream().allMatch(pin -> pin.getPinState() == LOW)
				&& Set.of(A).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			W.setPinState(invertState(D1.getPinState()));
		} else if (Set.of(A, C).stream().allMatch(pin -> pin.getPinState() == LOW)
				&& Set.of(B).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			W.setPinState(invertState(D2.getPinState()));
		} else if (Set.of(C).stream().allMatch(pin -> pin.getPinState() == LOW)
				&& Set.of(A, B).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			W.setPinState(invertState(D3.getPinState()));
		} else if (Set.of(A, B).stream().allMatch(pin -> pin.getPinState() == LOW)
				&& Set.of(C).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			W.setPinState(invertState(D4.getPinState()));
		} else if (Set.of(B).stream().allMatch(pin -> pin.getPinState() == LOW)
				&& Set.of(A, C).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			W.setPinState(invertState(D5.getPinState()));
		} else if (Set.of(A).stream().allMatch(pin -> pin.getPinState() == LOW)
				&& Set.of(B, C).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			W.setPinState(invertState(D6.getPinState()));
		} else if (Set.of(A, B, C).stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			W.setPinState(invertState(D7.getPinState()));
		}

	}

	private static PinState invertState(PinState pinState) {
		if (pinState == HIGH) {
			return LOW;
		} else if (pinState == UNKNOWN) {
			return UNKNOWN;
		} else {
			return HIGH;
		}
	}


	private static void nandGateLogicFunction(Chip chip, Set<Integer> inputPinIds, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Set<Pin> inputPins = inputPinIds.stream()
				.map(id -> pins.get(id))
				.collect(Collectors.toSet());

		Pin outputPin = pins.get(outputPinId);

		if (inputPins.stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			outputPin.setPinState(LOW);
		} else if (inputPins.stream().anyMatch(pin -> pin.getPinState() == LOW)) {
			outputPin.setPinState(HIGH);
		} else {
			outputPin.setPinState(UNKNOWN);
		}
	}

	private static void norGateLogicFunction(Chip chip, Set<Integer> inputPinIds, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Set<Pin> inputPins = inputPinIds.stream()
				.map(id -> pins.get(id))
				.collect(Collectors.toSet());

		Pin outputPin = pins.get(outputPinId);

		if (inputPins.stream().allMatch(pin -> pin.getPinState() == LOW)) {
			outputPin.setPinState(HIGH);
		} else if (inputPins.stream().anyMatch(pin -> pin.getPinState() == HIGH)) {
			outputPin.setPinState(LOW);
		} else {
			outputPin.setPinState(UNKNOWN);
		}
	}

	private static void andGateLogicFunction(Chip chip, Set<Integer> inputPinIds, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Set<Pin> inputPins = inputPinIds.stream()
				.map(id -> pins.get(id))
				.collect(Collectors.toSet());

		Pin outputPin = pins.get(outputPinId);

		if (inputPins.stream().allMatch(pin -> pin.getPinState() == HIGH)) {
			outputPin.setPinState(HIGH);
		} else if (inputPins.stream().anyMatch(pin -> pin.getPinState() == LOW)) {
			outputPin.setPinState(LOW);
		} else {
			outputPin.setPinState(UNKNOWN);
		}
	}

	private static void orGateLogicFunction(Chip chip, Set<Integer> inputPinIds, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Set<Pin> inputPins = inputPinIds.stream()
				.map(id -> pins.get(id))
				.collect(Collectors.toSet());

		Pin outputPin = pins.get(outputPinId);

		if (inputPins.stream().allMatch(pin -> pin.getPinState() == LOW)) {
			outputPin.setPinState(LOW);
		} else if (inputPins.stream().anyMatch(pin -> pin.getPinState() == HIGH)) {
			outputPin.setPinState(HIGH);
		} else {
			outputPin.setPinState(UNKNOWN);
		}
	}

	private static void notGateLogicFunction(Chip chip, int inputPinId, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Pin inputPin = pins.get(inputPinId);
		Pin outputPin = pins.get(outputPinId);

		if (inputPin.getPinState() == LOW) {
			outputPin.setPinState(HIGH);
		} else if (inputPin.getPinState() == HIGH) {
			outputPin.setPinState(LOW);
		} else {
			outputPin.setPinState(UNKNOWN);
		}
	}

	private static void identityGateLogicFunction(Chip chip, int inputPinId, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Pin inputPin = pins.get(inputPinId);
		Pin outputPin = pins.get(outputPinId);

		outputPin.setPinState(inputPin.getPinState());
	}

	private static void bcdDecoderLogicFunction(Chip chip, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Pin A = pins.get(15);
		Pin B = pins.get(14);
		Pin C = pins.get(13);
		Pin D = pins.get(12);

		Pin outputPin = pins.get(outputPinId);

		if (Set.of(A, B, C, D).stream().anyMatch(pin -> pin.getPinState() == UNKNOWN)) {
			return;
		}

		switch (outputPinId) {
			// 0
			case 1:
				if (Set.of(A, B, C, D).stream().allMatch(pin -> pin.getPinState() == LOW)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 1
			case 2:
				if (Set.of(B, C, D).stream().allMatch(p -> p.getPinState() == LOW) && A.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 2
			case 3:
				if (Set.of(A, C, D).stream().allMatch(p -> p.getPinState() == LOW) && B.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 3
			case 4:
				if (Set.of(C, D).stream().allMatch(p -> p.getPinState() == LOW) &&
						Set.of(A, B).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 4
			case 5:
				if (Set.of(A, B, D).stream().allMatch(p -> p.getPinState() == LOW) && C.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 5
			case 6:
				if (Set.of(B, D).stream().allMatch(p -> p.getPinState() == LOW) &&
						Set.of(A, C).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 6
			case 7:
				if (Set.of(A, D).stream().allMatch(p -> p.getPinState() == LOW) &&
						Set.of(B, C).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 7
			case 9:
				if (Set.of(A, B, C).stream().allMatch(p -> p.getPinState() == HIGH) && D.getPinState() == LOW) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 8
			case 10:
				if (Set.of(A, B, C).stream().allMatch(p -> p.getPinState() == LOW) && D.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// 9
			case 11:
				if (Set.of(B, C).stream().allMatch(p -> p.getPinState() == LOW) &&
						Set.of(A, D).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
		}
	}

	private static void grayDecoderLogicFunction(Chip chip, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

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

	private static void _74138DecoderLogicFunction(Chip chip, int outputPinId) {
		Map<Integer, Pin> pins = chip.getPinMap();

		Pin A = pins.get(1);
		Pin B = pins.get(2);
		Pin C = pins.get(3);
		Pin G2A = pins.get(4);
		Pin G2B = pins.get(5);
		Pin G1 = pins.get(6);

		Pin outputPin = pins.get(outputPinId);

		if (Set.of(A, B, C, G2A, G2B, G1).stream().anyMatch(pin -> pin.getPinState() == UNKNOWN)) {
			outputPin.setPinState(UNKNOWN);
			return;
		}


		Pin G2 = new PinIn(-1, UNKNOWN);
		if (Set.of(G2A, G2B).stream().anyMatch(pin -> pin.getPinState() == HIGH)) {
			G2.setPinState(HIGH);
		} else if (Set.of(G2A, G2B).stream().anyMatch(pin -> pin.getPinState() == UNKNOWN)) {
			G2.setPinState(UNKNOWN);
		} else {
			G2.setPinState(LOW);
		}

		if (G1.getPinState() == LOW || G2.getPinState() == HIGH) {
			outputPin.setPinState(HIGH);
			return;
		}

		switch (outputPinId) {
			// Y0
			case 15:
				if (Set.of(A, B, C).stream().allMatch(p -> p.getPinState() == LOW)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// Y1
			case 14:
				if (Set.of(B, C).stream().allMatch(p -> p.getPinState() == LOW) &&
						A.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// Y2
			case 13:
				if (Set.of(A, C).stream().allMatch(p -> p.getPinState() == LOW) &&
						B.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// Y3
			case 12:
				if (C.getPinState() == LOW &&
						Set.of(A, B).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// Y4
			case 11:
				if (Set.of(A, B).stream().allMatch(p -> p.getPinState() == LOW) && C.getPinState() == HIGH) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// Y5
			case 10:
				if (B.getPinState() == LOW &&
						Set.of(A, C).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// Y6
			case 9:
				if (A.getPinState() == LOW && Set.of(B, C).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
			// Y7
			case 7:
				if (Set.of(A, B, C).stream().allMatch(p -> p.getPinState() == HIGH)) {
					outputPin.setPinState(LOW);
				} else {
					outputPin.setPinState(HIGH);
				}
				break;
		}
	}

}
