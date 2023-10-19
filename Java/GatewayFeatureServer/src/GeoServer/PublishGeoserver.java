package GeoServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import MapFrame.Feature;
import MapFrame.Poly;
import MapFrame.Punto;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
public class PublishGeoserver {

	   String layerName = "bancos";//OUTPUT_SHAPE
	   String OUTPUT_SHAPE= layerName;
	   String OUTPUT_SHAP_FILE= OUTPUT_SHAPE+".shp";
	   String OUTPUT_ZIP_FILE = OUTPUT_SHAPE+".zip";
	   String SOURCE_FOLDER = "Zip";
	   
	   
	   String RESTURL  = "http://127.0.0.1:8080/geoserver";
	   String RESTUSER = "admin";
	   String RESTPW   = "1qaz2wsx";
	    
	   String workspace = "ws1";
	   String storename = "ResourceServer";
       
       GeoServerRESTReader reader=null;
	   GeoServerRESTPublisher publisher=null;

	   Zip zippedShapefile;
/**********
 * 
 * @param ServerURL
 * @param user
 * @param pwd
 */
       
       public  PublishGeoserver (String ServerURL,int port,String user,String pwd) {
    	   
    	   	RESTURL  = "http://"+ServerURL+":"+port+"/geoserver";
    	    RESTUSER = user;
    	    RESTPW   = pwd;
       }

