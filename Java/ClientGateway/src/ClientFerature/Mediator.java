
package ClientFerature;

import Buttons.CfgButton;
import Buttons.ConnectButton;
import Buttons.DisConnectButton;
import Buttons.UFLPButton;
import Buttons.UserLabel;
import Buttons.infoLog;
import MapFrame.Feature;
import MapFrame.Layer;
import MapFrame.Poly;
import MapFrame.Punto;
import Packet.PacketModifLayerDataB;
import UFLP.GeneticWindow;
import UFLP.UFLPWindow;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import eu.hansolo.custom.SteelCheckBox;




/**
 *
 * @author Monte
 */
public class Mediator {
    
    private ConnectButton       connect;
    private DisConnectButton    disconnect;
    private UFLPButton    		uflp;
    private CfgButton			cfgbutton;
    
    private infoLog             infolog;
    private UserLabel           userInfo;
    private Container           contPane;
    
    private NetworkManager      network;
    private Configuration       cfg;
    private UserManager         useerManager;
    
    MapFrame map;
    //TMP
    private String loginTmp;
    //END_TMP
    UFLPWindow 					uflpWindow;
    
    private LayerTable       LayersTable;       //Layer table
    SteelCheckBox del;
    
    boolean			UFLP_mode 	= false;
   /****************************************************
    * 
    */ 
    
    Mediator (Configuration Cfg){
        cfg=Cfg;
        
        useerManager = new UserManager(this);
       
    }
    
   /****************************************************
    * 
    */ 
   public void init (Container contPANEL){
        

            contPane = contPANEL;
            
            connect.setEnabled(true);

            AddLog("Client configuration::> Server ip: " + cfg.getServer() + " Server port: " + cfg.getPort());
            
            
 } 
 

 
/*****************************************************
 * 
 * 
 * @param key
 */
   public void registerComm(NetworkManager networkP){
        network=networkP;
    }



 /****************************************************
  * 
  * @param DisConnect
  */

    public void register(JButton button){
    	if (button instanceof UFLPButton) uflp = (UFLPButton) button;
    	else if (button instanceof ConnectButton) connect = (ConnectButton) button;
    	else if (button instanceof DisConnectButton) disconnect = (DisConnectButton) button;
    	else if (button instanceof CfgButton) cfgbutton = (CfgButton) button;

    }
    	   
/*****************************************************
 * 
 *
 */
 
    public void registerInfoLog(infoLog infoLOG){
        infolog=infoLOG;
    }

/*****************************************************
 * 
 * @param label
 */
    public void registerUserInfo(UserLabel label){
        userInfo=label;
    }

    
    

/*****************************************************
 * 
 * @param 
 */
  

  public void registerBB(LayerTable layerTable){
        LayersTable=layerTable;
    }
    
/*****************************************************
 * 
 */
  public void UFLP ()  {
	  
	  UFLP_mode 		  = true;
	  List<Layer> layers  = LayersTable.getPointLayers();
	  
	 if ( layers.size() < 2){
		 warningError("Two point layers is needed","ULP");
	 }
	 else{	 
		  // SI LAYER de puntos ES MAYOR QUE DOS EJECUTAMOS UFLP
	  
		  JFrame uflpFrame   = new JFrame (); 
		  uflpWindow 		 = new UFLPWindow (uflpFrame,network,layers);
		  uflpWindow.requestLayerPoints();
		  uflpWindow.showWindow();
		 
		  UFLP_mode 		= false;
	 }
  }

/*****************************************************
 * 
 */    
    public void Connect ()  {
    
    		cfgbutton.setEnabled(false);
            connect.setEnabled(false);
            Login login = useerManager.LoginManagement();

            if (login.cancelClick()) {
        		cfgbutton.setEnabled(true);
                connect.setEnabled(true); //Cancel button in login. I must active the connect button again
                disconnect.setEnabled(false);
                uflp.setEnabled(false);
            } else {
            	if(network.Connect()){
	               
	            	loginTmp = login.getUserName();
	                String pwdS = login.getPassword();
	                network.sendAuthentication(loginTmp, pwdS);
	            }
            	else{
            		cfgbutton.setEnabled(true);
            		 connect.setEnabled(true); //Cancel button in login. I must active the connect button again
                     disconnect.setEnabled(false);
                     uflp.setEnabled(false);
            	}
             }
        
    }
    
