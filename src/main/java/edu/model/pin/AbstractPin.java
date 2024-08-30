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
		System.out.println("[" + chipId + "][id: " + this.id + " " + this.getClass().getSimpleName() + "] : new pinState was set, " +
								   "actual: " + pinState + ", previous: " + this.state);
		this.state = pinState;
	}

	@Override
	public PinState getPinState(){
		return state;
	}


	@Override
	public AbstractPin clone() {
		try {
			AbstractPin cloned = (AbstractPin) super.clone();
			cloned.setId(-1); // proforma żeby był inny numer niż w orginale
			cloned.subscribers = new HashSet<>();
			return cloned;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // Nigdy nie powinno się zdarzyć, ponieważ implementujemy Cloneable
		}
	}

	@Override
	public void subscribe(Subscriber subscriber){
		if(!this.equals(subscriber)){
			System.out.println("[" + chipId + "][id: " + this.id + " " + this.getClass().getSimpleName() + "]" +
									   " : dodaję " +
									   "nowego " +
									   "subskrybenta: " + subscriber);
			subscribers.add(subscriber);

			System.out.println("\nLista subskrybetów: ");
			subscribers.forEach(System.out::println);
		}
	}

	@Override
	public void unsubscribe(Subscriber subscriber){
		subscribers.remove(subscriber);
	}

	@Override
	public void notifySubscribers(){
		System.out.println("[" + chipId + "][id: " + this.id + " " + this.getClass().getSimpleName() + "] : notifySubscribers");
		//subscribers.forEach(subscriber -> subscriber.update(this.state));
		for (Subscriber subscriber : subscribers) {
			subscriber.update(this.state);
		}
	}

	@Override
	public void update(PinState state){
		if (!this.state.equals(state)) {
			System.out.println("[" + chipId + "][id: " + this.id + " " + this.getClass().getSimpleName() + "] : I'm updating...");
			setPinState(state);
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
