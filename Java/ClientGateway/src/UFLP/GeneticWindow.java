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
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
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
import ClientFerature.Plot;
import ClientFerature.geneticLog;
import GeneticAlgorithm.Genetico;
import GeneticAlgorithm.Individual;
import GeneticAlgorithm.Problem;
import MapFrame.Layer;
import MapFrame.Punto;




/**
 *
 * @author Monte
 */

public class GeneticWindow extends JDialog  implements ActionListener {
      
	private final String LoadProblem ="Load Problem";
	private final String RunGeneticAlg ="Find a Solution";
	
	
	JComboBox ProblemsCbox, ClientsCbox;
    DefaultTableModel tableFixedCostModel,tableTransportationCostModel;
    DefaultTableModel tableFacilitiesSolModel;
    JButton RunGenetic;

    String [] problemas ={"cap71", "cap72", "cap73","cap74",
		 "cap101","cap102","cap103","cap104",
		 "cap131","cap132","cap133","cap134","capa", "capb","capc"};
    
    double [] soluciones = {932615.750,977799.400,1010641.450,1034976.975,
			796648.438,854704.200,893782.113,928941.750,
			793439.563,851495.325,893076.713,928941.750,17156454.478,12979071.581,11505594.329};
    
    JLabel CostValue,TimeValue;
    Problem problemCurrent;
    Plot 	plot	=	null;
    geneticLog   log;
    JTextField Seed;
    
/*************************************************************
 * GENERAL CONSTRUCTOR
 * 
 * 
 * 
 * ***********************************************************/
    
