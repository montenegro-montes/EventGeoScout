/*
 */

package Buttons;

import ClientFerature.Mediator;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.border.Border;

/**
 *
 * @author Monte
 */
public class infoLog extends JTextPane{
    Mediator med;
    
    public infoLog (Mediator md){
        super();
        med=md;
        setBackground(new Color(236,235,235));
        
        
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        setBorder(loweredbevel);
        setEditable(false);
        med.registerInfoLog(this);
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

}
