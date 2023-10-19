package es.uma.Utils;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import MapFrame.Poly;
import MapFrame.Punto;

/**
 * Created by monte on 9/10/15.
 */
public class GeoPoints {

    private double CuotaDistancia = 0.10;
    List<Location> lp = new ArrayList<Location>();
    GoogleMap map;
    int mtype;


   public GeoPoints(int type){

            mtype=type;
    }



    public GeoPoints(GoogleMap mapReady){
        map=mapReady;

    }

    public void setMap (GoogleMap mapReady){
        map=mapReady;
    }

    public void add(Location newPto){

       if(newPto!=null) {
           int lon = lp.size();

           if (lon > 0) {
               Location anterior = lp.get(lon - 1);
               double dist = anterior.distanceTo(newPto);

               if (dist > CuotaDistancia)
                   lp.add(newPto); //SI el siguiente punto es similar al anterior no añadir
           } else lp.add(newPto);
       }
    }

    public void paint (){
        LatLng pos;
        Location posLoc;


        map.clear();

       switch(mtype){
           case 0:

               posLoc = lp.get(0);
               pos=pto(posLoc);

               String patron="Latitude %.4f Longitude %.4f";
               String coordenadas=String.format(patron, pos.latitude,pos.longitude);

               Marker marker= map.addMarker(new MarkerOptions()
                                    .title("Captured Position")
                                    .snippet(coordenadas)
                                    .position(pos));


               break;
           case 1:

               Polyline poly=map.addPolyline(new PolylineOptions().geodesic(true));
                 poly.setPoints(listaPuntos());
               break;
           case 2:

                PolygonOptions rectOptions  = new PolygonOptions();
                rectOptions.addAll(listaPuntos());
                Polygon polygon = map.addPolygon(rectOptions);
                 break;

       }

    }

    /********************************
     *
     *
     */
    public void remove (){

        lp.clear();

    }

    /*****************
     *
     * @return
     */

    private List<LatLng> listaPuntos (){

        List<LatLng> lista = new ArrayList<LatLng>();

        Iterator<Location> it = lp.iterator();
        Location temp;

        while(it.hasNext()) {

            temp=it.next();
            lista.add(pto(temp));
        }

        return lista;
    }


    /********
     *
     * @param loc
     * @return
     */

    private LatLng pto (Location loc){

        LatLng pto= new LatLng(loc.getLatitude(),loc.getLongitude());

        return pto;
    }


    public Punto getPunto(){
        Location p=lp.get(0);
        Punto pto=new Punto(p.getLatitude(),p.getLongitude());
        return pto;
    }


    public Poly getPuntos(){

        int size  = lp.size();
        long id=System.currentTimeMillis();
        Poly poly = new Poly(id,size);

        List<Punto> ptos= new ArrayList<>();

        for (Location loc :lp){
            ptos.add(new Punto(loc.getLatitude(),loc.getLongitude()));
        }
        poly.setPtos(ptos);

        return poly;
    }

}
