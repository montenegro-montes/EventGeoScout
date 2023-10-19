package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Genetico {

		public  Problem 		problemCurrent;
		public final double 	crossRateOriginal	=	0.85;
		public final double 	mutateRateOriginal	=	0.001;

	 	private   double 		crossRate 		= crossRateOriginal; // 0.5;
	    private   double 		mutationRate 	= mutateRateOriginal; //0.01; SMALL o 0.005 Large
	 
	    private   boolean		elitism 		= true;
	    private  final double 	por_elitism		= 0.1;	
	    private  int		 	num_elitism		= 0;	

	    private  int			num_tamañoXover = 0;
	    private  final double 	por_Xover		= 1-por_elitism;	

	    public 	 boolean 		debug			= false;
	    
	    private int				estable 		 	  = 0; //Chequea estabilidad.
	    private final double 	MAX_ITER_ESTABLE_rate = 0.1; // 20% de las iteraciones
	    private int 			MAX_ITER_ESTABLE; 

	    		Individual better;
	    
		private  int _populationSize  = 10;
    	int 		 _maxGeneraciones = 1;
    	int 		 _generationCount = 0;
    	Population 	population;
    	long time;
    	

    	Individual fittest;
    	
    	List<Double> historyFitness, historySTDFitness;
    	
    	public static Fitness fitnessFunction;
	    /************************************************
	     * 
	     * @param populationSize
	     * @param maxGeneraciones
	     * @param problem
	     */

	    public Genetico(int populationSize, int maxGeneraciones,Problem problem ){
	    	 _populationSize	= populationSize;
	    	 _maxGeneraciones	= maxGeneraciones;
	    	 problemCurrent 	= problem;
	    	 fitnessFunction 	= new Fitness(problem);
	    	 population 		= new Population(_populationSize, problemCurrent,por_elitism,true);
	    	 historyFitness 	=  new Vector<Double>();
	    	 historySTDFitness	=  new Vector<Double>();
	    	 _generationCount 	= 0;
	    	 
	    	 
	    	 num_elitism 		=   (int) (_populationSize * por_elitism);
	    	 num_tamañoXover	=   (int) (_populationSize * por_Xover);	
	    	 
	    	 MAX_ITER_ESTABLE	=   (int) (_maxGeneraciones*MAX_ITER_ESTABLE_rate);
	    }	
	    
	    /************************************************
	     * 
	     */
	    public void run(){
	        
	    	 long ini = System.currentTimeMillis();
	    	 setFitness();
	    	 
	    	  if(debug){	
	    	 	System.out.println("RONDA INICIAL"+" sum_fitness: "+population.getSumFitness()+" DESV: "+population.getDesvFitness());
	    	 	population.print();
	    	  }
	    	
	    	  while (!cond_parada()){
	    		 
	    		   
	    		   population				 =	evolvePopulation(population);
	    		   setFitness();
	    		   
		    	
	             if(debug){	
	            	 System.out.println("RONDA "+_generationCount+" sum_fitness: "+population.getSumFitness()+" "+population.getDesvFitness());
	 	             population.print();
	 	    	  }
	             
	             checkEstabilidad();
	             
	             _generationCount++;
	             
	          }
	       
    		 better =	fittest;
    		 long fin = System.currentTimeMillis();
    		 
    		 time = fin-ini;
	    }
	    
	   /*********************************************************************************************************
		* 
		*/
  
	   public void checkEstabilidad(){
		  
		   if ((int)population.getDesvFitness() > 0){ //NO SE HA ESTABILIZADO
          	 elitism = true;
           }
           else{
          	 mutationRate += 0.0001;
          	 crossRate = (crossRate < 1) ? crossRate+0.001 : 1;
          	 if (crossRate ==1 ) crossRate = crossRateOriginal;
          	 elitism =	false;
          	 //System.out.println("* "+_generationCount+" ESTABLE STD: "+population.getDesvFitness()+" "+mutationRate+ " "+crossRate+" elite "+elitism);
           }
	   }
	    
	 /*********************************************************************************************************
	  * 
	  * @return
	  */
	    private boolean cond_parada(){
	    	boolean ret=false;
	    	
	    	if (_generationCount < _maxGeneraciones){
	    		if (problemCurrent.isKnownSolution() & (fittest.getFitness()==problemCurrent.getFitnesSolution())) 
	    			ret= true; //SALIDA POR SOLUCIÓN CONOCIDA
	    		else {
	    			if (!problemCurrent.isKnownSolution()){
	    				if (_generationCount>1){

	    					if (Double.compare(historyFitness.get(_generationCount-1), historyFitness.get(_generationCount))==0){
	    					 estable++; 
	    				   }
	    				   else estable=0;
	    				}
	    				if (estable >= MAX_ITER_ESTABLE) ret=true; //SALIDA POR SOLUCIÓN ESTABLE
	    			}
	    		}
	    	}
	    	else ret= true; //SALIDA POR MAX ITERACIONES
	
	    	
	    	return ret;
	    }
	    
		/*********************************************************************************************************
		  * 
		  * @return
		 */

	    public int getGenerationCount (){
	    	return _generationCount;
	    }
	  
	    /************************************************
	     * 
	     * @param oldPopulation
	     * @param newPopulation
	     */
	   
	    private void copyElitism (Population oldPopulation,Population newPopulation){
	    	
	    	 
	    	 if (elitism){
		    	for (int i=0;i<num_elitism;i++)
		    		newPopulation.addIndividual(oldPopulation.getIndividual(i));
	    	}
	    }
	    
	    /************************************************
	     * 
	     * @param PopulationTarget
	     * @param PopulationOrigin
	     */

	    private void copy (Population PopulationTarget,List<Individual>PopulationOrigin){
	    	int leng = PopulationOrigin.size();
	    	
	    	for (int i=0;i<leng;i++) 
	    		PopulationTarget.addIndividual(PopulationOrigin.get(i));
	    	
	    	PopulationOrigin.clear();
	    }


	    /************************************************
	     * 
	     * @param Population
	     * @return
	     */

	    private  Population evolvePopulation(Population Population) {

	     	int elitismOffset;
	        if (elitism) elitismOffset = num_elitism;
	        else  		 elitismOffset = 0;
	      
	     	Population newPopulation = new Population(_populationSize,problemCurrent,por_elitism,false);
	    	copyElitism	(Population, newPopulation);

	     	List<Individual>  xoverselected	= selected_crossover(Population, num_tamañoXover);
	     	List<Individual>  crossover		= CrossOver(xoverselected, Population);
	    	copy(newPopulation,crossover);
	    	xoverselected.clear(); crossover.clear();
	    	
	    	int padding_size			= _populationSize-newPopulation.size();
	    	List<Individual> padding	= ramdonPadding(Population,padding_size);
	    	copy(newPopulation,padding);
	    	padding.clear();
	    	
	        newPopulation.mutate(elitismOffset, mutationRate);

	        Population.clear();
	        return newPopulation;
	    }
	    
	    /*************************************************************************
	     * 
	     * @param Population
	     * @param tamaño
	     * @return
	     */
	      public List<Individual> ramdonPadding (Population Population,int tamaño ){
	    	  List<Individual>  newPopulation = new ArrayList<Individual> ();
	          Individual newIndiv;	
	          
	          boolean local = false;

	          for (int i=0;i < tamaño;i++){   //Selection
		    	   newIndiv	=	Population.generateRamdonIndividual(local);
		           newPopulation.add(newIndiv);
		      }
		      
		      return newPopulation;
	    }
	 
	    /*************************************************************************
	     * 
	     * @param pop
	     */
	    private void setFitness(){

	    	
	    	population.calculateFitness();
	    	
	    	fittest 		=	population.getFittest();
 		   
	    	historyFitness.add(fittest.getFitness());
	    	historySTDFitness.add(population.getDesvFitness());

	    }
	    /*************************************************************************
	     * 
	     */
	 
	  private List<Individual> CrossOver(List<Individual> Population, Population oldpopulation){
        List<Individual> newPopulation = new ArrayList <Individual>();
        
    	List <Integer> selected   = new Vector<Integer>();
        for (int i=0;i<Population.size();i++) selected.add(i);
   
    	
    	if (selected.size()%2!=0) selected.remove(0);
    	
    	for (int i=0;i<selected.size();i+=2){
    		
    		int posInd1=selected.get(i), posInd2=selected.get(i+1);

    		Individual ind1 =	Population.get(posInd1);
    		Individual ind2 =	Population.get(posInd2);
    		
    		if (ind1.isEqual(ind2)) ind2= oldpopulation.generateRamdonIndividual(false);

    		
    			Individual [] newInd;
    			//	 crossoverOnePoint(ind1,ind2); 
    			//	crossoverUniform(ind1, ind2); 
    			// crossoverTwoPoints(ind1,ind2); 
    		 
    		 if (RandomSingleton.getInstance().nextDouble() < crossRate) {
    			  newInd	= crossoverTwoPoints(ind1,ind2);
    			  newInd [0].setXover(ind1.getID(), ind2.getID());
    			  newInd [1].setXover(ind2.getID(), ind1.getID());
    		 }
    		 else{
    			 
    			 newInd 	= 	new Individual [2];
    			 newInd[0]  =  new Individual (ind1);
    			 newInd[1]  =  new Individual (ind2);
    			 newInd [0].setXover(ind1.getID(), ind1.getID());
    			 newInd [1].setXover(ind2.getID(), ind2.getID());

    		 }
    		
    		newPopulation.add(newInd[0]);
    		newPopulation.add(newInd[1]);
   
    	}
    
    	return newPopulation;
    }
    
	    /*************************************************************************
		 * 
		 * @param indiv1
		 * @param indiv2
		 * @return
		 */
	    @SuppressWarnings("unused")
		private  Individual [] crossoverUniform(Individual indiv1, Individual indiv2) {
	    	
	    	double p_mask		=	0.5;
	    	
	    	Individual [] newSol = new Individual [2];
	    	
	         
	        if (RandomSingleton.getInstance().nextDouble() <= crossRate){
	        	
	        	 newSol [0] = new Individual(problemCurrent.getNumFacilities(),problemCurrent.getNumClients());
		         newSol [1] = new Individual(problemCurrent.getNumFacilities(),problemCurrent.getNumClients());
		         
		        for (int i = 0; i < indiv1.size(); i++) {
		        
		        	if (RandomSingleton.getInstance().nextDouble() <= p_mask) {
		                newSol[0].setGene(i, indiv1.getGene(i));
		                newSol[1].setGene(i, indiv2.getGene(i));
		            } else {
		                newSol[0].setGene(i, indiv2.getGene(i));
		                newSol[1].setGene(i, indiv1.getGene(i));
		            }
		        }
		        
		       
	        }
	        else{
	        	newSol [0] =indiv1;
	        	newSol [1] =indiv2;
	        }
	        return newSol;
	    }
	    
	    /*************************************************************************
	     * 
	     * @param indiv1
	     * @param indiv2
	     * @return
	     */

	    @SuppressWarnings("unused")
		private  Individual[] crossoverOnePoint(Individual indiv1, Individual indiv2) {
	    	
	    	Individual [] newSol = new Individual [2];
	    	int len =indiv1.size();
	         newSol [0] = new Individual(problemCurrent.getNumFacilities(),problemCurrent.getNumClients());
	         newSol [1] = new Individual(problemCurrent.getNumFacilities(),problemCurrent.getNumClients());
	       
	         int point= RandomSingleton.getInstance().nextInt(len);
	            
	        for (int i = 0; i < point; i++) {
	                newSol[0].setGene(i, indiv1.getGene(i));
	                newSol[1].setGene(i, indiv2.getGene(i));
	        }

	        for (int i = point; i < len; i++) {
                newSol[0].setGene(i, indiv2.getGene(i));
                newSol[1].setGene(i, indiv1.getGene(i));
	        }

	        return newSol;
	    }

	    /*************************************************************************
	     * 
	     * @param indiv1
	     * @param indiv2
	     * @return
	     */

	    private  Individual[] crossoverTwoPoints(Individual indiv1, Individual indiv2) {
	    	
	    	Individual [] newSol = new Individual [2];
	    	
	         newSol [0] = new Individual(problemCurrent.getNumFacilities(),problemCurrent.getNumClients());
	         newSol [1] = new Individual(problemCurrent.getNumFacilities(),problemCurrent.getNumClients());
	       
	         int point1= (int) (RandomSingleton.getInstance().nextDouble()*indiv1.size());
	         int point2= (int) (RandomSingleton.getInstance().nextDouble()*indiv1.size());
	         
	         int swap;
	         if (point1 > point2) {
	        	 swap   = point1;
	        	 point1 = point2;
	        	 point2 = swap;
	         }
	         
	        for (int i = 0; i < point1; i++) {
	                newSol[0].setGene(i, indiv1.getGene(i));
	                newSol[1].setGene(i, indiv2.getGene(i));
	        }
	        
	        for (int i = point1; i < point2; i++) {
	        	 newSol[0].setGene(i, indiv2.getGene(i));
	             newSol[1].setGene(i, indiv1.getGene(i));
	        }

	        for (int i = point2; i < indiv1.size(); i++) {
                newSol[0].setGene(i, indiv1.getGene(i));
                newSol[1].setGene(i, indiv2.getGene(i));
	        }

	        return newSol;
	    }

	 

	    /*************************************************************************
	     * Mucho Mejor que random 
	     * @param oldPopulation
	     * @param tamaño
	     * @return
	     */
	    private  List <Individual> selected_crossover(Population oldPopulation,int tamaño) {
	    	
	    	List <Individual> selectedList = new ArrayList<Individual>();
	    	
	    	for (int i=0;i<tamaño;i++){
	    		selectedList.add( new Individual(oldPopulation.getIndividual(oldPopulation.Roulette_wheel_int())));
	    		//selectedList.add(new Individual(oldPopulation.getIndividual(RandomSingleton.getInstance().nextInt(oldPopulation.size()))));
	    	}
	    	
	    	return selectedList;
	    }    

	 
	    
	    /*************************************************************************
	     * 
	     * @return
	     */
		       
	    public Individual getSolution(){
	    	return better;
	    }
	    
	    public boolean[] getSolutionVector(){
	    	return better.genes;
	    }
	    
	    /*************************************************************************
	     * 
	     * @return
	     */
  
  		public long getTimeConsumed(){
  			return time;
  		}
  		
  		/*************************************************************************
  		 * 
  		 * @return
  		 */
  		public  List<Double> getHistoryFitness(){
  			return historyFitness;
  		}
  		
  		
  		/*************************************************************************
  		 * 
  		 * @return
  		 */
  		public  List<Double> getHistorySTDFitness(){
  			return historySTDFitness;
  		}
  		
  		public void clear(){
  	    	historyFitness.clear();
  	    	historySTDFitness.clear();
  		}

}