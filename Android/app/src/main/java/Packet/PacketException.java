/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Packet;

/**
 *
 * @author montenegro
 */
public class PacketException extends Exception {

    String msg;
    String description;
    
        public PacketException(String message) {
             super(message);
             msg=message;    
             
         }
        
        public void setDescription(String descriptionP){
            description=descriptionP;
        };
        
        
        
}

