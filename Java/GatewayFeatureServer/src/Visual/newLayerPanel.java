/*
 */

package Visual;



import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import MapFrame.Feature;
import Server.Mediator;


/**
 *
 * @author Monte
 */
public class newLayerPanel extends JPanel {
    Mediator med;
    
    private JComboBox type;
    private JTextField layerName;
  
  
    
  /**********************************************************
   * 
   * @param md
   */  
    public newLayerPanel (Mediator md){
        super(null);
        med=md;
        med.registerInfoPanel(this);
        

        layerName = new JTextField();
        type = new JComboBox(Feature.type.values());
      
        add(layerName);layerName.setBounds(20, 40, 150, 30);
        add(type);type.setBounds(175, 36, 115, 40);
      
        JLabel infoLayer  = new JLabel   ("Layer                              Type");
        add(infoLayer);infoLayer.setBounds(25, 15, 2500, 30);
        infoLayer.setFont(new Font("Tahoma", Font.BOLD, 12));
        infoLayer.setForeground(Color.BLUE);
        
            
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("New Layer"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        
    }
    
  //ADD METHODS TO CHANGE SOMETHING
    
     public String getLayerName(){
    	return layerName.getText();
    }
     
     public boolean isLayerEmpty(){
    	 if (layerName.getText().length()==0) return true;
    	 
    	 return false;
     }

     public Feature.type getType(){
    	 int index=type.getSelectedIndex();
    	 return ((Feature.type) type.getItemAt(index));
     }
     
     public int getTypeIndex(){
    	 int index=type.getSelectedIndex();
    	 return index;
     }
     
     public void cleanLayerName(){
    	 layerName.setText("");
     }
     public void cleanType(){
    	 type.setSelectedIndex(0);
     }

     public void setEnabled(boolean enable){
     	super.setEnabled(enable);
     	type.setEnabled(enable);
        layerName.setEnabled(enable);
     }
}
