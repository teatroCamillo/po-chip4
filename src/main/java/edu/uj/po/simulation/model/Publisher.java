package edu.uj.po.simulation.model;

import java.util.*;

public interface Publisher {
	void subscribe(Subscriber subscriber);

	void unsubscribe(Subscriber subscriber);

	void notifySubscribers();
}
