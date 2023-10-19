package simpleGa;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class BatteriaPruebas {

	InputStream _file;
	double _solucion;
	int _poblacion = 0, _generaciones=0;
	
	public BatteriaPruebas (InputStream file,double solucion){
		_file = file;
		_solucion = solucion;
	}
	
	public void setParamGenetico (int poblacion, int generaciones){
		_poblacion 		= poblacion;
		_generaciones	=generaciones;
	}
	
	public void run(boolean print_problema) throws IOException{

    	Problem problemCurrent	= new Problem (_file);
    	problemCurrent.setFitnesSolution(_solucion);
    
    	Genetico genProblemBasic= new Genetico (_poblacion,_generaciones,problemCurrent); 
    	//genProblemBasic.debug	=true;

    	genProblemBasic.run();
    	Individual sol			 		= genProblemBasic.getSolution();
    	long timeConsumed 		 		= genProblemBasic.getTimeConsumed();
    	List <Double>  fitnessHistory 	= genProblemBasic.getHistoryFitness();
    	int generationCountSol	 		= genProblemBasic.getGenerationCount();
    			 
    	problemCurrent.setGeneticParams(_poblacion, _generaciones);
    	problemCurrent.setFindSolution(sol);
    	problemCurrent.setTimeConsumed(timeConsumed);
    	problemCurrent.setHisttoryFitness(fitnessHistory);
    	problemCurrent.setGenerationCount(generationCountSol);
    	problemCurrent.setHisttoryFitnessEliStd(genProblemBasic.getHistorySumFitness());

    	problemCurrent.printSolution(print_problema);
    	
    	genProblemBasic.clear();


	}
	
	/* public static void main(String[] args) throws IOException {


	    	int wharehouses =	5;
	    	int clients		= 	3;
	    	double [] fixed_costs 		= {	5.0,	2.0,	7.0,	3.0,	4.0}; //MAX 21
	    	double [] [] transport_cost = {
							    			{1,		1.5,	0.8,	2,		1.6},
							    			{2,		1.7,	1,		1.5,	1.3},
							    			{1.5,	1.3,	2,		1.5,	1}
	    								  };
	    			
	    	Problem problemCurrent	= new Problem (wharehouses,clients);
	    	problemCurrent.setFixedCost(fixed_costs);
	    	problemCurrent.setTransportCost(transport_cost);
	    	
	    	double solucion = 6.5;
	    	problemCurrent.setFitnesSolution(solucion);
		 	int _poblacion = 10, _generaciones = 20;

	    	Genetico genProblemBasic= new Genetico (_poblacion,_generaciones,problemCurrent); 
	    	genProblemBasic.debug	=true;
	    	genProblemBasic.run();
	    	Individual sol			 = genProblemBasic.getSolution();
	    	long timeConsumed 		 = genProblemBasic.getTimeConsumed();
	    	List <Double> fitnessHistory = genProblemBasic.getHistoryFitness();
	    	int generationCountSol	 = genProblemBasic.getGenerationCount();
	    			 
	    	problemCurrent.setGeneticParams(_poblacion, _generaciones);
	    	problemCurrent.setFindSolution(sol);
	    	problemCurrent.setTimeConsumed(timeConsumed);
	    	problemCurrent.setHisttoryFitness(fitnessHistory);
	    	problemCurrent.setHisttoryFitnessEliStd(genProblemBasic.getHistorySumFitness());

	    	problemCurrent.setGenerationCount(generationCountSol);

	    	problemCurrent.printSolution(true);
	    	
		 	//String [] problemas ={"cap131.txt","cap132.txt","cap133.txt","cap134.txt"};
			//double [] soluciones = {793439.563,851495.325,893076.713,928941.750};
		 	
		 	
		 	String [] problemas ={"capc.txt"};
		 	double [] soluciones = {11505594.329}; 
		 	
	    		
	    	String [] problemas ={"cap71.txt", "cap72.txt", "cap73.txt","cap74.txt",
	    						 "cap101.txt","cap102.txt","cap103.txt","cap104.txt",
	    						 "cap131.txt","cap132.txt","cap133.txt","cap134.txt"};
			double [] soluciones = {932615.750,977799.400,1010641.450,1034976.975,
									796648.438,854704.200,893782.113,928941.750,
									793439.563,851495.325,893076.713,928941.750};
									  
		 	String [] problemas = {"capa.txt", "capb.txt","capc.txt"};
		 	double [] soluciones ={17156454.478,12979071.581,11505594.329};

		
		 	int poblacion = 175, generaciones = 5000;
		 	

		 	int nRepeticiones	=	20;
		 	
		 for (int j=0;j<nRepeticiones;j++)	
		 	for (int i=0;i<problemas.length;i++){
		 		System.out.println("\n\n +++++++++++ PROBLEMA "+i+" "+problemas[i]);
		 		BatteriaPruebas bateria = new BatteriaPruebas (problemas[i],soluciones[i]);
			 	bateria.setParamGenetico(poblacion, generaciones);
			 	bateria.run(false);
		 	}
		
		
	 }*/
}
