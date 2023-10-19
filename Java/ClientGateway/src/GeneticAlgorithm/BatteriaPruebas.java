package GeneticAlgorithm;

import java.io.IOException;
import java.util.List;

public class BatteriaPruebas {
	
	String _file;
	double _solucion;
	int _poblacion = 0, _generaciones=0;
	
	public BatteriaPruebas (String file,double solucion){
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
    	problemCurrent.setHisttoryFitnessEliStd(genProblemBasic.getHistorySTDFitness());

    	problemCurrent.printSolution(print_problema);
    	
    	genProblemBasic.clear();
	}
	
	 public static void main(String[] args) throws IOException {

	    	/**************************************
	    	 *  Parámetros son:
	    	 *  -----------
	    	 *  int	 	 n		= numero de facilities
	    	 *  Vector c_n		= costes fijos de facilities.
	    	 *  int      m		= numero de clientes/zonas
	    	 *  Matriz   h_ij	= costes de desplazamiento cliente j a facilities i.
	    	 * **************************************
	    	 */
		/* Warehouse: 6
		 Clientes:  8
		 Costes Fijos: 29.0 196.0 241.0 949.0 385.0 357.0 
		 Costes Desplazamiento: 
		 219.62216620615933 371.4461022141098 354.9633979378833 188.52374898037138 38.396895131596544 306.692587566809  
		 137.84130821319806 285.32783513707403 273.22719406972146 145.95542855258842 71.50547427599622 222.3013570754517  
		 100.36066625303033 182.51140086154183 201.25528776627235 199.24599968533596 178.41708283928898 102.33609064345852  
		 167.98164279175901 330.28556887146686 292.2802212189338 91.39542103265921 125.75671846330327 292.2603942862563  
		 82.36527742513093 81.56258940997219 68.68517828082763 192.3620660304472 284.11770762333754 103.1017901063886  
		 183.12034735808953 77.82806888231735 50.86665617304768 269.5759397745284 390.28772397785195 173.83716370977245  
		 34.648596222871625 169.90987005455943 124.99261611907835 103.73192260689697 228.7691063804953 167.5598182272201  
		 105.02802173253049 95.15763934865137 132.802286539909 226.74253176806502 263.0258918812148 37.39839654670215  */
	    	
		 	int wharehouses =	6;
	    	int clients		= 	8;
	    	double [] fixed_costs 		= {	29.0, 196.0, 241.0, 949.0, 385.0, 357.0 }; 
	    	double [] [] transport_cost = {
							    			{219.62216620615933, 371.4461022141098, 354.9633979378833, 188.52374898037138, 38.396895131596544, 306.692587566809  },
							    			{137.84130821319806, 285.32783513707403, 273.22719406972146, 145.95542855258842, 71.50547427599622, 222.3013570754517 },
							    			{100.36066625303033, 182.51140086154183, 201.25528776627235, 199.24599968533596, 178.41708283928898, 102.33609064345852  },
							    			{167.98164279175901, 330.28556887146686, 292.2802212189338, 91.39542103265921, 125.75671846330327, 292.2603942862563  },
							    			{82.36527742513093, 81.56258940997219, 68.68517828082763, 192.3620660304472, 284.11770762333754, 103.1017901063886},
							    			{183.12034735808953, 77.82806888231735, 50.86665617304768, 269.5759397745284, 390.28772397785195, 173.83716370977245},
							    			{34.648596222871625, 169.90987005455943, 124.99261611907835, 103.73192260689697, 228.7691063804953, 167.5598182272201},
							    			{105.02802173253049, 95.15763934865137, 132.802286539909, 226.74253176806502, 263.0258918812148, 37.39839654670215}
	    								  };
	    			
	    	Problem problemCurrent	= new Problem (wharehouses,clients);
	    	problemCurrent.setFixedCost(fixed_costs);
	    	problemCurrent.setTransportCost(transport_cost);
	    	
	    	//double solucion = 6.5;
	    	//problemCurrent.setFitnesSolution(solucion);
		 	int _poblacion = wharehouses*2, _generaciones = clients*100;

	    	Genetico genProblemBasic= new Genetico (_poblacion,_generaciones,problemCurrent); 
	    	//genProblemBasic.debug	=true;
	    	genProblemBasic.run();
	    	Individual sol			 = genProblemBasic.getSolution();
	    	long timeConsumed 		 = genProblemBasic.getTimeConsumed();
	    	List <Double> fitnessHistory = genProblemBasic.getHistoryFitness();
	    	int generationCountSol	 = genProblemBasic.getGenerationCount();
	    			 
	    	problemCurrent.setGeneticParams(_poblacion, _generaciones);
	    	problemCurrent.setFindSolution(sol);
	    	problemCurrent.setTimeConsumed(timeConsumed);
	    	problemCurrent.setHisttoryFitness(fitnessHistory);
	    	problemCurrent.setHisttoryFitnessEliStd(genProblemBasic.getHistorySTDFitness());

	    	problemCurrent.setGenerationCount(generationCountSol);

	    	problemCurrent.printSolution(true);
		 
	        
	    	/*int wharehouses =	5;
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

	    	problemCurrent.printSolution(true);*/
	    	
		 	//String [] problemas ={"cap131.txt","cap132.txt","cap133.txt","cap134.txt"};
			//double [] soluciones = {793439.563,851495.325,893076.713,928941.750};
		 	
		 	
		 	/*String [] problemas ={"capa.txt"};
		 	double [] soluciones = {17156454.478}; */
		 	
	    		
	    	/*String [] problemas ={"cap71.txt", "cap72.txt", "cap73.txt","cap74.txt",
	    						 "cap101.txt","cap102.txt","cap103.txt","cap104.txt",
	    						 "cap131.txt","cap132.txt","cap133.txt","cap134.txt"};
			double [] soluciones = {932615.750,977799.400,1010641.450,1034976.975,
									796648.438,854704.200,893782.113,928941.750,
									793439.563,851495.325,893076.713,928941.750};*/
									  
		 	/*String [] problemas = {"capa.txt", "capb.txt","capc.txt"};
		 	double [] soluciones ={17156454.478,12979071.581,11505594.329};*/

		
		 /*	int poblacion = 200, generaciones = 5000;
		 	

		 	int nRepeticiones	=	2;
		 	
		 for (int j=0;j<nRepeticiones;j++)	
		 	for (int i=0;i<problemas.length;i++){
		 		System.out.println("\n\n +++++++++++ PROBLEMA "+i+" "+problemas[i]);
		 		BatteriaPruebas bateria = new BatteriaPruebas (problemas[i],soluciones[i]);
			 	bateria.setParamGenetico(poblacion, generaciones);
			 	bateria.run(false);
		 	}*/
		
		
	 }
}
