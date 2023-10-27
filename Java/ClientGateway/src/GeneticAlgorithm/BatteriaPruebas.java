package GeneticAlgorithm;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BatteriaPruebas {
	
	String _file;
	double _solucion;
	int _poblacion = 0, _generaciones=0;
	long TimeConsumed;
	int  generationConsumed =0;
	
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

    	if (print_problema) problemCurrent.printSolution(print_problema);
    	
    	
    	TimeConsumed			 = problemCurrent.getTimeConsumed();
    	generationConsumed   = problemCurrent.getGenerationConsumed();
    	
    	genProblemBasic.clear();
	}
	
	public long getTimeConsumed() {
		return TimeConsumed;
	}
	
	
	public int getGenerationConsumed() {
		return generationConsumed;
	}
	
	public static double Avg (long []timesConsumed) {
		int sumatoria = 0;
		for (int x = 0; x < timesConsumed.length; x++) {
		    sumatoria += timesConsumed[x];
		}
		double avg = sumatoria / timesConsumed.length;
	
		return avg;
	}
	
	public static void printTime (long []timesConsumed) {
		
		System.out.println("\n\n Time:  ");
		
		for (int x = 0; x < timesConsumed.length; x++) {
		    System.out.print(timesConsumed[x]+ " ");
		}
		System.out.println(" ");
	}
	
	public static double best (long []timesConsumed) {
		Arrays.sort(timesConsumed);
		return timesConsumed[0];
	}
	
	public static double worst (long []timesConsumed) {
		return timesConsumed[timesConsumed.length-1];
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
	 	
	    		
	    	String [] problemas ={"cap71.txt", "cap72.txt", "cap73.txt","cap74.txt",
	    						 "cap101.txt","cap102.txt","cap103.txt","cap104.txt",
	    						 "cap131.txt","cap132.txt","cap133.txt","cap134.txt",
	    						 "capa.txt", "capb.txt","capc.txt"};
			double [] soluciones = {932615.750,977799.400,1010641.450,1034976.975,
									796648.438,854704.200,893782.113,928941.750,
									793439.563,851495.325,893076.713,928941.750,
									17156454.478,12979071.581,11505594.329};
						
									
									
								
		 	
		 	int poblacion = 200, generaciones = 5000;
		 	

		 	int nRepeticiones		=	1000;
		 	long [] timesConsumed 			= new long [nRepeticiones];
		 	long [] generacionesConsumed 	= new long [nRepeticiones];
		 	
		 	for (int i=0;i<problemas.length;i++){
		 		System.out.println("\n\n +++++++++++ PROBLEMA "+i+" "+problemas[i]);
		 		
		 		for (int j=0;j<nRepeticiones;j++) {	
		 			System.out.print(" "+j);
		 			BatteriaPruebas bateria  = new BatteriaPruebas (problemas[i],soluciones[i]);
		 			bateria.setParamGenetico(poblacion, generaciones);
		 			bateria.run(false);
		 			timesConsumed [j] 		 = bateria.getTimeConsumed();
		 			generacionesConsumed [j] = bateria.getGenerationConsumed();
		 	}
		 		System.out.println(" \n M Time "+BatteriaPruebas.Avg(timesConsumed)+" "+BatteriaPruebas.best(timesConsumed)+" "+BatteriaPruebas.worst(timesConsumed));
		 		System.out.println(" M Generations "+BatteriaPruebas.Avg(generacionesConsumed)+" "+BatteriaPruebas.best(generacionesConsumed)+" "+BatteriaPruebas.worst(generacionesConsumed));
		 }
		 
		
	 }
}
