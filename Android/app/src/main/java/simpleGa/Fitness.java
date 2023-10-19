package simpleGa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Fitness {
	private  	double [] 	_fixedCost; 		
	private  	double [][] _transportCost;
	private     int 		_clients;
	private 	double 		fitnessMAX   = 0;
	private 	final int  	numDecimales = 3;
	private 	Map<Individual,Double> _cacheFitness;
	
	/***************************************************************************
	 * fitnessMAX asignaremos a aquellos elementos que quiero penalizar
	 * 
	 * @param problema
	 */
	public Fitness (Problem problema){
		_clients		=	problema.getNumClients();
		_fixedCost 		= 	problema.getFixedCost();
		_transportCost 	=	problema.getTransportCost();
		_cacheFitness 	=   new HashMap<Individual,Double>();

		fitnessMAX		= problema.getMaxCost();
	}

	
	/***************************************************************************
	 * 
	 * @param genes
	 * @return
	 */
	
	public  double  setFitness (Individual individuo){
		
		if (_cacheFitness.containsKey(individuo)){ //ELEMENTO YA CALCULADO
			//System.out.println("CACHED :::::" +individuo+"  "+_cacheFitness.get(individuo));
			return fitnessMAX;//_cacheFitness.get(individuo); 
		}
		else{	
			
			boolean[] genes = individuo.getGenes();
	    	double fitness=0.0;
	    	double current;
	
			int length_facilities 	= genes.length;
	        int length_clients 		= _clients;
	        
	        for (int ind = 0; ind < length_facilities; ind++) 
	      		  if (genes[ind]) fitness 	+= _fixedCost[ind];
	  		
	        if (fitness==0){ //NO TENEMOS NINGUN LOCAL ABIERTO.
	         	 fitness 	= fitnessMAX;
	        }
	        else{
	        	for (int j=0;j<length_clients;j++ ){
		        		  double min	=	Integer.MAX_VALUE; 
		        		  for (int i = 0; i < length_facilities; i++) {
		        			  if (genes[i])  {
				        		  current 	= _transportCost[j][i];
				        		  if (current < min)  min = current;
				        	  }
				          }
		        	  fitness+=min;
	        		}
	      	  fitness= round(fitness, numDecimales); //Redondea tres decimales
	      	}
	        
	        _cacheFitness.put(individuo,fitness);
	
    	return fitness;
    }
	}
	

	
	/***************************************************************************
	 * 
	 * @param value
	 * @param places
	 * @return
	 */

	private  double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
