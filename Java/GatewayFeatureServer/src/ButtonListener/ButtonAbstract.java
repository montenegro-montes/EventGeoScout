/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ButtonListener;


import java.awt.event.ActionListener;

import javax.swing.JButton;

import Server.Mediator;
import Server.Mediator.tipoBotones;

/**
 *
 * @author montenegro
 */
public class ButtonAbstract extends JButton implements Command{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Mediator med;
    public tipoBotones tipo;
    
    /*****************************************************
     * 
     * @param md
     * @param fr
     */
    public ButtonAbstract (Mediator md, ActionListener fr, tipoBotones tipoP){
    	
        super("Abstract");
        
        tipo=tipoP;
        
        switch (tipo){
        	case AddLayer: this.setText("Add Layer"); break;
        	case DelLayer: this.setText("Del Layer"); break;
        	case Config: this.setText("Config");break;
        	case Start: this.setText("Start"); break;
        	case Stop: this.setText("Stop");break;
        	case Users: this.setText("Users");break;
        	case Publish: this.setText("Publish");break;
        	case Import: this.setText("Import Layer");break;
        	
        }
        
      
        med=md;
        addActionListener (fr);
        med.registerButton(this);
    }
  
	/*****************************************************
     * 
     */
    public void Execute (){
           med.onClick(tipo);
    }
    
}
