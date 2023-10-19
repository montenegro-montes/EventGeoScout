
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
public class PacketLayerDataR extends Packet  {
    
    public transient final static String HEAD ="LDR"; //LAYER DATA RECEIVE
    
    
    public String layerNameL  =null; 
    public int tipoL    =0;
    List<Punto> ptosL =null;
    List<Poly> polyL =null;
    /*
    ------------------------------------------------------------------------------------
     --> 
     +LDR layer_name tipo_layer data  
     
     -----------------------------------------------------------------------------------
    */
    
    
    /********************************************************************
     * 
     * @throws Exception.PacketException
     */
    
    public PacketLayerDataR  (String layerName, List<Punto> ptos)throws PacketException{
        super (pos,HEAD);
        
        setMessage (layerName,  0, ptos,null);
    }
    
/***************************************************************
 * 
 * @param layerName
 * @param type
 * @param poly
 * @throws PacketException
 */
    public PacketLayerDataR  (String layerName, Feature.type type, List<Poly> poly)throws PacketException{
        super (pos,HEAD);
        int tipo=1;
        
        if (type==Feature.type.Polygon) tipo=2;
        
        setMessage (layerName,  tipo, null,poly);
    }

    
  /********************************************************************
   * 
   */
    
    public void setMessage(String layerName, int tipo,List<Punto> ptos,List<Poly> poly ) throws PacketException{
        
            
    			layerNameL     = layerName; 
                    tipoL      = tipo;
                    ptosL	   = ptos;
                    polyL	   = poly;
        
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
       
        if (tipoL==0){ //POINT
        	
	        if (ptosL!=null){
	        	 s.writeInt(ptosL.size());
	        	for(Punto pto:ptosL){
	        		s.writeObject(pto);
	        	}
	        }
	        else s.writeInt(0);
        }
	    else {   
	        if (polyL!=null){
	        	 s.writeInt(polyL.size());
	        	for(Poly lyne:polyL){
	        		s.writeObject(lyne);
	        	}
	        }
	        else s.writeInt(0);
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
        int size		 = s.readInt();
        
        if (tipoL==0){ //POINT
        	ptosL = new ArrayList<Punto>();
        	for (int i=0;i<size;i++){
        	 Punto pto=	(Punto) s.readObject();
        	 ptosL.add(pto);
        	}
        }
        else {
        	polyL = new ArrayList<Poly>();
        	for (int i=0;i<size;i++){
        	 Poly lyne=	(Poly) s.readObject();
        	 polyL.add(lyne);
        	}
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
    
    public List<Punto> getPtos (){
    	return ptosL;
    }
 
    public List<Poly> getLynes (){
    	return polyL;
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
