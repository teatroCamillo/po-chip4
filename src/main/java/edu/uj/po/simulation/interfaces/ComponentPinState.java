package edu.uj.po.simulation.interfaces;

/**
 * Stan określonego pinu określonego komponentu.
 */
public record ComponentPinState( int componentId, int pinId, PinState state ) {
}