 /************************************************************
  *    
  */
    
    public void DisConnect ()  {
	 	 cfgbutton.setEnabled(true);
         connect.setEnabled(true);
         disconnect.setEnabled(false);
         uflp.setEnabled(false);
         LayersTable.clean();
         userInfo.setDisconnected();
         AddLog("Client "+loginTmp+" Desconected.");
         network.DisConnect();
         map.clear();
         LayersTable.clearSelection();
    }
    
    
   /*****************************************************
     * 
     */ 
     public void authIncorrect (boolean badPwd) {
         if (badPwd){
           warningError("Client "+loginTmp+" NOT authenticated, WRONG password.", "Authentication");
           AddLog("C > Client "+loginTmp+" NOT authenticated, WRONG password.");
         }
         else {
             warningError("Client "+loginTmp+" NOT authenticated, user ONLINE yet.", "Authentication");
             AddLog("Client "+loginTmp+" NOT authenticated, user ONLINE yet.");
         }
         
         cfgbutton.setEnabled(true);
         connect.setEnabled(true);
         disconnect.setEnabled(false);
         uflp.setEnabled(false);
     }
  
    /*****************************************************
     * 
     */
    
     public void authValid (List <Layer> layers) {
        try {

            connect.setEnabled(false);
            userInfo.setId(loginTmp);
            AddLog("Client "+loginTmp+" Authenticated.");
           
            LayersTable.loadNewData(layers);
            disconnect.setEnabled(true);
            
            uflp.setEnabled(true);
           
        } catch (Exception ex) {
            Logger.getLogger(Mediator.class.getName()).log(Level.SEVERE, null, ex);
        }
         
     }
     
 
/*********************
 * Me llega un paquete de la red
 */
     public void layerDataModif (PacketModifLayerDataB packet){
      	 
    	 if(del.isSelected()) map.setModeDel();
       	 else map.setModeAdd(); 
      	 
    	 boolean isAdd    = packet.isADD();
    	 String layerName = packet.getLayerName();
    	 LayersTable.updateFeatures(layerName,isAdd);
    	 
    	 int tipo= packet.getTypeIntLayer(); //MISMO QUE ANTES PERO EN INT PARA SERIALIZAR EN EL SOCKET
    	 network.sendLayer(layerName, tipo);
     }
   

/*****************************************************
 * 
 * @param User
 */    
    
    public void connexionReset (String User){
   
     
    }
    
/************************************************************
 *     
 */

    public void  tableSelected(){
    	
    	
   	 if(del.isSelected()) map.setModeDel();
   	 else 				  map.setModeAdd(); 
   	 
   		
	   	String layerName			=	LayersTable.getLayerSelected();
	   	Feature.type tipoFeature	=	LayersTable.getTypeSelected();
	   	map.setLayerAndType(layerName,tipoFeature);
		
	   	
	   	int tipo					=	 LayersTable.getTypeIntSelected(); //MISMO QUE ANTES PERO EN INT PARA SERIALIZAR EN EL SOCKET
	   	network.sendLayer(layerName, tipo);
		
	   		
    }
  
    
  /***************************************************
   *  
   * @param ptos
   */
    public void  ptosReceive(String nameLayer,List<Punto> ptos){
    	
    	if (!UFLP_mode)
    		map.displayWaypoint(ptos);
    	else 
    		uflpWindow.receiveDatos(nameLayer, ptos);
    }
    
    
    /***************************************************
     *  
     * @param ptos
     */   
    public void  lynesReceive(List<Poly> lynes){
    	 if	(del.isSelected()) map.displayLine(lynes,true);
    	 else map.displayLine(lynes,false);
    }
    
    
  
/****************************************    
 * 
 * @param mapP
 */
    public void registerMap(MapFrame mapP){
    
    	map= mapP;
    	
    }
    
