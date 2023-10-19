/*
 * CircuitWindow.java
 * 
 * Created on 03-dic-2007, 23:45:54
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package UFLP;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import ClientFerature.MapFrame;
import ClientFerature.Mediator;
import ClientFerature.NetworkManager;
import GeneticAlgorithm.Genetico;
import GeneticAlgorithm.Individual;
import GeneticAlgorithm.Problem;
import MapFrame.Layer;
import MapFrame.Punto;




/**
 *
 * @author Monte
 */

public class UFLPWindow extends JDialog  implements ActionListener {
      
	private final String Evaluation ="Find a Solution";
	private final String Cost ="Calculate Cost";
	
	List<Layer> _layers;
	HashMap _layerPoints;
	NetworkManager _network;
	
	JComboBox FacilitiesCbox, ClientsCbox;
    DefaultTableModel tableFixedCostModel,tableTransportationCostModel;
    DefaultTableModel tableFacilitiesSolModel,tableClientSolModel;

    JButton EvaluationButton,CalculateCostButton;
    MapFrameGenetic map;
    JLabel CostValue;

/*************************************************************
 * GENERAL CONSTRUCTOR
 * 
 * 
 * 
 * ***********************************************************/
    
    public UFLPWindow (JFrame frame,NetworkManager network, List<Layer> layers){
    
              super(frame, "UFLP evaluation", true);
              _network 		=network; 
              _layers 		= layers;
              _layerPoints 	= new HashMap();
              setLocation(100, 100);
               
              setLayout (null);
              setResizable(false);
              setPreferredSize(new Dimension(750,600));
              
        ///////////////////////////////////////////
        map = new MapFrameGenetic(); //med	
    	map.getMap();
    	add(map.getMap()); map.getMap().setBounds(10, 10, 530, 270);
    	///////////////////////////////////////////
                
    	 ///////////////////////////////////////////
    	 JLabel FixedCostS = new JLabel("FIXED COST:");
         FixedCostS.setFont(new Font("ARIAL", Font.BOLD, 16));
         FixedCostS.setForeground(Color.GRAY);
         FixedCostS.setBounds(10, 285, 130, 25);    
         add(FixedCostS);
         ///////////////////////////////////////////

    	 ///////////////////////////////////////////
    	 JLabel TransCostS = new JLabel("TRANSPORTATION COST:");
    	 TransCostS.setFont(new Font("ARIAL", Font.BOLD, 16));
    	 TransCostS.setForeground(Color.GRAY);
    	 TransCostS.setBounds(120, 285, 260, 25);    
         add(TransCostS);
         ///////////////////////////////////////////

         JLabel CostS = new JLabel("SOLUTION COST: ");
         CostS.setFont(new Font("ARIAL", Font.BOLD, 14));
         CostS.setForeground(Color.GRAY);
         CostS.setBounds(520, 530, 130, 25);    
         add(CostS);
         ///////////////////////////////////////////
         
         CostValue = new JLabel("0");
         CostValue.setFont(new Font("ARIAL", Font.BOLD, 14));
         CostValue.setForeground(Color.BLUE);
         CostValue.setBounds(650, 530, 130, 25);    
         add(CostValue);
         ///////////////////////////////////////////
         Object[][] dataLayer;
         dataLayer= getLayers (_layers);
         String[] tableLayercolumnNames 	= {"Layer", "Npuntos"};
         final JTable tableLayer 			= new JTable(dataLayer, tableLayercolumnNames);
         tableLayer.setPreferredScrollableViewportSize(new Dimension(500, 70));

         JScrollPane scrollPaneLayer = new JScrollPane(tableLayer);
         getContentPane().add(scrollPaneLayer, BorderLayout.CENTER);
         scrollPaneLayer.setBounds(560, 15, 170, 200);    

         add(scrollPaneLayer);
         pack();
         ///////////////////////////////////////////
    	 JLabel Facilities = new JLabel("Facilities: ");
    	 Facilities.setForeground(Color.GRAY);
    	 Facilities.setBounds(560, 220, 150, 25);    
         add(Facilities);

         
         String[] FacilitiesSandClients = getLayersComboBox(_layers);

         FacilitiesCbox = new JComboBox(FacilitiesSandClients);
         FacilitiesCbox.addActionListener(this);
         FacilitiesCbox.setBounds(560, 240, 60, 25);    
         add(FacilitiesCbox);
         
         ///////////////////////////////////////////
         ///////////////////////////////////////////
    	 JLabel Clients = new JLabel("Clients: ");
    	 Clients.setForeground(Color.GRAY);
    	 Clients.setBounds(670, 220, 150, 25);    
         add(Clients);



         ClientsCbox = new JComboBox(FacilitiesSandClients);
         ClientsCbox.addActionListener(this);
         ClientsCbox.setBounds(670, 240, 60, 25);    
         add(ClientsCbox);
         
         ///////////////////////////////////////////
         
         CalculateCostButton = new JButton(Cost);
         CalculateCostButton.setBounds(70, 530, 150, 25);    
         CalculateCostButton.addActionListener (this);
         add(CalculateCostButton);
         ///////////////////////////////////////////

         
          
        tableFixedCostModel = new DefaultTableModel();
        tableFixedCostModel.addColumn("Facility"); tableFixedCostModel.addColumn("Costs"); 
        
        JTable tableFixedCost 						= new JTable(tableFixedCostModel);
        
        tableFixedCost.setPreferredScrollableViewportSize(new Dimension(500, 70));

        JScrollPane scrollPaneFixedCost 	= new JScrollPane(tableFixedCost);
        getContentPane().add(scrollPaneFixedCost, BorderLayout.CENTER);
        scrollPaneFixedCost.setBounds(10, 315, 100, 200);    

        add(scrollPaneFixedCost);
        pack();
        ///////////////////////////////////////////
     
    
     
        
        ///////////////////////////////////////////
      
       tableTransportationCostModel  = new DefaultTableModel();
       JTable tableTransportationCost 		 = new JTable(tableTransportationCostModel);
       tableTransportationCost.setPreferredScrollableViewportSize(new Dimension(500, 70));

       JScrollPane scrollPaneTransportationCost = new JScrollPane(tableTransportationCost);
       getContentPane().add(scrollPaneTransportationCost, BorderLayout.CENTER);
       scrollPaneTransportationCost.setBounds(120, 315, 390, 200);    

       add(scrollPaneTransportationCost);
       pack();
       ///////////////////////////////////////////
        
       ///////////////////////////////////////////
  	 	JLabel SolutionS = new JLabel("SOLUTION:");
  	 	SolutionS.setFont(new Font("ARIAL", Font.BOLD, 16));
  	 	SolutionS.setForeground(Color.GRAY);
  	 	SolutionS.setBounds(520, 285, 260, 25);    
       add(SolutionS);
       ///////////////////////////////////////////
       
       tableClientSolModel  = new DefaultTableModel();
       tableClientSolModel.addColumn("Facility"); tableClientSolModel.addColumn("Open"); 

       JTable tableClientSol 		 = new JTable(tableClientSolModel);
       tableTransportationCost.setPreferredScrollableViewportSize(new Dimension(500, 70));

       JScrollPane scrollPanetableClientSolModel = new JScrollPane(tableClientSol);
       getContentPane().add(scrollPanetableClientSolModel, BorderLayout.CENTER);
       scrollPanetableClientSolModel.setBounds(520, 315, 100, 200);    

       add(scrollPanetableClientSolModel);
       pack();
       ///////////////////////////////////////////
        
       tableFacilitiesSolModel  = new DefaultTableModel();
       tableFacilitiesSolModel.addColumn("Client"); tableFacilitiesSolModel.addColumn("Facility"); 

       JTable tableFacilitytSol 		 = new JTable(tableFacilitiesSolModel);
       tableFacilitytSol.setPreferredScrollableViewportSize(new Dimension(500, 70));

       JScrollPane scrollPanetableFacilitySolModel = new JScrollPane(tableFacilitytSol);
       getContentPane().add(scrollPanetableFacilitySolModel, BorderLayout.CENTER);
       scrollPanetableFacilitySolModel.setBounds(630, 315, 100, 200);    

       add(scrollPanetableFacilitySolModel);
       pack();
       
        ///////////////////////////////////////////
        EvaluationButton = new JButton(Evaluation);
        EvaluationButton.setBounds(280, 530, 140, 25);    
        EvaluationButton.addActionListener (this);
        
        EvaluationButton.setEnabled(false);
        add(EvaluationButton);
        ///////////////////////////////////////////
	
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
    
    if ( cmd.equals (Evaluation)) Evaluation();
    else if  ( cmd.equals (Cost)) Cost(); 
      
  }
  
