package edu.model.pin;

public interface Publisher {
	void subscribe(Subscriber subscriber);
	void unsubscribe(Subscriber subscriber);
	void notifySubscribers();
}
