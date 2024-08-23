package edu.uj.po.simulation.util;

import edu.uj.po.simulation.Simulation;
import edu.uj.po.simulation.interfaces.*;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.chip.Chip7400;
import edu.uj.po.simulation.model.chip.HeaderIn;
import edu.uj.po.simulation.model.chip.HeaderOut;
import edu.uj.po.simulation.model.pin.PinIn;
import edu.uj.po.simulation.model.pin.PinOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

public class UtilTest {

	private Simulation simulation;

	@BeforeEach
	public void setUp() {
		simulation = new Simulation();
	}

	@Test
	public void testChipsNotNull() {
		assertNotNull(simulation.getChips(), "chips should not be null");
	}

	@Test
	public void testSaveCircuitState() throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException{
		int chip0 = simulation.createInputPinHeader(2);
		int chip1 = simulation.createChip(7402);
		int chip2 = simulation.createOutputPinHeader(1);

		simulation.connect(chip0, 1, chip1, 3);
		simulation.connect(chip0, 2, chip1, 2);
		simulation.connect(chip1, 1, chip2, 1);

		Set<ComponentPinState> states = new HashSet<>();
		states.add(new ComponentPinState(chip0, 1, PinState.HIGH));
		states.add(new ComponentPinState(chip0, 2, PinState.LOW));

		simulation.stationaryState(states);

		Set<ComponentPinState> expectedState = new HashSet<>();
		expectedState.add(new ComponentPinState(chip0, 1, PinState.HIGH));
		expectedState.add(new ComponentPinState(chip0, 2, PinState.LOW));

		expectedState.add(new ComponentPinState(chip1, 1, PinState.LOW));
		expectedState.add(new ComponentPinState(chip1, 2, PinState.LOW));
		expectedState.add(new ComponentPinState(chip1, 3, PinState.HIGH));
		expectedState.add(new ComponentPinState(chip1, 4, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chip1, 5, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chip1, 6, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chip1, 8, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chip1, 9, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chip1, 10, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chip1, 11, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chip1, 12, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chip1, 13, PinState.UNKNOWN));

		expectedState.add(new ComponentPinState(chip2, 1, PinState.LOW));

		Set<ComponentPinState> actualState = Util.saveCircuitState(simulation.getChips());

		assertEquals(expectedState, actualState, "The circuit state was not saved correctly.");
	}

	@Test
	public void testSaveCircuitState_EmptyChips() {
		simulation.getChips().clear();

		Set<ComponentPinState> actualState = Util.saveCircuitState(simulation.getChips());

		assertTrue(actualState.isEmpty(), "The circuit state should be empty when there are no chips.");
	}

	@Test
	public void testSaveCircuitState_SingleChip() {
		simulation.getChips().clear();
		Chip chip1 = new HeaderOut();
		chip1.putToPinMap(1, new PinIn());
		chip1.getPinMap().get(1).setPinState(PinState.HIGH);
		simulation.getChips().put(1, chip1);

		Set<ComponentPinState> expectedState = new HashSet<>();
		expectedState.add(new ComponentPinState(1, 1, PinState.HIGH));

		Set<ComponentPinState> actualState = Util.saveCircuitState(simulation.getChips());

		assertEquals(expectedState, actualState, "The circuit state was not saved correctly for a single chip.");
	}

	//TODO: poniżej testy dla saveCircuitHeaderOutState()
	@Test
	public void testSaveCircuitHeaderOutState_WithMultipleOutputHeaders() throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException {
		int chipId1 = simulation.createOutputPinHeader(2);
		int chipId2 = simulation.createOutputPinHeader(3);

		simulation.getChips().get(chipId1).getPinMap().get(1).setPinState(PinState.HIGH);
		simulation.getChips().get(chipId1).getPinMap().get(2).setPinState(PinState.LOW);
		simulation.getChips().get(chipId2).getPinMap().get(1).setPinState(PinState.UNKNOWN);
		simulation.getChips().get(chipId2).getPinMap().get(2).setPinState(PinState.HIGH);
		simulation.getChips().get(chipId2).getPinMap().get(3).setPinState(PinState.LOW);

		Set<ComponentPinState> expectedState = new HashSet<>();
		expectedState.add(new ComponentPinState(chipId1, 1, PinState.HIGH));
		expectedState.add(new ComponentPinState(chipId1, 2, PinState.LOW));
		expectedState.add(new ComponentPinState(chipId2, 1, PinState.UNKNOWN));
		expectedState.add(new ComponentPinState(chipId2, 2, PinState.HIGH));
		expectedState.add(new ComponentPinState(chipId2, 3, PinState.LOW));

		Set<ComponentPinState> actualState = Util.saveCircuitHeaderOutState(simulation.getChips());

		assertEquals(expectedState, actualState, "The circuit state of the output headers was not saved correctly.");
	}

