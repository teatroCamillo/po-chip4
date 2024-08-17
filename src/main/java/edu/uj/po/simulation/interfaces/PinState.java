package edu.uj.po.simulation.interfaces;

/**
 * Możliwe stany pinów. Stan UNKNOWN zarezerwowany jest dla pinów, których stan
 * nie może zostać ustalony.
 */
public enum PinState {
	HIGH, LOW, UNKNOWN;
}
