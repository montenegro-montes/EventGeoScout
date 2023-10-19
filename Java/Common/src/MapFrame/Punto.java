package MapFrame;

import java.io.Serializable;

public class Punto implements Serializable{
	double latitude=0.0;
	double longitude=0.0;
	
	Punto(){
		
	}
	
	public Punto (double latitudep, double longitudep){
		 latitude=latitudep;
		 longitude=longitudep;
		
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public String toString(){
		return "\t "+latitude+" "+longitude;
	}
	
	public double distance (Punto other){
		/*
		 * Haversine
			formula:	a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
						c = 2 ⋅ atan2( √a, √(1−a) )
						d = R ⋅ c
		 */
		 double R = 6371; // Radius of the earth in km
		 double dLat = deg2rad(other.latitude-latitude);  // deg2rad below
		 double dLon = deg2rad(other.longitude-longitude); 
		
		 double a= Math.sin(dLat/2) * Math.sin(dLat/2) +
				    Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(other.latitude)) * 
				    Math.sin(dLon/2) * Math.sin(dLon/2);
			
		 double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		 double d = R * c; // Distance in km
		 return d;
	
	}
	
	private double  deg2rad(double deg) {
		  return deg * (Math.PI/180);
	}
}