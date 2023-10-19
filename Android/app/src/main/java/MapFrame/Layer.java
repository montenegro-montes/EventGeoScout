package MapFrame;

import java.io.Serializable;

public class Layer  implements Serializable{
	private static final long serialVersionUID = 6529685098267757690L;

	String layer;
	int tipo,nelements;
	
	public Layer (String layerP, int tipoP,int nelementsP){
		layer=layerP;
		tipo = tipoP;
		nelements= nelementsP;
	}
	
	public Layer (String layerP, Feature.type tipoP,int nelementsP){
		layer=layerP;
		
		switch (tipoP){
			case Point: tipo=0; break;
			case LineString: tipo=1; break;
			case Polygon: tipo=3; break;
		}

		nelements= nelementsP;
	}
	
	public String getName (){
		return layer;
	}
	
	public int getTipo (){
		return tipo;
	}
	
	public int getNelements (){
		return nelements;
	}
	
	public String toString (){
		return "Layer: "+layer+" tipo "+tipo+" nelements "+nelements;
	}
}
