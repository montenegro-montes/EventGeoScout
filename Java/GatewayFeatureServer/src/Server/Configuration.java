package Server;


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
    private String GeoServer="127.0.0.1";
    private int GeoserverPort=8080;
    private String   userGeoValue  ="admin";   
    private String   pwdGeoValue  ="1qaz2wsx";   
   
    
    
    public Configuration(int Portp, String GeoServerp, int GeoserverPortp, String user, String pwd) {
      
    	Port=Portp;
    	GeoServer=GeoServerp;
    	GeoserverPort=GeoserverPortp;  
    	userGeoValue= user;
    	pwdGeoValue =pwd;
    }
    
    
    public int getPort ()    {     return  Port;    }
    public void setPort (int x) { Port=x;}
  
    public void setGeoServerPort (int x) {GeoserverPort=x;}
    public int getGeoServerPort () {return GeoserverPort;}
  
    public void setGeoServer (String x) {GeoServer=x;}
    public String getGeoServer () {return GeoServer;}
  
    public String getUser ()    {     return  userGeoValue;    }
    public String getPwd ()     {     return  pwdGeoValue;    }
    
    public String toString(){
        return "Cfg => Port: "+Port+" Geo Server: "+GeoServer+" Geo Server Port: "+GeoserverPort;
    }
    
  

  }
