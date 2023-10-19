
package Packet;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author montenegro
 */
public class PacketCNRst extends Packet  {
    
    public transient final static String HEAD ="CNR"; //CONEXION RESET
    
    
   public String ID;
    
    
    /********************************************************************
     * 
     * @throws Exception.PacketException
     */
    
    public PacketCNRst  (String user)throws PacketException{
        super (pos,HEAD);
        
        ID= user;
                    
        
    }

    
    
    
    /********************************************************************
     * 
     * @param s
     * @throws java.io.IOException
     */
    
     protected void writeObject (ObjectOutputStream s) throws IOException{
        super.writeObject(s);
        
        s.writeUTF(ID);
        
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

        ID=s.readUTF();
       
    }
/********************************************************************
 * 
 * @return
 */

    @Override
    public String toString(){
        
        
        String msg= "-->";
        
        msg=msg.concat(super.toString());
        
        msg=msg.concat(" "+ID);
        
        return msg;
    }
    
  
}
