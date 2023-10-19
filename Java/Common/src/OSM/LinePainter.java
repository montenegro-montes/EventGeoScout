
package OSM;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.painter.Painter;

/**
 * Paints a route
 * @author Martin Steiger
 */
public class LinePainter implements Painter<JXMapViewer>
{
	private Color color = Color.RED;
	private boolean antiAlias = true;
	private boolean line = false;
	private List<GeoPosition> track;
	
	/**
	 * @param track the track
	 */
	public LinePainter(List<GeoPosition> track)
	{
		// copy the list so that changes in the 
		// original list do not have an effect here
		this.track = new ArrayList<GeoPosition>(track);
	}

	public void setLine (boolean lineDraw){
		line= lineDraw;
	}
	
	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		g = (Graphics2D) g.create();

		// convert from viewport to world bitmap
		Rectangle rect = map.getViewportBounds();
		g.translate(-rect.x, -rect.y);

		if (antiAlias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// do the drawing
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(4));

		drawRoute(g, map);

		// do the drawing again
		g.setColor(color);
		g.setStroke(new BasicStroke(2));

		drawRoute(g, map);

		g.dispose();
	}

	/**
	 * @param g the graphics object
	 * @param map the map
	 */
	private void drawRoute(Graphics2D g, JXMapViewer map)
	{
		int lastX = 0;
		int lastY = 0;
		
		boolean first = true;
		
		for (GeoPosition gp : track)
		{
			// convert geo-coordinate to world bitmap pixel
			Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

			if (first)
			{
				first = false;
			}
			else
			{
				g.drawOval(lastX-2, lastY-2, 4, 4);
				g.drawOval((int) pt.getX()-2, (int) pt.getY()-2, 4, 4);
				
				g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
				if (line)first=true;
			}
			
			lastX = (int) pt.getX();
			lastY = (int) pt.getY();
		}
	}
/************************************************
 * 	
 * @return
 */
	public GeoPosition getIni(){
		return track.get(0);
	}
/**************************************************
 * 	
 * @return
 */
	public List<GeoPosition> getPtos(){
		return track;
	}
/******************************************************
 * 	
 * @return
 */
	public int getNumPtos (){
		return track.size();
	}
}
