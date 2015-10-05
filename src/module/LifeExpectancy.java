package module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class LifeExpectancy extends PApplet{

	UnfoldingMap map;
	Map<String,Float> lifeExpectancyByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;
	public void setup(){
		size(800,600,OPENGL);
		map= new UnfoldingMap(this, 0, 0, 800, 600,new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		lifeExpectancyByCountry= loadLifeExpectancyFromCSV("../data/LifeExpectancyWorldBank.csv");
		countries= GeoJSONReader.loadData(this, "../data/countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		shadeCountries();
	
	}
	

	public void draw()
	{
		map.draw();
	}
	
	private Map<String, Float> loadLifeExpectancyFromCSV(String fileName) {
	
		Map<String,Float> lifeExpMap = new HashMap<String,Float>();
		String[] rows= loadStrings(fileName);
		for(String row : rows)
		{
			String[] column = row.split(",");
			if(column[5].length()>3){
				float value = Float.parseFloat(column[5]);
				lifeExpMap.put(column[4], value);
				}
			}
		
		
		return lifeExpMap;
	}
	
private void shadeCountries() {
		for(Marker marker : countryMarkers)
		{
			String countryId= marker.getId();
			if(lifeExpectancyByCountry.containsKey(countryId))
			{
				float lifeExp = lifeExpectancyByCountry.get(countryId);
				int colorLevel = (int)map(lifeExp, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			}
			else
				marker.setColor(color(150,150,150));
	}
}
}
