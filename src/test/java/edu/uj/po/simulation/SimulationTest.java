package edu.uj.po.simulation;

import edu.uj.po.simulation.interfaces.UnknownChip;
import edu.uj.po.simulation.interfaces.UnknownComponent;
import edu.uj.po.simulation.interfaces.UnknownPin;
import edu.uj.po.simulation.interfaces.ShortCircuitException;
import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.pin.PinIn;
import edu.uj.po.simulation.model.pin.PinOut;
import edu.uj.po.simulation.model.chip.HeaderIn;
import edu.uj.po.simulation.model.chip.HeaderOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

	private Simulation simulation;

	@BeforeEach
	void setUp() {
		simulation = new Simulation();
	}

	@Test
	void testSimulationInitialization() throws UnknownChip{
		assertNotNull(simulation, "Simulation instance should be initialized.");
		assertTrue(simulation.createChip(7400) >= 0, "Simulation should have initialized available chips.");
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
		int chipId = simulation.createChip(chipCode);
		assertTrue(chipId >= 0, "Chip ID should be greater or equal to 0.");
		Chip chip = simulation.chips.get(chipId);
		assertEquals(expectedClassName, chip.getClass().getName(), "Created chip should be an instance of " + expectedClassName);
	}

	@Test
	void testCreateInputPinHeader() {
		int headerId = simulation.createInputPinHeader(2);
		assertTrue(headerId >= 0, "Input Pin Header ID should be greater or equal to 0.");
		Chip chip = simulation.chips.get(headerId);
		assertTrue(chip instanceof HeaderIn, "Created input pin header should be an instance of HeaderIn.");
		assertEquals(4, chip.getPinMap().size(), "Input pin header should have correct number of pins.");
	}

	@Test
	void testCreateOutputPinHeader() {
		int headerId = simulation.createOutputPinHeader(2);
		Chip chip = simulation.chips.get(headerId);
		assertTrue(chip instanceof HeaderOut, "Created output pin header should be an instance of HeaderOut.");
		assertEquals(4, chip.getPinMap().size(), "Output pin header should have correct number of pins.");
	}

	@Test
	void testConnect() throws UnknownComponent, UnknownPin, ShortCircuitException, UnknownChip{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createOutputPinHeader(1);

		Chip chip1 = simulation.chips.get(chipId1);
		Chip chip2 = simulation.chips.get(chipId2);

		chip1.putToPinMap(1, new PinIn());
		chip1.putToPinMap(2, new PinIn());
		chip1.putToPinMap(3, new PinOut());

		chip2.putToPinMap(1, new PinIn());

		simulation.connect(chipId1, 3, chipId2, 1);

		assertTrue(simulation.directConnections.stream()
						   .anyMatch(connection -> connection.targetChipId() == chipId2 && connection.targetPinId() == 1),
				   "Chip1 should be connected to Chip2.");
	}

	@Test
	void testConnectThrowsUnknownComponent() {
		assertThrows(UnknownComponent.class, () -> {
			simulation.connect(100, 1, 101, 1);
		}, "Should throw UnknownComponent if component does not exist.");
	}

	@Test
	void testConnectThrowsUnknownPin() throws UnknownChip{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createOutputPinHeader(1);

		assertThrows(UnknownPin.class, () -> {
			simulation.connect(chipId1, 7, chipId2, 1);
		}, "Should throw UnknownPin if pin does not exist.");
	}

	//TODO: do sprawdzenia
	@Disabled
	void testConnectThrowsShortCircuitException() throws UnknownComponent, UnknownPin, ShortCircuitException, UnknownChip{
		int chipId1 = simulation.createChip(7400);
		int chipId2 = simulation.createOutputPinHeader(1);

		Chip chip1 = simulation.chips.get(chipId1);
		Chip chip2 = simulation.chips.get(chipId2);

		chip1.putToPinMap(1, new PinIn());
		chip1.putToPinMap(2, new PinIn());
		chip1.putToPinMap(3, new PinOut());

		chip2.putToPinMap(1, new PinIn());

		simulation.connect(chipId1, 3, chipId2, 1);

		assertThrows(ShortCircuitException.class, () -> {
			simulation.connect(chipId1, 3, chipId2, 1);
		}, "Should throw ShortCircuitException if connection already exists.");
	}
}
