/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;


import java.util.ArrayList;
import java.util.List;

import ButtonListener.ButtonAbstract;
import GeoServer.PublishGeoserver;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import MapFrame.Feature;
import MapFrame.Feature.type;
import MapFrame.Layer;
import MapFrame.Poly;
import MapFrame.Punto;
import Users.PoolThreadUsers;
import Users.UserManager;
import Visual.LayerTable;
import Visual.CfgWindow;
import Visual.ImportURLLayersWindow;
import Visual.UserWindow;
import Visual.featureModePanel;
import Visual.infoLog;
import Visual.newLayerPanel;




/**
 *
 * @author Monte
 */
public class Mediator {
   
	public static enum tipoBotones {AddLayer, DelLayer, Config, Start, Stop, Users,Publish,Import};
    private GeoDatabase    database;
    
    //BOTONES
    	private ButtonAbstract     	startButton, configButton, userButton, stopButton, Import;
        private ButtonAbstract		addLayerButton,delLayerButton,publishLayer;//LAYERS
    // FIN BOTONES
    
    private CfgWindow           cfgWindow; //Ventana Configuracion
    private JFrame              cfgFrame;
    
    private UserWindow          userWindow;
    private JFrame              userFrame;
    
    private infoLog             infoLOG;
    
    private newLayerPanel       layerPanel;
    private Server              server;
    
    private LayerTable       	BBTable;
    private JFrame              principalFrame;
    
    private MapFrame			map;	

    private featureModePanel        feature;       
    private PoolThreadUsers   pool;

 /***********************************************
  * 
  * @param Cfg
  */   
   public  Mediator (JFrame frame){
         
       principalFrame   = frame;
       cfgFrame         = new JFrame (); 
       cfgWindow        = new CfgWindow (cfgFrame);
       pool             = new PoolThreadUsers();
       
    }

 /***********************************************
  * 
  */    
    public boolean init (){
        stopButton.setEnabled(false);
        startButton.setEnabled(false);
        configButton.setEnabled(false);
        userButton.setEnabled(false);
       
        
        if (iniDataBase())  {
	        userFrame = new JFrame (); 
        	userWindow = new UserWindow (cfgFrame,database);
        	
        	
        	return true;
        }
        else return false;
        
       
    }
     
 /***********************************************
  * 
  * @param infoPanelP
  */   
     public void registerInfoPanel(newLayerPanel newlayerPanelP){
        layerPanel=newlayerPanelP;
    }
     
     public void registerMap(MapFrame mapP){
    	 map=mapP;
     }
     
     public void registerMode(featureModePanel featurP){
    	 feature=featurP;
     }
     
 /***********************************************
  * 
  * @param checksB
  */
     
  
     
     public void registerInfoLog(infoLog info){
        infoLOG=info;
    }
     
     public void registerBB(LayerTable table){
        BBTable=table;
    }
     

 
 /***********************************************
  *AUX 
  */    
     
    boolean iniDataBase(){

            database= new GeoDatabase(); /* CREATE DATABASE */
                                            /// !!!!!!!!!!!!!!!!!! CREATE TABLES
           if (!database.connect()) {
        	   System.err.println("S: Error connecting  DataBase. EXIT!!!");
        	   infoLOG.addLog("S: Error connecting  DataBase. EXIT!!!.");
               return false;
           }

        //   database.deleteTables();
           if (!database.createTables()) {
        	   System.err.println("S: Error creating Tables of DataBase. EXIT!!!");
            return false;    
           }
           
           infoLOG.addLog("S: Database Configured.");
           
           if (!database.insertUsers()) {
        	   System.err.println("S: Error insert users. EXIT!!!");
            return false;    
           }
           infoLOG.addLog("S: Users Inserted. Database Ready!!");
           
            startButton.setEnabled(true);
            configButton.setEnabled(true);
            userButton.setEnabled(true);
    
          return true;
    } 
 /***********************************
  * 
  *    CARGA DATO TABLA SI HAY EN DB
  */
    public void LoadData(){
    	 List<Layer> layers=this.getLayers();
    	
         if (layers.size()>0){
      	   
      	   for (Layer layer : layers) {
      		   Feature.type tipo=Feature.type.Point;
      		   int size= layer.getNelements();
      		   int layerType= layer.getTipo();
      		   String layerName=layer.getName();
      		   
      		   switch (layerType){
      		   			case 0: tipo=Feature.type.Point; 		break;
      		   			case 1: tipo=Feature.type.LineString; 	break;
      		   			case 2: tipo=Feature.type.Polygon;		break;
      		   }
      	
      		  // System.out.println(layerName+" "+tipo+" "+size);
      		   BBTable.loadNewData(layerName,tipo,size);
      		}
      	  
         }
    }
/*************************
 * 
 * @return
 */
   public  List<Layer>  getLayers (){
    	List<Layer> layers=database.getLayers();
    	return layers;
    }

     
    
