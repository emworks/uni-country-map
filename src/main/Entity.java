package main;

abstract public class Entity {
	private String name;
	
	public Entity(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