 ///////////////////////////////////////////
 ///////////////////////////////////////////
  
 private void Evaluation (){
        
	 int facilities= tableFixedCostModel.getRowCount();
	 double [] fixed_costs = new double[facilities];
	 
	 for (int index=0;index < facilities;index++){
		 String temp = (String) tableFixedCostModel.getValueAt(index, 1);
		 if (temp.length()==0)fixed_costs[index] = 0.0;				// USUARIO DEJA VACIO LOS DATOS SE CONVIERTE EN CERO.
		 else fixed_costs[index] = Double.parseDouble(temp);
	 }
	 
	 int clients = tableTransportationCostModel.getRowCount();
	 
	 double [] [] transport_cost = new double [clients] [facilities];
	 
	 
	 for (int indexClients=0;indexClients < clients;indexClients++){
	 	 for (int indexnFacilities=0;indexnFacilities < facilities;indexnFacilities++){
	 		 Double temp = (Double) tableTransportationCostModel.getValueAt(indexClients, indexnFacilities+1);
			 transport_cost [indexClients] [indexnFacilities] = temp.doubleValue();
		 }
	 }

 	
 	
 	Problem problemCurrent	= new Problem (facilities,clients);
 	problemCurrent.setFixedCost(fixed_costs);
 	problemCurrent.setTransportCost(transport_cost);
 	
 	int _poblacion = facilities*2, _generaciones = clients*100;


 	Genetico genProblemBasic= new Genetico (_poblacion,_generaciones,problemCurrent); 
 	//genProblemBasic.debug	=true;
 	genProblemBasic.run();
 	
 	Individual sol			 		= genProblemBasic.getSolution();
 	long timeConsumed 		 		= genProblemBasic.getTimeConsumed();
 	List <Double> fitnessHistory 	= genProblemBasic.getHistoryFitness();
 	int generationCountSol	 		= genProblemBasic.getGenerationCount();
 			 
 	problemCurrent.setGeneticParams(_poblacion, _generaciones);
 	problemCurrent.setFindSolution(sol);
 	problemCurrent.setTimeConsumed(timeConsumed);
 	problemCurrent.setHisttoryFitness(fitnessHistory);
 	problemCurrent.setHisttoryFitnessEliStd(genProblemBasic.getHistorySTDFitness());

 	problemCurrent.setGenerationCount(generationCountSol);

 	problemCurrent.printSolution(true);
 	
 	boolean [] solution =genProblemBasic.getSolutionVector();
 	map.displaySolution(solution);
 	
 	for (int i=0;i<solution.length;i++){
 		tableClientSolModel.setValueAt(solution[i], i, 1);
 	}
 	
 	int [] asign = problemCurrent.clientAsign();
 	
 	for (int i=0;i<asign.length;i++){
 		tableFacilitiesSolModel.setValueAt("F"+asign[i], i, 1);
 	}
 	
 	CostValue.setText(""+fitnessHistory.get(generationCountSol));
 	
 	//tableFixedCostModel.addRow(new Object[]{"Facility "+index,""+random.nextInt(1000),false});
 }
 
///////////////////////////////////////////
///////////////////////////////////////////