 /*****************************************************
  * 
  * @param msg
  */   
    public void AddLog (String msg){
           infoLOG.addLog(msg);
    }
/*****************************************************
 * 
 * @param user
 * @param pwd
 * @return
 */    
    public int isAuthenticated (String user,String pwd){
        int ret=0;
        
        ret=database.isAuthenticated(user, pwd);  
        return ret;
    }
 

    
 /*****************************************************
  * 
  * @param msg
  * @param info
  */   
   public void severeError(String msg,String info){
         JOptionPane.showMessageDialog(principalFrame,msg,info,JOptionPane.ERROR_MESSAGE);
         panicMode();
    }
 
 
   
   
  
   /*****************************************************
    * 
    */   
   
   public void  panicMode(){
   
       startButton.setEnabled(false);
       stopButton.setEnabled(false);
       configButton.setEnabled(false);
       userButton.setEnabled(false);
   }

   
   
/*************************
 * BOTONES
 */

       public void registerButton(ButtonAbstract button){
      	 switch (button.tipo){
      	 		
      	 		case AddLayer:   addLayerButton=button;		break;
      	 		case DelLayer:   delLayerButton=button;	 	break;
      	 		
      	 		case Config: configButton =button; break;
            	case Start:	 startButton= button; break;
            	case Stop: 	stopButton= button;break;
            	case Users:	userButton= button; break;
            	case Publish:	publishLayer= button; break;
      	 }
      	 
       }   
  /*********************     
   * Lo llama el mapa cuando se añade un pto.
   * @param layerName
   * @param ptoAdd
   */
     public void addWayPoint(String layerName,GeoPosition ptoAdd){
    	   BBTable.updateFeatures(layerName,true);
    	   database.insertPto_(layerName, ptoAdd.getLatitude(),ptoAdd.getLongitude());
     }
     
     
     
     public void addWayPoint(String layerName,List<GeoPosition> ptoAdd){
  
  	   List<Punto> ptPuntos = new ArrayList<Punto>();
  	   
  	   for (int i=0;i<ptoAdd.size();i++){
  		 ptPuntos.add(new Punto(ptoAdd.get(i).getLatitude(),ptoAdd.get(i).getLongitude()));
  	   }
  	   
  	   database.insertPtos_(layerName,ptPuntos);
  	   
	   BBTable.updateFeatures(layerName,true,ptoAdd.size());
   }
     
 /*****************************
  * Borro el punto que está más próximo del evento
  * @param layerName
  * @param ptoAdd
  */
     public void delWayPoint(String layerName,GeoPosition ptoAdd){
  	   
    	 if (ptoAdd!=null){
    		 BBTable.updateFeatures(layerName,false); //Decremento la tabla
    		 database.deletePto(layerName, ptoAdd.getLatitude(),ptoAdd.getLongitude());
    	 }
   }
/************
 * 
 * @param layerName
 * @param ptoAdd
 */
     public void addLynePoint(String layerName,List<GeoPosition> ptosAdd){

    	 
    	 if (ptosAdd!=null){
    	 	 BBTable.updateFeatures(layerName,true); 
    		 database.insertLine(layerName, ptosAdd);
    		 //System.out.println("LINEAS INSERTADA");
    	 }
   }
/**********************
 *   
 * @param layerName
 * @param ptoIni
 * @param ptoFin
 */
    public void delLynePoint(String layerName,Poly ptos){
    	   
    	 if (ptos!=null){
    		 BBTable.updateFeatures(layerName,false); //Decremento la tabla
    		 database.deleteLine(layerName, ptos);
    		 
    	 }
   }
       
