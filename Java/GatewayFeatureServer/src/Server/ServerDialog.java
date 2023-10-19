/*
 * ServerDialog.java
 *
 * Created on 08-jul-2007, 20:38:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Server;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent.AboutEvent;
import com.apple.eawt.Application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import ButtonListener.ButtonAbstract;
import ButtonListener.Command;
import Server.Mediator.tipoBotones;
import Visual.LayerTable;
import Visual.featureModePanel;
import Visual.infoLog;
import Visual.newLayerPanel;



/**
 *
 * @author Monte
 */
public class ServerDialog implements ActionListener{
    private int x=800,y=715;
    private JFrame frame;
    

   
    // Bulletin Board
    LayerTable table;
    
    Mediator med;
    
    public ServerDialog(){
        
        
        frame = new JFrame("Features Gateway");
        loadIcon();
        frame.setLocation(0, 0);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(x, y));

        med = new Mediator(frame);
        
        
        ButtonAbstract   cfgButton      = new ButtonAbstract (med,this,tipoBotones.Config); 
        ButtonAbstract   startButton   = new ButtonAbstract  (med,this,tipoBotones.Start); 
        ButtonAbstract   usersButton   = new ButtonAbstract  (med,this,tipoBotones.Users); 
        ButtonAbstract   stopButton   = new ButtonAbstract   (med,this,tipoBotones.Stop); 
        
        ButtonAbstract   addLayerButton      = new ButtonAbstract (med,this,tipoBotones.AddLayer); 
        ButtonAbstract   delLayerButton      = new ButtonAbstract (med,this,tipoBotones.DelLayer); 
        ButtonAbstract   publishLayerButton  = new ButtonAbstract (med,this,tipoBotones.Publish); 
        ButtonAbstract   importButton 		 = new ButtonAbstract (med,this,tipoBotones.Import);       
        
        
        infoLog         log             = new infoLog  (med); 
        newLayerPanel   layerPanel      = new newLayerPanel(med);
        featureModePanel featurePanel   = new featureModePanel(med); 
        
        boolean database = med.init();

        
        //LABEL
        JLabel  titleL =new JLabel("Features Gateway");
        titleL.setFont(new Font("ARIAL", Font.BOLD, 18));
        JSeparator separator1 = new JSeparator(); JSeparator separator2 = new JSeparator();
        
        separator1.setBounds(x-170, 35, 170, 15);   separator2.setBounds(x-160,39, 160, 15);
        
        titleL.setBounds(x-172, 8, 170, 30);
       //END LABEL
        
     
        
        table= new LayerTable(med);
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 62, 450, 190);
        frame.add(scrollPane);
        //END Bulletin
     
        //LAYERS ADMIN
        frame.add(layerPanel); layerPanel.setBounds(x-320, 60, 300, 80);
        
        frame.add(addLayerButton); 	 	addLayerButton.setBounds(x-320, 140, 100, 30);
        frame.add(delLayerButton);		delLayerButton.setBounds(x-220, 140, 100, 30);   
        frame.add(publishLayerButton);  publishLayerButton.setBounds(x-120, 140, 99, 30);
        frame.add(importButton);   		importButton.setBounds(x-320, 170, 300, 30);
      
        
        frame.add(featurePanel); featurePanel.setBounds(x-320, 200, 300, 55);
         
        	
        
        //MAPA
		        MapFrame map = new MapFrame(med);	
		    	map.getMap();
				frame.add(map.getMap()); map.getMap().setBounds(20, 255, 750, 270);
		//FIN MAPA				
			
		
        //LOG
        JLabel LogInfoL= new JLabel("LOG INFO");
        LogInfoL.setFont(new Font("Tahoma", Font.BOLD, 14));
        LogInfoL.setForeground(Color.GRAY);
        JScrollPane scrollPaneLog = new JScrollPane(log);
        
        frame.add(LogInfoL); frame.add(scrollPaneLog);
        scrollPaneLog.setBounds(20, y-190, x-40, 100);
        LogInfoL.setBounds(20, y-208, 130, 15);
        //END LOG
        
        
        //BUTTONS START AND STOP
        
        cfgButton.setBounds(20, y-80, 99, 30);
        startButton.setBounds((x/2)-110, y-80, 99, 30);
        stopButton.setBounds((x/2), y-80, 99, 30);
        usersButton.setBounds(x-120, y-80, 99, 30);
     
        
      
        //END BUTTONS CONNECT AND DISCONNECT
        
  
        frame.add(titleL);    frame.add(separator1);frame.add(separator2);
        frame.add(startButton);  frame.add(stopButton); frame.add(usersButton); frame.add(cfgButton);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
       
        
        startButton.requestFocusInWindow();
       
      if (database)  med.LoadData(); //Cargo datos tabla de layers
      //else frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      
      
    }
    
    /**********************************************
     * 
     * @param e
     */
    
    
    
     public void actionPerformed(ActionEvent e) {
        Command cmd = (Command) e.getSource();
        cmd.Execute();
     
    }

/************
 * 
 *      Cambiamos el icono de la aplicacion y el about en mac
 */
     public void loadIcon(){
    	 String OS = System.getProperty("os.name"); 
    	 java.net.URL url = ClassLoader.getSystemResource("world.png");
         Toolkit kit = Toolkit.getDefaultToolkit();
         Image img = kit.createImage(url);
         
         
    	 if (OS.startsWith("Mac")){
    	   
           Application application = Application.getApplication();
           application.setDockIconImage(img);
           application.setAboutHandler(new AboutHandler() {

         @Override
			public void handleAbout(AboutEvent arg0) {
             JOptionPane.showMessageDialog(frame,"Features Gateway " +  "\nCreated at University of Malaga.");
				
			}

           });
       
           
    	 }else{
    		 frame.setIconImage(img);
    	 }
  
     }
    
    /**
     * MAIN
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        ServerDialog x = new ServerDialog();
        
    }
        
}