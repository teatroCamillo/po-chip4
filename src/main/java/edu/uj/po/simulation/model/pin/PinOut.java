package edu.uj.po.simulation.model.pin;

import edu.uj.po.simulation.interfaces.PinState;
import edu.uj.po.simulation.model.Pin;
import edu.uj.po.simulation.model.Subscriber;

import java.util.HashSet;
import java.util.Set;

public class PinOut implements Pin {

	private PinState state;
	private final Set<Subscriber> subscribers;

	public PinOut(){
		this.state = PinState.UNKNOWN;
		this.subscribers = new HashSet<>();
	}

	@Override
	public void setPinState(PinState pinState){
		System.out.println("wykonuję setPinState... PinOut");
		this.state = pinState;
		notifySubscribers();
	}

	@Override
	public PinState getPinState(){
		return state;
	}

	@Override
	public String toString(){
		return "PinOut{" + "state=" + state + '}';
	}

	@Override
	public PinOut clone(){
		try{
			return (PinOut) super.clone();
		} catch(CloneNotSupportedException e){
			throw new AssertionError();
		}
	}

	@Override
	public void subscribe(Subscriber subscriber){
		System.out.println("Subscrybuję: " + subscriber);
		subscribers.add(subscriber);
	}

	@Override
	public void unsubscribe(Subscriber subscriber){
		subscribers.remove(subscriber);
	}

	@Override
	public void notifySubscribers(){
		System.out.println("notifySubscribers PinOut");
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
			System.out.println("Update...");
			setPinState(state);
		}
	}
}