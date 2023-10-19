/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Buttons;

import ClientFerature.Mediator;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 *
 * @author montenegro
 */
public class ConnectButton extends JButton implements Command{

    Mediator med;
    /*****************************************************
     * 
     * @param md
     * @param fr
     */
    public ConnectButton (Mediator md, ActionListener fr){
        super("Connect");
        med=md;
        addActionListener (fr);
        med.register(this);
    }
    /*****************************************************
     * 
     */
    public void Execute (){
           med.Connect();
    }
    
}
