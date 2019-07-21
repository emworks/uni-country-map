package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PFont;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class UniCountryMap extends PApplet {
	private static final long serialVersionUID = 1L;
	
	public static final int SCREEN_WIDTH = 1045;
	public static final int SCREEN_HEIGHT = 600;
	
	private UnfoldingMap map;
	
	private String countryAbbrFile = "countries.abbr.json";
	private String universityFile = "universities.rank.json";
	private String countryFile = "countries.geo.json";
	
	private HashMap<String, ScoredCountry> universityMap;
	private List<Marker> countryMarkers;
	
	private SimplePolygonMarker lastSelected;
	
	public void setup() {
		size(SCREEN_WIDTH, SCREEN_HEIGHT, OPENGL);
		map = new UnfoldingMap(this);
		map.zoomToLevel(4);
		map.panTo(new Location(49.0f, 3.0f));
		MapUtils.createDefaultEventDispatcher(this, map);
		
		initCountryMarkers();
		
		map.addMarkers(countryMarkers);
	}
	
	public void draw() {
		background(10);
		map.draw();
	}
	
	private void initCountryMarkers() {
		countryMarkers = new ArrayList<Marker>();
		
		setUniversityMap();
//		printUniversityMap();
		
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		setCountryMarkers(countries, null);
		
		setCountryColor();
	}
	
	private void setUniversityMap() {
		JSONObject countryAbbrObject = loadJSONObject(countryAbbrFile);
		JSONArray universityArray = loadJSONArray(universityFile);
		universityMap = new HashMap<String, ScoredCountry>();
		
		int size = universityArray.size();
		for (int i = 0; i < size; i++) {
			JSONObject university = universityArray.getJSONObject(i);
			
			String name = university.getString("name");
			String countryCode = university.getString("country");
			String country = countryAbbrObject.getString(countryCode);
			int rank = university.getInt("rank");
			
			ScoredCountry countryItem = (universityMap.containsKey(country))
				? universityMap.get(country) : new ScoredCountry(country, size);
			countryItem.addItem(new RankedUniversity(name, country, rank));
			
			universityMap.put(country, countryItem);
		}
	}
	
	private void setCountryMarkers(List<Feature> countries, String name) {
		PFont markerHintFont = createFont("Arial", MarkerHint.TEXT_SIZE);
		for (Feature country : countries) {
			if (country.getType() == Feature.FeatureType.MULTI) {
				List<Feature> features = ((MultiFeature)country).getFeatures();
				setCountryMarkers(features, country.getStringProperty("name"));
				continue;
			}
			
			if (name != null) {
				country.addProperty("name", name);
			}
			
			String countryName = country.getStringProperty("name");
			if (universityMap.containsKey(countryName)) {
				ScoredCountry scoredCountry = universityMap.get(countryName);
				country.addProperty("universities", scoredCountry.getUniversities());
				country.addProperty("score", scoredCountry.getScore());
			}
			
			countryMarkers.add(new CountryMarker((ShapeFeature)country, markerHintFont));
		}
	}
	
	private float[] getMinMaxScore() {
		float[] minMax = { 999, 0 };
		for (ScoredCountry entry : universityMap.values()) {
			float score = entry.getScore();
			if (score == 0) {
				continue;
			}
			if (score < minMax[0]) minMax[0] = score;
			if (score > minMax[1]) minMax[1] = score;
		}
		return minMax;
	}
	
	private void setCountryColor() {
		float[] minMax = getMinMaxScore();
		for (Marker country : countryMarkers) {
			float score = ((CountryMarker)country).getScore();
			if (score == 0) {
				int color = color(255, 255, 255);
				country.setColor(color);
				continue;
			}
			int opacity = (int) map(score, minMax[0], minMax[1], 20, 240);
			int color = color(255, 255, 0, opacity);
			country.setColor(color);
		}
	}
	
	@Override
	public void mouseMoved() {
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected.setStrokeWeight(0);
			lastSelected = null;
		}
		selectMarkerIfHover(countryMarkers);
	}
	
	private void selectMarkerIfHover(List<Marker> markers) {
		for (Marker m : markers) {
			if (lastSelected != null || !m.isInside(map, mouseX, mouseY)) {
				continue;
			}
			lastSelected = (SimplePolygonMarker)m;
			lastSelected.setSelected(true);
			lastSelected.setStrokeWeight(3);
			break;
		}
	}
	
	public void printUniversityMap() {
		for (ScoredCountry entry : universityMap.values()) {
			System.out.println(entry);
		}
	}
}