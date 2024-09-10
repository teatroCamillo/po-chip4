package edu.logic;

import edu.model.chip.Chip;
import edu.model.pin.Pin;
import edu.model.pin.AbstractPin;

import java.util.Map;
import java.util.Set;


import static edu.logic.GateLogic.*;
import static edu.uj.po.simulation.interfaces.PinState.*;

public class ChipLogic {

	private final static GateLogic gateLogic = new GateLogic();

	public static void chip7400Logic(Chip chip) {
		gateLogic.nandGateLogic(chip, Set.of(1, 2), 3);
		gateLogic.nandGateLogic(chip, Set.of(4, 5), 6);
		gateLogic.nandGateLogic(chip, Set.of(9, 10), 8);
		gateLogic.nandGateLogic(chip, Set.of(12, 13), 11);
	}

	public static void chip7402Logic(Chip chip) {
		gateLogic.norGateLogic(chip, Set.of(2, 3), 1);
		gateLogic.norGateLogic(chip, Set.of(5, 6), 4);
		gateLogic.norGateLogic(chip, Set.of(8, 9), 10);
		gateLogic.norGateLogic(chip, Set.of(11, 12), 13);
	}

	public static void chip7404Logic(Chip chip) {
		gateLogic.notGateLogic(chip, 1, 2);
		gateLogic.notGateLogic(chip, 3, 4);
		gateLogic.notGateLogic(chip, 5, 6);
		gateLogic.notGateLogic(chip, 9, 8);
		gateLogic.notGateLogic(chip, 11, 10);
		gateLogic.notGateLogic(chip, 13, 12);
	}

	public static void chip7408Logic(Chip chip) {
		gateLogic.andGateLogic(chip, Set.of(1, 2), 3);
		gateLogic.andGateLogic(chip, Set.of(4, 5), 6);
		gateLogic.andGateLogic(chip, Set.of(9, 10), 8);
		gateLogic.andGateLogic(chip, Set.of(12, 13), 11);
	}

	public static void chip7410Logic(Chip chip) {
		gateLogic.nandGateLogic(chip, Set.of(1, 2, 13), 12);
		gateLogic.nandGateLogic(chip, Set.of(3, 4, 5), 6);
		gateLogic.nandGateLogic(chip, Set.of(9, 10, 11), 8);
	}

	public static void chip7411Logic(Chip chip) {
		gateLogic.andGateLogic(chip, Set.of(1, 2, 13), 12);
		gateLogic.andGateLogic(chip, Set.of(3, 4, 5), 6);
		gateLogic.andGateLogic(chip, Set.of(9, 10, 11), 8);
	}

	public static void chip7420Logic(Chip chip) {
		gateLogic.nandGateLogic(chip, Set.of(1, 2, 4, 5), 6);
		gateLogic.nandGateLogic(chip, Set.of(9, 10, 12, 13), 8);
	}

	public static void chip7431Logic(Chip chip) {
		gateLogic.notGateLogic(chip, 1, 2);
		gateLogic.identityGateLogic(chip, 3, 4);
		gateLogic.nandGateLogic(chip, Set.of(5, 6), 7);
		gateLogic.nandGateLogic(chip, Set.of(10, 11), 9);
		gateLogic.identityGateLogic(chip, 13, 12);
		gateLogic.notGateLogic(chip, 15, 14);
	}

	public static void chip7432Logic(Chip chip) {
		gateLogic.orGateLogic(chip, Set.of(1, 2), 3);
		gateLogic.orGateLogic(chip, Set.of(4, 5), 6);
		gateLogic.orGateLogic(chip, Set.of(9, 10), 8);
		gateLogic.orGateLogic(chip, Set.of(12, 13), 11);
	}

	public static void chip7434Logic(Chip chip) {
		gateLogic.identityGateLogic(chip, 1, 2);
		gateLogic.identityGateLogic(chip, 3, 4);
		gateLogic.identityGateLogic(chip, 5, 6);
		gateLogic.identityGateLogic(chip, 9, 8);
		gateLogic.identityGateLogic(chip, 11, 10);
		gateLogic.identityGateLogic(chip, 13, 12);
	}

