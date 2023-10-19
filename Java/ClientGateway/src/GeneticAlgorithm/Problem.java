package GeneticAlgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import ClientFerature.geneticLog;

public class Problem {

	int _facilities; //Num_Facilities
	int _clients; //Num_Clients
	
	double [] 	 _fixed_costs;
	double [] [] _transport_cost;

	Individual 		_sol	=	null;
	double 			_fitness_sol;
	long 			_timeConsumed;
	int 			_poblacion,  _generaciones,_generacionSol;
	List<Double> 	_fitnessHistory, _fitnessSumHistory;
	boolean 		knowSolution=false;
	private 		double 	maxCost 	=-1;
	double 			t					=0.0;
	Map<Integer,Integer> minHash	=	null;
	geneticLog 		_log=null;
	boolean 		solutionFound	= false;
	
	public Problem (String file) throws IOException{
		
	
		 	Reader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(file));
		 	BufferedReader br = new BufferedReader(reader);

		 
		 //	BufferedReader br = new BufferedReader( ClassLoader.getSystemResourceAsStream(file));
    	    
    	    String line = br.readLine();
    	    String[] elements = line.split(" ");
    	    
    	    int warehouse  =  Integer.parseInt(elements[1]);
    	    int clients	   =  Integer.parseInt( elements[2]);
    	    int warehouse_cont=0,	clients_cont=0;
    	  
    	    double [] costs = new double [warehouse];
    	    double [] capacity = new double [warehouse];
    	    double [][] costsTransp = new double [clients][warehouse];
    	    double [] demand = new double [clients];
    	    int j=-1;
	        int i=0;
     	   
    	    //System.out.println("clients: "+clients);

    	    while (line != null) {
    	      
    	      line = br.readLine();
    	      if (line!=null) {
    	        elements = line.split(" ");
    	        
    	        if(warehouse_cont < warehouse){
    	        	if (elements[1].equalsIgnoreCase("capacity")) capacity[warehouse_cont] =0;
    	        	else capacity[warehouse_cont] = Double.parseDouble(elements[1]);
	    	        costs[warehouse_cont] = Double.parseDouble(elements[2]);
	    	        warehouse_cont++;
    	        }
    	        else{
    	        	if (elements.length==2&&(i!=(clients-1))){
    	        		//System.out.println(line);
    	        		demand[clients_cont]=Double.parseDouble(elements[1]);
    	        		clients_cont++;
    	        		i=0; j++;
    	        	}
    	        	else{
    	        		for (int aux=1;aux<elements.length;aux++){
    	        			costsTransp[j][i] = Double.parseDouble(elements[aux]);
    	        			i++;
    	        		}
    	        	}
    	        }
    	       }
    	    }
    	    
    	     _facilities = warehouse; //Num_Facilities
    		 _clients = clients; //Num_Clients
    		 
