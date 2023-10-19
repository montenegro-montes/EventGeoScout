/*
 * CircuitWindow.java
 * 
 * Created on 03-dic-2007, 23:45:54
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Visual;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;

import Server.*;



/**
 *
 * @author Monte
 */

public class CfgWindow extends JDialog  implements ActionListener {

    private JTextField port;
    private JTextField serverGeo,serverPGeo;
    private JTextField userGeo;
    private JPasswordField pwdGeo;
    
      
    JTable tableV;
 
    
    private int      Port=8000;
    private int      GeoPort=8080;
    private String   GeoIP  ="127.0.0.1";
    private String   userGeoValue  ="admin";   
    private String   pwdGeoValue  ="1qaz2wsx";   
    
    private Configuration 	cfg  = new Configuration(Port,GeoIP, GeoPort,userGeoValue,pwdGeoValue);
    
    Mediator md;
/*************************************************************
 * GENERAL CONSTRUCTOR
 * 
 * 
 * 
 * ***********************************************************/
    
    public CfgWindow (JFrame frame){
    
              super(frame, "Configuration Values", true);
              
             
              setLocation(10, 20);
               
              setLayout (null);
              setResizable(false);
              setPreferredSize(new Dimension(350,310));
              
                  
        //CFG
        JPanel serverPane = new JPanel(null);
        JPanel serverGeoPane = new JPanel(null);
        
        
        
        //PORT
        JLabel portL = new JLabel("Server Port: ");
        portL.setForeground(Color.GRAY);
        port = new JTextField();
        port.setText(Port+"");
        portL.setBounds(10, 20, 130, 40);
        port.setBounds(140, 30, 100, 20);
        serverPane.add(portL); serverPane.add(port);
        //END PORT
        
        serverPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Local Servers"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        add(serverPane);
        serverPane.setBounds(20, 15, 300, 65);
        
        
        JLabel serverGeoL = new JLabel("Geo Server: ");
        serverGeoL.setForeground(Color.GRAY);
        serverGeo = new JTextField();
        serverGeo.setText(GeoIP);
        serverGeoL.setBounds(10, 10, 180, 40);
        serverGeo.setBounds(140, 20, 100, 20);
        serverGeoPane.add(serverGeoL); serverGeoPane.add(serverGeo);
        
        JLabel serverGeoPL = new JLabel("Geo Server Port: ");
        serverGeoPL.setForeground(Color.GRAY);
        serverPGeo = new JTextField();
        serverPGeo.setText(""+GeoPort);
        serverGeoPL.setBounds(10, 40, 180, 40);
        serverPGeo.setBounds(140, 50, 100, 20);
        serverGeoPane.add(serverGeoPL); serverGeoPane.add(serverPGeo);

        JLabel userLabel = new JLabel("User: ");
        userLabel.setForeground(Color.GRAY);
        userGeo = new JTextField();
        userGeo.setText(""+userGeoValue);
        userLabel.setBounds(10, 70, 180, 40);
        userGeo.setBounds(140, 80, 100, 20);
        serverGeoPane.add(userLabel); serverGeoPane.add(userGeo);

        JLabel userPswd = new JLabel("Pwd: ");
        userPswd.setForeground(Color.GRAY);
        pwdGeo = new JPasswordField();
        pwdGeo.setText(pwdGeoValue);
      
        userPswd.setBounds(10, 100, 180, 40);
        pwdGeo.setBounds(140, 110, 100, 20);
        serverGeoPane.add(userPswd); serverGeoPane.add(pwdGeo);

        
        
        serverGeoPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Geo Server"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        add(serverGeoPane);
        serverGeoPane.setBounds(20, 85, 300, 150);
        
        //END CFG
                
        ///////////////////////////////////////////
        JButton SaveButton = new JButton("Save");
        SaveButton.setBounds(120, 245, 100, 25);    
        SaveButton.addActionListener (this);
        add(SaveButton);
        ///////////////////////////////////////////
    

        pack();
	
   }

    /**********************************************
   * showWindow:  
   * 
   * ********************************************/
  
  public void showWindow () {
  
    setVisible(true);
  }
  
  /**********************************************
   * actionPerformed:  Treat events
   * 
   * ********************************************/
  
  public void actionPerformed (ActionEvent event) {

    String cmd = event.getActionCommand ();
    
    if ( cmd.equals ("Save")) Save();
      
  }
  
 ///////////////////////////////////////////
 ///////////////////////////////////////////
  
 private void Save (){
 
	 	///PUERTO  
        if (port.getText().length()==0){ Error("     Port field empty."); return;} else {
            try {
                Port= Integer.parseInt(this.port.getText().trim());
            }catch (NumberFormatException nfe){
                Error("Port field must be a number."); return;
            }
        }
        
        //GEOSERVER
        if (serverGeo.getText().length()==0){ Error("     GeoServer field empty."); return;} else {
            	GeoIP= serverGeo.getText().trim();
        }
        
        //GEOSERVER PORT   
        if (serverPGeo.getText().length()==0){ Error("     GeoServer Port field empty."); return;} else {
            try {
            	GeoPort= Integer.parseInt(serverPGeo.getText().trim());
            }catch (NumberFormatException nfe){
                Error("Port field must be a number."); return;
            }
        }
        
        
        //USER 
        if (userGeo.getText().length()==0){ Error("     GeoServer User field empty."); return;} else {
            	userGeoValue= userGeo.getText().trim();
        }
        
        //PWD 
        if ((pwdGeo.getPassword()).length==0){ Error("     GeoServer PWD field empty."); return;} else {
            	pwdGeoValue= pwdGeo.getPassword().toString();
        }
        
        
        cfg = new Configuration(Port,GeoIP, GeoPort,userGeoValue,pwdGeoValue);
             
        dispose();
 }
  
///////////////////////////////////////////

 private void Error(String msg) {
        
        JOptionPane.showMessageDialog(this,msg,"Configuration Error",JOptionPane.ERROR_MESSAGE);
    }
 
///////////////////////////////////////////
 
         
 	public Configuration getConfiguracion(){
 		return cfg;
 	}
 	
      
}
  


