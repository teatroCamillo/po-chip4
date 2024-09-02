package edu.manager;

import edu.model.pin.AbstractPin;
import edu.model.pin.PinIn;
import edu.model.pin.PinOut;
import edu.uj.po.simulation.interfaces.*;
import edu.model.Chip;
import edu.model.Connection;
import edu.model.creator.Creator;
import edu.model.creator.ChipCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComponentManager implements CircuitDesign {

	protected final Map<Integer, Chip> chips;
	protected final Set<Connection> directConnections;
	protected final Creator<Chip> creator;

	public ComponentManager(){
		this.chips = new HashMap<>();
		this.directConnections = new HashSet<>();
		this.creator = new ChipCreator();
	}

	protected void propagateSignal(){
		chips.values().forEach(chip -> {
			chip.getPinMap().values()
					.forEach(AbstractPin::notifySubscribers);
		});
	}

	private void setSubscribe(int component1,
							  int pin1,
							  int component2,
							  int pin2){
		Chip chip1 = chips.get(component1);
		Chip chip2 = chips.get(component2);
		AbstractPin p1 = chip1.getPinMap().get(pin1);
		AbstractPin p2 = chip2.getPinMap().get(pin2);

		if(p1 instanceof PinOut) chip1.getPinMap().get(pin1).subscribe(chip2.getPinMap().get(pin2));
		else if(p2 instanceof PinOut) chip2.getPinMap().get(pin2).subscribe(chip1.getPinMap().get(pin1));
		else {
			if(canReachOutputThroughAnotherInput(chip1.getChipId(), p1.getId())){
				chip1.getPinMap().get(pin1).subscribe(chip2.getPinMap().get(pin2));
			}
			else if(canReachOutputThroughAnotherInput(chip2.getChipId(), p2.getId()))
				chip2.getPinMap().get(pin2).subscribe(chip1.getPinMap().get(pin1));

			else chip1.getPinMap().get(pin1).subscribe(chip2.getPinMap().get(pin2));
		}
	}

	public void addNewConnection(int sourceChipId, int sourcePinId, int targetChipId, int targetPinId) {
		setSubscribe(sourceChipId, sourcePinId, targetChipId, targetPinId);
		this.directConnections.add(new Connection(sourceChipId,sourcePinId, targetChipId, targetPinId));
	}

	public Map<Integer, Chip> getChips(){
		return chips;
	}
	public Set<Connection> getDirectConnections(){
		return directConnections;
	}

	@Override
	public int createChip(int code) throws UnknownChip {
		return putToChipsMap((Chip)creator.create(code));
	}

	private int putToChipsMap(Chip newChip){
		int newChipId = newChip.getChipId();
		chips.put(newChipId, newChip);
		return newChipId;
	}

	@Override
	public int createInputPinHeader(int size){
		return putToChipsMap(creator.createHeaderIn(size));
	}

	@Override
	public int createOutputPinHeader(int size){
		return putToChipsMap(creator.createHeaderOut(size));
	}

	@Override
	public void connect(int component1,
						int pin1,
						int component2,
						int pin2) throws UnknownComponent, UnknownPin, ShortCircuitException {

		if (!chips.containsKey(component1)) {
			throw new UnknownComponent(component1);
		}
		if (!chips.containsKey(component2)) {
			throw new UnknownComponent(component2);
		}

		Chip chip1 = chips.get(component1);
		Chip chip2 = chips.get(component2);

		if (!chip1.getPinMap().containsKey(pin1)) {
			throw new UnknownPin(component1, pin1);
		}
		if (!chip2.getPinMap().containsKey(pin2)) {
			throw new UnknownPin(component2, pin2);
		}

		if (isConnectionExist(component1, pin1, component2, pin2)) {
			return;
		}

		if (isOutputPin(component1, pin1) && isOutputPin(component2, pin2)) {
			throw new ShortCircuitException();
		}

		if (isCreatingIndirectShortCircuit(component1, pin1, component2, pin2)) {
			throw new ShortCircuitException();
		}

		addNewConnection(component1, pin1, component2, pin2);
	}

	public boolean isConnectionExist(int component1,
									 int pin1,
									 int component2,
									 int pin2){
		return directConnections
				.stream()
				.anyMatch(connection ->
								  (connection.sourceChipId() == component1 &&
										  connection.sourcePinId() == pin1 &&
										  connection.targetChipId() == component2 &&
										  connection.targetPinId() == pin2) ||
										  (connection.sourceChipId() == component2 &&
												  connection.sourcePinId() == pin2 &&
												  connection.targetChipId() == component1 &&
												  connection.targetPinId() == pin1));
	}

	public boolean isPinConnected(AbstractPin pin){
		return directConnections
				.stream()
				.anyMatch(connection ->
								  (connection.sourceChipId() == pin.getChipId() &&
										  connection.sourcePinId() == pin.getId()) ||
										  (connection.targetChipId() == pin.getChipId() &&
												  connection.targetPinId() == pin.getId())
				);
	}

	private boolean isCreatingIndirectShortCircuit(int component1, int pin1, int component2, int pin2) {
		if(isOutputPin(component1, pin1)) return canReachOutputThroughAnotherInput(component2, pin2);
		if(isOutputPin(component2, pin2)) return canReachOutputThroughAnotherInput(component1, pin1);
		return canReachOutputThroughAnotherInput(component1, pin1) &&
				canReachOutputThroughAnotherInput(component2,pin2);
	}

	private boolean canReachOutputThroughAnotherInput(int chipId, int pinId) {
		Set<String> visited = new HashSet<>();
		return isConnectedToOutputRecursive(chipId, pinId, visited);
	}

	private boolean isConnectedToOutputRecursive(int chipId, int pinId, Set<String> visited) {
		String key = chipId + ":" + pinId;
		if (visited.contains(key)) return false;

		visited.add(key);

		for (Connection connection : directConnections) {
			if (connection.sourceChipId() == chipId && connection.sourcePinId() == pinId) {
				if (isOutputPin(connection.targetChipId(), connection.targetPinId())) return true;
				if (isInputPin(connection.targetChipId(), connection.targetPinId()))
					if (isConnectedToOutputRecursive(connection.targetChipId(), connection.targetPinId(), visited))
						return true;
			}
			else if(connection.targetChipId() == chipId && connection.targetPinId() == pinId) {
				if (isOutputPin(connection.sourceChipId(), connection.sourcePinId())) return true;
				if (isInputPin(connection.sourceChipId(), connection.sourcePinId()))
					if (isConnectedToOutputRecursive(connection.sourceChipId(), connection.sourcePinId(), visited))
						return true;
			}
		}
		return false;
	}

	private boolean isOutputPin(int chipId, int pinId) {
		Chip chip = chips.get(chipId);
		return chip.getPinMap().get(pinId) instanceof PinOut;
	}

	private boolean isInputPin(int chipId, int pinId) {
		Chip chip = chips.get(chipId);
		return chip.getPinMap().get(pinId) instanceof PinIn;
	}
}
