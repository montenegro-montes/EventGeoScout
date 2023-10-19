/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Users;



import java.math.BigInteger;
import java.util.Vector;

import MapFrame.Punto;



/**
 *
 * @author Montenegro
 */
public class PoolThreadUsers {

   
   private Vector pool;
   
    public PoolThreadUsers (){
        
        pool =  new Vector ();
    
    }
            
  /*****************************************************
   * NEW CLIENT
   * @param cl
   */  
    
     public void addClients(UserManager cl){
        
            pool.add(cl);     
    }
     
 /*****************************************************
  * GET A CLIENT USING ID
  * @param id
  * @return
  */    
     
     public UserManager getClients(String id){
        
           UserManager clAux=null;
         
                 int num=pool.size();

                 for (int i=0;i<num;i++){
                      clAux=(UserManager)pool.get(i);
                     if (clAux.isAuthenticated())
                          if (clAux.getUser().contentEquals(id)) break;
                  }
           
          return clAux;
    }
    
 


    public void broadCastConnexionReset(String User){
          int num=pool.size();
          UserManager clAux=null;

              for (int i=0;i<num;i++){
                  clAux=(UserManager)pool.get(i);

                 if (clAux.isAuthenticated() & clAux.isNetworkConnected())
                      clAux.sendConnexionReset(User);
                  
              }
    }
    

    public void sendBroadCastModif(String layerName,int tipo,boolean add, String userL){
 	   
          int num=pool.size();
          UserManager clAux=null;

              for (int i=0;i<num;i++){
                  clAux=(UserManager)pool.get(i);

                  if (clAux.isAuthenticated() & clAux.isNetworkConnected())
                	  if (clAux.getUser().compareTo(userL)!=0){
                		
                          clAux.sendDataBroadCastLayer(layerName,tipo,add);
                	  }
                	    
              }
          
          
    }


     
}