	public static void chip7442Logic(Chip chip) {
		gateLogic.bcdDecoderLogic(chip, 1);
		gateLogic.bcdDecoderLogic(chip, 2);
		gateLogic.bcdDecoderLogic(chip, 3);
		gateLogic.bcdDecoderLogic(chip, 4);
		gateLogic.bcdDecoderLogic(chip, 5);
		gateLogic.bcdDecoderLogic(chip, 6);
		gateLogic.bcdDecoderLogic(chip, 7);
		gateLogic.bcdDecoderLogic(chip, 9);
		gateLogic.bcdDecoderLogic(chip, 10);
		gateLogic.bcdDecoderLogic(chip, 11);
	}

	public static void chip7444Logic(Chip chip) {
		gateLogic.grayDecoderLogic(chip, 1);
		gateLogic.grayDecoderLogic(chip, 2);
		gateLogic.grayDecoderLogic(chip, 3);
		gateLogic.grayDecoderLogic(chip, 4);
		gateLogic.grayDecoderLogic(chip, 5);
		gateLogic.grayDecoderLogic(chip, 6);
		gateLogic.grayDecoderLogic(chip, 7);
		gateLogic.grayDecoderLogic(chip, 9);
		gateLogic.grayDecoderLogic(chip, 10);
		gateLogic.grayDecoderLogic(chip, 11);
	}

	public static void chip7482Logic(Chip chip) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

		Pin A1 = pins.get(2);
		Pin A2 = pins.get(14);
		Pin B1 = pins.get(3);
		Pin B2 = pins.get(13);
		Pin C0 = pins.get(5);

		Pin S1 = pins.get(1);
		Pin S2 = pins.get(12);
		Pin C2 = pins.get(10);

		if (Set.of(A1, A2, B1, B2, C0).stream().anyMatch(pin -> pin.getPinState() == UNKNOWN)) {
			S1.setPinState(UNKNOWN);
			S2.setPinState(UNKNOWN);
			C2.setPinState(UNKNOWN);
			return;
		}

