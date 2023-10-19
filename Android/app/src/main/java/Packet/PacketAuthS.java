
package Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author montenegro
 */
public class PacketAuthS extends Packet  {
    
    transient final static String HEAD ="AUS"; //AUTH RECV
    
    
    public transient final static String BAS ="BAS";  //BASIC AUTHENTICATION
    
    public String Option   =null; 
    public String Login    =null; 
    public String Pwd      =null; 
    /*
    ------------------------------------------------------------------------------------
     --> 
     +AUS TYPE login@password   //NOW ONLY BASIC
     
     -----------------------------------------------------------------------------------
    */
    
    
    /********************************************************************
     * 
     * @throws Exception.PacketException
     */
    
    public PacketAuthS  (String loginP, String pwdP
                           )throws PacketException{
        super (pos,HEAD);
        
        setMessage (BAS, loginP, pwdP);
    }

    
  /********************************************************************
   * 
   */
    
    public void setMessage(String OptionP, String loginP, String pwdP
                          ) throws PacketException{
        
            
                    Option     = OptionP; 
                    Login      = loginP;
                    Pwd        = pwdP;
        
    }
           
    /********************************************************************
     * 
     * @param s
     * @throws java.io.IOException
     */
    
     protected void writeObject (ObjectOutputStream s) throws IOException{
        super.writeObject(s);
        
        s.writeUTF(Option);
        s.writeUTF(Login);
        s.writeUTF(Pwd);
        
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
        Login       = s.readUTF();        
        Pwd         = s.readUTF();        
        
    }
/********************************************************************
 * 
 * @return
 */

    @Override
    public String toString(){
        
        
        String msg= "-->";
        
        msg=msg.concat(super.toString());
        
        msg=msg.concat(" "+Option+" "+Login+""+Pwd);
        
        return msg;
    }
    
  
}
