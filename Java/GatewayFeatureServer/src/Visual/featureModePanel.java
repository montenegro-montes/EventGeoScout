/*
 */

package Visual;



import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import MapFrame.Feature;
import Server.Mediator;


/**
 *
 * @author Monte
 */
public class featureModePanel extends JPanel {
    Mediator med;
    
    private JRadioButton addFeatureButton,delFeatureButton;
  
  
    
  /**********************************************************
   * 
   * @param md
   */  
    public featureModePanel (Mediator md){
        super(null);
        med=md;
        med.registerMode(this);
        

        addFeatureButton = new JRadioButton("Add");
        addFeatureButton.setSelected(true);
        delFeatureButton = new JRadioButton("Delete");
       
        ButtonGroup featuresOption = new ButtonGroup();
        featuresOption.add(addFeatureButton);
        featuresOption.add(delFeatureButton);
       
        add(addFeatureButton);	addFeatureButton.setBounds(60, 15, 80, 30);
        add(delFeatureButton);	delFeatureButton.setBounds(160, 15, 80, 30);
        
        ActionListener listener = new ActionListener() {
            
			@Override
			public void actionPerformed(ActionEvent e) {
				med.modeChanged();
				
			}
        };
        
        addFeatureButton.addActionListener(listener);    
        delFeatureButton.addActionListener(listener);  
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Feature Mode:"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        
    }
   
  //ADD METHODS TO CHANGE SOMETHING
    
    public boolean isAddSelected(){
    	return addFeatureButton.isSelected();
    }
    
    public void setEnabled(boolean enable){
    	super.setEnabled(enable);
    	addFeatureButton.setEnabled(enable);
    	delFeatureButton.setEnabled(enable);
    }
 
}
