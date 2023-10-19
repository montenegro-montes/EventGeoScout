/*
 * BulletinDatabase.java
 * 
 * Created on 13-jun-2007, 0:14:20
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import MapFrame.Feature;
import MapFrame.Layer;
import MapFrame.Poly;
import MapFrame.Punto;

/**
 * 
 * @author Monte
 */
public class GeoDatabase {

    private java.sql.Connection con = null;
    private String urlUsers,urlUsersLocal;
    
    public GeoDatabase() {
        
        urlUsersLocal = "jdbc:derby:layer;create=true";
    	urlUsers ="jdbc:derby://localhost:1527/layer;create=true";
    	
        Properties p = System.getProperties();
        p.put("derby.storage.fileSyncTransactionLog", "true");
   }
    
  
   
  /*************************
   * CONEXION
   * @return
   */
   public boolean connect(){
        
        Boolean op=true;
      
     /*    try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        } catch(Exception e) {
            op=false;
            System.err.println("SQLException: " + e.getMessage());
        }  */     
        try{
	        Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
	        con = DriverManager.getConnection(urlUsers,"monte", "1qaz2wsx");
	    } catch(Exception e) {
	        System.out.println("BASE DATOS LOCAL: " + e.getMessage());
	    	
	        try{
	        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
	        con = DriverManager.getConnection(urlUsersLocal,"monte", "1qaz2wsx");
	        } catch(Exception ex){
	        	 System.err.println("SQLException: " + e.getMessage());
	        	op=false;
	    	}
        }   
       
        
        return op;
   }
  /**********************************
   *  
   * @return
   */

 
  public boolean deleteTables(){
      
      Boolean op0,op1,op2,op3,op4;
      
      	String createString = "DROP TABLE users"; 
 		op0=updateSQL(createString);
 
        createString = "DROP TABLE layers"; 
		op1=updateSQL(createString);
		
		createString = "DROP TABLE waypoints";                       
		op2=updateSQL(createString);
		
		createString = "DROP TABLE poly";
		op3=updateSQL(createString);
	
		createString = "DROP TABLE points";
		op4=updateSQL(createString);
	
      return op0&op1&op2&op3&op4;
}


  

/**************  
 * 
 * @return
 */
  
