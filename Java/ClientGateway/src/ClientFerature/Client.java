/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientFerature;

import Packet.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

      ObjectOutputStream oos = null;
      ObjectInputStream ois = null;
      Socket socket = null;
        
        String ip;
        int port;
        NetworkManager network;
       boolean online;
       Connect recieveThread;
/***************************************************************
 * 
 * 
 * @param ipP
 * @param portP
 * @param networkp
 */                
 public   Client(String ipP, int portP, NetworkManager networkp) {
   
        ip=ipP;
        port=portP;
        network=networkp;

        
    }
    
/***************************************************************
 * 
 * @throws java.net.UnknownHostException
 * @throws java.io.IOException
 */    
   public void Connect () throws UnknownHostException, IOException {
      
        // open a socket connection
        socket = new Socket(ip, port);
        // open I/O streams for objects
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        online =true;
        
        recieveThread = new Connect(ois, network);
        recieveThread.start();
   }    
   
   public void Disconnect() throws IOException{
	   recieveThread.Disconnect();
	   close();
	   socket.close();
   }
/***************************************************************
 * 
 * @param packet
 * @throws java.io.IOException
 */   
   public void send (Packet packet) throws IOException {
         oos.writeObject(packet);
   }
   
  /***************************************************************
   * 
   * @throws java.io.IOException
   */      
    public void close () throws IOException{
        oos.close();
        ois.close();
    }
  
    /***************************************************************
     * 
     */
 class Connect extends Thread {
   
   private ObjectInputStream ois = null;
    NetworkManager network;
    
   /***************************************************************
    * 
    * @param oisP
    * @param networkP
    */

   public Connect(ObjectInputStream oisP, NetworkManager networkP) {
   
       network=networkP;
       ois=oisP;
       
       
   }

   public void Disconnect(){
	   online= false;
	   interrupt();
   }
/***************************************************************
 * 
 */
  
   public void run()  {
   
     while (online){  
                try {

                    Object packet = ois.readObject();
                    network.packetReceived(packet);
                } catch (IOException ex) {
                    if (online) network.networkErrorComunication();
                    break;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
     }

    }
  
 }
} 