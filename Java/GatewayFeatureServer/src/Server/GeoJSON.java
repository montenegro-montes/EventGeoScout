package Server;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;
import org.jdesktop.swingx.mapviewer.GeoPosition;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoJSON {
	URL url		=	null;
	int nptos	=	0;
	String layer_name;
	List <GeoPosition> ptos;
	
	public GeoJSON(){
		
		ptos	= new ArrayList<GeoPosition>();
		//File src = new File("poi_semaforos.geojson");;
	}
	
	public void setURL(String urlS) throws MalformedURLException{
		
			//url = new URL("http://datosabiertos.malaga.eu/recursos/transporte/trafico/poi_semaforos.geojson");
			url = new URL(urlS);
	}
	
	
	public void readPoints() throws JsonParseException, JsonMappingException, IOException{
		GeoJsonObject object	=	null;	
		ObjectMapper obj		=	new ObjectMapper();
		
		
		obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		object= obj.readValue(url, GeoJsonObject.class);
	
		 if (object instanceof FeatureCollection) {	
				List<Feature> features= ((FeatureCollection) object).getFeatures();
				nptos=	features.size()-1;
				
				if (nptos>0) {
					layer_name = features.get(0).getProperty("Name");
					if (layer_name==null) layer_name ="Unkown";
				}
				//System.out.println(features.size()+" "+features.get(0).getProperty("Name"));
				
				for (int i=0;i<nptos;i++){
					GeoJsonObject pto = features.get(i).getGeometry();
					if (pto instanceof Point) {
						LngLatAlt coor =((Point) pto).getCoordinates();
						if (coor!=null)
						ptos.add(new GeoPosition(coor.getLatitude(),coor.getLongitude()));
						//System.out.println("LATITUDE "+ ((Point) pto).getCoordinates().getLatitude()+" Lng "+
						//((Point) pto).getCoordinates().getLongitude());
					}
				}
			}
	}
	
	public List<GeoPosition> getPoints(){
		return ptos;
	}
	
	
	public int getnumPoints(){
		return nptos;
	}
	
	public String getLayerName(){
		return layer_name;
	}
	
	public void setLayerName (String layer){
		layer_name = layer;
	}
}
