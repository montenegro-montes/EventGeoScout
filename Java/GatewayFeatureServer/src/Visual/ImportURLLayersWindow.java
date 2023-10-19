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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import MapFrame.Feature;
import Server.GeoJSON;





/**
 *
 * @author Monte
 */

public class ImportURLLayersWindow extends JDialog  implements ActionListener {
      
	private final String UrlButton ="Get URL Layer";
	private final String DBButton ="Add Layer";

	
    JButton RunGenetic;

    JTextField URL,LayerLabel,dataToExport;

    GeoJSON importar=null;
    JButton ADD_DB;
    boolean save	=	false;
    JLabel Layer_L,info;
    JComboBox type;
    String[] typesLayer = { "GeoJSON","KML","SHP"};

/*************************************************************
 * GENERAL CONSTRUCTOR
 * 
 * 
 * 
 * ***********************************************************/
    
    public ImportURLLayersWindow (JFrame frame){
    
              super(frame, "Import Layer" , true);
             
              setLocation(100, 100);
               
              setLayout (null);
              setResizable(false);
              setPreferredSize(new Dimension(700,140));
            //  setPreferredSize(new Dimension(700,440));
              
 
        ///////////////////////////////////////////
             JLabel URL_L = new JLabel("URL: ");
         	 URL_L.setForeground(Color.BLACK);
         	 URL_L.setBounds(10, 20, 150, 25);    
             add(URL_L);

              URL = new JTextField(450);      
              //URL.setText("http://datosabiertos.malaga.eu/recursos/transporte/trafico/poi_semaforos.geojson");
              URL.setBounds(40,20, 340, 20);
              add(URL);
             	
              type = new JComboBox(typesLayer);
              type.setBounds(380, 18, 125, 25);    
              add(type); 
              
           ///////////////////////////////////////////
			JButton URL_GET = new JButton(UrlButton);
			URL_GET.setBounds(510, 20, 140, 25);    
			URL_GET.addActionListener (this);
			add(URL_GET);
			///////////////////////////////////////////
			
			
			  Layer_L = new JLabel("Layer Name: ");
			  Layer_L.setForeground(Color.GRAY);
			  Layer_L.setBounds(10, 50, 150, 25);    
	          add(Layer_L);

             LayerLabel = new JTextField(450);      
             LayerLabel.setBounds(90,50, 400, 20);
             LayerLabel.setEnabled(false);
             add(LayerLabel);
	         
             info = new JLabel("");
             info.setForeground(Color.red);
             info.setBounds(95, 75, 350, 25);    
             add(info);
             
       /*      dataToExport= new JTextField(500);      
             dataToExport.setBounds(10,100, 500, 300);
             dataToExport.setEnabled(false);
             add(dataToExport);*/
             
             
			ADD_DB = new JButton(DBButton);
			ADD_DB.setBounds(510, 50, 140, 25);    
			ADD_DB.addActionListener (this);
			ADD_DB.setEnabled(false);
			add(ADD_DB);
         ///////////////////////////////////////////
	         
	        
         pack(); 
         
         addWindowListener(new WindowAdapter() {
           public void windowClosed(WindowEvent e)           {
           }

           public void windowClosing(WindowEvent e){
        	   
           }
         });
         
         
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
    
    if ( cmd.equals (UrlButton)) Load();
    else  if ( cmd.equals (DBButton)) Save();
  }
  
  
///////////////////////////////////////////
  
  private void Save(){
	  save = true;
	  String layerl=LayerLabel.getText();
	  
	  if (layerl.length()>0) importar.setLayerName(layerl);
		  
	  dispose();
  }
///////////////////////////////////////////

  private void Load (){
	 
	  String urlS= URL.getText();
	  
	  if (urlS.length()>0){
		 
		  switch(type.getSelectedIndex()){
		  	case 0:	getGJSON(urlS); break; //GeoJSON
		  	case 1: getKML(urlS);	break; //KML
		  	case 2: getSHP(urlS);	break; //SHP
		  	
		  }
	  }
	  else{
		  JOptionPane.showMessageDialog(null,"Please, insert a valid URL","URL empty",JOptionPane.WARNING_MESSAGE);
	  }
	  
  }
///////////////////////////////////////////

  private void getGJSON (String urlS){
	  
	  importar = new GeoJSON();
		 
		try {
			importar.setURL(urlS);
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null,"Please, insert a valid URL","URL error",JOptionPane.WARNING_MESSAGE);
			importar = null;
			return;
			//e.printStackTrace();
		}
		  
		  
		try {
			importar.readPoints();
		} catch (JsonParseException e) {
			JOptionPane.showMessageDialog(null,"Error parsing GeoJSON","JsonParseException",JOptionPane.WARNING_MESSAGE);
			dataToExport.setText("");
				//e.printStackTrace();
			return;
		} catch (JsonMappingException e) {
			String msg = e.getLocalizedMessage();
			int index  = msg.indexOf("at [Source:");
			msg = msg.substring(0, index);
			JOptionPane.showMessageDialog(null,msg,"Error parsing GeoJSON_JsonMappingException",JOptionPane.WARNING_MESSAGE);	
			e.printStackTrace();
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Error parsing GeoJSON","IOException",JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
			return;
		}
		
		  
		  String msg ="Found layer "+importar.getLayerName()+ " with "+importar.getnumPoints()+ " points";
		  Layer_L.setForeground(Color.BLACK);
		  LayerLabel.setEnabled(true);
		  LayerLabel.setText(importar.getLayerName());
		  
		  info.setText(msg);
		  //JOptionPane.showMessageDialog(null,msg,"New points found",JOptionPane.WARNING_MESSAGE); 
		  ADD_DB.setEnabled(true);
  }
  
  private void getKML (String urlS){
	  
	  JOptionPane.showMessageDialog(null,"Sorry, KML Not implemented yet","ToDo",JOptionPane.WARNING_MESSAGE);

  }
  
  private void getSHP (String urlS){
	  JOptionPane.showMessageDialog(null,"Sorry, SHP Not implemented yet","ToDo",JOptionPane.WARNING_MESSAGE);

  }
  
  public GeoJSON getResult(){
	  return importar;
  }

  public boolean havetoSave(){
	  return save;
  }
 	
}
  


