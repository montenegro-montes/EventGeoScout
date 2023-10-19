package Users;


import java.io.IOException;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.Socket;

import Server.Mediator;
import Server.NetworkManager;

/**
 *
 * @author Montenegro
 */
public class ThreadUsers extends Thread{
    
    UserManager    userManager;
    Mediator       med;
    NetworkManager netManager;
    boolean        workDone=false;
    
    public ThreadUsers (Mediator md,Socket client){
        med=md;
        userManager = new UserManager  (md);
        netManager = new NetworkManager (client,userManager);
        
        if (netManager.isNetworkOK()){
			userManager.registerThread(this);
				userManager.registerNetManager(netManager);
        }
    }
   
/************************************
 *     
 */
    public void run() {           
    	 if (netManager.isNetworkOK()){
	    	med.addElementPool(userManager);
	
	       while (!workDone){ 
	         if (!workDone) netManager.read();
	       }
    	 }
    	 else workDone=true;
    }
    
   /**********************************************
    * 
    */ 
    public void stopThread() {           
        workDone=true;
    }
}
