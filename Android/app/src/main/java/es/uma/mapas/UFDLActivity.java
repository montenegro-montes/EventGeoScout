package es.uma.mapas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import MapFrame.Layer;
import MapFrame.Punto;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import GeneticAlgorithms.Genetico;
import GeneticAlgorithms.Individual;
import GeneticAlgorithms.Problem;

public class UFDLActivity extends AppCompatActivity implements OnMapReadyCallback{

    final int MY_PERMISSIONS_REQUEST_MAPS=1;

    Spinner facilitySpinner, clientSpinner;
    ListView layersPoint,facilityList,clientList;
    String [] layersFound;
    GeoCapture Application;
    GoogleMap _map;

    Button calculateSolution;
    int npuntosClient,npuntosFacility;
    double [] cost;
    double [][] distancePtos;
    List<Punto> ListPuntosFacility,ListPtosClient;

    TextView solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ufdl);

        calculateSolution = (Button) findViewById(R.id.buttonCalculateaSolution);
        solution = (TextView) findViewById(R.id.textViewSolution);


        MapFragment mapFragment = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_MAPS);
        }

        Application = (GeoCapture) getApplicationContext();
        List<Layer> layers = Application.getLayers();
        List<Layer> layersPto = new ArrayList<>();

        for (int i=0;i<layers.size();i++){
            Layer aux =layers.get(i);
            if(aux.getTipo()==0) layersPto.add(aux);
        }


        int nLayers=layersPto.size();

        layersPoint = (ListView) findViewById(R.id.listViewLayers);

        layersFound = new String[nLayers];
        int []    npoints     = new int[nLayers];

        for (int i=0;i<nLayers;i++){
            layersFound[i]  = layersPto.get(i).getName();
            npoints[i]      = layersPto.get(i).getNelements();
        }


        layersPoint.setAdapter(new ListLayerAdapter(getApplicationContext(),layersFound,npoints));


        String layersS []= new String [nLayers];
        for (int i=0;i<nLayers;i++) layersS[i]= new String(i+"");

        facilitySpinner = (Spinner) findViewById(R.id.spinnerFacility);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, layersS);
        facilitySpinner.setAdapter(adapter);

        clientSpinner = (Spinner) findViewById(R.id.spinnerClient);
        clientSpinner.setAdapter(adapter);


    }

    private class ListLayerAdapter extends BaseAdapter {
        private Context mContext;
        int length      =   0;
        String [] _layersFund;
        int []    _point;

        public ListLayerAdapter(Context c, String [] layersFund, int []points) { //Constructor
            mContext    = c;
            _layersFund = layersFund;
            _point      = points;

            length      = _layersFund.length+1;


        }

        public int getCount() {
            return length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View gridElement=null;

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridElement = inflater.inflate(R.layout.list_view_layers_items, null);

            TextView textViewText = (TextView) gridElement.findViewById(R.id.layer_text);

            TextView textViewStatus = (TextView) gridElement.findViewById(R.id.layer_npoints);

            if (position==0){
                textViewText.setText("Layer");
                textViewText.setTypeface(null, Typeface.BOLD);
                textViewText.setTextColor(Color.BLACK);
                textViewStatus.setText("Points");
                textViewStatus.setTypeface(null, Typeface.BOLD);
                textViewStatus.setTextColor(Color.BLACK);

            }else{
                textViewText.setText(_layersFund[position-1]);
                textViewStatus.setText(_point[position-1]+"");
            }


            return gridElement;
        }
    }

    private class ListFacilityAdapter extends BaseAdapter {
        private Context mContext;
        int length      =   0;
        double [] _cost;
        boolean[] _open;

        public ListFacilityAdapter(Context c,  double []costs,boolean [] open) { //Constructor
            mContext    = c;
            _cost       = costs;
            _open       = open;
            length      = costs.length+1;


        }

        public int getCount() {
            return length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup
                parent) {

            View gridElement=null;

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridElement = inflater.inflate(R.layout.list_view_facility_items, null);

            TextView textViewText = (TextView) gridElement.findViewById(R.id.facility_text);
            TextView textViewCost = (TextView) gridElement.findViewById(R.id.facility_cost);
            TextView textViewOpen = (TextView) gridElement.findViewById(R.id.facility_open);




            if (position==0){
                textViewText.setText("N.");
                textViewText.setTypeface(null, Typeface.BOLD);
                textViewText.setTextColor(Color.BLACK);

                textViewCost.setText("Cost");
                textViewCost.setTypeface(null, Typeface.BOLD);
                textViewCost.setTextColor(Color.BLACK);

                textViewOpen.setText("Open");
                textViewOpen.setTypeface(null, Typeface.BOLD);
                textViewOpen.setTextColor(Color.BLACK);

            }else{
                textViewText.setText("F"+(position-1));
                //textViewCost.setText(String.format("%.2f", _cost[position-1]));
                textViewCost.setText(_cost[position-1]+"");

                if (_open[position-1]) textViewOpen.setText("Yes");
                else  textViewOpen.setText("No");
            }


            return gridElement;
        }
    }

    private class ListCLientAdapter extends BaseAdapter {
        private Context mContext;
        int length      =   0;
        String [] _facilitiesAssigned;

        public ListCLientAdapter(Context c, int nClients, String [] facilitiesAssigned) { //Constructor
            mContext    = c;
            _facilitiesAssigned = facilitiesAssigned;
            length      = nClients+1;


        }

        public int getCount() {
            return length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup
                parent) {

            View gridElement=null;

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridElement = inflater.inflate(R.layout.list_view_clients_items, null);

            TextView textViewText = (TextView) gridElement.findViewById(R.id.layer_text);

            TextView textViewStatus = (TextView) gridElement.findViewById(R.id.layer_npoints);

            if (position==0){
                textViewText.setText("Client");
                textViewText.setTypeface(null, Typeface.BOLD);
                textViewText.setTextColor(Color.BLACK);
                textViewStatus.setText("Assigned");
                textViewStatus.setTypeface(null, Typeface.BOLD);
                textViewStatus.setTextColor(Color.BLACK);

            }else{
                textViewText.setText("C"+(position-1));
                textViewStatus.setText(_facilitiesAssigned[position-1]+"");
            }


            return gridElement;
        }
    }

    public void onMapReady(GoogleMap map) {
        _map = map;

        LatLng greenRay = new LatLng(36.71853911463124,-4.496980905532837);
        int zoom = 17;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(greenRay, zoom));

    }

    public void onClick (View v){
        switch (v.getId()){

            case R.id.buttonCalculateCost:


                int facilitySelected    = facilitySpinner.getSelectedItemPosition();
                int clientFacility      = clientSpinner.getSelectedItemPosition();

                if (facilitySelected!=clientFacility){

                    String faciltyS =   layersFound[facilitySelected];
                    String clientS  =   layersFound[clientFacility];

                    HashMap<String,List<Punto>> ListPoint = Application.getLayersPoint();

                    ListPuntosFacility  =   ListPoint.get(faciltyS);
                    ListPtosClient      =   ListPoint.get(clientS);


                    putMap(ListPuntosFacility,ListPtosClient);



                    npuntosFacility             =   ListPuntosFacility.size();

                    boolean [] open = new boolean[npuntosFacility];
                            //{false,false,false,false,false};

                    cost = new double [npuntosFacility];                  //FIXED COST
                    Random r = new Random();

                    for (int i=0;i<npuntosFacility;i++){
                        cost[i]= r.nextInt(1000);
                        open[i]= false;
                    }

                    facilityList = (ListView) findViewById(R.id.listViewFacility);
                    facilityList.setAdapter(new ListFacilityAdapter(getApplicationContext(),cost,open));

                    npuntosClient           = ListPtosClient.size();
                    String [] assignFacility = new String[npuntosClient]; //= {"F1","F2","F3","F4","f5"};

                    clientList = (ListView) findViewById(R.id.listViewClient);

                    for (int i=0;i<npuntosClient;i++) {
                        assignFacility[i] = "F?";
                    }

                    clientList.setAdapter(new ListCLientAdapter(getApplicationContext(),npuntosClient,assignFacility));

                    distancePtos  = new double [npuntosClient][npuntosFacility];     //TRANSPORT COST

                    for (int indexClients=0;indexClients<npuntosClient;indexClients++){
                        for (int indexFacilities=0;indexFacilities<npuntosFacility;indexFacilities++){
                            distancePtos [indexClients] [indexFacilities] = ListPtosClient.get(indexClients).distance(ListPuntosFacility.get(indexFacilities));
                        }
                    }

                    calculateSolution.setEnabled(true);
                    solution.setText("Solution cost: ");

                }
                else {

                    new AlertDialog.Builder(UFDLActivity.this)
                            .setTitle("Layer selection")
                            .setMessage("Client and Facilities must been different layers")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    calculateSolution.setEnabled(false);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
            case R.id.buttonCalculateaSolution:

                Evaluation (this.npuntosClient,this.npuntosFacility,this.cost,this.distancePtos );
                break;
        }
    }


    private void putMap (List<Punto> ListPuntosFacility, List<Punto> ListPtosClient){
        _map.clear();

        List<Marker> markers = new ArrayList<>();

        for(Punto p :ListPuntosFacility){
            Marker marker=   _map.addMarker(new MarkerOptions()
                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            markers.add(marker);
        }


        for(Punto p :ListPtosClient){
            Marker marker  = _map.addMarker(new MarkerOptions()
                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            markers.add(marker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 30;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        _map.moveCamera(cu);
       // _map.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);

    }


    private void Evaluation (int clients,int facilities,double []fixed_costs,double [][]transport_cost ){


        Problem problemCurrent	= new Problem (facilities,clients);
        problemCurrent.setFixedCost(fixed_costs);
        problemCurrent.setTransportCost(transport_cost);

        int _poblacion = facilities*2, _generaciones = clients*100;


        Genetico genProblemBasic= new Genetico (_poblacion,_generaciones,problemCurrent);
        //genProblemBasic.debug	=true;
        genProblemBasic.run();

        Individual sol			 		= genProblemBasic.getSolution();
        long timeConsumed 		 		= genProblemBasic.getTimeConsumed();
        List <Double> fitnessHistory 	= genProblemBasic.getHistoryFitness();
        int generationCountSol	 		= genProblemBasic.getGenerationCount();

        problemCurrent.setGeneticParams(_poblacion, _generaciones);
        problemCurrent.setFindSolution(sol);
        problemCurrent.setTimeConsumed(timeConsumed);
        problemCurrent.setHisttoryFitness(fitnessHistory);
        problemCurrent.setHisttoryFitnessEliStd(genProblemBasic.getHistorySTDFitness());

        problemCurrent.setGenerationCount(generationCountSol);

        problemCurrent.printSolution(true);

        boolean [] open =genProblemBasic.getSolutionVector();

        facilityList = (ListView) findViewById(R.id.listViewFacility);
        facilityList.setAdapter(new ListFacilityAdapter(getApplicationContext(),cost,open));


        int [] asign = problemCurrent.clientAsign();

        String [] assignFacility = new String[npuntosClient];

        for (int i=0;i<npuntosClient;i++) {
            assignFacility[i] = "F"+asign[i];
        }

        clientList.setAdapter(new ListCLientAdapter(getApplicationContext(),npuntosClient,assignFacility));

       // List<Punto> ListPuntosFacility  =   ListPoint.get(faciltyS);
       // List<Punto> ListPtosClient      =   ListPoint.get(clientS);

        List<Punto> ListPuntosFacilityOpen = new ArrayList<>();

        for(int i=0;i<ListPuntosFacility.size();i++){
            if (open[i]) ListPuntosFacilityOpen.add(ListPuntosFacility.get(i));
        }

        putMap(ListPuntosFacilityOpen,ListPtosClient);

        solution.setText("Solution cost: "+fitnessHistory.get(generationCountSol));
    }
}
