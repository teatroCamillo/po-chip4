package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.UnknownChip;
import edu.uj.po.simulation.manager.ComponentManager;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.chip.HeaderIn;
import edu.uj.po.simulation.model.chip.HeaderOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CreateChipTest{

	private ComponentManager componentManager;

	@BeforeEach
	void setUp() {
		componentManager = new ComponentManager();
	}

	@Test
	void testSimulationInitialization() throws UnknownChip{
		assertNotNull(componentManager, "Simulation instance should be initialized.");
		assertTrue(componentManager.createChip(7400) >= 0, "Simulation should have initialized available chips.");
	}

	@ParameterizedTest
	@CsvSource({
			"7400, edu.uj.po.simulation.model.chip.Chip7400",
			"7402, edu.uj.po.simulation.model.chip.Chip7402",
			"7404, edu.uj.po.simulation.model.chip.Chip7404",
			"7408, edu.uj.po.simulation.model.chip.Chip7408",
			"7410, edu.uj.po.simulation.model.chip.Chip7410"
	})
	void testCreateChip(int chipCode, String expectedClassName) throws UnknownChip {
		int chipId = componentManager.createChip(chipCode);
		assertTrue(chipId >= 0, "Chip ID should be greater or equal to 0.");
		Chip chip = componentManager.getChips().get(chipId);
		assertEquals(expectedClassName, chip.getClass().getName(), "Created chip should be an instance of " + expectedClassName);
	}

	@Test
	void testCreateInputPinHeader() {
		int headerId = componentManager.createInputPinHeader(2);
		assertTrue(headerId >= 0, "Input Pin Header ID should be greater or equal to 0.");
		Chip chip = componentManager.getChips().get(headerId);
		assertTrue(chip instanceof HeaderIn, "Created input pin header should be an instance of HeaderIn.");
		assertEquals(2, chip.getPinMap().size(), "Input pin header should have correct number of pins.");
	}

	@Test
	void testCreateOutputPinHeader() {
		int headerId = componentManager.createOutputPinHeader(3);
		Chip chip = componentManager.getChips().get(headerId);
		assertTrue(chip instanceof HeaderOut, "Created output pin header should be an instance of HeaderOut.");
		assertEquals(3, chip.getPinMap().size(), "Output pin header should have correct number of pins.");
	}

	@Test
	void testCreateChipThrowsUnknownChip() {
		int unsupportedChipCode = 1111;

		assertThrows(UnknownChip.class, () -> {
			componentManager.createChip(unsupportedChipCode);
		}, "Should throw UnknownChip if the chip code is not supported.");
	}
}
