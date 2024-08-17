package edu.uj.po.simulation.interfaces;

/**
 * Komponent o wskazanym identyfikatorze nie istnieje.
 */
public class UnknownComponent extends Exception {

	private static final long serialVersionUID = 7834944488895446024L;
	private final int componentId;

	public UnknownComponent(int componentId) {
		this.componentId = componentId;
	}

	/**
	 * Błędny identyfikator elementu układu.
	 * 
	 * @return wartość błędnego identyfikatora
	 */
	public int getComponentId() {
		return componentId;
	}

}
