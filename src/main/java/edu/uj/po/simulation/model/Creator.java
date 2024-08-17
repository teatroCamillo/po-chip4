package edu.uj.po.simulation.model;

public abstract class Creator {
	public abstract Chip create(int code);
	public abstract Chip createHeaderIn(int size);
	public abstract Chip createHeaderOut(int size);
}
