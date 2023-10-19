/*
 * CircuitWindow.java
 * 
 * Created on 03-dic-2007, 23:45:54
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ClientFerature;


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




/**
 *
 * @author Monte
 */

public class CfgWindow extends JDialog  implements ActionListener {

    private JTextField port;
    private JTextField server;
    
      
    JTable tableV;
 
    
    private int      Port	=	8000;
    private String   Server =	"127.0.0.1";
  
    
    private Configuration 	_cfg;
    
    Mediator md;
/*************************************************************
 * GENERAL CONSTRUCTOR
 * 
 * 
 * 
 * ***********************************************************/
    
    public CfgWindow (JFrame frame,Configuration cfg){
    
              super(frame, "Configuration Values", true);
              
             
              setLocation(10, 20);
               
              setLayout (null);
              setResizable(false);
              setPreferredSize(new Dimension(350,190));
              
         _cfg = cfg;     
                  
         Server = _cfg.getServer();
         Port	= _cfg.getPort();
         
        //CFG
        JPanel serverGeoPane = new JPanel(null);

        JLabel serverGeoL = new JLabel("Server: ");
        serverGeoL.setForeground(Color.GRAY);
        server = new JTextField();
        server.setText(Server);
        serverGeoL.setBounds(10, 10, 180, 40);
        server.setBounds(140, 20, 150, 20);
        serverGeoPane.add(serverGeoL); serverGeoPane.add(server);
        
        JLabel serverGeoPL = new JLabel("Server Port: ");
        serverGeoPL.setForeground(Color.GRAY);
        port = new JTextField();
        port.setText(""+Port);
        serverGeoPL.setBounds(10, 40, 180, 40);
        port.setBounds(140, 50, 150, 20);
        serverGeoPane.add(serverGeoPL); serverGeoPane.add(port);

        
        serverGeoPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Feature Gateway"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        add(serverGeoPane);
        serverGeoPane.setBounds(20, 15, 300, 100);
        
        //END CFG
                
        ///////////////////////////////////////////
        JButton SaveButton = new JButton("Save");
        SaveButton.setBounds(120, 120, 100, 25);    
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
        if (server.getText().length()==0){ Error("     GeoServer field empty."); return;} else {
            	Server= server.getText().trim();
        }
          
        _cfg = new Configuration(Server,Port);
             
        dispose();
 }
  
///////////////////////////////////////////

 private void Error(String msg) {
        
        JOptionPane.showMessageDialog(this,msg,"Configuration Error",JOptionPane.ERROR_MESSAGE);
    }
 
///////////////////////////////////////////
 
         
 	public Configuration getConfiguracion(){
 		return _cfg;
 	}
 	
      
}
  


