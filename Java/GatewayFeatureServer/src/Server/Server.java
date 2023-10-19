/*
 * Server.java
 * 
 * Created on Jun 6, 2007, 12:12:02 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Server;





import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import Users.ThreadUsers;



/**
 *
 * @author montenegro
 */
public class Server extends Thread{

    private ServerSocket listenSocket;
    
    private Configuration cfg=null;     /* CONFIGURATION INSTANCE */
    
    private boolean jobDone=false;
    

    Mediator med;
    
    public Server(Mediator md,Configuration configuration){
        cfg=configuration;
        med=md;
        ini();
        
    }
    
/**********************************
 * 
 * 
 * 
 * ********************************************/
    
   private void ini() {
       

           int serverPort=cfg.getPort();
           
           try{
                listenSocket=new ServerSocket(serverPort);
           }
            catch(Exception s){ 
               System.out.println("S: Error try to listen port: "+serverPort);
            }
           
           String localIp=null;
           try{
            InetAddress local = InetAddress.getLocalHost();
            localIp=local.getHostAddress();
           }catch(IOException s){ 
               localIp= new String ("Unknow"); 
           }
           
         med.AddLog("S: Feature Server ready to listen on: "+localIp+":"+serverPort);

    }
/**********************************
 * MAIN procedure of Server Bulletin
 * 
 * 
 * ********************************************/
   
    public void run() {           

        bidderStage();

    }
    
/**********************************
 * bidderStage:
 *      Bidder Stage
 * 
 * 
 * ********************************************/
    
    public void bidderStage() {           
           
           while(!jobDone){   //MAIN 
            

                if (listenSocket==null) {
                    int serverPort=cfg.getPort();
                    JOptionPane.showMessageDialog(null," This program needs the port "+serverPort+" to act as server.","Server Error",JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
                
                Socket clientSocket=null;

                    try{
                        clientSocket= listenSocket.accept(); 
                    }
                    catch(IOException s){ // COMPARE EXCEPTION WITH TIMEOUT
                        break;
                    }
            

                    ThreadUsers  cl =new ThreadUsers (med,clientSocket);
                    cl.start();
 
           }
    }

  
    /**********************************
     *  stopServer.
     * 
     * Stop the server
     * 
     * ************************************* 
     */
    
    public void stopServer(){
    
        jobDone=true;
        try {
            listenSocket.close();
        }
        catch (Exception e){
//           dialog.AddLog(e.toString());
        }
        
        
    }
    
  

}
