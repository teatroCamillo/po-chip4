package edu.model.pin;

import edu.model.Pin;
import edu.uj.po.simulation.interfaces.PinState;
import edu.model.Subscriber;

import java.util.HashSet;
import java.util.Set;

public class PinIn implements Pin{

	private PinState state;
	private Set<Subscriber> subscribers;

	public PinIn(){
		this.state = PinState.UNKNOWN;
		this.subscribers = new HashSet<>();
	}

	public PinIn(int number, PinState state){
		this.state = state;
	}

	@Override
	public void setPinState(PinState pinState){
		System.out.println("wykonuję setPinState... PinIn");
		this.state = pinState;
		notifySubscribers();
	}

	@Override
	public PinState getPinState(){
		return state;
	}

	@Override
	public String toString(){
		return "PinIn{" + "state=" + state + '}';
	}

	@Override
	public PinIn clone(){
		try{
			return (PinIn) super.clone();
		} catch(CloneNotSupportedException e){
			throw new AssertionError();
		}
	}

	@Override
	public void subscribe(Subscriber subscriber){
		System.out.println("Subscrybuję: " + subscriber);
		subscribers.add(subscriber);

		System.out.println("Lista subskrybetów: " + this);
		subscribers.forEach(System.out::println);
	}

	@Override
	public void unsubscribe(Subscriber subscriber){
		subscribers.remove(subscriber);
	}

	@Override
	public void notifySubscribers(){
		System.out.println("notifySubscribers PinIn");
		//subscribers.forEach(subscriber -> subscriber.update(this.state));
		for (Subscriber subscriber : subscribers) {
			subscriber.update(this.state);
		}
	}

	@Override
	public void update(PinState state){
		System.out.println("update PRZED IFem");
		// Sprawdzenie, czy stan faktycznie się zmienił
		if (!this.state.equals(state)) {
			System.out.println("update");
			setPinState(state);
		}
	}
}
