package MapFrame;

import java.io.Serializable;
import java.util.List;

public class Poly implements Serializable{

	long id;
	int nelements;
	 List<Punto>  point;
	 
	public Poly  (long idP,int nelementsP){
		id = idP;
		nelements= nelementsP;
	}
	

	public void setPtos ( List<Punto>  pointD){
			point=pointD;
	}

	public  List<Punto>  getPtos (){
		return point;
	}
	
	public long getID (){
		return id;
	}
	
	public int getNelements (){
		return nelements;
	}
	
	public String toString(){
		String ret;
		ret= id+" "+nelements;
		
		for (Punto p: point){
			ret=ret+"\n Pto:"+p;
		}
		
		return ret;
	}
}