   /***********************************
    *  
    * @throws FileNotFoundException
    * @throws IllegalArgumentException
    */
     public boolean connect () {
    	
		   try {
			reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	     
	    publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
	    boolean serverRunning = true;
	    serverRunning = reader.existGeoserver();
	    
	    return serverRunning; 
     }
  /************************
   *    
   */
   public boolean checkWorkspace(){
   
	   return checkWorkspace(workspace);
   }
   
   private void setNames(String layerName){
	    storename	=	layerName; //TODO NO ESTA BIEN UNA DB POR LAYER
	    OUTPUT_SHAPE= layerName;
	    OUTPUT_SHAP_FILE= OUTPUT_SHAPE+".shp";
	    OUTPUT_ZIP_FILE = OUTPUT_SHAPE+".zip";
   }
 /************
  *     
  * @param workspaceP
  */
   public boolean checkWorkspace(String workspaceP){
	   workspace=workspaceP;
	   
	   boolean workSpace = true;
	    
	    workSpace = reader.existsWorkspace(workspace);
	    
	    if (workSpace)	 System.out.format("WorkSpace %s está.",workspace);
        else {		
        	System.out.format("WorkSpace %s no está.",workspace);
	       
    	   	workSpace=publisher.createWorkspace(workspace);
	        if (workSpace)	 System.out.format("WorkSpace %s creado.",workspace);
	        else 		System.out.format("WorkSpace %s no creado.",workspace);
        } 	 
	    return workSpace;
    }
   
  /******************** 
   * 
   * @return
 * @throws IOException 
   */
   public boolean publish() throws IOException{
	   
	  
		File zipFile = new File(OUTPUT_ZIP_FILE);
	
		boolean published=false;
		try {
			
			publisher.removeDatastore(workspace, storename, true);
			published = publisher.publishShp(workspace, storename, layerName, zipFile,"EPSG:4326",new NameValuePair("charset","UTF-8"));
				
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		   
		zippedShapefile.deleteZip();
	   return published;
   	}
		  

	/******************
	   *      
	   */
	   private void checkDirectory(){
	    	
	    	File theDir = new File(SOURCE_FOLDER);

	    	// if the directory does not exist, create it
	    	if (!theDir.exists()) {
	    	    System.out.println("creating directory: " + SOURCE_FOLDER);
	    	    boolean result = false;

	    	    try{
	    	        theDir.mkdir();
	    	        result = true;
	    	    } 
	    	    catch(SecurityException se){
	    	        //handle it
	    	    }        
	    	    if(result) {    
	    	        System.out.println("DIR created");  
	    	    }
	    	}	
	    	else System.out.println("DIR exists "+SOURCE_FOLDER);  
	    }

	   
	   
/**********************
 * 	
 * @throws SchemaException
 * @throws IOException
 * @throws NoSuchAuthorityCodeException
 * @throws FactoryException
 */
	public void shapefilePtos (String layerNameP,List<Punto> ptos) throws SchemaException, IOException, NoSuchAuthorityCodeException, FactoryException{
		
		 layerName=layerNameP;
		 setNames(layerNameP);
		 
		 checkDirectory(); //Directorio Temporal para hacer el fichero.
			
		 final SimpleFeatureType TYPE = DataUtilities.createType("Location",
	                "the_geom:Point:srid=4326,"  // <- the geometry attribute: Point type
	     );
	        
		    
	    List<SimpleFeature> features = new ArrayList<SimpleFeature>();
	    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
	       
	      //  Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
	        for (Punto puntos: ptos){
	        	SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
	        	featureBuilder.add(geometryFactory.createPoint(new Coordinate(puntos.getLongitude(),puntos.getLatitude())));
	      
	        	SimpleFeature feature = featureBuilder.buildFeature(null);
	        	features.add(feature);
	        }
	        
	  
	                   
	        File newFile = new File(SOURCE_FOLDER+"//"+OUTPUT_SHAP_FILE);
	        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

	        Map<String, Serializable> params = new HashMap<String, Serializable>();
	        params.put("url", newFile.toURI().toURL());
	        params.put("create spatial index", Boolean.TRUE);

	        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
	        newDataStore.createSchema(TYPE);
	        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
		    newDataStore.forceSchemaCRS( crs );
	        Transaction transaction = new DefaultTransaction("create");

	        String typeName = newDataStore.getTypeNames()[0];
	        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
	        SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
	      
	        
	        System.out.println("SHAPE:"+SHAPE_TYPE);

	        if (featureSource instanceof SimpleFeatureStore) {
	            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
	            SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features);
	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(collection);
	                transaction.commit();
	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();
	            } finally {
	                transaction.close();
	            }
	           
	            zippedShapefile = new Zip(SOURCE_FOLDER, OUTPUT_ZIP_FILE );
	   		    zippedShapefile.Zipit();
	   		
	        } else {
	            System.out.println(typeName + " does not support read/write access");
	            
	        }
	}

/***************************
 * 	  
 * @param layerNameP
 * @throws SchemaException
 * @throws IOException
 * @throws NoSuchAuthorityCodeException
 * @throws FactoryException
 */
	public void shapefileLines(String layerNameP,List<Poly> polys,Feature.type tipo) throws SchemaException, IOException, NoSuchAuthorityCodeException, FactoryException{
	
	     layerName=layerNameP;
		 setNames(layerNameP);
		 
		 checkDirectory(); //Directorio Temporal para hacer el fichero.
		 final SimpleFeatureType TYPE_LINE;
		 
		if (tipo==Feature.type.LineString)
			  TYPE_LINE = DataUtilities.createType("Location",
		                "the_geom:LineString:srid=4326,"  // <- the geometry attribute: Point type
		     );
		else
			  TYPE_LINE = DataUtilities.createType("Location",
		                "the_geom:Polygon:srid=4326,"  // <- the geometry attribute: Point type
		     );
		 
		 
		   List<SimpleFeature> features = new ArrayList<SimpleFeature>();
		
		   for(Poly poly:polys){
			   
			  List<Punto> ptos= poly.getPtos();
		
		    	 SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE_LINE);
		    	 GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );

		   	  		Coordinate coord []= new Coordinate[ptos.size()];
					for (int i=0;i<ptos.size();i++)
					        coord[i] = new Coordinate(ptos.get(i).getLongitude(), ptos.get(i).getLatitude());
					       
					LineString line=  geometryFactory.createLineString(coord);
					featureBuilder.add(line);
				    
