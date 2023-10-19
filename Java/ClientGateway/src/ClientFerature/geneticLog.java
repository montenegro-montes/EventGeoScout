/*
 */

package ClientFerature;


import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.border.Border;


/**
 *
 * @author Monte
 */
public class geneticLog extends JTextPane{
    
    public geneticLog (){
        super();
        //setBackground(new Color(236,235,235));
        
        
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        setBorder(loweredbevel);
        setEditable(false);
    }
    
    /**********************************************************
     * 
     * @param text
     */
    
    public void addLog (String text) {
             String temp;
             
             temp= getText();     
             
             if (temp.length()==0) setText(text);
             else setText(temp+"\n"+text);
          
             setCaretPosition(getDocument().getLength());
             
    }
    
    public void addLog_S (String text) {
        String temp;
        
        temp= getText();     
        
        if (temp.length()==0) setText(text);
        else setText(temp+" "+text);
     
        setCaretPosition(getDocument().getLength());
        
}
    
     public void clear () {
             setText("");
          
             setCaretPosition(getDocument().getLength());
             
        }

}
