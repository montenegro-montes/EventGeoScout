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
public class DisConnectButton extends JButton implements Command{

    Mediator med;
    /*****************************************************
     * 
     * @param md
     * @param fr
     */
    public DisConnectButton (Mediator md, ActionListener fr){
        super("DisConnect");
        med=md;
        addActionListener (fr);
       // med.registerDisConnect(this);
        med.register(this);
    }
    /*****************************************************
     * 
     */
    public void Execute (){
           med.DisConnect();
    }
    
}