  public boolean createTables(){
        
        boolean op1,op2;
        
        op1=createTableUsers();    
        op2=createTableLayers();
        
        return op1&op2;
    }
  
  
 
/******************************************************
 *   
 * @return
 */
  public boolean createTableLayers(){
        
        String createString;
        boolean op1=true,op2=true,op3=true,op4=true;
        
        createString = "CREATE TABLE  layers (id VARCHAR(100) NOT NULL PRIMARY KEY, type  int, nelements INTEGER)";  
        op1=updateSQL(createString);
       
        createString = "CREATE TABLE  waypoints (layer VARCHAR(100), latitude DOUBLE PRECISION, longitude DOUBLE PRECISION)";                       
        op2=updateSQL(createString);
        
        createString = "CREATE TABLE  poly (layer  VARCHAR(100), idpoints BIGINT, nelements INTEGER)";                       
        op3=updateSQL(createString);
        
        createString = "CREATE TABLE  points (idpoints BIGINT, latitude DOUBLE PRECISION, longitude DOUBLE PRECISION, posicion INTEGER)";                       
        op4=updateSQL(createString);
        return op1&op2&op3&op4;
   }
  /*****************************************************
   * 
   * @return
   */
    public boolean createTableUsers(){
        
        String createString;
        boolean op=true;
           
        createString =  "CREATE TABLE  users(id VARCHAR(10) NOT NULL PRIMARY KEY, passwd  VARCHAR(512), online INT)";                        
        op=updateSQL(createString);
      
        return op;
    }
 
/****************************************************
 * 
 * @param layerName
 * @return
 */
    public boolean deleteLayer(String layerName,Feature.type tipo){
    	 boolean op1=true,op2=true,op3;
    	 
    	 String  createString;
    	if (tipo==Feature.type.Point){
    		 createString = "DELETE FROM waypoints WHERE layer='"+layerName+"'";
		        op1=updateSQL(createString);
    	}
    	else{
    		
			List<Long> ids=getIDPolys(layerName);
			for (Long id: ids){
				 createString = "DELETE FROM points WHERE idpoints="+id.longValue();
				//	System.out.println(createString);
					 op2&=updateSQL(createString);
			        	
			}

    		createString = "DELETE FROM poly WHERE layer='"+layerName+"'";
			op1=updateSQL(createString);
			
    	}
    	
    	createString = "DELETE FROM layers WHERE id='"+layerName+"'";
        op3=updateSQL(createString);
 		return op1&op2&op3;
           
    }
 /*******************************************
  * Delete point
  * @param latitude
  * @param longitude
  * @return
  */
 	public boolean deletePto(String layer,double latitude,double longitude){
     int num;
     String createString;
     num=getNumElements(layer)-1;
     	
 	   createString = "UPDATE layers SET nelements="+num+ " WHERE id='"+layer+"'";
  	   updateSQL(createString); 
  	   //System.out.println(createString);
  	   
 	  createString = "DELETE FROM waypoints WHERE layer='"+layer+"' AND latitude="+latitude+" AND longitude="+longitude;
 		 // System.out.println(createString);
 		return updateSQL(createString);
  	}
 	 
/************************************************
 * 
 * @param layer
 * @param ini
 * @param fin
 * @return
 */
 	public boolean deleteLine(String layer,Poly pto){
 	 boolean op1,op2,op3;
 	 String sentenceSQL;
 	 long idpoints=pto.getID();
 	 
 	 
 	 	sentenceSQL = "DELETE FROM points WHERE idpoints="+idpoints;
 	 	op1= updateSQL(sentenceSQL);
	
	 	int num=getNumElements(layer); num--;
	 	
	 	sentenceSQL = "UPDATE layers SET nelements="+num+ " WHERE id='"+layer+"'";
	 	//System.out.println(sentenceSQL);
	 	op2=updateSQL(sentenceSQL);
	 	//System.out.println(sentenceSQL);
	 	
 		sentenceSQL = "DELETE FROM poly WHERE idpoints="+idpoints;
	 	//System.out.println(sentenceSQL);
	 	op3= updateSQL(sentenceSQL);
	 		 	
	 	
	 	return op1&op2&op3;
 	}
 	

 
  /********************************
   *   
   * @param Id
   * @param type
   */

    public boolean insertLayer_(String Id, int type){
        
        String createString= "INSERT INTO layers VALUES('"+Id+"',"+type+",0)";
        return updateSQL(createString);
    }
  
  
    /*****************************************************
     *    
     * @param layer
     * @param pt
     * @return
     */
 
    	public boolean insertPto_(String layer,double latitude, double longitude){
       	
    			return insertPto_(layer,new Punto (latitude,  longitude) );
    	}
    
    	public boolean insertPtos_ (String layer,List<Punto> pts){
            int nelements= getNumElements(layer);
            int sizePtos = pts.size();
     	    int num=nelements+sizePtos;
			boolean op1=false,op2;

     	    String createString;

     	    for (int i=0;i<sizePtos;i++){
     	    	createString=  "INSERT INTO waypoints VALUES('"+layer+"',"+ pts.get(i).getLatitude()+","+pts.get(i).getLongitude()+")";
     	    	op1=updateSQL(createString);
     	    	
 		    }
     	   
     	   createString = "UPDATE layers SET nelements="+num+ " WHERE id='"+layer+"'";
     	   op2=updateSQL(createString); 
     	   
            return op1&op2;
    	}
    	