    		 setFixedCost(costs);
    		 setTransportCost(costsTransp);
    		 br.close();
	}
	
	public Problem (int n, int m){
		_facilities = n;
		_clients = m;
		
		_fixed_costs 	= new double [n];
		_transport_cost = new double [n][m];
	}

	
	public void setSeed(int seed){
		RandomSingleton.clear();
		RandomSingleton.setSeed(seed);
	}
	
	public void setFixedCost (double [] fixed_costs){
		_fixed_costs =fixed_costs;
	}

	public void setTransportCost (double [] [] transport_cost){
		 _transport_cost = transport_cost;
	}

	public double [] getFixedCost (){
		return _fixed_costs;
	}
	
	public double [] [] getTransportCost(){
		return _transport_cost;
	}
	
	public int getNumFacilities (){
		return _facilities;
	}
	
	public int getNumClients(){
		return _clients;
	}
	
	public void setFindSolution (Individual sol){
		_sol =	sol;
	}
	
	public void setFitnesSolution (double fitness_sol){
		_fitness_sol =	fitness_sol;
		knowSolution = 	true;
	}
	
	public double getFitnesSolution (){
		return _fitness_sol;
	}
	public boolean isKnownSolution(){
		return knowSolution;
	}
	
	public void setTimeConsumed(long timeConsumed){
		_timeConsumed = timeConsumed;
	}
	
	public void setGeneticParams (int poblacion, int generaciones ){
		_poblacion 	  = poblacion;
		_generaciones = generaciones;
	}
	
	public void setHisttoryFitness (List<Double> fitnessHistory){
		_fitnessHistory 	  = fitnessHistory;
	}

	public void setHisttoryFitnessEliStd (List<Double> fitnessSumHistory){
		_fitnessSumHistory 	  = fitnessSumHistory;
	}
	
	public void setGenerationCount (int count){
		_generacionSol 	  = count;
	}
	
	
	public void printSolution (boolean problema){
		
	if (problema){	
		salida_ln("\n\n********* PROBLEM ********");
		salida_ln("Warehouse: "+_facilities);
		salida_ln("Clients:  "+_clients);
		salida("Fix Costs: ");
		for (int i=0;i<_facilities;i++)
			salida(_fixed_costs[i]+" ");
		
		salida_ln("\nTransport Costs: ");
		for (int j=0;j<_clients;j++){
			for (int i=0;i<_facilities;i++)
				System.out.print(_transport_cost[j][i]+" ");
			salida_ln(" ");
		}
	}	
		if(knowSolution)
			if (_sol.getFitness()== _fitness_sol){
				solutionFound = true;
				salida_ln("\n******* SOLUTION FOUND**********");
			}
			else
				salida_ln("\n******* SOLUTION NOT FOUND **********");
		else salida_ln("\n******* STABLE SOLUTION **********");
		
		if (_sol==null){
			salida_ln("Solution not  known");
		}else{
			salida_ln("Indivual: "+_sol);
			salida_ln("fitness: "+ _sol.getFitness());
			salida("Warehouse open(s) [start on 0]: ");
			
			for (int i=0;i<_facilities;i++){
				if (_sol.getGene(i)) salida(i+ " ");
			}
			salida_ln("");
			if (problema){		
				
				int [] assig= clientAsign();
				for (int i=0;i<_clients;i++){
					System.out.print("Cliente "+i+": "+assig[i]+" ");
					if (i>1 & i%7==0) salida_ln("");
				}
			}
		}
		
		salida_ln("\nPoblation: "+_poblacion+ " Max Generation: "+_generaciones + " Generation stopped: "+_generacionSol);
		salida_ln("Time (milliseconds): "+_timeConsumed+" Seed Random: "+RandomSingleton.getInstance().getSemilla());
		
		salida_ln("\nRecord Fitness:");
		
		int length=_fitnessHistory.size();
		for (int i=0;i<length;i++){
			salida(_fitnessHistory.get(i)+"; ");
		}
		
		
	salida_ln("\nElitism Record Fitness:");
		
		int lengthSum=_fitnessSumHistory.size();
		for (int i=0;i<lengthSum;i++){
			salida(_fitnessSumHistory.get(i)+"; ");
		}
	}
	
	
	private void calculate_t (){

		double denominador=0;
		
		for (int i=0;i<_facilities;i++){
			denominador+=_fixed_costs[i];
		}
		denominador =denominador/_facilities;
		
		double divisor=0;
		
		for (int j=0;j<_clients;j++){
			for (int i=0;i<_facilities;i++){
				divisor+=_transport_cost[j][i];
			}
		}
		
		divisor = divisor / (_facilities*_clients);
		
		t= (denominador/divisor);
	}
	
	public double get_t(){
		if (t==0.0) calculate_t();
		return t;
	}
	
	private double calculateMaxCost (){
		double maxCost=0;
		
		for (int ind = 0; ind < _facilities; ind++){ 
			maxCost 	+= _fixed_costs[ind];
			for (int j=0;j<_clients;j++)
				maxCost 	+= _transport_cost [j][ind];
		}
		
		return Math.round(maxCost);	
	}
	
	public double getMaxCost(){
		if (maxCost==-1)	maxCost = calculateMaxCost();
		
		return maxCost;
	}
	
	
	public int [] clientAsign (){
		
		int [] clientAsignMin = new int [_clients];
		double current=0;
		boolean[] genes = _sol.getGenes();

		for (int j=0;j<_clients;j++ ){
        		  double min	=	Integer.MAX_VALUE; int min_j=0;
        		  for (int i = 0; i < _facilities; i++) {
        			  if (genes[i])  {
		        		  current 	= _transport_cost[j][i];
		        		  if (current < min) {
		        			  min = current;
		        			  min_j = i;
		        		  }
		        	  }
		          }
        		  clientAsignMin[j]=min_j;	  
    	}
		return clientAsignMin;
		
	}	
	
	public void setLog(geneticLog log){
		_log = log;
	}
	
	public void salida_ln (String out){
		if (_log==null) System.out.println(out);
		else _log.addLog(out);
	}
	
	public void salida (String out){
		if (_log==null) System.out.print(out);
		else _log.addLog_S(out);
	}
	
	public boolean isSolutionFound(){
		return solutionFound;
	}
}
