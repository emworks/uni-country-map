package main;

import java.util.List;

import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

public class CountryMarker extends SimplePolygonMarker implements MarkerHint {
	private PFont hintFont;
	
	public CountryMarker(ShapeFeature country) {
		super(country.getLocations(), country.getProperties());
	}
	
	public CountryMarker(ShapeFeature country, PFont hintFont) {
		this(country);
		this.hintFont = hintFont;
	}
	
	public String toString() {
		return getName() + ": " + getScore();
	}
	
	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		if (mapPositions.isEmpty() || isHidden()) {			
			return;
		}
		
		pg.pushStyle();
		
		if (isSelected() && getScore() != 0) {
			pg.strokeWeight(strokeWeight);
			if (hintFont != null) {
				pg.textFont(hintFont);
			}
			showHint(pg, 10, 10);
			pg.stroke(strokeColor);
		}
		
		pg.fill(color);

		pg.beginShape();
		for (MapPosition pos : mapPositions) {
			pg.vertex(pos.x, pos.y);
		}
		pg.endShape(PConstants.CLOSE);
		
		pg.popStyle();
	}
	
	public String getName() {
		return getStringProperty("name");
	}
	
	public float getScore() {
		if (getProperty("score") != null) {
			return (float) getProperty("score");
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getUniversities() {
		return (List<Object>) getProperty("universities");
	}
	
	@Override
	public String getHintTitle() {
		return toString();
	}
	
	@Override
	public String getHintText() {
		String universities = "";
		List<Object> universitiesList = getUniversities();
		if (universitiesList != null) {
			for (Object university : universitiesList) {
				universities += university + "\n";
			}
		}
		return universities;
	}
	
	@Override
	public int getHintTitleLinesCount() {
		return 1;
	}

	@Override
	public int getHintTextLinesCount() {
		return getUniversities().size();
	}
}
