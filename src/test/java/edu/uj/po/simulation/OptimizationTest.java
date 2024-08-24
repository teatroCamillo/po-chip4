package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizationTest{

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	// dla stanu w chwili zero nie testuje póki co
	// T1
	@Disabled
	@ParameterizedTest
	@CsvSource({
			"2, HIGH, LOW, '1,2'"
	})
	void testFromTaskDescription(int tick, String headerOutInputPin1State, String headerOutInputPin2State,
								 String setElements) throws UnknownChip, UnknownStateException, UnknownPin,
			ShortCircuitException,
			UnknownComponent{
		// 1. Deklaracja chipów
		int chipIn1 = simulation.createInputPinHeader(4);
		int chip7408c1 = simulation.createChip(7408);
		int chip7408c2 = simulation.createChip(7408);
		int chip7408c3 = simulation.createChip(7408);
		int chipOut1 = simulation.createOutputPinHeader(2);

		// 2. Deklaracja połączeń
		simulation.connect(chipIn1, 1, chip7408c1, 1);
		simulation.connect(chipIn1, 1, chip7408c2, 2);
		simulation.connect(chipIn1, 2, chip7408c1, 2);
		simulation.connect(chipIn1, 2, chip7408c2, 5);
		simulation.connect(chipIn1, 2, chip7408c3, 5);
		simulation.connect(chipIn1, 3, chip7408c2, 1);
		simulation.connect(chipIn1, 4, chip7408c2, 4);

		simulation.connect(chip7408c1, 3, chip7408c3, 13);

		simulation.connect(chip7408c2, 3, chip7408c3, 12);
		simulation.connect(chip7408c2, 6, chip7408c3, 4);

		simulation.connect(chip7408c3, 6, chipOut1, 1);
		simulation.connect(chip7408c3, 11, chipOut1, 2);

		// 3. Deklaracja dla stanu stacjonarnego
		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipIn1, 1, PinState.LOW));
		states.add(new ComponentPinState(chipIn1, 2, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 3, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 4, PinState.LOW));

		// 4. Ustalenie stanu stacjonarnego
		simulation.stationaryState(states);

		// 5. Deklaracja dla stanu w chwili 0
		Set<ComponentPinState> states0 = new HashSet<>();
		states0.add(new ComponentPinState(chipIn1, 1, PinState.LOW));
		states0.add(new ComponentPinState(chipIn1, 2, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn1, 3, PinState.LOW));
		states0.add(new ComponentPinState(chipIn1, 4, PinState.HIGH));

		// 6. Optymalizacja
		Set<Integer> expected = Arrays.stream(setElements.split(","))
				.map(Integer::valueOf)
				.collect(Collectors.toSet());
		System.out.println("Expected set: " + expected);

		Set<Integer> actual = simulation.optimize(states0, tick);

		assertEquals(expected, actual);
	}

}
