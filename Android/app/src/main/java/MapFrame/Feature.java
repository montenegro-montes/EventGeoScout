package MapFrame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Feature implements Serializable{

	public static enum type { Point , LineString, Polygon};
	
	type tipo;
	Punto value;
	List<Punto> values;
	
	public Feature (type tipop){
		tipo=tipop;

		switch (tipo){
			case Point: value = new Punto(); break;
			case LineString:
		//	case Polygon: 
						values = new ArrayList<Punto>(); break;
		}
	}
	
	Punto getPoint(int index){
		switch (tipo){
			case Point: return value;
			case LineString: if (index<2) return values.get(index);
							 //else return Exception a; 
		//	case Polygon: if (index<values.size())return values.get(index);
			 				//else return Exception a; 
		}	
		return null;
	}
	
	void addPoint(Punto p){
		switch (tipo){
			case Point:  value=new Punto(p.latitude,p.longitude); break;
			case LineString: //else return Exception a; 
							break;
		//	case Polygon: break; 
		}	
	}

	
	void addPoints(List<Punto> p){
		switch (tipo){
			case Point:  break;
			case LineString: if(p.size()==2) values.addAll(p);  
							break;
		//	case Polygon: 	if(p.size()>2) values.addAll(p);
						//	break; 
		}	
	}
	
	
}


