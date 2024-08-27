package edu.model.creator;

import edu.model.Chip;
import edu.model.Creator;
import edu.model.chip.*;
import edu.uj.po.simulation.interfaces.UnknownChip;

import java.util.HashMap;
import java.util.Map;

public class ChipCreator extends Creator {

	private final Map<Integer, Chip> chipCodeMap;
	protected Integer uniqueChipIdGenerator;

	public ChipCreator(){
		this.chipCodeMap = new HashMap<>();
		chipCodeMap.put(0, new HeaderIn());
		chipCodeMap.put(1, new HeaderOut());
		chipCodeMap.put(7400, new Chip7400());
		chipCodeMap.put(7402, new Chip7402());
		chipCodeMap.put(7404, new Chip7404());
		chipCodeMap.put(7408, new Chip7408());
		chipCodeMap.put(7410, new Chip7410());
		chipCodeMap.put(7411, new Chip7411());
		chipCodeMap.put(7420, new Chip7420());
		chipCodeMap.put(7431, new Chip7431());
		chipCodeMap.put(7432, new Chip7432());
		chipCodeMap.put(7434, new Chip7434());
		chipCodeMap.put(7442, new Chip7442());
		chipCodeMap.put(7444, new Chip7444());
		chipCodeMap.put(7482, new Chip7482());
		chipCodeMap.put(74138, new Chip74138());
		chipCodeMap.put(74152, new Chip74152());

		this.uniqueChipIdGenerator = 0;
	}

	@Override
	public Chip create(int code) throws UnknownChip{
		if(!chipCodeMap.containsKey(code)) throw new UnknownChip();
		Chip newChip = chipCodeMap.get(code).clone();
		newChip.setChipId(uniqueChipIdGenerator++);
		return newChip;
	}

	@Override
	public Chip createHeaderIn(int size){
		Chip newChip = chipCodeMap.get(0).clone(size);
		newChip.setChipId(uniqueChipIdGenerator++);
		return newChip;
	}

	@Override
	public Chip createHeaderOut(int size){
		Chip newChip = chipCodeMap.get(1).clone(size);
		newChip.setChipId(uniqueChipIdGenerator++);
		return newChip;
	}
}
