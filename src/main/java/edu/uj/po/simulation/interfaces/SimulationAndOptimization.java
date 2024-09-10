package edu.uj.po.simulation.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * Interface for Simulation functionality.
 * The primary functionality is the simulation of a circuit. Optimization is also a form of simulation,
 * but conducted with a slightly different goal, which here is the removal of unnecessary components.
 */
public interface SimulationAndOptimization {
	/**
	 * The method sets the states of input pin headers. Running the simulation
	 * of the circuit leads to determining the state of all relevant inputs/outputs of the circuit.
	 *
	 * @param states a set of states for the input pin headers
	 * @throws UnknownStateException if a pin used in the simulation is in an undefined state
	 */
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException;

	/**
	 * The method initiates the circuit simulation by specifying the state of the input pin headers at time 0
	 * and the number of time steps to execute. The result of the simulation is a map containing,
	 * for each time step (from 0 to the specified number of ticks inclusive), sets that provide
	 * information on the state of all output pins of the pin headers. Unconnected output pins
	 * will be in the UNKNOWN state.
	 *
	 * @param states0 the set of input pin header states at time 0
	 * @param ticks   the number of simulation steps
	 * @return a map where the key is the time step number (from time 0 to the specified ticks, inclusive).
	 * @throws UnknownStateException if a pin used in the simulation is in an undefined state
	 */
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0, int ticks)
			throws UnknownStateException;

	/**
	 * The method optimizes the circuit by detecting components whose absence would not affect
	 * the circuit's behavior. The initial state of the input pin headers at time zero and the number
	 * of time steps for analyzing the circuit's operation are provided by the user. If removing a component
	 * does not affect any of the output pin header states, the component is added to the resulting
	 * set of component identifiers that can be removed.
	 *
	 * @param states0 the set of input pin header states at time 0
	 * @param ticks   the number of simulation steps
	 * @return a set of component identifiers whose removal will not affect the observed states
	 *         of the output pin headers during the simulation
	 * @throws UnknownStateException if a pin used in the simulation is in an undefined state
	 */
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException;

}