	@Test
	public void testSaveCircuitHeaderOutState_WithSingleOutputHeader() throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException {
		int chipId1 = simulation.createOutputPinHeader(2);

		simulation.getChips().get(chipId1).getPinMap().get(1).setPinState(PinState.HIGH);
		simulation.getChips().get(chipId1).getPinMap().get(2).setPinState(PinState.UNKNOWN);

		Set<ComponentPinState> expectedState = new HashSet<>();
		expectedState.add(new ComponentPinState(chipId1, 1, PinState.HIGH));
		expectedState.add(new ComponentPinState(chipId1, 2, PinState.UNKNOWN));

		Set<ComponentPinState> actualState = Util.saveCircuitHeaderOutState(simulation.getChips());

		assertEquals(expectedState, actualState, "The circuit state of the single output header was not saved correctly.");
	}

	@Test
	public void testSaveCircuitHeaderOutState_EmptyChips() {
		simulation.getChips().clear();

		Set<ComponentPinState> actualState = Util.saveCircuitHeaderOutState(simulation.getChips());

		assertTrue(actualState.isEmpty(), "The circuit state should be empty when there are no output headers.");
	}

	@Test
	public void testSaveCircuitHeaderOutState_WithInputAndOutputHeadersAndChip() throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException {
		int chipIdIn = simulation.createInputPinHeader(2);
		int chipId7400 = simulation.createChip(7400);
		int chipIdOut = simulation.createOutputPinHeader(2);

		simulation.getChips().get(chipIdIn).getPinMap().get(1).setPinState(PinState.HIGH);
		simulation.getChips().get(chipIdIn).getPinMap().get(2).setPinState(PinState.LOW);
		simulation.getChips().get(chipId7400).getPinMap().get(3).setPinState(PinState.HIGH);
		simulation.getChips().get(chipId7400).getPinMap().get(6).setPinState(PinState.LOW);
		simulation.getChips().get(chipIdOut).getPinMap().get(1).setPinState(PinState.HIGH);
		simulation.getChips().get(chipIdOut).getPinMap().get(2).setPinState(PinState.UNKNOWN);

		Set<ComponentPinState> expectedState = new HashSet<>();
		expectedState.add(new ComponentPinState(chipIdOut, 1, PinState.HIGH));
		expectedState.add(new ComponentPinState(chipIdOut, 2, PinState.UNKNOWN));

		Set<ComponentPinState> actualState = Util.saveCircuitHeaderOutState(simulation.getChips());

		assertEquals(expectedState, actualState, "The circuit state with input and output headers and a chip was not saved correctly.");
	}

	@Test
	public void testSaveCircuitHeaderOutState_WithMultipleChipsAndHeaders() throws UnknownChip, UnknownPin, ShortCircuitException, UnknownComponent, UnknownStateException {
		int chipIdIn = simulation.createInputPinHeader(2);
		int chipId7400 = simulation.createChip(7400);
		int chipId7404 = simulation.createChip(7404);
		int chipIdOut = simulation.createOutputPinHeader(3);

		// Ustawienie stanów na pinach
		simulation.getChips().get(chipIdIn).getPinMap().get(1).setPinState(PinState.HIGH);
		simulation.getChips().get(chipIdIn).getPinMap().get(2).setPinState(PinState.LOW);
		simulation.getChips().get(chipId7400).getPinMap().get(3).setPinState(PinState.HIGH);
		simulation.getChips().get(chipId7400).getPinMap().get(6).setPinState(PinState.LOW);
		simulation.getChips().get(chipId7404).getPinMap().get(2).setPinState(PinState.LOW);
		simulation.getChips().get(chipId7404).getPinMap().get(4).setPinState(PinState.HIGH);
		simulation.getChips().get(chipIdOut).getPinMap().get(1).setPinState(PinState.LOW);
		simulation.getChips().get(chipIdOut).getPinMap().get(2).setPinState(PinState.HIGH);
		simulation.getChips().get(chipIdOut).getPinMap().get(3).setPinState(PinState.UNKNOWN);

		Set<ComponentPinState> expectedState = new HashSet<>();
		expectedState.add(new ComponentPinState(chipIdOut, 1, PinState.LOW));
		expectedState.add(new ComponentPinState(chipIdOut, 2, PinState.HIGH));
		expectedState.add(new ComponentPinState(chipIdOut, 3, PinState.UNKNOWN));

		Set<ComponentPinState> actualState = Util.saveCircuitHeaderOutState(simulation.getChips());

		assertEquals(expectedState, actualState, "The circuit state with multiple chips and headers was not saved correctly.");
	}
}
