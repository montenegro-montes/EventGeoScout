
package Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import MapFrame.Feature;

/**
 *
 * @author montenegro
 */
public class PacketLayerDataS extends Packet  {
    
    transient final static String HEAD ="LDS"; //LAYER DATA SEND
    
    
    public String layerNameL  =null; 
    public int tipoL    =0;
    /*
    ------------------------------------------------------------------------------------
     --> 
     +LDS layer_name tipo_layer   //NOW ONLY BASIC
     
     -----------------------------------------------------------------------------------
    */
    
    
    /********************************************************************
     * 
     * @throws Exception.PacketException
     */
    
    public PacketLayerDataS  (String layerName, int tipo)throws PacketException{
        super (pos,HEAD);
        
        setMessage (layerName,  tipo);
    }

    
  /********************************************************************
   * 
   */
    
    public void setMessage(String layerName, int tipo) throws PacketException{
        
            
    			layerNameL     = layerName; 
                    tipoL      = tipo;
        
    }
           
    /********************************************************************
     * 
     * @param s
     * @throws java.io.IOException
     */
    
     protected void writeObject (ObjectOutputStream s) throws IOException{
        super.writeObject(s);
        
        s.writeUTF(layerNameL);
        s.writeInt(tipoL);
        
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

        layerNameL      = s.readUTF();        
        tipoL      		 = s.readInt();        
        
    }
/********************************************************************
 * 
 * @return
 */

    @Override
    public String toString(){
        
        
        String msg= "-->";
        
        msg=msg.concat(super.toString());
        
        msg=msg.concat(" "+layerNameL+" "+tipoL);
        
        return msg;
    }
    
    
    public String getLayerName (){
    	return layerNameL;
    }
  
    public int getTypeIntLayer (){
    	return tipoL;
    }
    
    
    public Feature.type getTypeLayer (){
    	switch(tipoL){
    		case 0: return Feature.type.Point;
    		case 1: return Feature.type.LineString;
    		case 2: return Feature.type.Polygon;
    	}
    	
    	return Feature.type.Point;
    }
}
