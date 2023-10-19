package simpleGa;

public class Individual implements Comparable<Object>{

    int 				length			= 0; 
    public boolean[] 	genes			= null;			
    private double 		_fitness 		= 0;
    int 				_client			= 0;
    int 				_hashCode		= 0;
    static int 			ID				= 0;
    int					myID			= 0;
    boolean 			xOver			= false;
    int []				xOverp			= null;
    boolean				mutation		= false;

    @SuppressWarnings("unused")
	private Individual (){
    	
    }
  
 
    
    public  Individual (Individual other){
    	length 		= other.length;
    	_client 	= other._client;
    	_fitness 	= other._fitness;
    	_hashCode 	= other._hashCode;
    	genes 		= new boolean[length];

    	for (int i = 0; i < length; i++) {
            genes[i]=other.genes[i];
        }
    	
    	ID++;
    	myID		= ID;
    }
    
    
    public Individual(int n,int m){
    	
    	length	=n;
    	genes = new boolean[length];
    	_client = m;
    	
    	for (int i=0;i<length;i++)
    		genes[i]=false;
    	
    					  ID++;
    	myID			= ID;
    }

    /*******************
     * 
     */
    public void generateIndividual() {
    	
        for (int i = 0; i < length; i++) {
        	boolean gene = RandomSingleton.getInstance().nextBoolean();
            genes[i]=gene;
        }
    }
   
    /*****************
     * PAPER JAPOS
     * 
     * @param t
     */
    
    public void generateIndividual(double t) {
    	double cota = 1.5;
    	double ro1	= 0.07, ro2	=	0.3;
    	
        for (int i = 0; i < length; i++) {
        	double r = RandomSingleton.getInstance().nextDouble();
            
        	if (t>=cota) {
        		if (r<=ro1) genes[i]=true;
        		else		genes[i]=false;
        	}
        	else {
        		if (r<=ro2) genes[i]=true;
        		else		genes[i]=false;
        	}
        }
    }
  

    public boolean getGene(int index) {
        return 		genes[index];
    }

    public void setGene(int index, boolean value) {
    	genes[index]=value;
        _fitness = 0;
    }

    public int size() {
        return length;
    }
    
    public void setFitness (double fitness){
    	_fitness = fitness;
    }
    
    private double calculateFitness() {
    	
    	_fitness 	= Genetico.fitnessFunction.setFitness(this);

    	return _fitness;
    }
    
     
    public double getFitness() {

          if (_fitness ==0){
        	 _fitness= calculateFitness();
          }
          return _fitness;
    }

   
    @Override
    public String toString() {
        String geneString = " FITNESS: "+_fitness+" ID: "+myID;
        
        if (xOver) 		
        	geneString += "	Xover "+ this.xOverp[0]+ " "+this.xOverp[1];
        if (mutation)	geneString += " Mutado";
      
        for (int i = 0; i < size(); i++) {
            geneString +=" "+getGene(i);
        }
        
       
        return geneString;
    }
     
  

    
    public boolean equals(Object o) {
        Individual another = (Individual)o;

        for(int i=0; i<length;i++){
            if (genes[i] != another.getGene(i)) return false;
        }
        return true;
    }
    
    public int hashCode() {
    	
    	if (_hashCode==0){
    		
	        final int prime = 31;
	        _hashCode = 1;
	        
	        for(Boolean gen : genes){
	        	_hashCode = prime * _hashCode +  gen.hashCode();
	        }
    	}
        return _hashCode;
    }
    
   
    
    public void mutate(double mutationRate){
    	int len = genes.length;
    	 for (int i = 0; i < len; i++) {
	            if (RandomSingleton.getInstance().nextDouble() <= mutationRate) {
	            	genes[i] = !genes[i];
				   	mutation=true;

	            }
	      }
    }
    
   public boolean [] getGenes (){
	   return genes;
   }


@Override
	public int compareTo	(Object o) {
		Individual a	= (Individual) o;
		int dev=0;
		
		double resta= (_fitness-a.getFitness());
		if (resta==0) return 0;
		if (resta>0) dev = 1;
		else		 dev = -1;
			
		return dev;  //CUIDADO ESTÁ PARA MINIMIZAR
	}


	
	public int distancia (Individual other){
		int distancia=0;
		 for (int i = 0; i < length; i++) {
	           if ( genes[i]!=other.genes[i])  distancia++;
	      }
		 return distancia;
	}
	
 
	public boolean isEqual(Individual other){
		return _fitness == other._fitness;
	}
	 
	
	public int getID(){
	    	return myID;
	 }

	public void setXover(int p1, int p2){
	    	xOver	= true;
	    	xOverp	= new int[2];
	    	xOverp	[0] = p1;
	    	xOverp	[1] = p2;
	 }
	    
	public void setMutation(){
	    	mutation=true;
	 }
	
	
	
}