/*
 * BulletinTable.java
 * 
 * Created on 12-nov-2007, 18:29:51
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ClientFerature;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import MapFrame.Feature;
import MapFrame.Layer;




/**
 *
 * @author Monte
 */
public class LayerTable extends JTable {
     protected LayerDataTable m_data;
     int columnLayer    =0; //Nombre layer
     int columnType     =1; //Typo, Point;Line ...
     int columnElements =2; //Numero elementos
     Mediator med;
     
     boolean outputReady = false;
     boolean disconnected = false;
     
     
/**********************************************
 * 
 * @param md
 */     
   public LayerTable(Mediator md) {
            super();
    
            med=md;
            m_data = new LayerDataTable();

            setAutoCreateColumnsFromModel(false);
            setModel(m_data);
            
    //Ponemos la cabecera        
        for (int k = 0; k < LayerDataTable.m_columns.length; k++) {
          DefaultTableCellRenderer renderer = new ColoredTableCellRenderer();
            renderer.setHorizontalAlignment(LayerDataTable.m_columns[k].m_alignment);
            
            TableColumn column = new TableColumn(k,LayerDataTable.m_columns[k].m_width, renderer, null);
            addColumn(column);  
    }


            
            
    JTableHeader header = getTableHeader();
    header.setUpdateTableInRealTime(false);
    
    setShowVerticalLines(false);
    //m_table.setShowHorizontalLines( false );
    setRowSelectionAllowed( true );
    setPreferredScrollableViewportSize(new Dimension(700, 370));
    getTableHeader().setReorderingAllowed(false);
    
   
     addMouseListener(new MouseAdapter (){
            public void mouseClicked (MouseEvent e){
            	
                med.tableSelected();
                
            }
        });
    
        
        med.registerBB(this);

   } 
   
	  /***********************************************************
	   * 
	   * @return
	   */

   public String [] getLayers (){
   
    int nLayers     =   m_data.m_vector.size();
    String [] ret   = 	new String [nLayers];
    LayerData aux;
    
    for (int i=0;i<nLayers;i++){
        aux=(LayerData) m_data.m_vector.get(i);    
        ret[i]=aux.m_layer;
    }
    
    return ret;
   }
   
	  /***********************************************************
	   * 
	   * @return
	   */

   public List<Layer> getPointLayers (){
	   	int nLayers     =   m_data.m_vector.size();
	    List<Layer>ret   = 	new ArrayList<Layer>();
	    LayerData aux;
	    
	    for (int i=0;i<nLayers;i++){
	        aux=(LayerData) m_data.m_vector.get(i);    
	        if (aux.m_typelayer ==  Feature.type.Point)
	        ret.add(new Layer(aux.m_layer,aux.m_typelayer,aux.m_numElements));
	    }
	    
	    return ret;
   }
	  /***********************************************************
	   * 
	   * @param layer
	   */
   
   public void loadNewData(String layer){

       m_data.addElement(layer);
       addNotify();
       repaint();
    }

	  /***********************************************************
	   * 
	   * @param layer
	   * @param type
	   */

   public void loadNewData(String layer,Feature.type type){

       m_data.addElement(layer,type);
       addNotify();
       repaint();
    }
   
	  /***********************************************************
	   * 
	   * @param layers
	   */

   public void loadNewData(List <Layer> layers){
	   for (Layer layer: layers){
		   
		   m_data.addElement(layer.getName(),layer.getTipo(),layer.getNelements());
	   }
       addNotify();
       repaint();
    }
	  /***********************************************************
	   * 
	   * @param layer
	   * @param type
	   * @param numElement
	   */

  
   public void loadNewData(String layer,Feature.type type,int numElement){

       m_data.addElement(layer,type,numElement);
       addNotify();
       repaint();
    }
 
   
   public void updateFeatures(String layer,boolean add){

	   m_data.updateLayer(layer,add);
  
       addNotify();
       repaint();
	} 
    
	  /***********************************************************
	   * 
	   */

    public void clean (){
       m_data.clear();
        addNotify();
        repaint();
    }
	  /***********************************************************
	   * 
	   */

    public void deleteSelected (){
    	int index=getSelectedRow();
  
       if(index >=0 & isRowSelected(index)){
    	   m_data.deleteElement(index);
         addNotify();
         repaint();
        }
       
     }
	  /***********************************************************
	   * 
	   * @return
	   */

