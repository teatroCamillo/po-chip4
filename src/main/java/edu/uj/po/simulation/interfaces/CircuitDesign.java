package edu.uj.po.simulation.interfaces;

public interface CircuitDesign {
	/**
	 * The method requests the creation of an integrated circuit with the given numerical code.
	 * Example codes include 7400, 7402, etc. The method returns a unique identifier,
	 * which will be used to identify this circuit. The method must return
	 * a globally unique identifier – every created element must have a different one.
	 *
	 * @param code the type of integrated circuit
	 * @return a globally unique identifier for the element.
	 * @throws UnknownChip if the code does not correspond to any implemented circuit
	 */
	public int createChip(int code) throws UnknownChip, CloneNotSupportedException;

	/**
	 * Creates an input pin header with the given size. It is possible to use multiple pin headers
	 * in the project. The method must return a globally unique identifier – each created element must be unique.
	 *
	 * @param size the number of pins in the pin header
	 * @return a globally unique identifier for the element
	 */
	public int createInputPinHeader(int size);

	/**
	 * Creates an output pin header with the given size. It is possible to use multiple pin headers
	 * in the project. The method must return a globally unique identifier – each created element must be unique.
	 *
	 * @param size the number of pins in the pin header
	 * @return a globally unique identifier for the element
	 */
	public int createOutputPinHeader(int size);

	/**
	 * The method requests the connection of pin pin1 of component component1 with pin pin2 of component component2.
	 * A component can be either an integrated circuit or a pin header.
	 * The method checks the validity of the request, and if the data is incorrect or
	 * connection rules are violated, it throws the appropriate exception.
	 *
	 * @param component1 the identifier of the first component
	 * @param pin1       the pin number of the first component
	 * @param component2 the identifier of the second component
	 * @param pin2       the pin number of the second component
	 * @throws UnknownComponent      if the component is unknown (invalid component identifier)
	 * @throws UnknownPin            if the pin is unknown for the given component
	 * @throws ShortCircuitException if a connection error occurs – connecting two different outputs.
	 */
	public void connect(int component1, int pin1, int component2, int pin2)
			throws UnknownComponent, UnknownPin, ShortCircuitException;

}