			        SimpleFeature feature = featureBuilder.buildFeature(null);
		            features.add(feature);
		   }	
	        
            
            File newFile = new File(SOURCE_FOLDER+"//"+OUTPUT_SHAP_FILE);
	        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

	        Map<String, Serializable> params = new HashMap<String, Serializable>();
	        params.put("url", newFile.toURI().toURL());
	        params.put("create spatial index", Boolean.TRUE);

	        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
	        newDataStore.createSchema(TYPE_LINE);
	        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
		    newDataStore.forceSchemaCRS( crs );
	        Transaction transaction = new DefaultTransaction("create");

	        String typeName = newDataStore.getTypeNames()[0];
	        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
	        SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
	      
	        
	        System.out.println("SHAPE:"+SHAPE_TYPE);

	        if (featureSource instanceof SimpleFeatureStore) {
	            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
	            SimpleFeatureCollection collection = new ListFeatureCollection(TYPE_LINE, features);
	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(collection);
	                transaction.commit();
	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();
	            } finally {
	                transaction.close();
	            }
	           
	            zippedShapefile = new Zip(SOURCE_FOLDER, OUTPUT_ZIP_FILE );
	   		    zippedShapefile.Zipit();
	   		
	        } else {
	            System.out.println(typeName + " does not support read/write access");
	            
	        }
		
	}

/******************	
 * 
 * @param args
 * @throws IOException
 * @throws SchemaException
 * @throws NoSuchAuthorityCodeException
 * @throws FactoryException
 */
	

	public static void main(String[] args) throws IOException, SchemaException, NoSuchAuthorityCodeException, FactoryException {
		// TODO Auto-generated method stub
	/*	
		List <Punto> listaPuntos = new ArrayList<Punto> ();
		listaPuntos.add(new Punto(36.7173350989895, -4.48167085647583));
		listaPuntos.add(new Punto(36.715546240904246, -4.477357864379883));
		listaPuntos.add(new Punto(36.71570104757726, -4.480104446411133));
		listaPuntos.add(new Punto(36.71300048642773, -4.478816986083984));
		listaPuntos.add(new Punto(36.71260485484803, -4.482464790344238));
		listaPuntos.add(new Punto(36.71568384685121, -4.4821858406066895));
		listaPuntos.add(new Punto(36.71735229934583, -4.478645324707031));
		listaPuntos.add(new Punto(36.71439378142239, -4.480319023132324));
		
		List <Punto> listaPuntos2 = new ArrayList<Punto> ();
		listaPuntos2.add(new Punto(36.7173350989895, -4.48167085647583));
		listaPuntos2.add(new Punto(36.715546240904246, -4.477357864379883));
	
		
		PublishGeoserver conexionServer =new PublishGeoserver();
		 if (conexionServer.connect()){
			System.out.format("Server %s funcionando.\n",conexionServer.RESTURL);
			if (conexionServer.checkWorkspace("workSpaceUMA")){
			 		System.out.format("WorkSpace  %s existe o creado.\n",conexionServer.workspace);
			 	
			 		
			 		conexionServer.shapefilePtos("parque",listaPuntos);
			 		boolean published=conexionServer.publish();
			 		if (published)	 System.out.println("Zip publicado.");
			        else 			 System.out.println("Zip no publicado.");
			 		
			 		conexionServer.shapefilePtos("bancos",listaPuntos2);
			 		published=conexionServer.publish();
			 		if (published)	 System.out.println("Zip publicado.");
			        else 			 System.out.println("Zip no publicado.");
			 		
			 		conexionServer.shapefileLines("bici",listaPuntos);
			 		published=conexionServer.publish();
			 		if (published)	 System.out.println("Zip publicado.");
			        else 			 System.out.println("Zip no publicado.");
			}
			    
		 }
		 else  System.out.format("Server %s no funcionando.\n",conexionServer.RESTURL);
		 
		*/
		}
	

}

	



