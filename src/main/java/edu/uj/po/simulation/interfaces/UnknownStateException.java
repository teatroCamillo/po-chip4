package edu.uj.po.simulation.interfaces;

/**
 * Exception thrown when a pin required for the simulation is in the UNKNOWN state.
 */
public class UnknownStateException extends Exception {
	private static final long serialVersionUID = -8226709432532311464L;
	private final ComponentPinState pin;

	public UnknownStateException(ComponentPinState unknownStateLocalation) {
		this.pin = unknownStateLocalation;
	}

	/**
	 * The method returns information about the problematic pin.
	 *
	 * @return A ComponentPinState object for the problematic pin in the UNKNOWN state.
	 */
	public ComponentPinState pinState() {
		return pin;
	}

}
