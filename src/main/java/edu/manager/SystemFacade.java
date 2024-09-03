package edu.manager;

import edu.model.chip.Chip;
import edu.uj.po.simulation.interfaces.*;
import java.util.Map;
import java.util.Set;

public class SystemFacade implements UserInterface {

	protected final SimulationManager simulationManager;
	protected final CircuitManager circuitManager;

	public SystemFacade(){
		this.circuitManager = new CircuitManager();
		this.simulationManager = new SimulationManager(this.circuitManager);
	}

	@Override
	public int createChip(int code) throws UnknownChip {
		return circuitManager.createChip(code);
	}

	@Override
	public int createInputPinHeader(int size){
		return circuitManager.createInputPinHeader(size);
	}

	@Override
	public int createOutputPinHeader(int size){
		return circuitManager.createOutputPinHeader(size);
	}

	@Override
	public void connect(int component1,
						int pin1,
						int component2,
						int pin2) throws UnknownComponent, UnknownPin, ShortCircuitException{
		circuitManager.connect(component1, pin1, component2, pin2);
	}

	@Override
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException{
		simulationManager.stationaryState(states);
	}

	@Override
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0,
														   int ticks) throws UnknownStateException{
		return simulationManager.simulation(states0, ticks);
	}

	@Override
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException{
		return simulationManager.optimize(states0, ticks);
	}

	public Map<Integer, Chip> getChips(){
		return circuitManager.getChips();
	}
}
