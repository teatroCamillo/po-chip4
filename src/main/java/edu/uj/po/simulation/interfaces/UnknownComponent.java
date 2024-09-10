package edu.uj.po.simulation.interfaces;

/**
 * The component with the specified identifier does not exist.
 */
public class UnknownComponent extends Exception {

	private static final long serialVersionUID = 7834944488895446024L;
	private final int componentId;

	public UnknownComponent(int componentId) {
		this.componentId = componentId;
	}

	/**
	 * Invalid circuit component identifier.
	 *
	 * @return the value of the invalid identifier
	 */
	public int getComponentId() {
		return componentId;
	}

}
