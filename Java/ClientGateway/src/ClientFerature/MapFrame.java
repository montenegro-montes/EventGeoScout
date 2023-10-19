package ClientFerature;

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
public class MapFrame 
{
	JXMapKit jXMapKit;
	JXMapViewer mapViewer;
	Set <Waypoint> 		listaWayPoints	= new HashSet<Waypoint>(); //Ptos
	List<Poly> 			listaPolyView 	= new ArrayList<Poly>();
	List<GeoPosition> 	listaLineWay 	= new ArrayList<GeoPosition>(); //Lineas
	
	Mediator med;
	String layerName;
	
	type tipo=null; //Tipo de elemento añadir o borrar;
	boolean add=true;
	
	public MapFrame (Mediator md){
		med=md;
		
		if (med!=null) med.registerMap(this);
		
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
        
        jXMapKit.getMainMap().addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent me) {
                int zoom=mapViewer.getZoom();
           
                Rectangle rect2 = mapViewer.getViewportBounds();
                Point ptoEvent = new Point(me.getX() + rect2.x,me.getY() + rect2.y);
                GeoPosition event=   mapViewer.getTileFactory().pixelToGeo(ptoEvent,zoom );
                 
                if (tipo!=null)
	                switch (tipo){
	                	case Point: 		if (add) addWaypoint(event);
	                						else	delWaypoint(event);
	                						break;
	                						
	                	case LineString: 	if (add) addLine(event,false); 
	                						else delLine(event);	
	                						break;	
	                	case Polygon: 		
	                						if (add) addPoly(event,false);  
	                						else delLine(event); 
	                						break;	
	                	
	                }
                
                
            }
        });
    
 
    
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
		
		med.addWayPoint(layerName,ptoAdd); 
	}

/************************
 * 	
 * @param ptoAdd
 */
	public void displayWaypoint(List<Punto> ptoAdd) {
		
		List<GeoPosition> track = new ArrayList<GeoPosition>();
		listaWayPoints = new HashSet<Waypoint>();
		
		for (Punto p: ptoAdd){
			listaWayPoints.add(new DefaultWaypoint(p.getLatitude(),p.getLongitude()));
			track.add(new GeoPosition(p.getLatitude(),p.getLongitude()));
		}
        
		// Set the focus
		mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

		
		WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
		waypointPainter.setWaypoints(listaWayPoints);
		
		List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
		painters.add(waypointPainter);
		
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
	}
	
/************************************************************
 * 	
 * @param ptoAdd
 */

	public void delWaypoint(GeoPosition ptoAdd) {
		
		Set <Waypoint> listaWayPointsLocal = new HashSet<Waypoint>();
		
		GeoPosition pointDel=null;
		
        Rectangle rect = mapViewer.getViewportBounds();
		        
		Point2D point  = mapViewer.getTileFactory().geoToPixel(ptoAdd, mapViewer.getZoom());
		Point 	gp_point = new Point((int) point.getX() - rect.x,(int) point.getY() - rect.y);
        
		  for (Waypoint waypoint : listaWayPoints) {
           	
			  Point2D gp_pt  = mapViewer.getTileFactory().geoToPixel(waypoint.getPosition(), mapViewer.getZoom());
	          Point converted_gp_pt = new Point((int) gp_pt.getX() - rect.x,(int) gp_pt.getY() - rect.y);
       
	          if (converted_gp_pt.distance(gp_point) < 10) {
	        	  pointDel=waypoint.getPosition();
                //  System.out.println("SI "+converted_gp_pt.distance(gp_point));
              } else {
              //    System.out.println("NOT  "+converted_gp_pt.distance(gp_point));
                  listaWayPointsLocal.add(waypoint);
              }
              
            
          }
		  
		  listaWayPoints.clear();
		  listaWayPoints.addAll(listaWayPointsLocal);
		  
		WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
		waypointPainter.setWaypoints(listaWayPoints);
		
		List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
		painters.add(waypointPainter);
		
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
		
		med.delWayPoint(layerName,pointDel);
	}
	
	