 private void Cost(){
	 
	 int clients 	= ClientsCbox.getSelectedIndex();
	 int facilities = FacilitiesCbox.getSelectedIndex();
	 
	 if (clients==facilities){
		 Error("Clients and Facilities can not be the same selection");
	 }
	 else{
		 //AQUI EJECUTO SEND MSG
		 String facilitiesLayer =  _layers.get(facilities).getName();
		 String clientsLayer 	=  _layers.get(clients).getName();

		 
		 clearTable(tableFixedCostModel);
		
		 clearTable(tableTransportationCostModel);
		
		 tableTransportationCostModel.setColumnCount(0);
		 tableTransportationCostModel.addColumn("Clients");
		 
		 List<Punto> ptosFacilities = (List<Punto>)_layerPoints.get(facilitiesLayer);
	     for (int index=0;index<ptosFacilities.size();index++){
	    	 Punto pto		= ptosFacilities.get(index);
	    	 Random random  = new Random();
	    	 tableTransportationCostModel.addColumn("F"+index);
	    	 tableFixedCostModel.addRow(new Object[]{"F"+index,""+random.nextInt(1000)});
	     }

	     
	     List<Punto> ptosClients= (List<Punto>)_layerPoints.get(clientsLayer);

	     map.displayClientsandFacilities(ptosFacilities,ptosClients);

	     double distancePtos [][] = new double [ptosClients.size()][ptosFacilities.size()];
	     
	     for (int indexClients=0;indexClients<ptosClients.size();indexClients++){
	    	 for (int indexFacilities=0;indexFacilities<ptosFacilities.size();indexFacilities++){
	    		 distancePtos [indexClients] [indexFacilities] = ptosClients.get(indexClients).distance(ptosFacilities.get(indexFacilities));
	    	 }
	     }
	     
	     for (int indexClients=0;indexClients<ptosClients.size();indexClients++){
	    	Object [] rowX =new Object[ptosFacilities.size()+1];
	    	rowX[0]="C"+indexClients; 
	    	for (int indexFacilities=0;indexFacilities<ptosFacilities.size();indexFacilities++)
	    		rowX[indexFacilities+1] = distancePtos[indexClients][indexFacilities];
	    	tableTransportationCostModel.addRow(rowX);
	     }
	     
	     clearTable(tableClientSolModel);
			
	     tableClientSolModel.setColumnCount(0);
	     tableClientSolModel.addColumn("Clients"); tableClientSolModel.addColumn("Open");
	     for (int index=0;index<ptosFacilities.size();index++){
	    	 tableClientSolModel.addRow(new Object[]{"F"+index,""+false});
	     }
	     
	     
	     clearTable(tableFacilitiesSolModel);
			
	     tableFacilitiesSolModel.setColumnCount(0);
	     tableFacilitiesSolModel.addColumn("Clients"); tableFacilitiesSolModel.addColumn("Facility");
	     for (int index=0;index< ptosClients.size();index++){
	    	 tableFacilitiesSolModel.addRow(new Object[]{"C"+index,""+"?"});
	     }
	     
	     EvaluationButton.setEnabled(true);
	     //MEJORA PERO ASINCRONO
	     //_network.sendLayer(_layers.get(clients).getName(), _layers.get(clients).getTipo());
		 //_network.sendLayer(_layers.get(facilities).getName(), _layers.get(facilities).getTipo());
	 }
 }
 
///////////////////////////////////////////

