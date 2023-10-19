package es.uma.mapas;

import android.app.Application;

import java.util.HashMap;
import java.util.List;

import MapFrame.Layer;
import MapFrame.Poly;
import MapFrame.Punto;
import es.uma.Kernel.Mediator;


public class GeoCapture  extends Application {
    private static GeoCapture singleton;

    List<Layer> mlayers;
    Mediator mmed;
    HashMap<String,List<Punto>> listPointshMap;
    ActivityLayers mlayer;
    List<Punto> mptos=null;
    List<Poly> mlynes=null;

    public String mainActivityBroadcast="mainActivityBroadcast";
    public String auth="auth";

    private String ServerIP ="";
    private int ServerPort;

    public GeoCapture getInstance(){
        return singleton;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        listPointshMap = new HashMap<String,List<Punto>>();
    }

    public void save(){


    }

    public void setLayers(  List<Layer> layers){
        mlayers = layers;
    }

    public List<Layer>  getLayers( ){
        return  mlayers;
    }



    public void setMediator (Mediator med){
        mmed=med;
    }

    public Mediator getMediator (){
        return mmed;
    }

    public void setLayerActivity (  ActivityLayers layer)
    {
        mlayer=layer;
    }

    public ActivityLayers getLayerActivity (  )
    {
        return mlayer;
    }

    public void setPuntos(String name,List<Punto> ptos){

        listPointshMap.put(name,ptos);
        mptos=ptos;
    }

    public  List<Punto> getPuntos(){
        return mptos;
    }


    public void setLynes(List<Poly> lynes){
        mlynes=lynes;
    }


    public List<Poly> getLynes(){
       return mlynes;
    }

    public HashMap<String,List<Punto>> getLayersPoint(){

           return  listPointshMap;
    }
}