    public boolean isAnyLayerSelected(){
    	int index=getSelectedRow();
    	return (index >=0 & isRowSelected(index));
    }
	  /***********************************************************
	   * 
	   * @return
	   */

    public Feature.type getTypeSelected (){
    	int index=getSelectedRow();
  
       if(index >=0 & isRowSelected(index)){
    	   return m_data.getType(index);
         }
       return null;
     }
    
    
	  /***********************************************************
	   * 
	   * @return
	   */

    public int getTypeIntSelected (){
    	int index=getSelectedRow();
  
       if(index >=0 & isRowSelected(index)){
    	   return m_data.getTypeInt(index);
         }
       return -1;
     }

	  /***********************************************************
	   * 
	   * @return
	   */

    
    public String getLayerSelected (){
    	int index=getSelectedRow();
  
       if(index >=0 & isRowSelected(index)){
    	   return m_data.getLayerName(index);
         }
       return null;
     }
    
	  /***********************************************************
	   * 
	   * @return
	   */

    public int getNumElement(){
    	return m_data.getRowCount();
    }

	
}


class ColoredTableCellRenderer extends DefaultTableCellRenderer{

  public void setValue(Object value) {

    if (value instanceof ColorData) {
      ColorData cvalue = (ColorData)value;
      setForeground(cvalue.m_color);
      setText(cvalue.m_data.toString());
    }
 
    else
      super.setValue(value);

  }

}




/*********************************************************
 * 
 * @author montenegro
 */

class LayerData{
	

  
  public String          m_layer;
  public Feature.type	 m_typelayer;
  public int         	 m_numElements;
  
  
  public LayerData(String layer, Feature.type type,int numElements){
          

    m_layer     = layer;
    m_typelayer = type;
    m_numElements = numElements;
   

  }

  public LayerData(String layer){
          

    m_layer        	= layer;
    m_typelayer     =  Feature.type.Point;
    m_numElements   = 0;
  

  }


  public LayerData(String layer,Feature.type type){
      

	    m_layer        	= layer;
	    m_typelayer     = type;
	    m_numElements   = 0;
	  
  }


}


/*********************************************************
 * 
 * @author montenegro
 */

class ColorData{

  public Color  m_color;
  public Object m_data;
  public static Color GREEN = new Color(0, 128, 0);
  public static Color RED = Color.red;

  public ColorData(Color color, Object data) {
    m_color = color;
    m_data  = data;
  }

  public ColorData(Double data) {
    m_color = data.doubleValue() >= 0 ? GREEN : RED;
    m_data  = data;
  }

  public String toString() {

    return m_data.toString();

  }

}

/*********************************************************
 * 
 * @author montenegro
 */
class ColumnData{

  public String  m_title;
  public int     m_width;
  public int     m_alignment;

  public ColumnData(String title, int width, int alignment) {

    m_title = title;
    m_width = width;
    m_alignment = alignment;

  }

}

/*********************************************************
 * 
 * @author montenegro
 */

class LayerDataTable extends AbstractTableModel{

  static final public ColumnData m_columns[] = {

    new ColumnData( "Layer", 3, JLabel.CENTER),
    new ColumnData( "Type", 17, JLabel.CENTER ),
    new ColumnData( "Elements", 18, JLabel.CENTER ),   
  };

 
  /*********************************************************
 * 
 */
  
  protected Vector m_vector;

  /***********************************************************
   * 
   */

  public LayerDataTable() {

    m_vector = new Vector();

  }
  
  /***********************************************************
   * 
   */

  public void clear () {
      m_vector.removeAllElements();
      
  }
  
  /***********************************************************
   * 
   * @param index
   */

  public void deleteElement (int index) {
	
	  int size=m_vector.size();
	  
	if(size>0 & (index < size))
      m_vector.remove(index);
     
  }
  
  /***********************************************************
   * 
   * @param layer
   */

  public void addElement(String layer) {
     if(!isIncludeYet(layer)) m_vector.addElement(new LayerData(layer));
     else  JOptionPane.showMessageDialog(null,"Please, Introduce a new layer.","Error Adding Layer",JOptionPane.WARNING_MESSAGE); 
    
  }
  /***********************************************************
   * 
   * @param layer
   * @param type
   */