    public GeneticWindow (JFrame frame){
    
              super(frame, "Genetic algorithm", true);
             
              setLocation(100, 100);
               
              setLayout (null);
              setResizable(false);
              setPreferredSize(new Dimension(770,520));
              
              log             = new geneticLog  (); 
 
        ///////////////////////////////////////////
             JLabel ProblemsL = new JLabel("Problems: ");
         	 ProblemsL.setForeground(Color.GRAY);
         	 ProblemsL.setBounds(10, 20, 150, 25);    
              add(ProblemsL);

					  
  
              ProblemsCbox = new JComboBox(problemas);
              ProblemsCbox.addActionListener(this);
              ProblemsCbox.setBounds(80, 20, 120, 25);    
              add(ProblemsCbox);
                
    	
			///////////////////////////////////////////
			JButton LoadButton = new JButton(LoadProblem);
			LoadButton.setBounds(225, 20, 140, 25);    
			LoadButton.addActionListener (this);
			add(LoadButton);
			///////////////////////////////////////////
		      
	         RunGenetic = new JButton(RunGeneticAlg);
	         RunGenetic.setBounds(610, 20, 150, 25);    
	         RunGenetic.addActionListener (this);
	         RunGenetic.setEnabled(false);
	         add(RunGenetic);
    	
         ///////////////////////////////////////////
	         
	         JLabel SeedL = new JLabel("Random Seed: ");
	         SeedL.setForeground(Color.BLACK);
	         SeedL.setBounds(380, 20, 150, 25);    
              add(SeedL);
              
             
              Seed = new JTextField(15);           
              Seed.setBounds(470,20, 100, 20);
              Seed.setText("5");
              add(Seed);
              
        
         ///////////////////////////////////////////
    	 JLabel FixedCostS = new JLabel("FIXED COST:");
         FixedCostS.setFont(new Font("ARIAL", Font.BOLD, 16));
         FixedCostS.setForeground(Color.GRAY);
         FixedCostS.setBounds(10, 55, 130, 25);    
         add(FixedCostS);
         ///////////////////////////////////////////
            
         tableFixedCostModel = new DefaultTableModel();
         
         JTable tableFixedCost 						= new JTable(tableFixedCostModel);
         
         tableFixedCost.setPreferredScrollableViewportSize(new Dimension(500, 70));

         JScrollPane scrollPaneFixedCost 	= new JScrollPane(tableFixedCost);
         getContentPane().add(scrollPaneFixedCost, BorderLayout.CENTER);
         scrollPaneFixedCost.setBounds(10, 75, 750, 60);    

         add(scrollPaneFixedCost);
         
          ///////////////////////////////////////////
         ///////////////////////////////////////////
    	 JLabel TransCostS = new JLabel("TRANSPORTATION COST:");
    	 TransCostS.setFont(new Font("ARIAL", Font.BOLD, 16));
    	 TransCostS.setForeground(Color.GRAY);
    	 TransCostS.setBounds(10, 140, 260, 25);    
         add(TransCostS);
         ///////////////////////////////////////////
         tableTransportationCostModel  = new DefaultTableModel();
         JTable tableTransportationCost 		 = new JTable(tableTransportationCostModel);
         tableTransportationCost.setPreferredScrollableViewportSize(new Dimension(500, 70));

         JScrollPane scrollPaneTransportationCost = new JScrollPane(tableTransportationCost);
         getContentPane().add(scrollPaneTransportationCost, BorderLayout.CENTER);
         scrollPaneTransportationCost.setBounds(10, 160, 750, 100);    

         add(scrollPaneTransportationCost);
         
           
         ///////////////////////////////////////////
         JLabel SolS = new JLabel("SOLUTION: ");
         SolS.setFont(new Font("ARIAL", Font.BOLD, 16));
         SolS.setForeground(Color.GRAY);
         SolS.setBounds(10, 260, 130, 25);    
         add(SolS);
         
         CostValue = new JLabel();
         CostValue.setFont(new Font("ARIAL", Font.BOLD, 14));
         CostValue.setForeground(Color.BLUE);
         CostValue.setBounds(105, 260, 270, 25);    
         add(CostValue);
         
         TimeValue = new JLabel("");
         TimeValue.setFont(new Font("ARIAL", Font.BOLD, 12));
         TimeValue.setForeground(Color.BLACK);
         TimeValue.setBounds(370, 260, 290, 25);    
         add(TimeValue);
         ///////////////////////////////////////////
         tableFacilitiesSolModel  	= new DefaultTableModel();
         JTable tableFacilitiesSol 	= new JTable(tableFacilitiesSolModel);
         tableFacilitiesSol.setPreferredScrollableViewportSize(new Dimension(500, 70));

         JScrollPane scrolltableFacilitiesSol = new JScrollPane(tableFacilitiesSol);
         getContentPane().add(scrolltableFacilitiesSol, BorderLayout.CENTER);
         scrolltableFacilitiesSol.setBounds(10, 280, 750, 75);    

         add(scrolltableFacilitiesSol);
         ///////////////////////////////////////////
   
         
         
         tableFacilitiesSol.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         tableFixedCost.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         tableTransportationCost.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         
         //LOG
         JLabel LogInfoL= new JLabel("Genetic Algorithm Info");
         LogInfoL.setFont(new Font("Tahoma", Font.BOLD, 12));
         LogInfoL.setForeground(Color.GRAY);
         JScrollPane scrollPaneLog = new JScrollPane(log);
         
         add(LogInfoL); add(scrollPaneLog);
         LogInfoL.setBounds(10, 358, 180, 15);
         scrollPaneLog.setBounds(10, 378, 750, 100);
         //END LOG
         
         pack(); 
         
         addWindowListener(new WindowAdapter() {
           public void windowClosed(WindowEvent e)           {
           }

           public void windowClosing(WindowEvent e){
        	   if (plot!=null) plot.closing();
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
    
    if ( cmd.equals (LoadProblem)) Load();
    else if  ( cmd.equals (RunGeneticAlg)) Evaluation(); 
      
  }
  
  private void Load (){
	 String file	= (String) ProblemsCbox.getSelectedItem();
	 file			= file.concat(".txt");
	 double [] fixedCost 		= null;
	 double [] [] transportCost = null;
	 int clients=-1, facilities=-1;
	 
	 TimeValue.setText("");
	 CostValue.setText("");
	 log.clear();
	 if (plot!=null) {
		 plot.closing();
		 plot = null;
	 }
		
	 try {
		problemCurrent	= new Problem (file);
		fixedCost 		= problemCurrent.getFixedCost();
		transportCost	= problemCurrent.getTransportCost();
		clients			= problemCurrent.getNumClients();
		facilities		= problemCurrent.getNumFacilities();
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	 
	 clearTable(tableFacilitiesSolModel);
	 tableFacilitiesSolModel.setColumnCount(0);
	 
	 clearTable(tableFixedCostModel);
	 tableFixedCostModel.setColumnCount(0);

     for (int index=0;index<fixedCost.length;index++){
    	 tableFixedCostModel.addColumn("F"+index);
     }
     
 	Double [] tmp = new Double  [fixedCost.length];
 	for (int index=0;index<fixedCost.length;index++){
 		tmp[index]= new Double(fixedCost[index]);
 	}
 	tableFixedCostModel.addRow(tmp); 
	
 	clearTable(tableTransportationCostModel);
 	tableTransportationCostModel.setColumnCount(0);
	
 	Object [] facilitiesSol =new Object[facilities];
	Object [] clientSol =new Object[facilities];
	
 	tableTransportationCostModel.addColumn("Clients");
 	 for (int index=0;index<facilities;index++){
    	 tableTransportationCostModel.addColumn("F"+index);
    	 tableFacilitiesSolModel.addColumn("F"+index);
    	 facilitiesSol[index]=false;
    	 clientSol[index]="";
     }

 	tableFacilitiesSolModel.addRow(facilitiesSol);
 	tableFacilitiesSolModel.addRow(clientSol);

 	
 	 for (int indexClients=0;indexClients<clients;indexClients++){
	    	Object [] rowX =new Object[facilities+1];
	    	rowX[0]="C"+indexClients; 
	    	for (int indexFacilities=0;indexFacilities<facilities;indexFacilities++)
	    		rowX[indexFacilities+1] = transportCost[indexClients][indexFacilities];
	    	tableTransportationCostModel.addRow(rowX);
	     }

 	 RunGenetic.setEnabled(true);
  }
  
 ///////////////////////////////////////////
 ///////////////////////////////////////////
  
 private void Evaluation (){
        
	int randomSeed=5;
	
	 try {
		 randomSeed = Integer.parseInt(Seed.getText());
	    } 
	 catch (NumberFormatException e) {
	        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Random seed", JOptionPane.CLOSED_OPTION);
	        return;
	 }
	 

	String file	= (String) ProblemsCbox.getSelectedItem();
	file			= file.concat(".txt");

	int selected	= ProblemsCbox.getSelectedIndex();
	
	double solucion=soluciones[selected];
 	problemCurrent.setFitnesSolution(solucion);
 	problemCurrent.setSeed(randomSeed); //Cambia semilla y resetea el Generador de numeros aleatorios, !!! Importante
 	
 	
	int _poblacion = problemCurrent.getNumFacilities()*2, _generaciones = problemCurrent.getNumClients()*100;
	problemCurrent.setLog(log);

 	Genetico genProblemBasic= new Genetico (_poblacion,_generaciones,problemCurrent); 
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
 	problemCurrent.printSolution(false);
 	
 	boolean [] solution = genProblemBasic.getSolutionVector();
 
 	for (int i=0;i<solution.length;i++){
 		tableFacilitiesSolModel.setValueAt(solution[i],0, i);
 	}
 	
 	int [] asign = problemCurrent.clientAsign();
 	
 	for (int i=0;i<asign.length;i++){
 		String value	=	(String) tableFacilitiesSolModel.getValueAt(1, asign[i]);
 		if (value.length()==0) value = "C"+i;
 		else				   value = value+",C"+i; 
 		tableFacilitiesSolModel.setValueAt(value,1, asign[i]);
 	}
 	if (problemCurrent.isSolutionFound()){
 		CostValue.setText("Found, fitness value "+fitnessHistory.get(generationCountSol));
 	}
 	else 	{
 		CostValue.setText("Not Found, fitness value "+fitnessHistory.get(generationCountSol));
 	}
 	
	TimeValue.setText("took "+timeConsumed+" milliseconds "+" and "+generationCountSol+" generations.");
 		
 	Chart("Fitness vs STD Elitism",fitnessHistory,"fitness",genProblemBasic.getHistorySTDFitness(),"STD Elitism");
 }
 
///////////////////////////////////////////

	
 	
 	public void clearTable(DefaultTableModel mode){
 	    
 		mode.getDataVector().removeAllElements();
 	    revalidate();
 	}
 	
///////////////////////////////////////////
	
 	public void Chart(String title,List <Double> valores,String funcionLabe,List <Double> valores2,String funcionLabe2){
 		
 		if (plot!=null) plot.closing();
 		
		 plot = new Plot(title);
	     plot.addData(valores,funcionLabe);
	     plot.addData(valores2,funcionLabe2);
	     plot.paint();
	     plot.pack();
		 plot.setVisible(true);
		 
		 
		
 	}
 	
 	
 	

 	
}
  


