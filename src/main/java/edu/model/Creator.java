package edu.model;

import edu.uj.po.simulation.interfaces.UnknownChip;

//TODO: czy to nie powinno być interfejsem?
public abstract class Creator {
	public abstract Chip create(int code) throws UnknownChip;
	public abstract Chip createHeaderIn(int size);
	public abstract Chip createHeaderOut(int size);
}