/**********************************************
 * 	
 * @param ptoAdd
 */
	public void addLine(GeoPosition point,boolean showPoints) {
			
		int size=listaLineWay.size();
		double ratio=0;
		
		if (size>1) {
			GeoPosition compara;
			
				compara =listaLineWay.get(size-1); 	//SI LINESTRING CON EL ULTIMO
				 ratio= 0.001;
			 
			 double dist=Math.abs(point.getLatitude()- compara.getLatitude())+	
			             Math.abs(point.getLongitude()-compara.getLongitude());
			 
			 
			 if (dist < ratio) {//TERMINA UN POLY y EMPIEZA OTRO
				 
					 
				 med.addLynePoint(layerName,1,listaLineWay);
				 
				 int nelementsP = listaLineWay.size();
				 long idP =System.currentTimeMillis();
				 
				 Poly polyFeature= new Poly(idP,nelementsP); //GUARDO EN EL POLY QUE VISUALIZA
				 polyFeature.setPtos(aux(listaLineWay));
				 listaPolyView.add(polyFeature);
				 
				 listaLineWay =  new ArrayList<GeoPosition>();
				 /// EMPIEZO UN ELEMENTO NUEVO
			 }
			 else listaLineWay.add(point);
		}
		else{
			listaLineWay.add(point);
		}
		
		int index=0;
		Set <Waypoint> listaWayPointsLocal = new HashSet<Waypoint>();
		size=listaLineWay.size();
		
		//System.out.println("SIZE: "+size);
		for (GeoPosition waypoint : listaLineWay) {
			if ((index==0)){
				listaWayPointsLocal.add(new DefaultWaypoint(waypoint));
			}
			index++;
		}
		
		LinePainter routePainter = new LinePainter(listaLineWay);
	
		displayLine( listaPolyView, showPoints,routePainter ); //MODO ADD	
		
	}
/*********************************************************
 * 
 * @param point
 * @param showPoints
 */
	public void addPoly(GeoPosition point,boolean showPoints) {
		
		int size=listaLineWay.size();
		double ratio=0;
		
		if (size>1) {
			GeoPosition compara;
			
			compara =listaLineWay.get(0);		 //SI ES POLYGONO CON EL PRIMERO
			ratio= 0.001;
			 
			 double dist=Math.abs(point.getLatitude()- compara.getLatitude())+	
			             Math.abs(point.getLongitude()-compara.getLongitude());
			 
			// System.out.println("DIST: "+dist+ "RATIO: "+ratio+"  "+isLineString);
			 
			 if (dist < ratio) {//TERMINA UN POLY y EMPIEZA OTRO
				 
				 listaLineWay.add(compara); //POLIGONO TENGO QUE INCLUIR EL PRIMERO 
					 
				 med.addLynePoint(layerName,2,listaLineWay);
				 
				 int nelementsP = listaLineWay.size();
				 long idP =System.currentTimeMillis();
				 
				 Poly polyFeature= new Poly(idP,nelementsP); //GUARDO EN EL POLY QUE VISUALIZA
				 polyFeature.setPtos(aux(listaLineWay));
				 listaPolyView.add(polyFeature);
				 
				 listaLineWay =  new ArrayList<GeoPosition>();
				 /// EMPIEZO UN ELEMENTO NUEVO
			 }
			 else listaLineWay.add(point);
		}
		else{
			listaLineWay.add(point);
		}
		
		int index=0;
		Set <Waypoint> listaWayPointsLocal = new HashSet<Waypoint>();
		size=listaLineWay.size();
		
		//System.out.println("SIZE: "+size);
		for (GeoPosition waypoint : listaLineWay) {
			if ((index==0)){
				listaWayPointsLocal.add(new DefaultWaypoint(waypoint));
			}
			index++;
		}
		
		LinePainter routePainter = new LinePainter(listaLineWay);
	
		displayLine( listaPolyView, showPoints,routePainter ); //MODO ADD	
		
	}
/*********************************************************
 * 
 * @param polyShow
 * @param showPoints
 */
	public void displayLine(List<Poly> polyShow,boolean showPoints ) { //MODO VIEW
		
		displayLine( polyShow, showPoints,null );
	}
	
	
