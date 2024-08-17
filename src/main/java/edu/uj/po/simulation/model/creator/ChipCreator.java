package edu.uj.po.simulation.model.creator;

import edu.uj.po.simulation.model.Chip;
import edu.uj.po.simulation.model.Creator;
import edu.uj.po.simulation.model.chip.Chip7400;
import edu.uj.po.simulation.model.chip.HeaderIn;
import edu.uj.po.simulation.model.chip.HeaderOut;

import java.util.HashMap;
import java.util.Map;

public class ChipCreator extends Creator{

	private final Map<Integer, Chip> chipCodeMap;

	public ChipCreator(){
		this.chipCodeMap = new HashMap<>();
		chipCodeMap.put(0, new HeaderIn());
		chipCodeMap.put(1, new HeaderOut());
		chipCodeMap.put(7400, new Chip7400());
	}

	@Override
	public Chip create(int code){
		return chipCodeMap.get(code).clone();
	}

	@Override
	public Chip createHeaderIn(int size){
		return chipCodeMap.get(0).clone(size);
	}

	@Override
	public Chip createHeaderOut(int size){
		return chipCodeMap.get(1).clone(size);
	}
}
