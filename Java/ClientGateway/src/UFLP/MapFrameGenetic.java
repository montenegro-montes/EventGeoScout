package UFLP;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.LocalResponseCache;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

import MapFrame.Feature;
import MapFrame.Poly;
import MapFrame.Punto;
import MapFrame.Feature.type;
import OSM.LinePainter;


/**
*
 */
public class MapFrameGenetic 
{
	JXMapKit jXMapKit;
	JXMapViewer mapViewer;
	Set <Waypoint> 		listaWayPoints	= new HashSet<Waypoint>(); //Ptos
	//List<GeoPosition> 	listaLineWay 	= new ArrayList<GeoPosition>(); //Lineas
	
	List<Punto> _ptoFacilities, _ptoClients;
	String layerName;
	
	type tipo=null; //Tipo de elemento añadir o borrar;
	
	public MapFrameGenetic (){
		
		
		TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		tileFactory.setThreadPoolSize(50);

		// Setup local file cache
		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

		
		jXMapKit = new JXMapKit();
        jXMapKit.setTileFactory(tileFactory);

        
        //Initial location
    	GeoPosition deportes= new GeoPosition(36.715503, -4.480589);
        
        jXMapKit.setZoom(3);
        jXMapKit.setAddressLocation(deportes);
        jXMapKit.setAddressLocationShown(false);
        
        
        mapViewer=jXMapKit.getMainMap();
        
       /* jXMapKit.getMainMap().addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent me) {
                int zoom=mapViewer.getZoom();
           
                Rectangle rect2 = mapViewer.getViewportBounds();
                Point ptoEvent = new Point(me.getX() + rect2.x,me.getY() + rect2.y);
                GeoPosition event=   mapViewer.getTileFactory().pixelToGeo(ptoEvent,zoom );
                 
                if (tipo!=null)
	                switch (tipo){
	                	case Point: 		if (add) addWaypoint(event);
	                						break;
	                	
	                }
                
                
            }
        });*/
    
 
    
	}

/*****************************************************************
 * 
 * 
 * @param ptoAdd
 */
	public void addWaypoint(GeoPosition ptoAdd) {
	 
        listaWayPoints.add(new DefaultWaypoint(ptoAdd));
        
		WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
		waypointPainter.setWaypoints(listaWayPoints);
		
		List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
		painters.add(waypointPainter);
		
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
		
		//System.out.println(ptoAdd);
		
	}

	
	public void displaySolution(boolean [] solutions) {
		
		//Color [] clients = new Color[];
		
		clear();
		
		Color [] clientsColor 	= new Color [_ptoClients.size()];
		
		for (int i=0;i<_ptoClients.size();i++){
			clientsColor[i]= Color.CYAN;
		}
		
		Color [] facilitiesColor  = new Color [_ptoFacilities.size()];
		
		for (int i=0;i<_ptoFacilities.size();i++){
			if (solutions[i]) facilitiesColor[i]= Color.GREEN;
			else			facilitiesColor[i]= Color.WHITE;
		}
		
		displayWaypoints (facilitiesColor,clientsColor);
	
	}
	
/************************
 * 	
 * @param ptoAdd
 */
	public void displayClientsandFacilities(List<Punto> ptoFacilities,List<Punto> ptoClients) {
		_ptoFacilities  = ptoFacilities;
		_ptoClients	 	= ptoClients;	

		
		Color [] clientsColor 	= new Color [_ptoClients.size()];
		
		for (int i=0;i<_ptoClients.size();i++){
			clientsColor[i]= Color.CYAN;
		}
		
		Color [] facilitiesColor  = new Color [_ptoFacilities.size()];
		
		for (int i=0;i<_ptoFacilities.size();i++){
			facilitiesColor[i]= Color.WHITE;
		}
		
		displayWaypoints (facilitiesColor,clientsColor);
	
	}
	
	
	
	
	public void displayWaypoints(Color[]facilitiesColor,Color []clientsColor) {
		
		
		
		List<GeoPosition> track = new ArrayList<GeoPosition>();
		listaWayPoints = new HashSet<Waypoint>();
		
		int index=0;
		for (Punto p: _ptoFacilities){
			listaWayPoints.add(new CustomWayPoint("F"+index,facilitiesColor[index],p.getLatitude(),p.getLongitude()));
			track.add(new GeoPosition(p.getLatitude(),p.getLongitude()));
			index++;
		}
        
		index=0;
		
		for (Punto p: _ptoClients){
				listaWayPoints.add(new CustomWayPoint("C"+index,clientsColor[index],p.getLatitude(),p.getLongitude()));
				track.add(new GeoPosition(p.getLatitude(),p.getLongitude()));
				index++;
		}
		
		showWaypoints(track,listaWayPoints);
		
	}
	
	
	/**********************************
	 * 
	 * @param track
	 * @param listaWayPoints
	 */
	
	private void showWaypoints(List<GeoPosition> track,Set<Waypoint> listaWayPoints) {
		
		
		// Set the focus
		mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

		WaypointPainter<CustomWayPoint> waypointPainter = new WaypointPainter<CustomWayPoint>();
		waypointPainter.setRenderer(new FancyWaypointRenderer());
		waypointPainter.setWaypoints((Set<? extends CustomWayPoint>) listaWayPoints);

		List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
		painters.add(waypointPainter);
		
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
		
		
	}

	



/**********************************
 * 
 */
 public void clear(){
		mapViewer.setOverlayPainter(null);
 }
/**********************
 * 	
 * @return
 */
	public JXMapKit getMap(){
		return 	jXMapKit;
	    
	}

	
	
	
    /**
     * @param args the program args (ignored)
     */
	/*  public static void main(String[] args){
    	MapFrame map = new MapFrame(null);
    	map.setLayerAndType(" ",type.LineString);
    	map.setModeAdd();*/
    //	map.setModeDel(); 
    	
   /* 	map.addWaypoint(new GeoPosition(36.7173350989895, -4.48167085647583));
    	map.addWaypoint(new GeoPosition(36.715546240904246, -4.477357864379883));
    	map.addWaypoint(new GeoPosition(36.71570104757726, -4.480104446411133));
    	map.addWaypoint(new GeoPosition(36.71300048642773, -4.478816986083984));
    	map.addWaypoint(new GeoPosition(36.71260485484803, -4.482464790344238));
    	map.addWaypoint(new GeoPosition(36.71568384685121, -4.4821858406066895));
    	map.addWaypoint(new GeoPosition(36.71735229934583, -4.478645324707031));
    	map.addWaypoint(new GeoPosition(36.71439378142239, -4.480319023132324));
   */ 	

  /*  	map.addLine(new GeoPosition(36.7173350989895, -4.48167085647583),true);
    	map.addLine(new GeoPosition(36.715546240904246, -4.477357864379883),true);
    	map.addLine(new GeoPosition(36.71570104757726, -4.480104446411133),true);
    	map.addLine(new GeoPosition(36.71300048642773, -4.478816986083984),true);
    	map.addLine(new GeoPosition(36.71260485484803, -4.482464790344238),true);
    	map.addLine(new GeoPosition(36.71568384685121, -4.4821858406066895),true);
    	map.addLine(new GeoPosition(36.71735229934583, -4.478645324707031),true);
    	map.addLine(new GeoPosition(36.71439378142239, -4.480319023132324),true);*/
		// Create waypoints from the geo-positions
		
		/*JFrame frame = new JFrame("JXMapviewer ");
          frame.getContentPane().add(map.getMap());
          frame.setSize(800, 600);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);*/
  /*  }
    */
        
}
