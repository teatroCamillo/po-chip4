package edu.uj.po.simulation.interfaces;

/**
 * Wyjątek używany w przypadku wskazania pinu nieistniejącego w danym
 * komponencie.
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
	 * Identyfikator komponentu, w którym pin o podanym numerze nie istnieje.
	 * 
	 * @return identyfikator komponentu
	 */
	public int getComponentId() {
		return componentId;
	}

	/**
	 * Wskazany, błędny numer pinu.
	 * 
	 * @return błędny numer pinu
	 */
	public int getPin() {
		return pin;
	}

}
