package es.uma.mapas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import MapFrame.Poly;
import MapFrame.Punto;
import androidx.appcompat.app.AppCompatActivity;
import es.uma.Utils.GeoPoints;


public class ViewLocalizationActivity extends AppCompatActivity implements
        OnMapReadyCallback {


    private boolean needsInit = false;
    private GoogleMap map = null;
    private GeoPoints LocationPoints=null;

    String layerName;
    int     type;
    TextView layerNameView,typeLayerView,nElementsView;
    GeoCapture Application;
    LatLngBounds bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Application= (GeoCapture)getApplicationContext();

            setContentView(R.layout.activity_viewgeo);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                 layerName = extras.getString("layerName");
                 type = extras.getInt("type", 0);

                 layerNameView =(TextView) findViewById(R.id.textLayerName);
                 layerNameView.setText(layerName);

                typeLayerView =(TextView) findViewById(R.id.textType);

                switch (type){
                    case 0: typeLayerView.setText("Point"); break;
                    case 1: typeLayerView.setText("LineString");break;
                    case 2: typeLayerView.setText("Polygon");break;
                }
            }

            MapFragment mapFrag =
                    (MapFragment) getFragmentManager().findFragmentById(R.id.map);

            if (savedInstanceState == null) {
                needsInit = true;
            }

            mapFrag.getMapAsync(this);
            LocationPoints= new GeoPoints(type);



        }




    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;

        LocationPoints.setMap(map);

        map.clear();
        map.getUiSettings().setMyLocationButtonEnabled(false);



        if (needsInit) {


            if (type==0) {


                List<Marker> markers = new ArrayList<>();
                List<Punto> ptos= Application.getPuntos();
             //   List<Punto> ptos=  new ArrayList<>();
             //   ptos.add(new Punto(36.71853911463124,-4.496980905532837));
                        //Application.getPuntos();

                nElementsView =(TextView) findViewById(R.id.textNElements);
                nElementsView.setText(ptos.size()+"");

                for(Punto p :ptos){
                    Marker marker=   map.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getLatitude(), p.getLongitude())));
                    markers.add(marker);
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }
                bounds = builder.build();
            /*   int padding = 60; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                map.moveCamera(cu);*/

                moveCamera();
            }

            if (type==1) {
                List<Poly> lynes= Application.getLynes();
                nElementsView =(TextView) findViewById(R.id.textNElements);
                nElementsView.setText(lynes.size()+"");

                List<Polyline> polylines = new ArrayList<>();

                for(Poly l :lynes){

                    List<Punto> ptos= l.getPtos();
                    List<LatLng> ptosL= new ArrayList<>();
                    Polyline poly=map.addPolyline(new PolylineOptions().geodesic(true));

                    for(Punto p :ptos){
                        ptosL.add(new LatLng(p.getLatitude(),p.getLongitude()));
                    }

                    poly.setPoints(ptosL);
                    polylines.add(poly);
                }


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Polyline poly : polylines) {
                    List<LatLng> ptos = poly.getPoints();

                    for (LatLng pto : ptos)
                        builder.include(pto);
                }
                bounds = builder.build();

                moveCamera();

            }

            if (type==2) {
                List<Poly> lynes= Application.getLynes();
                nElementsView =(TextView) findViewById(R.id.textNElements);
                nElementsView.setText(lynes.size()+"");

                List<LatLng> ptosL= new ArrayList<>();

                for(Poly l :lynes){

                    List<Punto> ptos= l.getPtos();


                    PolygonOptions poly = new PolygonOptions();

                    for(Punto p :ptos){
                        ptosL.add(new LatLng(p.getLatitude(),p.getLongitude()));
                        poly.add(new LatLng(p.getLatitude(),p.getLongitude()));
                    }

                    Polygon polygon = map.addPolygon(poly);
                }


               LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng ptos : ptosL)
                      builder.include(ptos);
                bounds = builder.build();

                moveCamera();
            }
        }


    }

    private void moveCamera (){

        try {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        } catch (IllegalStateException e) {
            // layout not yet initialized
            final View mapView = getFragmentManager()
                    .findFragmentById(R.id.map).getView();
            if (mapView.getViewTreeObserver().isAlive()) {
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @SuppressWarnings("deprecation")
                            @SuppressLint("NewApi")
                            // We check which build version we are using.
                            @Override
                            public void onGlobalLayout() {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                    mapView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(this);
                                } else {
                                    mapView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(this);
                                }
                                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                            }
                        });
            }
        }
    }



    public void finish() {
        Intent data = new Intent();
        data.putExtra("valorRetorno1", " Procesado by Intent2");
        data.putExtra("valorRetorno2", "Valor Seleccionado es: ");

        setResult(RESULT_OK, data);
        super.finish();
    }



}



