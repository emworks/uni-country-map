package main;

import java.util.List;

public interface Scorable {
	public default float countScore() {
		int score = 0;
		int range = getRange();
		List<Object> list = getData();
		for (Object item : list) {
			int rank = getRank(item);
			score += range - Math.ceil(rank / 10) * 10;
		}
		return (float) Math.cbrt(score);
	}
	
	abstract public List<Object> getData();
	abstract public int getRank(Object item);
	abstract public int getRange();
}