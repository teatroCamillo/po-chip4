package edu.manager;

import java.util.Arrays;
import java.util.Set;

public class Composite implements Component{
	Set<Component> children;

	public Composite(Component... components) {
		add(components);
	}

	public void add(Component component) {
		children.add(component);
	}

	public void add(Component... components) {
		children.addAll(Arrays.asList(components));
	}

	public void remove(Component child) {
		children.remove(child);
	}

	public void remove(Component... components) {
		Arrays.asList(components).forEach(children::remove);
	}

	public void clear() {
		children.clear();
	}

	@Override
	public void simulate(){
		System.out.println("Simulate form Composite");
	}
}
