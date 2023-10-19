/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import MapFrame.Feature;
import MapFrame.Layer;
import MapFrame.Poly;
import MapFrame.Punto;
import Packet.Packet;
import Packet.PacketAuthR;
import Packet.PacketAuthS;
import Packet.PacketCNRst;
import Packet.PacketLayerDataR;
import Packet.PacketLayerDataS;
import Packet.PacketException;
import Packet.PacketModifLayerDataB;
import Packet.PacketModifLayerDataS;
import Users.UserManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Montenegro
 */
public class NetworkManager {

    public final static int AUT=0;
    public final static int LDS=1;
    public final static int LMS=2;
 
    public final static int NO_VALID=-1;
    
    Socket client  = null;
    UserManager bdM;
    ObjectOutputStream oos = null;
    ObjectInputStream  ois = null;
    boolean NetworkOK = true;
    
    public NetworkManager (Socket socket, UserManager bM) {
        
            client  = socket;
            bdM     = bM;

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            bdM.NetworkConnection(socket.getInetAddress().getHostAddress());
       } catch (IOException ex) {
    	   	NetworkOK = false;
            bdM.NetworkDesconnection();
            //Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
/********************************************************************
    * 
    * @param packetAux
    */   
    
    private void write (Packet packetAux){
        try {
            oos.writeObject(packetAux);
        } catch (IOException ex) {
            bdM.NetworkDesconnection();
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
/********************************************************************
 * 
 * @return
 */        
    public Packet read (){
          Packet packetAux=null;

        try {
            packetAux = (Packet) ois.readObject();
            
            System.out.println("SERVER packet received: "+packetAux);
            
            stateMachine (packetAux);
            
        } catch (IOException ex) {
            bdM.NetworkDesconnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packetAux;
    }
     
 /********************************************************************
  * 
  */       
     
     public void stateMachine (Packet packet){
         
          
        switch (convert(packet.cmd)){
            
            case AUT:   
                        PacketAuthS packetA = (PacketAuthS) packet;
                        String user=packetA.Login;
                        String pwd=packetA.Pwd;
                        
                        bdM.Authentication(user, pwd); //LLAMA BASE DE DATOS
                        break;
            case LDS: 
                        PacketLayerDataS packetLayer = (PacketLayerDataS) packet;
                        List<Punto> ptos=null;
                        List<Poly> poly=null;
                        
                        String layerName= packetLayer.getLayerName();
                        Feature.type tipo = packetLayer.getTypeLayer();
                        
                        if (tipo==Feature.type.Point)  ptos= bdM.getPuntos(layerName);
                        else 						   poly=bdM.getPoly(layerName);
                      
                        sendLayerData(layerName,tipo,ptos,poly);
                        break;
                            
            case LMS:
            			 PacketModifLayerDataS packetDataLayer = (PacketModifLayerDataS) packet;
            			 String layerNameMod= packetDataLayer.getLayerName();
                         Feature.type tipoMod = packetDataLayer.getTypeLayer();
                         Punto ptoMod = packetDataLayer.getPunto();
                         boolean add  = packetDataLayer.isADD();
                         
                         if (tipoMod==Feature.type.Point){
                        	 bdM.ModifLayer(layerNameMod,ptoMod,add);
                         }
                        	
                         else{
                        	 Poly ptosMod = packetDataLayer.getPuntos();
                        	 bdM.ModifLayer(layerNameMod, tipoMod,ptosMod,add);
                         }
                         
            			break;
            case NO_VALID: 
                        break;
        }
                
         
     }
        
/***************************************************************
 * 
 * @param cmd
 * @return
 */     
     public int convert (String cmd){
       int  ret=NO_VALID;
        
       if (cmd.contentEquals("AUS")) ret=AUT;
            else if (cmd.contentEquals("LDS")) ret=LDS;
                else if (cmd.contentEquals("LMS")) ret=LMS;
       
       return ret;
     }
    
/***************************************************************
 * 
 */
     public void sendAuth(List<Layer> layers) {
        try {
            PacketAuthR Auth = new PacketAuthR(layers);
            write(Auth);
        } catch (PacketException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
/***************************************************************
 * 
 * @param wrongPWD
 */     
     
    public void sendErrAuth(boolean wrongPWD) {
                
        try {    
            
           String cmd;
           if (wrongPWD) cmd=PacketAuthR.WPW;
           else          cmd=PacketAuthR.ONL;
           
             PacketAuthR Auth = new PacketAuthR(cmd);
             write(Auth); 
         } catch (PacketException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /***************************************************************
     * 
     */
     public void sendLayerData(String layerName, Feature.type type,List<Punto> ptos, List<Poly> polys) {
        try {
        	PacketLayerDataR LayerData ;
        	if (type==Feature.type.Point)   LayerData = new PacketLayerDataR(layerName,ptos);
        	else 							LayerData = new PacketLayerDataR(layerName,type,polys);
        	
            write(LayerData);
        } catch (PacketException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     /***************************************************************
      * 
      */
    
    public void sendConnexionReset(String User){
        try {    
            PacketCNRst ConexionRst;
            
            ConexionRst = new PacketCNRst (User);
            
            write(ConexionRst); 
            
         } catch (PacketException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    


 public void sendDataBroadCastLayer (String layername,int tipo,boolean add){
     try {

     	PacketModifLayerDataB packet = new PacketModifLayerDataB(layername,tipo,add);
     	write(packet);
	             
         System.out.println("SERVER ENVIANDO --> "+packet);
     } catch (PacketException ex) {
         Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
     }

 }
 
 public boolean isNetworkOK(){
	 return NetworkOK;
 }
  
}
