package main;

import java.util.ArrayList;
import java.util.List;

public class ScoredCountry extends Country implements Scorable {
	private List<RankedUniversity> universities;
	private float score;
	private int range;
	
	public ScoredCountry(String name, int range) {
		super(name);
		this.range = range;
		universities = new ArrayList<RankedUniversity>();
	}
	
	public String toString() {
		return getName() + ":\n" + "Score: " + getScore() + "\n" + "Universities: " + getUniversities() + "\n";
	}
	
	public void addItem(RankedUniversity item) {
		universities.add(item);
		score = countScore();
	}
	
	public List<RankedUniversity> getUniversities() {
		return universities;
	}
	
	public float getScore() {
		return score;
	}
	
	@Override
	public List<Object> getData() {
		return new ArrayList<Object>(getUniversities());
	}
	
	@Override
	public int getRange() {
		return range;
	}
	
	@Override
	public int getRank(Object item) {
		return ((RankedUniversity)item).getRank();
	}
}