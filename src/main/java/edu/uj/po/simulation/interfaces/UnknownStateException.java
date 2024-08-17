package edu.uj.po.simulation.interfaces;

/**
 * Wyjątek zwracany jeśli konieczny do wykonania symulacji pin znajduje się w
 * stanie UNKNOWN.
 */
public class UnknownStateException extends Exception {
	private static final long serialVersionUID = -8226709432532311464L;
	private final ComponentPinState pin;

	public UnknownStateException(ComponentPinState unknownStateLocalation) {
		this.pin = unknownStateLocalation;
	}

	/**
	 * Metoda zwraca informacje o problematycznym pinie.
	 * 
	 * @return Obiekt ComponentPinState dla problematycznego pinu w stanie UNKNOWN.
	 */
	public ComponentPinState pinState() {
		return pin;
	}

}