		if (arePinsLow(A1, B1, A2, B2, C0)) {
			S1.setPinState(LOW);
			S2.setPinState(LOW);
			C2.setPinState(LOW);
		} else if (arePinsLow(B1, A2, B2, C0) && isPinHigh(A1)) {
			S1.setPinState(HIGH);
			S2.setPinState(LOW);
			C2.setPinState(LOW);
		} else if (arePinsLow(A1, A2, B2, C0) && isPinHigh(B1)) {
			S1.setPinState(HIGH);
			S2.setPinState(LOW);
			C2.setPinState(LOW);
		} else if (arePinsLow(A2, B2, C0) && isPinHigh(A1, B1)) {
			S1.setPinState(LOW);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(A1, B1, B2, C0) && isPinHigh(A2)) {
			S1.setPinState(LOW);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(B1, B2, C0) && isPinHigh(A1, A2)) {
			S1.setPinState(HIGH);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(A1, B2, C0) && isPinHigh(B1, A2)) {
			S1.setPinState(HIGH);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(B2, C0) && isPinHigh(A1, B1, A2)) {
			S1.setPinState(LOW);
			S2.setPinState(LOW);
			C2.setPinState(HIGH);
		} else if (arePinsLow(A1, B1, C0) && isPinHigh(A2, B2)) {
			S1.setPinState(LOW);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(B1, C0) && isPinHigh(A1, A2, B2)) {
			S1.setPinState(HIGH);
			S2.setPinState(LOW);
			C2.setPinState(HIGH);
		} else if (arePinsLow(A1, C0) && isPinHigh(B1, A2, B2)) {
			S1.setPinState(HIGH);
			S2.setPinState(LOW);
			C2.setPinState(HIGH);
		} else if (arePinsLow(C0) && isPinHigh(A1, B1, A2, B2)) {
			S1.setPinState(LOW);
			S2.setPinState(HIGH);
			C2.setPinState(HIGH);
		} else if (arePinsLow(A1, B1, A2, B2) && isPinHigh(C0)) {
			S1.setPinState(HIGH);
			S2.setPinState(LOW);
			C2.setPinState(LOW);
		} else if (arePinsLow(B1, A2, B2) && isPinHigh(A1, C0)) {
			S1.setPinState(LOW);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(A1, A2, B2) && isPinHigh(B1, C0)) {
			S1.setPinState(LOW);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(A2, B2) && isPinHigh(A1, B1, C0)) {
			S1.setPinState(HIGH);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(A1, B1, B2) && isPinHigh(A2, C0)) {
			S1.setPinState(HIGH);
			S2.setPinState(HIGH);
			C2.setPinState(LOW);
		} else if (arePinsLow(B1, B2) && isPinHigh(A1, A2, C0)) {
			S1.setPinState(LOW);
			S2.setPinState(LOW);
			C2.setPinState(HIGH);
		} else if (arePinsLow(A1, B2) && isPinHigh(B1, A2, C0)) {
			S1.setPinState(LOW);
			S2.setPinState(LOW);
			C2.setPinState(HIGH);
		} else if (arePinsLow(B2) && isPinHigh(A1, B1, A2, C0)) {
			S1.setPinState(HIGH);
			S2.setPinState(LOW);
			C2.setPinState(HIGH);
		} else if (arePinsLow(A1, B1) && isPinHigh(A2, B2, C0)) {
			S1.setPinState(HIGH);
			S2.setPinState(LOW);
			C2.setPinState(HIGH);
		} else if (arePinsLow(B1) && isPinHigh(A1, A2, B2, C0)) {
			S1.setPinState(LOW);
			S2.setPinState(HIGH);
			C2.setPinState(HIGH);
		} else if (arePinsLow(A1) && isPinHigh(B1, A2, B2, C0)) {
			S1.setPinState(LOW);
			S2.setPinState(HIGH);
			C2.setPinState(HIGH);
		} else if (arePinsHigh(A1, B1, A2, B2, C0)) {
			S1.setPinState(HIGH);
			S2.setPinState(HIGH);
			C2.setPinState(HIGH);
		} else {
			S1.setPinState(UNKNOWN);
			S2.setPinState(UNKNOWN);
			C2.setPinState(UNKNOWN);
		}
	}

	private static boolean arePinsLow(Pin... pins) {
		return Set.of(pins).stream().allMatch(pin -> pin.getPinState() == LOW);
	}

	private static boolean arePinsHigh(Pin... pins) {
		return Set.of(pins).stream().allMatch(pin -> pin.getPinState() == HIGH);
	}

	private static boolean isPinHigh(Pin... pins) {
		return Set.of(pins).stream().allMatch(pin -> pin.getPinState() == HIGH);
	}

	public static void chip74138Logic(Chip chip) {
		gateLogic._74138DecoderLogic(chip, 7);
		gateLogic._74138DecoderLogic(chip, 9);
		gateLogic._74138DecoderLogic(chip, 10);
		gateLogic._74138DecoderLogic(chip, 11);
		gateLogic._74138DecoderLogic(chip, 12);
		gateLogic._74138DecoderLogic(chip, 13);
		gateLogic._74138DecoderLogic(chip, 14);
		gateLogic._74138DecoderLogic(chip, 15);
	}

	public static void chip74152Logic(Chip chip) {
		Map<Integer, AbstractPin> pins = chip.getPinMap();

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

		int selector = (A.getPinState() == HIGH ? 1 : 0) |
				(B.getPinState() == HIGH ? 2 : 0) |
				(C.getPinState() == HIGH ? 4 : 0);

		Pin selectedPin;
		switch (selector) {
			case 0 -> selectedPin = D0;
			case 1 -> selectedPin = D1;
			case 2 -> selectedPin = D2;
			case 3 -> selectedPin = D3;
			case 4 -> selectedPin = D4;
			case 5 -> selectedPin = D5;
			case 6 -> selectedPin = D6;
			case 7 -> selectedPin = D7;
			default -> throw new IllegalStateException("Unexpected selector value: " + selector);
		}
		W.setPinState(gateLogic.invertState(selectedPin.getPinState()));
	}
}