  public void addElement(String layer, Feature.type type) {
	  
	  if(!isIncludeYet(layer)) m_vector.addElement(new LayerData(layer,type));
	  else  JOptionPane.showMessageDialog(null,"Please, Introduce a new layer.","Error Adding Layer",JOptionPane.WARNING_MESSAGE); 
	    
  }
  /***********************************************************
   * 
   * @param layer
   * @param tipo
   * @param numElement
   */

	public void addElement(String layer, int tipo,int numElement) {
		  
		
		Feature.type type=Feature.type.Point;
		
		switch(tipo){
			case 0: type= Feature.type.Point; break;
			case 1: type= Feature.type.LineString; break;
			case 2: type= Feature.type.Polygon; break;
		}
		
		  if(!isIncludeYet(layer)) m_vector.addElement(new LayerData(layer,type,numElement));
		  else  JOptionPane.showMessageDialog(null,"Please, Introduce a new layer.","Error Adding Layer",JOptionPane.WARNING_MESSAGE); 
		    
	}
	  /***********************************************************
	   * 
	   * @param layer
	   * @param type
	   * @param numElement
	   */

  public void addElement(String layer, Feature.type type,int numElement) {
	  
	  if(!isIncludeYet(layer)) m_vector.addElement(new LayerData(layer,type,numElement));
	  else  JOptionPane.showMessageDialog(null,"Please, Introduce a new layer.","Error Adding Layer",JOptionPane.WARNING_MESSAGE); 
	    
  } 
  
  /***********************************************************
   * 
   * @param layerLook
   * @return
   */
   private boolean isIncludeYet(String layerLook) {
  
	  int lon = m_vector.size();
	  LayerData layer;
	  
		  for (int i=0; i<lon;i++){
		      layer = (LayerData)  m_vector.get(i);
		      
		      if (layer.m_layer.compareTo(layerLook)==0) return true;
		  }
		  return false;
   }
 
	  /***********************************************************
	   * 
	   * @param layer
	   * @param add
	   */

  public void updateLayer(String layer,boolean add) {
      
      int lon = m_vector.size();
      LayerData datos;
      for (int i=0; i<lon;i++){
          datos = (LayerData)  m_vector.get(i);
          //System.out.println("TABLE "+datos.m_layer+" "+ layer);
          if (datos.m_layer.contentEquals(layer)) {
        	  if (add)datos.m_numElements++;
        	  else if (datos.m_numElements>0)datos.m_numElements--;
        	  break;
          }
      }
  }
  
  /***********************************************************
   * 
   * @return
   */

  
  public int size(){
	  return m_vector.size();
  }
 
  
  /***********************************************************
   * 
   */


  public int getRowCount() {

    return m_vector==null ? 0 : m_vector.size();

  }

  /**********************************************************
   * 
   */

  public int getColumnCount() {
    return m_columns.length;
  }
  
  /***********************************************************
   * 
   */


  public String getColumnName(int column) {

    return m_columns[column].m_title;

  }

  /***********************************************************
   * 
   */

  public boolean isCellEditable(int nRow, int nCol) {
    return false;

  }
  
  /***********************************************************
   * 
   * @param nRow
   * @return
   */

  public String getLayerName(int nRow) {
	  return (String) getValueAt(nRow,0); 
  }

  /***********************************************************
   * 
   * @param nRow
   * @return
   */

  public Feature.type getType(int nRow) {
	  return (Feature.type) getValueAt(nRow,1); 
  }

  /***********************************************************
   * 
   * @param nRow
   * @return
   */

  public int getTypeInt(int nRow) {
	  Feature.type tipo= (Feature.type) getValueAt(nRow,1);
	  
	  switch (tipo){
	  	case   Point: return 0;
	  	case   LineString: return 1;
	  	case   Polygon: return 2;
	  }
	  return -1;
  }

    
  /***********************************************************
   * 
   */

  public Object getValueAt(int nRow, int nCol) {

    if (nRow < 0 || nRow >= getRowCount()) return "";

    LayerData row = (LayerData)  m_vector.elementAt(nRow);

    switch (nCol) {

      case 0: return row.m_layer;
      case 1: return row.m_typelayer;
      case 2: return row.m_numElements;
 
    }

    return "";

  }


}



 

