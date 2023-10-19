
package ClientFerature;


import MapFrame.Poly;
import MapFrame.Punto;
import Packet.Packet;
import Packet.PacketAuthR;
import Packet.PacketAuthS;
import Packet.PacketCNRst;
import Packet.PacketException;
import Packet.PacketLayerDataR;
import Packet.PacketLayerDataS;
import Packet.PacketModifLayerDataB;
import Packet.PacketModifLayerDataS;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Montenegro
 */
public class NetworkManager {
     String ip;
     int port;
     Mediator md;
     Client protocol;
    /*****************************************************
     * 
     * @param ipP
     * @param portP
     * @param med
     */            
 public   NetworkManager(String ipP, int portP, Mediator med) {
            ip = ipP;
            port = portP;
            md = med;
            md.registerComm(this);

            protocol = new Client(ip, port, this);
    }
 
 /**********************************************************
  * 
  */
 
 public boolean Connect (){
	 try {
		protocol.Connect();
	} catch (Exception e) {
	//	e.printStackTrace();
        networkErrorComunication ();
        return false;
	}
	 return true;
 }
/************************************
 * 
 */
 public void DisConnect (){
	 try {
		protocol.Disconnect();
	} catch (Exception e) {
		e.printStackTrace();
       // networkErrorComunication ();
	}
 }
/*****************************************************
 * 
 * @param id
 * @param pwd
 */
public void sendAuthentication (String id, String pwd){
        try {

            PacketAuthS packet = new PacketAuthS(id, pwd);
            protocol.send(packet);
        } catch (IOException ex) {
            networkErrorComunication ();
        } catch (PacketException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}


public void sendLayer (String layername, int tipo){
    try {

    	PacketLayerDataS packet = new PacketLayerDataS(layername, tipo);
        protocol.send(packet);
        
        System.out.println("CLIENTE ENVIANDO --> "+packet);
    } catch (IOException ex) {
        networkErrorComunication ();
    } catch (PacketException ex) {
        Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
    }

}


public void sendDataLayer (String layername, int tipo, Poly ptos,boolean add){

	   try {

	    	PacketModifLayerDataS packet = new PacketModifLayerDataS(layername, tipo, ptos,add);
	        protocol.send(packet);
	        
	        System.out.println("CLIENTE ENVIANDO --> "+packet);
	    } catch (IOException ex) {
	        networkErrorComunication ();
	    } catch (PacketException ex) {
	        Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
	    }
}



public void sendDataLayer (String layername, Punto pto,boolean add){
    try {

    	PacketModifLayerDataS packet = new PacketModifLayerDataS(layername, pto,add);
        protocol.send(packet);
        
        System.out.println("CLIENTE ENVIANDO --> "+packet);
    } catch (IOException ex) {
        networkErrorComunication ();
    } catch (PacketException ex) {
        Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
    }

}
 /*****************************************************
  * 
  */
public void networkErrorComunication (){
        
            String msg="Feature Server "+ip+":"+port+" Not Reachable";
            String type="Error Feature Connection.";
            md.severeError(msg, type);
   
}

/*****************************************************
 * 
 */
public void Auth (Packet packetAut){
    PacketAuthR auth= (PacketAuthR) packetAut;
    
   //System.out.println("RECIVE AUTH: "+packetAut);
   
    if( auth.isPositive() ) md.authValid ( auth.getLayers());
    else md.authIncorrect(auth.isBadPwd());
}

/******************************************************
 * 
 * @param packetLayer
 */

public void Layer (Packet packetLayer){
	PacketLayerDataR layer= (PacketLayerDataR) packetLayer;

	if (layer.tipoL==0) {
		md.ptosReceive(layer.getLayerName(),layer.getPtos());
	}
	else{
		md.lynesReceive(layer.getLynes());
	}
		
    System.out.println("RECIVE LAYER: "+layer);

}


/*****************************************************
 * 
 * @param packetAsk
 */


public void ConnexionReset  (Packet packetCNReset) {
    PacketCNRst pWPRes= (PacketCNRst) packetCNReset;
 
    md.connexionReset(pWPRes.ID);
    
}

/// BROADCAST
public void ReceiveData(Packet packetLayerData) {
	
	PacketModifLayerDataB pData = (PacketModifLayerDataB)  packetLayerData;
	md.layerDataModif(pData);
	
	  System.out.println("RECIVE LAYER: "+pData);
}

/*****************************************************
 * 
 */
  

public void Selector (Packet packetAux){
    

    if (packetAux.cmd.contentEquals(PacketAuthR.HEAD)){
        Auth(packetAux);
    }
    else if (packetAux.cmd.contentEquals(PacketLayerDataR.HEAD)){
       Layer(packetAux);
    }
    else if (packetAux.cmd.contentEquals(PacketCNRst.HEAD)){
        ConnexionReset (packetAux);
    }
    else if (packetAux.cmd.contentEquals(PacketModifLayerDataB.HEAD)){
        ReceiveData (packetAux);
    } 

}

/*****************************************************
   * 
   * 
   * @param packet
   */ 
   public void packetReceived(Object packet){
   
      // System.out.println("CLIENT RECEIVE: "+packet);
      
       Packet auxpacket = (Packet) packet;
    
       Selector (auxpacket);
  
   }
   
     


}
