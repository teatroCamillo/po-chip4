package edu.model.pin;

import edu.model.Pin;
import edu.model.Publisher;
import edu.model.Subscriber;
import edu.uj.po.simulation.interfaces.PinState;

import java.util.HashSet;
import java.util.Set;

public class AbstractPin implements Pin, Publisher, Subscriber {

	protected int id;
	protected int chipId;
	protected PinState state;
	protected Set<Subscriber> subscribers;

	public AbstractPin(){
		this.state = PinState.UNKNOWN;
		this.subscribers = new HashSet<>();
	}

	@Override
	public void setPinState(PinState pinState){
		this.state = pinState;
	}

	@Override
	public PinState getPinState(){
		return state;
	}

	@Override
	public void subscribe(Subscriber subscriber){
		if(!this.equals(subscriber)) subscribers.add(subscriber);
	}

	@Override
	public void unsubscribe(Subscriber subscriber){
		subscribers.remove(subscriber);
	}

	@Override
	public void notifySubscribers(){
		//v3 - 6 attempts were made on the tester, the result was 6x 100% passed tests - average time: 2656ms
//		if(!subscribers.isEmpty())
//			subscribers.forEach(subscriber -> subscriber.update(this.state));

		//v2 - 6 attempts were made on the tester, the result was 6x 100% passed tests - average time: 2665ms
		//subscribers.forEach(subscriber -> subscriber.update(this.state));

		//v1 - 6 attempts were made on the tester, the result was 6x 100% passed tests - average time: 2661ms
//		if(!subscribers.isEmpty())
//			for(Subscriber subscriber : subscribers)
//				subscriber.update(this.state);

		//v0 - 6 attempts were made on the tester, the result was 6x 100% passed tests - average time: 2601ms
		for(Subscriber subscriber : subscribers){
			subscriber.update(this.state);
		}
	}

	@Override
	public void update(PinState state){
		if (!this.state.equals(state)) {
			setPinState(state);
			if(!subscribers.isEmpty())
				for (Subscriber subscriber : subscribers)
					subscriber.update(this.state);
		}
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getChipId(){
		return chipId;
	}

	public void setChipId(int chipId){
		this.chipId = chipId;
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "{" + "id=" + id + ", state=" + state + ", subscribers=" + subscribers + '}';
	}
}
