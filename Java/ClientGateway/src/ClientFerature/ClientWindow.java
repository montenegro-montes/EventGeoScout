package ClientFerature;


import Buttons.CfgButton;
/*
 * BulletinWindow.java
 * 
 * Created on 16-nov-2007, 18:16:57
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import Buttons.Command;
import Buttons.ConnectButton;
import Buttons.DisConnectButton;
import Buttons.GeneticButton;
import Buttons.UFLPButton;
import Buttons.UserLabel;
import Buttons.infoLog;
import ClientFerature.NetworkManager;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.Application;
import com.apple.eawt.AppEvent.AboutEvent;

import eu.hansolo.custom.SteelCheckBox;




/**
 *
 * @author Monte
 */
public class ClientWindow implements ActionListener{
	private JFrame content_pane;
    
    public LayerTable table;           //TABLE NEED VBLE METHOD TO MODIFY IN NEW ELEMENTS ARRIVED
    public int STATUS_AUCTION	=	-1;
    public Configuration cfg;
    private int x	=	800,	y	=	650;
    
public ClientWindow(Configuration Cfg) {
        
       
        content_pane	=    new JFrame("Features Client");
        content_pane.setLocation(0, 0);
        content_pane.setLayout (null);
        content_pane.setResizable(false);
        content_pane.setPreferredSize(new Dimension(x, y));
        
        cfg				=	Cfg;
        Mediator med 	= new Mediator(cfg);
        ConnectButton   	connectButton  		= new ConnectButton(med,this);
        DisConnectButton    disconnectButton  	= new DisConnectButton(med,this);
        UFLPButton			uflpButton			= new UFLPButton(med,this);
        GeneticButton		geneticButton		= new GeneticButton(med,this);
        CfgButton			cfgButton			= new CfgButton(med,this);

        infoLog         infolog             = new infoLog(med);
        UserLabel       User                = new UserLabel(med);
        
        NetworkManager   network            = new NetworkManager(cfg.getServer(),cfg.getPort(),med);
        

        loadIcon();
      
        med.init(content_pane);

    //TABLE INI     
        
        table= new LayerTable(med);
            
       
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
//USER
        JLabel UserS = new JLabel("USER:");
        UserS.setFont(new Font("ARIAL", Font.BOLD, 18));
        UserS.setForeground(Color.WHITE);
           
//MAP 
        
        MapFrame map = new MapFrame(med);	
    	map.getMap();
    	content_pane.add(map.getMap()); map.getMap().setBounds(170, 220, 600, 270);

//FEATURES
    	JLabel FeatureL= new JLabel("Features Status");
    	content_pane.add (FeatureL); 
    	FeatureL.setFont(new Font("Tahoma", Font.BOLD, 14));
    	FeatureL.setForeground(Color.GRAY);
          
    	SteelCheckBox del = new SteelCheckBox();
    	del.setText("Add Mode");
    	del.setColored(true);
    	del.setRised(true);
    	del.setSelectedColor(eu.hansolo.tools.ColorDef.RED);
    
    	content_pane.add (del);
    	med.registerDel(del);
    	

    	ChangeListener chListener = new ChangeListener() {
    		
    		public void stateChanged(ChangeEvent changeEvent) {
    			
    			med.delCheck();
    			
    		}
    	};
    	
    	del.addChangeListener(chListener);

    	
 //LOG INFO 
        JLabel LogInfoL= new JLabel("LOG INFO");
        LogInfoL.setFont(new Font("Tahoma", Font.BOLD, 14));
        LogInfoL.setForeground(Color.GRAY);
        content_pane.add (LogInfoL); 

        
        JScrollPane scrollPaneLog = new JScrollPane(infolog);
        content_pane.add (scrollPaneLog); 
        
        content_pane.add (UserS); 
        content_pane.add (User); 
        
        content_pane.add(connectButton);
        content_pane.add(disconnectButton);
        content_pane.add(uflpButton);
        content_pane.add(geneticButton);
        content_pane.add(cfgButton);
        
        content_pane.add (scrollPane);

        UserS.setBounds     (25,  20, 120, 20);     
        User.setBounds      (90, 25, 200, 13);     
         
        JSeparator separator1,separator2 = new JSeparator(); 
        separator1 = new JSeparator();
        content_pane.add (separator1); content_pane.add (separator2);
        separator1.setBounds(15, 60, 135, 15);
        separator2.setBounds(15, 205, 135, 15);
        
        cfgButton.setBounds(20, 85, 122, 30); 
        connectButton.setBounds(20, 125, 122, 30); 
        disconnectButton.setBounds(20, 165, 122, 30); 
        
        JSeparator separator3,separator4 = new JSeparator(); 
        separator3 = new JSeparator();
        content_pane.add (separator3); content_pane.add (separator4);
        separator3.setBounds(15, 350, 135, 15);	separator4.setBounds(15, 445, 135, 15);
        geneticButton.setBounds(20, 370, 122, 30); 
        uflpButton.setBounds(20, 405, 122, 30); 
        
        disconnectButton.setEnabled(false);    
        scrollPane.setBounds(170, 65, 600, 150);
        
        FeatureL.setBounds(25, 215, 122, 26); 
        del.setBounds(35, 255, 122, 26); 

        LogInfoL.setBounds(25, 450, 100, 80);
        scrollPaneLog.setBounds(25, 500, 745, 80);
    
        uflpButton.setEnabled(false);
        content_pane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content_pane.pack();
        content_pane.setVisible(true);
        
    }

  /***********************************************************************************
  * 
  * @param e
  */

    public void actionPerformed(ActionEvent e) {
        Command cmd = (Command) e.getSource();
        cmd.Execute();
    }

 /************************************************************* 
  * Load Icon
  * 
  *    
  */
  
    public void loadIcon(){
   	 String OS = System.getProperty("os.name"); 
   	 java.net.URL url = ClassLoader.getSystemResource("ClientIcon.jpeg");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        
        
   	 if (OS.startsWith("Mac")){
   	   
          Application application = Application.getApplication();
          application.setDockIconImage(img);
          application.setAboutHandler(new AboutHandler() {

        @Override
			public void handleAbout(AboutEvent arg0) {
            JOptionPane.showMessageDialog(content_pane,"Client Features Gateway " +  "\nCreated at University of Malaga.");
				
			}

          });
      
          
   	 }else{
   		content_pane.setIconImage(img);
   	 }
 
    }  
    
/*************************************************************    
 * Main
 * 
 * @param args
 */
    
  public static void main(String[] args) {
	  
	  Configuration cfg  	 =   new Configuration("127.0.0.1",8000);
	  ClientWindow cliente 	 = new ClientWindow(cfg);
        
    }

}
