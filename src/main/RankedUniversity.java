package main;

public class RankedUniversity extends University {
	private int rank;
	
	public RankedUniversity(String name, String country, int rank) {
		super(name, country);
		this.rank = rank;
	}
	
	public String toString() {
		return getName() + " (" + getRank() + ")";
	}
	
	public int getRank() {
		return rank;
	}
}