package edu.uj.po.simulation.interfaces;

/**
 * Exception used when a specified pin does not exist in the given component.
 */
public class UnknownPin extends Exception {
	private static final long serialVersionUID = 3834350553925476176L;
	private final int componentId;
	private final int pin;

	public UnknownPin(int componentId, int pin) {
		this.componentId = componentId;
		this.pin = pin;
	}

	/**
	 * The identifier of the component in which the pin with the specified number does not exist.
	 *
	 * @return the identifier of the component
	 */
	public int getComponentId() {
		return componentId;
	}

	/**
	 * The specified invalid pin number.
	 *
	 * @return the invalid pin number
	 */
	public int getPin() {
		return pin;
	}

}