/*******************	
 * 
 * @param pto
 * @param showPoints
 */
	
		
	private void displayLine(List<Poly> polyShow,boolean showPoints,LinePainter routePainterAdd ) {
			 
		List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
		
		listaPolyView=polyShow;
		
		List<Punto> puntos;
		List<GeoPosition> lineString;
		LinePainter routePainter;
		
		
		List<GeoPosition> track = new ArrayList<GeoPosition>();
		Set <Waypoint> listaLineWayLocal = new HashSet<Waypoint>(); //Ptos

		GeoPosition geoPto;
		
		for (Poly poly: polyShow){
			
			puntos=poly.getPtos();
			lineString= new ArrayList<GeoPosition>();
				
			for (Punto point: puntos){
				geoPto = new GeoPosition(point.getLatitude(),point.getLongitude());
				lineString.add(geoPto);
				track.add(geoPto);
				if (showPoints) listaLineWayLocal.add(new DefaultWaypoint(point.getLatitude(),point.getLongitude()));
				
			}		
			
			routePainter = new LinePainter(lineString);
			painters.add(routePainter);
		}
		
		if (routePainterAdd!=null && (routePainterAdd.getNumPtos()>0)){ 	//MODO EDITOR
			painters.add(routePainterAdd);
			GeoPosition ini=routePainterAdd.getIni();
			listaLineWayLocal.add(new DefaultWaypoint(ini.getLatitude(),ini.getLongitude()));
		}	
		else							// MODO VISUALIZAR 
			mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7); //EN MODO EDITOR QUITAR FOCO

				WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();	
				waypointPainter.setWaypoints(listaLineWayLocal);
		painters.add(waypointPainter);
		
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
		
	}
	
	
/*************************
 * 
 * @param ptoAdd
 */
public void delLine(GeoPosition ptoDEL) {
	
	//CONVIERTO EL PUNTO A POSICION EN EL MAPA
	  Rectangle rect = mapViewer.getViewportBounds();
      Point2D pointDEL  = mapViewer.getTileFactory().geoToPixel(ptoDEL, mapViewer.getZoom());
	  Point   gp_pointDEL = new Point((int) pointDEL.getX() - rect.x,(int) pointDEL.getY() - rect.y);
	  boolean target=false;
	  		int index=0;
	        
	  		
	  		for (Poly  poly: listaPolyView) {
		        
	  			List<Punto> puntoList = poly.getPtos();
	  			
			        for (Punto point : puntoList) {
			        	 GeoPosition waypoint = new GeoPosition(point.getLatitude(),point.getLongitude());
						 Point2D gp_pt  = mapViewer.getTileFactory().geoToPixel(waypoint, mapViewer.getZoom());
				         Point converted_gp_pt = new Point((int) gp_pt.getX() - rect.x,(int) gp_pt.getY() - rect.y);
				          
				          if (converted_gp_pt.distance(gp_pointDEL) < 10){
				     //   	  System.out.println("HE ENCONTRADO UN PUNTO: "+index);
				        	  target=true;
				        	  break;
				          }
				         // System.out.println("NOO HE ENCONTRADO UN PUNTO: "+index);
				          
			          }   
			        if (target) break;
			        index++;
	  		}
	
	  	if (target){
	  		med.delLynePoint(layerName, listaPolyView.get(index));
	  		listaPolyView.remove(index);
	  	}
	  		
		displayLine( listaPolyView, true,null ); 
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
	
	public void setLayerAndType (String layer,type tipoP){
		tipo		=	tipoP;
		layerName	=	layer;
		
		if (tipo == null){
			listaWayPoints 	= new HashSet<Waypoint>(); //Ptos
			listaPolyView 	= new ArrayList<Poly>();
			listaLineWay 	= new ArrayList<GeoPosition>(); //Lineas
		}
	}
	
	public void setModeAdd(){
		add=true;
	}

	public void setModeDel(){
		add=false;
	}
	
	public List <Punto> aux(List<GeoPosition>  pointD){
		List <Punto> point = new ArrayList<Punto>();
		
	 for (GeoPosition pos: pointD){
		 point.add(new Punto(pos.getLatitude(),pos.getLongitude()));
	 }
	 return point;
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