    public void registerDel(SteelCheckBox delP){
        
    	del= delP;
    	
    }   
 /*******************************************
  *    MODIFICA EL CHECK LECTURA O MODIFICACION 
  */
    public void delCheck(){
    	
     	 if(del.isSelected())
     		del.setText("Del Mode");
       	 else
       		del.setText("Add Mode");
     	 
     	
	   	Feature.type tipoFeature=LayersTable.getTypeSelected();
	   	
	   	if (tipoFeature!=Feature.type.Point) //PARA QUE NO REPINTE PTOS
	   		tableSelected(); 
	   	else{
	   		if(del.isSelected()) map.setModeDel();
	      	else map.setModeAdd(); 
	   	}
    }
/*****************************************************
 * 
 * @return
 */
    public Configuration getConfig (){
        return cfg;
    }
    
 /*****************************************************
  * 
  * @param txt
  */   
    public void AddLog (String txt){
        infolog.addLog(txt);
    }
    
    
 
/*****************************************************
  * 
  * @param msg
  * @param info
  */   
   public void severeError(String msg,String info){
         JOptionPane.showMessageDialog(contPane,msg,info,JOptionPane.ERROR_MESSAGE);
         panicMode();
    }
/*****************************************************
 * 
 * @param msg
 * @param info
 */
   public void warningError(String msg,String info){
         JOptionPane.showMessageDialog(contPane,msg,info,JOptionPane.ERROR_MESSAGE);
         
    }

/*****************************************************
 * 
 */
   public void panicMode (){
       userInfo.setDisconnected();
       connect.setEnabled(false);
       disconnect.setEnabled(false);
   } 
   
   /*****************************************************
    * 
    * @param layerName
    * @param ptoAdd
    */
   
  public void addWayPoint(String layerName,GeoPosition ptoAdd){
	  LayersTable.updateFeatures(layerName,true);
	  network.sendDataLayer(layerName, new Punto(ptoAdd.getLatitude(),ptoAdd.getLongitude()),true);
	  //MANDAR PUNTO AL SERVIDOR
  }
  
  /*****************************************************
   * 
   * @param layerName
   * @param ptoAdd
   */

  public void delWayPoint(String layerName,GeoPosition ptoAdd){
 	   
 	 if (ptoAdd!=null){
 		 LayersTable.updateFeatures(layerName,false); //Decremento la tabla
 		 network.sendDataLayer(layerName, new Punto(ptoAdd.getLatitude(),ptoAdd.getLongitude()),false);
 		
 	 }
  }
  
  /*****************************************************
   * 
   * @param layerName
   * @param tipo
   * @param listaLineWay
   */

  public void addLynePoint(String layerName,int tipo,List<GeoPosition>  listaLineWay){
	  LayersTable.updateFeatures(layerName,true);
	  
	  Poly poly = new Poly(666,listaLineWay.size());
	  poly.setPtos(aux(listaLineWay));
	  
	  network.sendDataLayer(layerName, tipo, poly,true);
  }
  
  /*****************************************************
   * 
   * @param layerName
   * @param ptos
   */

  public void delLynePoint(String layerName,Poly ptos){
	  
	  int tipo= LayersTable.getTypeIntSelected();
	  
 	 if (ptos!=null){
 		 LayersTable.updateFeatures(layerName,false); //Decremento la tabla
 		 network.sendDataLayer(layerName, tipo,  ptos,false); //DELETE
 		 
 	 }
  }
  
	public List <Punto> aux(List<GeoPosition>  pointD){
		List <Punto> point = new ArrayList<Punto>();
		
	 for (GeoPosition pos: pointD){
		 point.add(new Punto(pos.getLatitude(),pos.getLongitude()));
	 }
	 return point;
}
  
  /*****************************************************
   * 
   */

  public void Genetic(){
	  JFrame geneticFrame   		= new JFrame (); 
	  GeneticWindow geneticWindow   = new GeneticWindow (geneticFrame);
	  geneticWindow.showWindow();
	 
  }
  
  public void Configuration(){
	 
	  
	  JFrame cfgFrame   = new JFrame (); 
	  CfgWindow cfgWindow   = new CfgWindow (cfgFrame,cfg);
	  cfgWindow.showWindow();
	  cfg=cfgWindow.getConfiguracion();
	  AddLog(cfg.toString());
  }
}

