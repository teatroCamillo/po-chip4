package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationTest{

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	// TODO: czy uwzględniać taki przypadek
	//  - gdy na listiw wyjściowej jeden lub wiele pinów jest niepodłączonych stan UNKONOWN

	// TODO: można raz stworzony test testować dla różnej ilośic TICKS - albo jeszcze prościej:
	//  dodać asercje dla różnych ticks w jednym teście bo przecież w mapie jest storowany każdy TICK

	// T1
	@Test
	void testSimulationSimpleCircuit() throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent{
		// 1. Deklaracja chipów
		int chipIn1 = simulation.createInputPinHeader(2);
		int chip7400 = simulation.createChip(7400);
		int chipOut1 = simulation.createOutputPinHeader(1);

		// 2. Deklaracja połączeń
		simulation.connect(chipIn1, 1, chip7400, 1);
		simulation.connect(chipIn1, 2, chip7400, 2);
		simulation.connect(chip7400, 3, chipOut1, 1);

		// 3. Deklaracja dla stanu stacjonarnego
		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 2, PinState.LOW));

		// 4. Ustalenie stanu stacjonarnego
		simulation.stationaryState(states);

		// 5. Deklaracja dla stanu w chwili 0
		Set<ComponentPinState> states0 = new HashSet<>();
		states0.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn1, 2, PinState.HIGH));

		// 6. Symulacja dla określonej ilości TICKS
		Map<Integer, Set<ComponentPinState>> result = simulation.simulation(states0, 3);

		// 7. Assertion
		assertEquals(PinState.HIGH, result.get(0).stream().findFirst().get().state());
		assertEquals(PinState.LOW, result.get(1).stream().findFirst().get().state());
		assertEquals(PinState.LOW, result.get(2).stream().findFirst().get().state());
		assertEquals(PinState.LOW, result.get(3).stream().findFirst().get().state());
	}

	// T2
	@ParameterizedTest
	@CsvSource({
			"0, HIGH, HIGH",
			"1, LOW, HIGH",
			"2, LOW, HIGH",
			"3, LOW, LOW",
			"4, LOW, LOW"
	})
	void testSimulationComplexCircuit(int tick, String headerOutInputPin1State, String headerOutInputPin2State) throws UnknownChip,
			UnknownStateException,	UnknownPin,
			ShortCircuitException, UnknownComponent {

		int chipIn1 = simulation.createInputPinHeader(3);
		int chip7400 = simulation.createChip(7400);
		int chip7402 = simulation.createChip(7402);
		int chip7404 = simulation.createChip(7404);
		int chipOut1 = simulation.createOutputPinHeader(2);

		simulation.connect(chipIn1, 1, chip7400, 1);
		simulation.connect(chipIn1, 2, chip7400, 2);
		simulation.connect(chipIn1, 3, chip7402, 9);

		simulation.connect(chip7400, 3, chip7402, 8);
		simulation.connect(chip7400, 3, chipOut1, 1);

		simulation.connect(chip7402, 10, chip7404, 3);

		simulation.connect(chip7404, 4, chipOut1, 2);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 2, PinState.LOW));
		states.add(new ComponentPinState(chipIn1, 3, PinState.HIGH));

		simulation.stationaryState(states);

		Set<ComponentPinState> states0 = new HashSet<>();
		states0.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn1, 2, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn1, 3, PinState.LOW));

		Map<Integer, Set<ComponentPinState>> result = simulation.simulation(states0, 4);

		assertEquals(PinState.valueOf(headerOutInputPin1State), result.get(tick).stream()
				.filter(state -> state.pinId() == 1)
				.findFirst().orElseThrow().state());

		assertEquals(PinState.valueOf(headerOutInputPin2State), result.get(tick).stream()
				.filter(state -> state.pinId() == 2)
				.findFirst().orElseThrow().state());
	}

	//T3
	@ParameterizedTest
	@CsvSource({
			"0, HIGH, HIGH, LOW",
			"1, HIGH, HIGH, HIGH",
			"2, HIGH, HIGH, HIGH",
			"3, HIGH, HIGH, HIGH"
	})
	void testSimulationMaxComplexCircuit(int tick,
										 String headerOutInputPin1State,
										 String headerOutInputPin2State,
										 String headerOutInputPin3State)
			throws UnknownChip, UnknownStateException, UnknownPin, ShortCircuitException, UnknownComponent {
		int chipIn0 = simulation.createInputPinHeader(1);
		int chipIn1 = simulation.createInputPinHeader(2);
		int chipIn2 = simulation.createInputPinHeader(3);
		int chip7400 = simulation.createChip(7400);
		int chip7402 = simulation.createChip(7402);
		int chip7404 = simulation.createChip(7404);
		int chip7408 = simulation.createChip(7408);
		int chip7410 = simulation.createChip(7410);
		int chipOut0 = simulation.createOutputPinHeader(1);
		int chipOut1 = simulation.createOutputPinHeader(2);

		simulation.connect(chipIn0, 1, chip7410, 9);

		simulation.connect(chipIn1, 1, chip7410, 10);
		simulation.connect(chipIn1, 2, chip7410, 11);

		simulation.connect(chipIn2, 1, chip7408, 5);
		simulation.connect(chipIn2, 2, chip7402, 2);
		simulation.connect(chipIn2, 3, chip7402, 3);

		simulation.connect(chip7410, 8, chip7408, 4);

		simulation.connect(chip7408, 6, chip7404, 13);
		simulation.connect(chip7408, 6, chipOut1, 2);

		simulation.connect(chip7404, 12, chip7400, 4);

		simulation.connect(chip7402, 1, chip7400, 5);

		simulation.connect(chip7400, 6, chipOut0, 1);
		simulation.connect(chip7400, 6, chipOut1, 1);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chipIn0, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 1, PinState.HIGH));
		states.add(new ComponentPinState(chipIn1, 2, PinState.LOW));
		states.add(new ComponentPinState(chipIn2, 1, PinState.LOW));
		states.add(new ComponentPinState(chipIn2, 2, PinState.LOW));
		states.add(new ComponentPinState(chipIn2, 3, PinState.HIGH));

		simulation.stationaryState(states);

		Set<ComponentPinState> states0 = new HashSet<>();
		states0.add(new ComponentPinState(chipIn0, 1, PinState.LOW));
		states0.add(new ComponentPinState(chipIn1, 1, PinState.LOW));
		states0.add(new ComponentPinState(chipIn1, 2, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn2, 1, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn2, 2, PinState.HIGH));
		states0.add(new ComponentPinState(chipIn2, 3, PinState.LOW));

		Map<Integer, Set<ComponentPinState>> result = simulation.simulation(states0, 5);


		assertEquals(PinState.valueOf(headerOutInputPin1State), result.get(tick).stream()
				.filter(state -> state.componentId() == chipOut0 && state.pinId() == 1)
				.findFirst().orElseThrow().state());

		assertEquals(PinState.valueOf(headerOutInputPin2State), result.get(tick).stream()
				.filter(state -> state.componentId() == chipOut1 && state.pinId() == 1)
				.findFirst().orElseThrow().state());

		assertEquals(PinState.valueOf(headerOutInputPin3State), result.get(tick).stream()
				.filter(state -> state.componentId() == chipOut1 && state.pinId() == 2)
				.findFirst().orElseThrow().state());
	}
}