 /***************************
  *    
  */
  public void modeChanged(){
	  
	//  System.out.println(" MODE CHANGED "+BBTable.getRowCount());
	  if(BBTable.getRowCount()>0){
			  Feature.type tipoFeature=BBTable.getTypeSelected();
			  List<Poly> ptosLines=null;
			  String layerName=null;
			
			  layerName=BBTable.getLayerSelected();
			  map.setLayerAndType(layerName,tipoFeature);
			  
			  if ((tipoFeature!=null)	&&  (tipoFeature!=Feature.type.Point))	{
				   ptosLines=database.getLines(layerName);
				  
			  }	
			  
			  if(feature.isAddSelected()){
				  if ((tipoFeature!=null)	&&  (tipoFeature!=Feature.type.Point))	{
					 map.displayLine(ptosLines,false);
				  }
				   
			  }
			  else {
				  if ((tipoFeature!=null)	&&  (tipoFeature!=Feature.type.Point))	{
					 map.displayLine(ptosLines,true);
				  }
			  }
	  }
	
  }
  
  /**********************
   * Lo llama la tabla para poner en modo edicion 
   * 
   */
  public void tableSelected(){
	  
	// System.out.println("tableSelected LINEAS "+BBTable.getRowCount());

	String layerName=BBTable.getLayerSelected();
	Feature.type tipoFeature=BBTable.getTypeSelected();
	map.setLayerAndType(layerName,tipoFeature);
	
	if(tipoFeature!=null)	
		switch(tipoFeature){
			case Point: List<Punto> ptos=database.getPtos(layerName);
						map.displayWaypoint(ptos);
						break;
						
			case  LineString: List<Poly> ptosLines=database.getLines(layerName);
								if(feature.isAddSelected())  map.displayLine(ptosLines,false);
								else map.displayLine(ptosLines,true);
							break;
			case  Polygon:  List<Poly> ptosLinesPoly=database.getLines(layerName);
							if(feature.isAddSelected())  map.displayLine(ptosLinesPoly,false);
							else map.displayLine(ptosLinesPoly,true);
							break;
		}
		
		
		
  }
   
  public List<Punto> getPuntos (String layerName){
	  return database.getPtos(layerName);
  }
  
