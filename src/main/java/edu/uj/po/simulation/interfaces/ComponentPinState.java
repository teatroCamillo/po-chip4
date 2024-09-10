package edu.uj.po.simulation.interfaces;

/**
 * The state of a specified pin of a specified component.
 */
public record ComponentPinState( int componentId, int pinId, PinState state ) {
}