    	///////////
       public boolean insertPto_ (String layer, Punto pts){
           int nelements= getNumElements(layer);
           String createString;
    	   int num=nelements+1;
           boolean op1,op2;
           
           createString=  "INSERT INTO waypoints VALUES('"+layer+"',"+ pts.getLatitude()+","+pts.getLongitude()+")";
		   op1=updateSQL(createString);
    	   
    	   createString = "UPDATE layers SET nelements="+num+ " WHERE id='"+layer+"'";
    	   op2=updateSQL(createString); 
    	   
           return op1&op2;
       }
       ///////////
      /* public boolean insertPto_ (String layer, List<Punto> pts){
    	   boolean op=true;
    	   
    	   if (pts==null) return false;
    	   else{
    		   for (Punto punto : pts){
    			   op&=insertPto_ (layer, punto);
    		   }
    	   }
    	   return op;
       }*/
      ////
      
	   
	  
 /****************************************************
  *   
  * @param layer
  * @param ptOrg
  * @param ptDest
  * @return
  */
       
   public boolean insertLine (String layer,List<GeoPosition> ptos){
	   boolean op1,op2=true,op3;
	   
	    long time= System.currentTimeMillis();
	    int  num= getNumElements(layer)+1;
     	int ptosSize=ptos.size();
	 	
    	String createString=  "INSERT INTO poly VALUES('"+layer+"',"+time+","+ptosSize+")";
     	op1= updateSQL(createString);
        
     	int index=0;
     	for (GeoPosition pto:ptos){
     		createString=  "INSERT INTO points VALUES("+time+","+pto.getLatitude()+","+pto.getLongitude()+","+index+")";
     		op2&= updateSQL(createString);
     		index++;
     	}
        
        createString = "UPDATE layers SET nelements="+num+ " WHERE id='"+layer+"'";
        op3= updateSQL(createString); 
 	   
       return op1&op2&op3;
   }
   
  
  
   
 /******************************************************
  *   
  * @param createString
  * @return
  */
   private boolean updateSQL(String createString){
        
        Statement stmt;
        boolean estado=true;
        
        try {
            stmt = con.createStatement();
    
            stmt.executeUpdate(createString);    
            stmt.close();
        
        } catch (SQLException ex) {
        	
        	
        	if( ex.getErrorCode()==30000 ) { //ERROR CODE TABLA EXISTE
                return true; // That's OK
            }
            System.err.println("SQLException: " + ex.getMessage()+ "cc "+ex.getErrorCode());
            estado =false;
        }  
        return estado;
    }
   
  
   
    
 /***************************
  * 
  * @param layer
  * @return
  */
   public int getNumElements(String layer){
	
	   ResultSet rs;
       String createString;
       Statement stmt;
       int numelements=0;
      
       try {
       stmt = con.createStatement();
       
	       createString = "select nelements from layers where id='"+layer+"'";
	       rs=stmt.executeQuery(createString); 
	       
	       if (rs.next())   numelements=rs.getInt("nelements");
	       
	       rs.close();
           stmt.close();
           
       } catch (SQLException ex) {
           System.err.println("SQLException login: " + ex.getMessage());
       }
       return numelements;
   }
   
   /*******************************
    * 
    * @param layer
    * @return
    */
    
   public List<Long> getIDPolys(String layer){
		
	   ResultSet rs;
       String createString;
       Statement stmt;
       Long idpoints;
       List <Long> listaID = new ArrayList<Long>();
       
       try {
       stmt = con.createStatement();
       
	       createString = "select idpoints from poly where layer='"+layer+"'";
	       rs=stmt.executeQuery(createString);
	       
	       while (rs.next()) {
	       	idpoints=rs.getLong("idpoints");
	       	listaID.add(idpoints);
	       }
           
       } catch (SQLException ex) {
           System.err.println("SQLException login: " + ex.getMessage());
       }
       return listaID;
   }
  
