package ClientFerature;


/*
 * Configuration.java
 * 
 * Created on 13-jun-2007, 14:58:19
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Monte
 */
public class Configuration {

    private int Port=8000;
    private String Server="127.0.0.1";
   
    
    
    public Configuration(String Serverp,int Portp ) {
      
    	Port=Portp;
    	Server=Serverp;

    }
    
    
    public int getPort ()    {     return  Port;    }
    public void setPort (int x) { Port=x;}
  
    public void setServerPort (int x) {Port=x;}
    public int getGeoServerPort () {return Port;}
  
    public void setServer (String x) {Server=x;}
    public String getServer () {return Server;}
  
   
    public String toString(){
        return "Client configuration::> Server ip: "+Server+" Server port: "+Port;
    }
    

  }
