/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Buttons;


import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import ClientFerature.Mediator;

/**
 *
 * @author montenegro
 */
public class UserLabel extends JLabel {
    Mediator med;
    
    
    /**1*******************************************************
     * 
     * @param md
     */
    public UserLabel (Mediator md){
        super ("Not connected");
        med=md;
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.RED);
         med.registerUserInfo(this);
    }
    
    /*********************************************************
     * 
     * @param id
     */
    public void setId (String id){
        setText(id);
        setForeground(Color.GREEN); // Show the user and put the GREEN label 
    }
    
     /*********************************************************
      * 
      */
    public void setDisconnected (){
        setText ("Not connected");
        setForeground(Color.RED); // Show the user and put the GREEN label 
    }
}