 /*****************************************
  *   
  * @param layer
  * @return
  */
    public List<Punto> getPtos(String layer){
    	  ResultSet rs;
          String createString;
          Statement stmt;
          double latitude,longitude;
          List<Punto>  ptos = new ArrayList<Punto>();
          
          try {
              stmt = con.createStatement();
                                         
              createString = "select latitude,longitude from waypoints where layer='"+layer+"'";
              rs=stmt.executeQuery(createString);
              
              while (rs.next()) {
            	  latitude=rs.getDouble("latitude");
            	  longitude=rs.getDouble("longitude");
            	  ptos.add(new Punto(latitude,longitude));
            	 // System.out.println("Latitude: "+latitude+" Longitude: "+longitude);
              }
          
             rs.close();
             stmt.close();
    
          } catch (SQLException ex) {
              System.err.println("SQLException login: " + ex.getMessage());
          }
          
          return ptos;
    }
    
 /************************   
  * 
  * @param layer
  * @return
  */
    
    
    
    public List<Punto> getPtosfromPoly(long idpoints){
    	  ResultSet rs;
          String createString;
          Statement stmt;
          int posicion;
          double latitude, longitude;
          List<Punto>  point = new ArrayList<Punto>();
          
          try {
              stmt = con.createStatement();
              
              createString = "SELECT latitude, longitude, posicion from points where idpoints="+idpoints;
              rs=stmt.executeQuery(createString);
              
              while (rs.next()) {
            	  latitude=rs.getDouble("latitude");
            	  longitude=rs.getDouble("longitude");
            	  posicion=rs.getInt("posicion");
            	  point.add(posicion,new Punto(latitude,longitude));
              }
          
             rs.close();
             stmt.close();
    
          } catch (SQLException ex) {
              System.err.println("SQLException login: " + ex.getMessage());
          }
          
          return point;
    }
   
   public List<Poly> getLines(String layer){
  	  ResultSet rs;
        String createString;
        Statement stmt;
        long idpoints;
        int nelements;
        List<Poly>  polys = new ArrayList<Poly>();
        Poly polyFeature;
        
        try {
            stmt = con.createStatement();
            
            createString = "select idpoints, nelements from poly where layer='"+layer+"'";
            rs=stmt.executeQuery(createString);
            
            while (rs.next()) {
            	idpoints=rs.getLong("idpoints");
            	nelements=rs.getInt("nelements");
            	
            	polyFeature=new Poly(idpoints,nelements);
            	polyFeature.setPtos(getPtosfromPoly(idpoints));
             	 
            	polys.add(polyFeature);
            }
        
           rs.close();
           stmt.close();
  
        } catch (SQLException ex) {
            System.err.println("SQLException login: " + ex.getMessage());
        }
        
        return polys;
  }
    
