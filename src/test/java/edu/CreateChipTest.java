package edu;

import edu.manager.CircuitManager;
import edu.uj.po.simulation.interfaces.UnknownChip;
import edu.model.chip.Chip;
import edu.model.chip.HeaderIn;
import edu.model.chip.HeaderOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CreateChipTest{

	private CircuitManager circuitManager;

	@BeforeEach
	void setUp() {
		circuitManager = new CircuitManager();
	}

	@Test
	void testSimulationInitialization() throws UnknownChip{
		assertNotNull(circuitManager, "edu.Simulation instance should be initialized.");
		assertTrue(circuitManager.createChip(7400) >= 0, "edu.Simulation should have initialized available chips.");
	}

	@ParameterizedTest
	@CsvSource({
			"7400, edu.model.chip.Chip7400",
			"7402, edu.model.chip.Chip7402",
			"7404, edu.model.chip.Chip7404",
			"7408, edu.model.chip.Chip7408",
			"7410, edu.model.chip.Chip7410",
			"7411, edu.model.chip.Chip7411",
			"7420, edu.model.chip.Chip7420",
			"7431, edu.model.chip.Chip7431",
			"7432, edu.model.chip.Chip7432",
			"7434, edu.model.chip.Chip7434",
			"7442, edu.model.chip.Chip7442",
			"7444, edu.model.chip.Chip7444",
			"7482, edu.model.chip.Chip7482",
			"74138, edu.model.chip.Chip74138",
			"74152, edu.model.chip.Chip74152"
	})
	void testCreateChip(int chipCode, String expectedClassName) throws UnknownChip {
		int chipId = circuitManager.createChip(chipCode);
		assertTrue(chipId >= 0, "Chip ID should be greater or equal to 0.");
		Chip chip = circuitManager.getChips().get(chipId);
		assertEquals(expectedClassName, chip.getClass().getName(), "Created chip should be an instance of " + expectedClassName);
	}

	@Test
	void testCreateInputPinHeader() {
		int headerId = circuitManager.createInputPinHeader(2);
		assertTrue(headerId >= 0, "Input Pin Header ID should be greater or equal to 0.");
		Chip chip = circuitManager.getChips().get(headerId);
		assertTrue(chip instanceof HeaderIn, "Created input pin header should be an instance of HeaderIn.");
		assertEquals(2, chip.getPinMap().size(), "Input pin header should have correct number of pins.");
	}

	@Test
	void testCreateOutputPinHeader() {
		int headerId = circuitManager.createOutputPinHeader(3);
		Chip chip = circuitManager.getChips().get(headerId);
		assertTrue(chip instanceof HeaderOut, "Created output pin header should be an instance of HeaderOut.");
		assertEquals(3, chip.getPinMap().size(), "Output pin header should have correct number of pins.");
	}

	@Test
	void testCreateChipThrowsUnknownChip() {
		int unsupportedChipCode = 1111;

		assertThrows(UnknownChip.class, () -> {
			circuitManager.createChip(unsupportedChipCode);
		}, "Should throw UnknownChip if the chip code is not supported.");
	}

	@Test
	void testCreateChipThrowsUnknownChipFor4442() {
		int unsupportedChipCode = 4442;

		assertThrows(UnknownChip.class, () -> {
			circuitManager.createChip(unsupportedChipCode);
		}, "Should throw UnknownChip if the chip code 7442 is not supported.");
	}
}
