package main;

public class University extends Entity {
	private String country;
	
	public University(String name, String country) {
		super(name);
		this.country = country;
	}

	public String getCountry() {
		return country;
	}
}
