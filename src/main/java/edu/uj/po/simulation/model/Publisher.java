package edu.uj.po.simulation.model;

import java.util.*;

public interface Publisher{
	Set<Subscriber> subscribers = new HashSet<>();

	void subscribe(Subscriber subscriber);

	void unsubscribe(Subscriber subscriber);

	void report(Chip chip);
}
