package edu.model;

public interface Publisher {
	void subscribe(Subscriber subscriber);
	void unsubscribe(Subscriber subscriber);
	void notifySubscribers();
}