 private void Error(String msg) {
        
        JOptionPane.showMessageDialog(this,msg,"Configuration Error",JOptionPane.ERROR_MESSAGE);
 }
 
///////////////////////////////////////////
 
   private Object [][] getLayers (List<Layer> layers){
	   Object [] [] ret = new Object [layers.size()] [2];
	   
	   for (int i=0;i<layers.size();i++){
		   ret [i][0] = layers.get(i).getName();
		   ret [i][1] = layers.get(i).getNelements();
	   }
	   
	   return ret;
   }
   
///////////////////////////////////////////

   private String [] getLayersComboBox (List<Layer> layers){
	   String [] ret = new String[layers.size()];
			   
	   for (int i=0;i<layers.size();i++){
		   ret[i] = ""+i;
	   }
	   
	   return ret;
   } 
   
///////////////////////////////////////////

 	public void receiveDatos(String name,List<Punto> ptos){
 		_layerPoints.put(name,ptos);
 		
 		//System.out.println("Name "+name+" ptos: "+ptos);
 	}
///////////////////////////////////////////

 	public void requestLayerPoints(){
 		
 		for (int i=0;i<_layers.size();i++){
		  _network.sendLayer(_layers.get(i).getName(), _layers.get(i).getTipo());
	  } 
 	}
 	
///////////////////////////////////////////

 	public void clearTable(DefaultTableModel mode){
 	    
 		mode.getDataVector().removeAllElements();
 	    revalidate();
 	}
}
  