 /*********************************
  *    
  * @return
  */
    public List<Layer> getLayers() {
        List<Layer>  layers = new ArrayList<Layer>();
        ResultSet rs;
        String layer,createString;
        int type,nelements;
        createString = "select * from layers";
        
        rs= consul(createString);
        
        try {
			while (rs.next()) {
				  layer = rs.getString("id");
				  type = rs.getInt("type");
				  nelements = rs.getInt("nelements");
				  layers.add(new Layer(layer,type,nelements));
				 // System.out.println("Layer: "+layer+" Type: "+type+ " "+nelements);
			  }
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return layers;
    }
    
    /*****************************
     * 
     * @param user
     * @param pwd
     * @return
     */
    public int isAuthenticated(String user,String pwd){
        
        int authenticatedB = 0;
        Statement stmt;
        String createString;
        ResultSet rs;
        String DBpwd=null;
        int online=-1;
        
        try {
            stmt = con.createStatement();
                                       
            createString = "select passwd,online from users where id='"+user+"'";
            rs=stmt.executeQuery(createString);
            
            while (rs.next()) {
                DBpwd=rs.getString("passwd");
                online=rs.getInt("online");
            }

            
           if (online==1) authenticatedB=-1;
           else{
             if  (DBpwd!=null) if (DBpwd.contentEquals(pwd)) authenticatedB=1; 
           }
            
           rs.close();
           stmt.close();
  
           if(authenticatedB==1)   setOnline (true, user); //AQUí ESTA PUESTO ONLINE
  
        } catch (SQLException ex) {
            System.err.println("SQLException login: " + ex.getMessage());
        }
       return authenticatedB;
       
    }

  /*********  
   * 
   * @param online
   * @param user
   * @return
   */
    public boolean setOnline (boolean online, String user){
        
        String createString;
        
            if (online)     createString = "UPDATE users SET online=1  WHERE id='"+user+"'";
            else            createString = "UPDATE users SET online=0  WHERE id='"+user+"'";
            
        return updateSQL(createString);
    }

   /**************** 
    * 
    * @param user
    * @param pwd
    * @return
    */
    public boolean updatePwd (String user, String pwd){
        
        String createString = "UPDATE users SET passwd='"+pwd+"'  WHERE id='"+user+"'";
        return updateSQL(createString);
       
    }
    
 /****************************
  *    
  * @param consult
  * @return
  */
  
    private ResultSet consul (String consult){
       ResultSet rs=null;
       Statement stmt;
    
       try {
            stmt = con.createStatement();
                                       
            rs=stmt.executeQuery(consult);
            
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
       
       return rs;
        
    } 

    public Hashtable<String, String> getUsers (){
          
      Hashtable<String, String> users =new  Hashtable<String, String>();
      ResultSet rs;
      Statement stmt;
      String createString;
      
        
        try {
            stmt = con.createStatement();
                                       
            createString = "select id,passwd from users";
            rs=stmt.executeQuery(createString);
            
        String ID, PWD;
            
            
            while (rs.next()) {
     
               ID=rs.getString(1);
               PWD=rs.getString(2);
               
               users.put(ID, PWD);
               
            }
            
            rs.close();
            stmt.close();
        
        } catch (SQLException ex) {
            System.err.println(" getUsers SQLException: " + ex.getMessage());
        }
    
      return users;
    }
    
    /******************************** 	
     * 
     * @param Id
     * @return
     */
        public boolean deleteUser(String Id){
           
        	String createString = "DELETE FROM users WHERE id='"+Id+"'";
            return updateSQL(createString);
               
        }
        ///////////////////////////////////////////

        
        public boolean insertUsers(){
            
            boolean op=true;
        
            String key=new String ("12");
            String keyC=codifPwd(key);
            
            	op=insertUser("Admin",keyC);
                for (int i=1;i<5;i++){
            	   op=insertUser("User"+i,keyC);
                 }
                  return op;
        }



    			///////////////////////////////////////////
    			private static String stringHexa(byte[] bytes) {
    			   StringBuilder s = new StringBuilder();
    			   for (int i = 0; i < bytes.length; i++) {
    			       int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
    			       int parteBaixa = bytes[i] & 0xf;
    			       if (parteAlta == 0) s.append('0');
    			       s.append(Integer.toHexString(parteAlta | parteBaixa));
    			   }
    			   return s.toString();
    			}
    			
    			
    			private String codifPwd(String pwd){
    			
    			    
    			          try {
    			            MessageDigest md = MessageDigest.getInstance("MD5");
    			            md.update(pwd.getBytes());
    			            return stringHexa (md.digest());
    			          } catch (NoSuchAlgorithmException e) {
    			            return null;
    			          }
    			
    			}
        ////////////////////////////////////////////////////
    			
     /*****************   
      * 
      * @param Id
      * @param Pwd
      * @return
      */
        public boolean insertUser(String Id, String Pwd){
           
            String createString = "INSERT INTO users VALUES('"+Id+"','"+Pwd+"',0)";
     		return updateSQL(createString);
        }
           
    
    public void closeDatabase (){
        try {
            con.close();
        } catch (SQLException ex) {
           System.err.println("SQLException: " + ex.getMessage());
        }
    }
    
    protected void Debug(){
    	
    	List<Layer> layers=getLayers();
    	
    	for(Layer layer: layers){
    		System.out.println(layer);
    		
    		if (layer.getTipo()==0){ // PTO
    			List<Punto> ptos=getPtos(layer.getName());
    			for(Punto pto: ptos){
    				System.out.println(pto);
    			}
    		}
    		else{
    			List<Poly> polys=getLines(layer.getName());
    			System.out.println(polys);
    		}
    	}
    }
/******************************    
 * MAIN PRUEBA
 * @param args
 */
     public static void main(String[] args) {
	  
    	 
   	  /*
         GeoDatabase database= new GeoDatabase();
         if (!database.connect()) {
         	System.err.println("S: Error DataBase connect process. EXIT!!!");
         		return;    
          }
         database.Debug();
    	List <Punto> listaPuntos = new ArrayList<Punto> ();
		listaPuntos.add(new Punto(36.7173350989895, -4.48167085647583));
		listaPuntos.add(new Punto(36.715546240904246, -4.477357864379883));
		listaPuntos.add(new Punto(36.71570104757726, -4.480104446411133));
		listaPuntos.add(new Punto(36.71300048642773, -4.478816986083984));
		listaPuntos.add(new Punto(36.71260485484803, -4.482464790344238));
		listaPuntos.add(new Punto(36.71568384685121, -4.4821858406066895));
		listaPuntos.add(new Punto(36.71735229934583, -4.478645324707031));
		listaPuntos.add(new Punto(36.71439378142239, -4.480319023132324));
		
	
        GeoDatabase database= new GeoDatabase();
        if (!database.connect()) {
        	System.err.println("S: Error DataBase create process. EXIT!!!");
        		return;    
         }
        database.deleteTables();
        if (! database.createTableLayers()) {
       	 System.err.println("S: Error creating Tables of DataBase. EXIT!!!");
         return;    
        }
        
        database.insertLayer_("layer1", 0); 
        database.insertLayer_("layer2", 1); 
        
        database.insertPto_ ("layer1",new Punto(36.7173350989895, -4.48167085647583));
        
        int nelements= database.getNumElements("layer1");
        System.out.println("Nelements: "+nelements);
        
        database.insertPto_ ("layer1",listaPuntos);
        nelements= database.getNumElements("layer1");
        System.out.println("Nelements: "+nelements);
        
       
        database.insertLine("layer2", new Punto(36.7173350989895, -4.48167085647583), new Punto(36.715546240904246, -4.477357864379883));
        database.insertLine("layer2", new Punto(36.71570104757726, -4.480104446411133),new Punto(36.71439378142239, -4.480319023132324));
        nelements= database.getNumElements("layer2");
        System.out.println("Nelements: "+nelements);
        
        List<Poly> polyFe=database.getLines("layer2");
        System.out.println("Nelements poly: "+polyFe.size());
    
        System.out.println("Nelements pto: "+polyFe.get(0).getPtos().size());
        System.out.println("Nelements pto: "+polyFe.get(1).getPtos().size());*/
            
            
        /*
       
         if (!database.createTables()) {
        	 System.err.println("S: Error creating Tables of DataBase. EXIT!!!");
          return;    
         }
         
         System.out.println("S: Database Configured.");
         
         // database.insertBids();
         if (!database.insertUsers()) {
        	 System.err.println("S: Error insert users. EXIT!!!");
          return;    
         }
  
        database.insertLayer("layer1", 0); 
        database.insertLayer("layer2", 0); 
        
        Random r = new Random();
        r.nextDouble();
    
           for (int i=1;i<5;i++){
        	   database.insertPto("layer1",r.nextDouble(),+r.nextDouble());
           }
           for (int i=1;i<10;i++){
        	   database.insertPto("layer2",r.nextDouble(),+r.nextDouble());
           }
        
        database.insertPtos();
        List <Punto> ptos=database.getPtos("layer1");
        System.out.println("LISTA: "+ptos.size());
        
        List <Punto> ptos2=database.getPtos("layer2");
        System.out.println("LISTA: "+ptos2.size());
        
        List <Layer> layers= database.getLayers();
        database.closeDatabase();*/
    }
}
