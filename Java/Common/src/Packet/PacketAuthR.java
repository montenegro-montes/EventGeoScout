
package Packet;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import MapFrame.Feature;
import MapFrame.Layer;


/**
 *
 * @author montenegro
 */
public class PacketAuthR extends Packet  {
    
    public transient final static String HEAD ="AUR"; //AUTH RECV
    
    
    public transient final static String OKA ="OKA";
    public transient final static String WPW ="WPW";
    public transient final static String ONL ="ONL";
    
  //  transient final static long NULL =-1;

    public String Option   =null; 
    List<Layer> layerL;
    /*
    ------------------------------------------------------------------------------------
     --> 
     +AUS  login@password   
     
     <--
     +AUR OKA lefTime#targetTime MaxBid,MinBid,StepBid,nBulletin,ID
     -AUR WPW
     -AUR ONL
     * 
     * OPTIONS OKA -- Authentication OK
       OPTIONS WPW -- Wrong password
       OPTIONS ONL -- User online yet
    ------------------------------------------------------------------------------------
    */
    
    
    /********************************************************************
     * 
     * @throws Exception.PacketException
     */
    
    public PacketAuthR  (List<Layer> layers)throws PacketException{
        super (pos,HEAD);
        
        setMessage (OKA,layers);
    }

    
    public PacketAuthR  (String OptionP)throws PacketException{
        super (neg,HEAD);
        
    
        setMessage (OptionP, null);
        
    }
    
    
  /********************************************************************
   * 
   * 
   * 
   * @param OptionP
   * @param leftTimeP
   * @param targetTimeP
   * @param MaxBidP
   * @param MinBidP
   * @param StepBidP
   * @param NBulletinP
   * @param AuctionIDP
   */
    
    public void setMessage(String OptionP,List<Layer> layers) throws PacketException{
        
        if  (!OptionP.contentEquals(OKA)) 
            if (!OptionP.contentEquals(WPW) && !OptionP.contentEquals(ONL) ){
                nEx=new PacketException (OptionP);
                nEx.setDescription("Message is void.");
                throw nEx;
            }
            else {
                    Option     = OptionP; 
                    code=neg;     //code neg
            }
        else{
            
                    Option     = OptionP; 
                    layerL    = layers;
                   
        }
    }
           
    /********************************************************************
     * 
     * @param s
     * @throws java.io.IOException
     */
    
     protected void writeObject (ObjectOutputStream s) throws IOException{
        super.writeObject(s);
        int length= layerL.size();
        
        s.writeUTF(Option);
        s.writeInt(length);
        
        if (length>0){
       	   
       	   for (Layer layer : layerL) {
       		  
       		   int size= layer.getNelements();
       		   int layerType= layer.getTipo();
       		   String layerName=layer.getName();
       		   
       		   s.writeInt(size);
    		   s.writeInt(layerType);
    		   s.writeUTF(layerName);
    
       		}
       	  
          }
 
    }
    /********************************************************************
     * 
     * @param s
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
     
    protected void readObject (ObjectInputStream s) throws IOException, 
                                                    ClassNotFoundException{
        super.readObject(s);

        Option      = s.readUTF();     
        int numLayer = s.readInt();
        
        if (numLayer>0){
        	layerL = new ArrayList<Layer>();
        	
        	for (int i=0;i<numLayer;i++){
        		int size = s.readInt();
        		int layerType =		 s.readInt();
        		String layerName   = s.readUTF();
        		
        		
	       		Layer newlayer = new Layer(layerName, layerType,size);
	       		
	       		layerL.add(newlayer);
        	}
        }
        
 	 
	
    }
/********************************************************************
 * 
 * @return
 */

    @Override
    public String toString(){
        String msg= "<--";
        
        msg=msg.concat(super.toString());
   
        msg=msg.concat(" "+Option);
        
        if (layerL!=null){
        	int length= layerL.size();
        	msg=msg.concat(" "+length);
        	
        	if (length>0){
        	   
        	   for (Layer layer : layerL) {
        		  
        		   int size= layer.getNelements();
        		   int layerType= layer.getTipo();
        		   String layerName=layer.getName();
        		   
        		   
        		   msg=msg.concat(" "+layerName+" "+layerType+" "+size+" ");
        
        	
        		}
        	}
        }
        return msg;
    }
    /***************************************
     * 
     * @return
     */
   public boolean isBadPwd(){
       return Option.contentEquals(WPW);
   }
   
   public int LayerSize(){
	   
	   if (this.layerL!=null) return layerL.size();
	   else return 0;
   }

   public List<Layer> getLayers(){
	   return layerL;
   }
}
