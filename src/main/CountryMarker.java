package main;

import java.util.List;

import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

public class CountryMarker extends SimplePolygonMarker implements Labeled {
	private PFont labelFont;
	
	public CountryMarker(ShapeFeature country) {
		super(country.getLocations(), country.getProperties());
	}
	
	public CountryMarker(ShapeFeature country, PFont labelFont) {
		this(country);
		this.labelFont = labelFont;
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
			if (labelFont != null) {
				pg.textFont(labelFont);
			}
			showLabel(pg, 10, 10);
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
	public String getLabelTitle() {
		return toString();
	}
	
	@Override
	public String getLabelText() {
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
	public int getLabelTitleLinesCount() {
		return 1;
	}

	@Override
	public int getLabelTextLinesCount() {
		return getUniversities().size();
	}
}
