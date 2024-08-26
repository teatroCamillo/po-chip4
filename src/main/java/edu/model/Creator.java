package edu.model;

//TODO: czy to nie powinno być interfejsem?
public abstract class Creator {
	public abstract Chip create(int code);
	public abstract Chip createHeaderIn(int size);
	public abstract Chip createHeaderOut(int size);
}
