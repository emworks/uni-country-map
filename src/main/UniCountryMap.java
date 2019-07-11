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
import processing.data.JSONArray;
import processing.data.JSONObject;

public class UniCountryMap extends PApplet {
	private UnfoldingMap map;
	
	private String countryFile = "countries.geo.json";
	private String uniFile = "uni.json";
	
	private HashMap<String, Integer> uniMap;
	private List<Marker> countryMarkers;
	private ArrayList keyList;
	
	public static final int SCORE_MIN = 3;
	public static final int SCORE_MAX = 17;
	
	public void setup() {
		size(950, 600, OPENGL);
		map = new UnfoldingMap(this);
		map.zoomToLevel(4);
		map.panTo(new Location(49.0f, 3.0f));
		MapUtils.createDefaultEventDispatcher(this, map);
		
		setUniMap();
		setCountryMarkers();
		
		map.addMarkers(countryMarkers);
	}
	
	public void draw() {
		background(10);
		map.draw();
		addKey();
	}
	
	public void setUniMap() {
		JSONArray unis = loadJSONArray(uniFile);
		uniMap = new HashMap<String, Integer>();
		for (int i = 0; i < unis.size(); i++) {
			JSONObject uni = unis.getJSONObject(i);
			String name = uni.getString("name");
			int score = uni.getInt("score");
			uniMap.put(name, score);
		}
	}
	
	public void setCountryMarkers() {
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		for (Marker country: countryMarkers) {
			String countryName = country.getStringProperty("name");
			if (uniMap.containsKey(countryName)) {
				float score = (float) Math.cbrt(uniMap.get(countryName));
				int colorLevel = (int) map(score, SCORE_MIN, SCORE_MAX, 0, 255);
//				System.out.println(countryName + " " + colorLevel + " " + score);
				int color = color(0, colorLevel, 0);
				country.setColor(color);
			}
		}
	}
	
	public void addKey() {
		
	}
}
