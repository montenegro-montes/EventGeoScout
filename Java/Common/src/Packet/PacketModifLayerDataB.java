
package Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import MapFrame.Feature;
import MapFrame.Poly;
import MapFrame.Punto;

/**
 *
 * @author montenegro
 */
public class PacketModifLayerDataB extends Packet  {
    
    public transient final static String HEAD ="LMB"; //LAYER MODIF SEND
    
    
    public String layerNameL  =null; 
    public int tipoL    =2;

    public boolean add=true; //PARA SABER SI ES ADD O DELETE
    
    /*
    ------------------------------------------------------------------------------------
     --> 
     +LDS layer_name tipo_layer   
     
     -----------------------------------------------------------------------------------
    */
    
    
    /********************************************************************
     * 
     * @throws Exception.PacketException
     */
    
    
    public PacketModifLayerDataB  (String layerName, int tipo, boolean addP)throws PacketException{
        super (pos,HEAD);
        add=addP;
        setMessage (layerName,tipo);  //ESTO SE UTILIZA SOLO PARA BROADCAST
    }
    
    
    
  /********************************************************************
   * 
   */
    
   
    
    
    public void setMessage(String layerName,int tipo) throws PacketException{
        
        
  		layerNameL     = layerName; 
        tipoL =tipo;

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
        s.writeBoolean(add);
        
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
        tipoL 			= s.readInt();
         add			= s.readBoolean();
        
        
        
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
    
    public boolean isADD(){
    	return add;
    }
}
