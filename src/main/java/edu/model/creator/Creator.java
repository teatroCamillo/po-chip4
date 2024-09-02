package edu.model.creator;

import edu.uj.po.simulation.interfaces.UnknownChip;

public interface Creator<T> {
	T create(int code) throws UnknownChip;
	T createHeaderIn(int size);
	T createHeaderOut(int size);
}