  public List<Poly> getPoly (String layerName){
	  return database.getLines(layerName);
  }
  
  
 /**********************
  * 
  * @param tipo
  */
   public void onClick(tipoBotones tipo){
	   switch (tipo){
	   			
	   			case AddLayer:     
	   	    	 		   	    	 if (!layerPanel.isLayerEmpty()){
						   	    		 String layer=layerPanel.getLayerName();
						   	    		 Feature.type type=layerPanel.getType();
						   	    		 BBTable.loadNewData(layer,type);
						   	    		 
						   	    		 database.insertLayer_(layer, layerPanel.getTypeIndex());
						   	    		
						   	    		 layerPanel.cleanLayerName();
						   	    		 layerPanel.cleanType();
						   	    	 }
	   	    	 		   	    	 BBTable.clearSelection();
	   	    	 		   	    	 map.setLayerAndType(null, null);	
	   	    	 		   	    	 break;
						   	         
	   			case DelLayer:  
	   							String layer = BBTable.getLayerSelected();
	   							Feature.type FeatureSel=BBTable.getTypeSelected();
	   							database.deleteLayer(layer,FeatureSel);
	   							BBTable.deleteSelected(); 
	   								
	   							if (FeatureSel==type.Point) {
									List<Punto> ptos=new ArrayList<Punto>();
									map.displayWaypoint(ptos); //LIMPIAR SI TENIAMOS ALGO
								}
	   							else{
	   								List<Poly> polyShow=new ArrayList<Poly>();
	   								map.displayLine(polyShow, false); //LIMPIAR SI TENIAMOS ALGO
	   							}
	   							BBTable.clearSelection();
	   							map.setLayerAndType(null, null);	
	   							break;
	   			
	   			case Config:  	configButton.setEnabled(false);
				                cfgWindow.showWindow();
				                cfgFrame.requestFocus();
				                configButton.setEnabled(true);
				                
				                break;
				                
	        	case Start: 	startButton.setEnabled(false);
					            
					             //SERVIDOR COMIENZA
					             server = new Server (this,cfgWindow.getConfiguracion()); 
					             server.start();
					             ////
					             configButton.setEnabled(false);
					             userButton.setEnabled(false);
					             publishLayer.setEnabled(false);
					             stopButton.setEnabled(true);
					             addLayerButton.setEnabled(false);
					             delLayerButton.setEnabled(false);
					             feature.setEnabled(false);
					             layerPanel.setEnabled(false);
					             BBTable.setEnabled(false);
					             BBTable.clearSelection();
					             
					             map.clear();
					             break;
	        	case Stop: 		
	        		            stopButton.setEnabled(false);
				                startButton.setEnabled(true);
				                configButton.setEnabled(true);
				                userButton.setEnabled(true);
				                infoLOG.clear();
				                publishLayer.setEnabled(true);
				                server.stopServer();
				                
				                addLayerButton.setEnabled(true);
					             delLayerButton.setEnabled(true);
					             feature.setEnabled(true);
					             layerPanel.setEnabled(true);
					             BBTable.setEnabled(true);
				                break;
				case Users: 
				        	     userButton.setEnabled(false);
				                 userWindow.showWindow();
				                 userFrame.requestFocus();
				                 userButton.setEnabled(true);
				        		break;
				        		
				case Publish: 
					   	    
					   	    	int selected=BBTable.getSelectedRow();	 
					   	    	if (selected >-1){
					   	    		String layerName=BBTable.getLayerSelected();
					   	    		FeatureSel=BBTable.getTypeSelected();
					   	    		List<Punto> puntos=null;
					   	    		List<Poly> poly=null;
					   	    		
					   	    		switch (FeatureSel){
					   	    			case Point: puntos=database.getPtos(layerName);
					   	    						publishPtoGeoserver(layerName,puntos);
					   	    						break;
					   	    			case LineString: 
					   	    					
					   	    			case Polygon: poly= database.getLines(layerName);
					   	    						  publishPolyGeoserver(layerName,poly,FeatureSel);
					   	    						  break;
					   	    		}
					   	    			
					   	    	
					   	    	}
					   	    	else 
					   	    	 JOptionPane.showMessageDialog(principalFrame,"Please, select first a layer","Publish",JOptionPane.ERROR_MESSAGE);
								break;
								
								
				case Import:	
								JFrame GeoJSONFrame   		= new JFrame (); 
								ImportURLLayersWindow GeoJSON 		= new ImportURLLayersWindow(GeoJSONFrame);
								GeoJSON.showWindow();
								if (GeoJSON.havetoSave()){
									GeoJSON result = GeoJSON.getResult();
									if (result!=null){
										String layerS = result.getLayerName();
										
										BBTable.loadNewData(layerS,Feature.type.Point);
										database.insertLayer_(layerS, 0);
						   	    		
						   	    		int nptos				=	result.getnumPoints();
						   	    		List<GeoPosition> ptos 	=	result.getPoints();
						   	    		
						   	    		addWayPoint(layerS,ptos);
						   	    		
						   	    		/*for (int i=0;i<nptos;i++){
						   	    			addWayPoint(layerS,ptos.get(i));
						   	    		}*/
						   	    			
									}
								}
								break;
	   }
   }
/***************************************
 * 
 * @param pto
 */
   public void EventMapa (GeoPosition pto){
	  
	   type tipo=null; //Tipo de elemento añadir o borrar;
	   boolean add=feature.isAddSelected();
	   tipo=BBTable.getTypeSelected();
		
	if (BBTable.isEnabled()&&tipo!=null) //SOLO FUNCIONA SI ESTA LA TABLA SELECCIONADA
           switch (tipo){
           	case Point: 		if (add) map.addWaypoint(pto);
           						else	 map.delWaypoint(pto);
           						break;
           						
           	case LineString: 	if (add) map.addLine(pto,false); 
           						else 	 map.delLine(pto);	
           						break;	
           	case Polygon: 		
           						if (add) map.addPoly(pto,false);  
           						else 	 map.delLine(pto); 
           						break;	
           	
           }
   }
   
/*******************************************************************
 * 
 * @param layerName
 * @param listaPuntos
 */
   private void publishPtoGeoserver(String layerName,List<Punto> listaPuntos){
		
	   Configuration cfg = cfgWindow.getConfiguracion();
	   
	   
	   String Geoserver = cfg.getGeoServer();
	   int GeoserverPort = cfg.getGeoServerPort();
	   String pwd = cfg.getPwd();
	   String user = cfg.getUser();
			   
	   PublishGeoserver conexionServer =new PublishGeoserver (Geoserver,GeoserverPort,user, pwd);
		 if (conexionServer.connect()){
			if (conexionServer.checkWorkspace("workSpaceUMA")){
					boolean published=false;
				
					try {
						conexionServer.shapefilePtos(layerName,listaPuntos);
	   	    			published = conexionServer.publish();
					} catch (Exception e) {
						e.printStackTrace();
					}
			 		
			 		if (published)	 System.out.println("Zip publicado.");
			        else 			 System.out.println("Zip no publicado.");
			}
		 }
		 else{  
			 String mensaje = "Server "+Geoserver+" in port "+ GeoserverPort+ " not responding.";
			 JOptionPane.showMessageDialog(principalFrame,mensaje,"Geoserver Connection", JOptionPane.ERROR_MESSAGE);
		 }
		 
   }
   
   
   
