/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Packet;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author montenegro
 */
public class Packet implements Serializable {
    public transient static final char pos='+';
    public transient static final char neg='-';
    
     char   code;
     public  String cmd;
    
    
    transient PacketException nEx;
  
    /********************************************************************
     * 
     * @param codeP
     * @param cmdP
     */
    
   public Packet (char codeP,String cmdP){//, String dataP){
       code=codeP;  
       cmd=cmdP;
        
    }
   
   
/********************************************************************
 * 
 * @param s
 * @throws java.io.IOException
 */

    
    protected void writeObject (ObjectOutputStream s) throws IOException{
    
        s.defaultWriteObject();
        s.writeChar(code);
        s.writeUTF(cmd);
        
        
    }
   
/********************************************************************
 * 
 * @param s
 * @throws java.io.IOException
 * @throws java.lang.ClassNotFoundException
 */
   
    protected void readObject (ObjectInputStream s) throws IOException, 
                                                    ClassNotFoundException{
    
        s.defaultReadObject();
        
        code =s.readChar(); 
        cmd  =s.readUTF();        
        
    }
    
    
/********************************************************************
 * 
 * @return
 */   
    @Override

    public String toString(){
        
        return code+cmd;
    }
    
    public boolean isPositive (){
        return code==pos;
    }
    
}
