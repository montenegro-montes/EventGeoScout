package es.uma.mapas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import es.uma.Kernel.Mediator;
import es.uma.Utils.GeoPoints;


public class GetLocalizationActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationSource,
        LocationListener {


    private OnLocationChangedListener mapLocationListener = null;
    private LocationManager locMgr = null;
    private Criteria crit = new Criteria();
    private boolean needsInit = false;
    private GoogleMap map = null;
    private GeoPoints LocationPoints = null;

    private Location recentLoc;
    String layerName;
    int type;
    TextView layerNameView, typeLayerView;
    GeoCapture Application;

    CameraUpdate center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Application = (GeoCapture) getApplicationContext();

            setContentView(R.layout.activity_addgeo);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                layerName = extras.getString("layerName");
                type = extras.getInt("type", 0);

                layerNameView = (TextView) findViewById(R.id.textLayerName);
                layerNameView.setText(layerName);

                typeLayerView = (TextView) findViewById(R.id.textType);

                switch (type) {
                    case 0:
                        typeLayerView.setText("Point");
                        break;
                    case 1:
                        typeLayerView.setText("LineString");
                        break;
                    case 2:
                        typeLayerView.setText("Polygon");
                        break;
                }
            }

            MapFragment mapFrag =
                    (MapFragment) getFragmentManager().findFragmentById(R.id.map);

            if (savedInstanceState == null) {
                needsInit = true;
            }

            mapFrag.getMapAsync(this);
            LocationPoints = new GeoPoints(type);


        }


    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;

        LocationPoints.setMap(map);


        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        //Criteria.ACCURACY_COARSE
        crit.setAccuracy(Criteria.ACCURACY_COARSE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locMgr.requestLocationUpdates(0L, 0.0f, crit, this, null);

        map.setLocationSource(this);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);


        if (needsInit) {

            Location local = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (local!=null) {
                center = CameraUpdateFactory.newLatLng(new LatLng(local.getLatitude(), local.getLongitude()));
                moveCamera();
            }
           // CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

           // map.moveCamera(center);
           // map.animateCamera(zoom);
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        if (locMgr != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locMgr.requestLocationUpdates(0L, 0.0f, crit, this, null);
        }

        if (map != null) {
            map.setLocationSource(this);
        }
    }

    @Override
    public void onPause() {
        map.setLocationSource(null);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locMgr.removeUpdates(this);

        super.onPause();
    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        this.mapLocationListener = listener;
    }

    @Override
    public void deactivate() {
        this.mapLocationListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
          if (mapLocationListener != null) {
            mapLocationListener.onLocationChanged(location);

            LatLng pos =   new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cu = CameraUpdateFactory.newLatLng(pos);

            map.animateCamera(cu);
            recentLoc = location;


        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // unused
    }

    @Override
    public void onProviderEnabled(String provider) {
        // unused
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // unused
    }

   /********* EVENTOS BOTONES ************/

    public void onCheckLocalization (View v){

        LocationPoints.add(recentLoc);
        LocationPoints.paint();
    }

    public void onClear (View v){
        map.clear();
        LocationPoints.remove();
    }

    public void onSave (View v){

        Mediator med= Application.getMediator();

        System.out.println("SAVE "+type);

        switch(type){
            case 0:   med.sendDataLayer(layerName,LocationPoints.getPunto(),true); break;
            case 1:
            case 2:   med.sendDataLayer(layerName, type, LocationPoints.getPuntos(),true); break;


        }

        finish();

    }

    public void finish() {
        Intent data = new Intent();
        data.putExtra("valorRetorno1", " Procesado by Intent2");
        data.putExtra("valorRetorno2", "Valor Seleccionado es: ");

        setResult(RESULT_OK, data);
        super.finish();
    }

    private void moveCamera (){

         final CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        try {
            map.moveCamera(center);
            map.animateCamera(zoom);
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
                                map.moveCamera(center);
                                map.animateCamera(zoom);
                            }
                        });
            }
        }
    }
}



