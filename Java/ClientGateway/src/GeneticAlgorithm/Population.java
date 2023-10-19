package GeneticAlgorithm;

import java.util.List;
import java.util.Vector;


public class Population {

    List <Individual>	individuals;
    
    int 	 			_length_individuals;
    int 				_length_client;
    int  				_populationSize;
    Problem  			_problem;
    Individual 			_fittest		=null;
    
    int					_numIndStd 		  = 30; // Numero de individuos para hacer std. Puesto para poblacion 300 y 0.1 elitismo
    double 				sum_fitnessElitim =	0;
    /************
     * JAPO
     */
    double _t			=	0;
    
    double max_fitness	=	Double.MIN_VALUE,
    	   min_fitness	=	Double.MAX_VALUE,
    	   sum_fitness	=   0;
    /**********************************************
     * 
     * @param populationSize
     * @param initialise
     */
    
	public Population(int populationSize,Problem problem, double por_elitism,boolean initialise) {
    	_problem			= 	problem;
    	_length_individuals = 	problem.getNumFacilities();
    	_length_client		=	problem.getNumClients();
    	_populationSize		=	populationSize;
        individuals 	    = 	new Vector <Individual>();
        _fittest			=	null;
        
        if (initialise) {
        	
        	 _t = problem.get_t();
        	 
        	 boolean local = true;

        	 
        	for (int i=0;i<_populationSize;i++){
        	    addIndividual(generateRamdonIndividual(local) );
            }
        	
        	java.util.Collections.sort(individuals);

        	

        }
         max_fitness	=	Double.MIN_VALUE;
         min_fitness	=	Double.MAX_VALUE;
         sum_fitness	=   0;
         sum_fitnessElitim	= 0;
         _numIndStd 	=   (int) (_populationSize * por_elitism);
         

    }
    /**********************************************
     * 
     * @param index
     * @return
     */
    public Individual getIndividual(int index) {
    	return individuals.get(index);
    }
    /**********************************************
     * 
     * @return
     */
     public Individual getFittest() {
        
         java.util.Collections.sort(individuals);
         return individuals.get(0);
        
        //return _fittest;
    }
   
    /**********************************************
    * 
    * @return
    */
    public int size() {
        return individuals.size();
    }
    
    
    
    
     /**********************************************
      * 
      * @param index
      * @param indiv
      */
    
    public void addIndividual(Individual indiv) {
    	individuals.add(indiv);
	}
    
   

    /**********************************************
     * IMPORTANTE
     * @param index
     */
    public void calculateFitness() {
    	Individual temp;
    	double fit;
    	sum_fitness	=	0;
    	_populationSize = individuals.size();
    	//System.out.println("POPULATION SIZE "+ _populationSize);		
    	for (int index = 0; index < _populationSize ; index++) {
    		
    		temp = individuals.get(index);
    		
    		fit 	= temp.getFitness();
    		
    		if (fit > max_fitness){
    			max_fitness = fit;
    		}
	    	if (fit < min_fitness) {
	    		min_fitness = fit; 
	    		_fittest 	= temp; 
	    	}
	    	
	    	sum_fitness += fit;
	    	if (index<this._numIndStd) this.sum_fitnessElitim += fit;
    	}
    
    }
   
    /**********************************************
     * 
     */

    public void print (){
       
    	int len= individuals.size();
 	   
    	for (int i=0; i<len;i++){
    		System.out.println("i: "+i+" "+individuals.get(i));
 	   }
     }
    /**********************************************
     * 
     * @param elitismOffset
     * @param mutationRate
     */
 
    public void mutate (int elitismOffset,double mutationRate){
 	   
 	   int length= individuals.size();
 	   
 	   
 		   for (int i = elitismOffset; i < length; i++) {
 				   		individuals.get(i).mutate(mutationRate);
 		  }	   
 	  
    }
    
   
    
   /**********************************************
    * Cuidado con la población baja, p.ej. 1
    * @return
    */

    
    public double getDesvFitness(){ //Desv típica de los numIndStd primeros.
        double resta,temp,cont=0;
        double sol = Double.MAX_VALUE;
        
        if (_numIndStd>1){
	    	double media=this.sum_fitnessElitim/_numIndStd;
	    		
	    		for (int i=0;i<_numIndStd;i++){
	    			temp = individuals.get(i).getFitness() - media;
	    			resta = temp * temp;
	    			cont += resta;
	    		}
	    	
	    	double div = cont / (_numIndStd-1);
	    	sol = java.lang.Math.sqrt(div);
    	}
    	return sol;
    }
    
    /**********************************************
     * 
     * @return
     */


    public double getSumFitness(){
    	return sum_fitness;
    }
    
    /**********************************************
     * 
     * @return
     */

    public Individual Roulette_wheel (){
    	return individuals.get(Roulette_wheel_int());
    }
    
    /**********************************************
     * 
     * @return
     */

    
    public int Roulette_wheel_int (){
    	double r= RandomSingleton.getInstance().nextDouble();
    	double p= r*getSumFitness();
    	double t = max_fitness+min_fitness;
    	
    	int i=0;
    	for (i=0; i<_populationSize;i++){
    		p-=(t-individuals.get(i).getFitness());
    		
    		if (p<0) break;
    	}
    	
    	if (i==_populationSize) i--;
    	
    	return i;
    }
    /*********************************************
     * 
     */
    public void clear () {
    
        individuals.clear();
    }
    
   
    /********************************************************
     * 
     * 
     * @return
     */
    public Individual generateRamdonIndividual(boolean local){
    	 Individual newIndividual = new Individual(_length_individuals,_length_client);
         
         if (local) newIndividual.generateIndividual(_t); //JAPO
         else       newIndividual.generateIndividual();

         return newIndividual;
    }
    
    /********************************************************
     * 
     * @param numIndStd
     */

    public void setNumIndStd (int numIndStd){
    	_numIndStd = numIndStd;
    }
    
}