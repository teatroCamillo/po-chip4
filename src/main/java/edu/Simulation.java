package edu;

import edu.manager.SystemFacade;
import edu.model.Chip;
import edu.uj.po.simulation.interfaces.*;
import edu.manager.ComponentManager;
import edu.manager.SimulationManager;

import java.util.*;

public class Simulation implements UserInterface {

	protected final SystemFacade systemFacade;

	public Simulation(){
		this.systemFacade = new SystemFacade();
	}

	@Override
	public int createChip(int code) throws UnknownChip {
		return systemFacade.createChip(code);
	}

	@Override
	public int createInputPinHeader(int size){
		return systemFacade.createInputPinHeader(size);
	}

	@Override
	public int createOutputPinHeader(int size){
		return systemFacade.createOutputPinHeader(size);
	}

	@Override
	public void connect(int component1,
						int pin1,
						int component2,
						int pin2) throws UnknownComponent, UnknownPin, ShortCircuitException {
		systemFacade.connect(component1, pin1, component2, pin2);
	}

	@Override
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException {
		systemFacade.stationaryState(states);
	}

	@Override
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0,
														   int ticks) throws UnknownStateException {
		return systemFacade.simulation(states0, ticks);
	}

	@Override
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException {
		return systemFacade.optimize(states0, ticks);
	}

	public Map<Integer, Chip> getChips(){
		return systemFacade.getChips();
	}
}