   private void publishPolyGeoserver(String layerName,List<Poly> listaPuntos,Feature.type tipo){
	   Configuration cfg = cfgWindow.getConfiguracion();
		  
	   String Geoserver = cfg.getGeoServer();
	   int GeoserverPort = cfg.getGeoServerPort();
	   String pwd = cfg.getPwd();
	   String user = cfg.getUser();
	   
	   
			   
	   PublishGeoserver conexionServer =new PublishGeoserver (Geoserver,GeoserverPort,user, pwd);
		 if (conexionServer.connect()){
			if (conexionServer.checkWorkspace("workSpaceUMA")){
					boolean published=false;
				
					try {
						conexionServer.shapefileLines(layerName,listaPuntos,tipo);
	   	    			published = conexionServer.publish();
					} catch (Exception e) {
						e.printStackTrace();
					}
			 		
			 		if (published)	 System.out.println("Zip publicado.");
			        else 			 System.out.println("Zip no publicado.");
			}
		 }
		 else {
			 String mensaje = "Server "+Geoserver+" in port "+ GeoserverPort+ " not responding.";
			 JOptionPane.showMessageDialog(principalFrame,mensaje,"Geoserver Connection", JOptionPane.ERROR_MESSAGE);
		 }
		 
   } 
    ///////////////////////////////////////////////////////////////

   public void addElementPool (UserManager cl){
       pool.addClients(cl);
   }
 /*******************************
  *   
  * @param user
  */
   public void disconnected (String user, String addressClient){
	   AddLog("C->S: Client "+user+" disconnected from "+ addressClient);
       database.setOnline(false, user);
   }
   
   public void sendBroadCastModif(String layerName,int tipo,boolean add, String userL){
	   pool.sendBroadCastModif(layerName, tipo,add,  userL);
		   
   }
}

