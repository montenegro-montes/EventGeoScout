
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
public class PacketModifLayerDataS extends Packet  {
    
    public transient final static String HEAD ="LMS"; //LAYER MODIF SEND
    
    
    public String layerNameL  =null; 
    public int tipoL    =0;
    public Punto ptoL =null;
    public Poly polyL =null;
    
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
    
  
    
    public PacketModifLayerDataS  (String layerName, Punto pto,boolean addP)throws PacketException{
        super (pos,HEAD);
        add=addP;
        setMessage (layerName,pto);
    }

 
    public PacketModifLayerDataS  (String layerName, int tipo, Poly ptos,boolean addP)throws PacketException{
        super (pos,HEAD);
        add=addP;
        setMessage (layerName, tipo,ptos);
    }
    
  /********************************************************************
   * 
   */
    
    public void setMessage(String layerName, Punto pto ) throws PacketException{
        
            
    			layerNameL     = layerName; 
                    tipoL      = 0;
                    ptoL	   = pto;
        
    }
 
    public void setMessage(String layerName,int tipo, Poly poly ) throws PacketException{
        
        
		layerNameL     = layerName; 
            tipoL      = tipo;
            polyL	   = poly;

    }
    
    
    public void setMessage(String layerName) throws PacketException{
        
        
  		layerNameL     = layerName; 
            

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
        if (tipoL==0){
        	s.writeObject(ptoL);
        }
        else {
        	s.writeObject(polyL);
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

        layerNameL      = s.readUTF();        
        tipoL      		 = s.readInt();
        add				= s.readBoolean();
        
        if (tipoL==0)
        	ptoL = (Punto) s.readObject();
        else {
        	polyL = (Poly) s.readObject();
        }
        
        
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
    
    public Punto getPunto (){
    	return ptoL;
    }
    
    public Poly getPuntos (){
    	return polyL;
    }
    
    public boolean isADD(){
    	return add;
    }
}
