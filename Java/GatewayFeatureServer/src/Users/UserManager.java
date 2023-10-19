/*
 */

package Users;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import MapFrame.Feature;
import MapFrame.Layer;
import MapFrame.Poly;
import MapFrame.Punto;
import MapFrame.Feature.type;
import Server.Mediator;
import Server.NetworkManager;

/**
 *
 * @author Monte
 */
public class UserManager {

   
    Mediator        med;
    

    ThreadUsers   thread    =null;
  
    private String          addressClient   ="Unkown";
    private NetworkManager  netManager      = null;
  
    boolean isconnected      =  false;  //Network Connection
    String userL 	 =  "Unkown"; 
    boolean isAuthticated  = false;
    
    /**********************************************
     * 
     * 
     * @param medP
     */
    public UserManager (Mediator medP){

         med=medP;
         
    }
    
    /**********************************************
     * 
     * @param netMag
     */
       
      public void registerNetManager(NetworkManager netMag){
          netManager= netMag;
      }
    
    /**********************************************
     * 
     */
    public void NetworkConnection (String address) {
        isconnected     = true;
        addressClient   = address;
        med.AddLog("C->S: Client connected from "+ addressClient+" not Authenticated");
    }
    
    /**********************************************
     * 
     */
    public void NetworkDesconnection (){
        
        	if (thread!=null) thread.stopThread();
        	isconnected     = false;
            med.disconnected(userL,addressClient);  
        
     }
    
    /**********************************************
     * 
     * @return
     */
    
    public boolean isNetworkConnected (){
            return isconnected;
    }
  /************************************************
   * 
   * @return
   */
    public boolean isAuthenticated (){
        return isAuthticated;
    }
    
    /**********************************************
     * 
     * @param user
     * @param pwd
     */
    
    public void Authentication (String user,String pwd){
        int result;
        
  
            result=med.isAuthenticated(user, pwd);

            switch (result){

                    case 1:   
                           med.AddLog("C->S: User "+user+" from "+ addressClient+" Authenticated");
                           List<Layer> layers =med.getLayers();
                           netManager.sendAuth(layers); // MANDAR NUMEROS DE ELEMENTOS QUE HAY EN LA TABLA
                           userL=user; //GUARDO EL USUARIO.
                           isAuthticated=true;
                           break;

                    case 0: //ERROR AUTHENTICATION
                            med.AddLog("C->S: User "+user+" from "+ addressClient+" WRONG password");
                            netManager.sendErrAuth(true);
                            break;

                   case -1: //USER ONLINE       
                            med.AddLog("C->S: User "+user+" from "+ addressClient+" ONLINE");
                            netManager.sendErrAuth(false);
                            break;
            }
                     
    }
  
    /// MODIF ESTE ES EL QUE CONTROLA SI AÑADO O ELIMINO Y QUE TIPO  
    
    public void ModifLayer(String layerName,Punto pto,boolean add){
 	
 	   					GeoPosition ptoAdd =   new GeoPosition(pto.getLatitude(), pto.getLongitude());
 	   			if(add)		
 	   					med.addWayPoint( layerName, ptoAdd);
 	   			else		
 	   					med.delWayPoint(layerName, ptoAdd);
 	
 	   			//ToDo Broadcast LAYER
 	   		med.sendBroadCastModif(layerName,0, add, userL);
   }
    
    
   public void ModifLayer(String layerName,Feature.type tipo, Poly poly,boolean add){
	   int type=1;
	   if (tipo==Feature.type.Polygon) type=2;
	   					
	   List<GeoPosition> ptosAdd = new ArrayList<GeoPosition>();
			for (Punto punto: poly.getPtos()){
				ptosAdd.add(new GeoPosition(punto.getLatitude(), punto.getLongitude()));
			}
			if(add)	
				med.addLynePoint(layerName, ptosAdd);	  
			else
				med.delLynePoint(layerName, poly);	  
	   			
			med.sendBroadCastModif(layerName,type, add, userL);
  }
   
 /**********************************
  * FUNCIONES PARA ENVIAR EN REMOTO 
  * @param layerName
  * @return
  */
   
  public List<Punto> getPuntos (String layerName){
	  return med.getPuntos(layerName);
					
				
  } 
    
  public List<Poly> getPoly (String layerName){
	  return med.getPoly(layerName);			
  }  
    


    public void sendConnexionReset (String User){
        netManager.sendConnexionReset(User);
    }

 
    public void registerThread(ThreadUsers threadBidder){
        thread= threadBidder;
        
    }
  
   public String getUser(){
	   return userL;
   }
    
   public void sendDataBroadCastLayer(String layername,int tipo,boolean add){
       netManager.sendDataBroadCastLayer(layername,tipo,add);
   }
}