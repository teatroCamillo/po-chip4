package edu.model.creator;

import edu.model.Chip;
import edu.model.Creator;
import edu.model.chip.*;

import java.util.HashMap;
import java.util.Map;

public class ChipCreator extends Creator{

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

		this.uniqueChipIdGenerator = 0;
	}

	@Override
	public Chip create(int code){
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
