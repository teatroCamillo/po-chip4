package edu.uj.po.simulation.util;

import edu.uj.po.simulation.interfaces.ComponentPinState;
import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.chip.Chip7400;
import edu.uj.po.simulation.model.chip.HeaderIn;
import edu.uj.po.simulation.model.chip.HeaderOut;
import edu.uj.po.simulation.model.pin.PinIn;
import edu.uj.po.simulation.model.pin.PinOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UtilTest {

	private Map<Integer, Chip> chips;

	@BeforeEach
	public void setUp() {
		System.out.println("Setting up the test...");
		chips = new HashMap<>();

		// Dodajemy przykładowe chipy do mapy
		Chip chip1 = new HeaderIn();
		chip1.putToPinMap(1, new PinIn());
		chip1.putToPinMap(2, new PinOut());
		chip1.getPinMap().get(1).setPinState(PinState.HIGH);
		chip1.getPinMap().get(2).setPinState(PinState.LOW);

		Chip chip2 = new Chip7400();
		chip2.putToPinMap(1, new PinIn());
		chip2.putToPinMap(2, new PinOut());
		chip2.getPinMap().get(1).setPinState(PinState.UNKNOWN);
		chip2.getPinMap().get(2).setPinState(PinState.HIGH);

		chips.put(1, chip1);
		chips.put(2, chip2);
	}

	@Test
	public void testChipsNotNull() {
		assertNotNull(chips, "chips should not be null");
	}

//	@Test
//	public void testSaveCircuitState() {
//		Set<ComponentPinState> expectedState = new HashSet<>();
//		expectedState.add(new ComponentPinState(1, 1, PinState.HIGH));
//		expectedState.add(new ComponentPinState(1, 2, PinState.LOW));
//		expectedState.add(new ComponentPinState(2, 1, PinState.UNKNOWN));
//		expectedState.add(new ComponentPinState(2, 2, PinState.HIGH));
//
//		Set<ComponentPinState> actualState = Util.saveCircuitState(chips);
//
//		assertEquals(expectedState, actualState, "The circuit state was not saved correctly.");
//	}
//
//	@Test
//	public void testSaveCircuitState_EmptyChips() {
//		chips.clear();  // Usunięcie wszystkich chipów z mapy
//
//		Set<ComponentPinState> actualState = Util.saveCircuitState(chips);
//
//		assertTrue(actualState.isEmpty(), "The circuit state should be empty when there are no chips.");
//	}
//
//	@Test
//	public void testSaveCircuitState_SingleChip() {
//		chips.clear();
//		Chip chip1 = new HeaderOut();
//		chip1.putToPinMap(1, new PinIn());
//		chip1.getPinMap().get(1).setPinState(PinState.HIGH);
//		chips.put(1, chip1);
//
//		Set<ComponentPinState> expectedState = new HashSet<>();
//		expectedState.add(new ComponentPinState(1, 1, PinState.HIGH));
//
//		Set<ComponentPinState> actualState = Util.saveCircuitState(chips);
//
//		assertEquals(expectedState, actualState, "The circuit state was not saved correctly for a single chip.");
//	}
}
