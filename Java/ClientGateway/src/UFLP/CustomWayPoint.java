package UFLP;

import java.awt.Color;

import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;

public class CustomWayPoint extends DefaultWaypoint{
	private final String label;
	private final Color color;

	/**
	 * @param label the text
	 * @param color the color
	 * @param coord the coordinate
	 */
	
	public CustomWayPoint(String label, Color color, double Latitude,double Longitude){
		super(Latitude, Longitude);
		this.label = label;
		this.color = color;
	}
	
	public CustomWayPoint(String label, Color color, GeoPosition coord){
		super(coord);
		this.label = label;
		this.color = color;
	}

	/**
	 * @return the label text
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @return the color
	 */
	public Color getColor()
	{
		return color;
	}

	
}