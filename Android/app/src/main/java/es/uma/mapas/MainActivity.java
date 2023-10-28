package es.uma.mapas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MapFrame.Layer;
import MapFrame.Punto;
import androidx.appcompat.app.AppCompatActivity;
import es.uma.Kernel.Mediator;
import es.uma.Kernel.NetworkManager;

public class MainActivity extends AppCompatActivity {

    View decorView;
    NetworkManager network;
    LoginDialogFragment AlertDialog;
    Mediator med;
    Button buttonUFDL, buttonLayer;
    TextView textViewUser;
    GeoCapture Application;

    Receiver broadCast;

    boolean isAuth  =   false;

    IntentFilter filter;

    int SERVER_PORT ;
    String SERVER_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Application = (GeoCapture) getApplicationContext();
        med = new Mediator(Application);
        Application.setMediator(med);

        decorView = getWindow().getDecorView();

        buttonUFDL = (Button) findViewById(R.id.UFDLButton);
        buttonLayer = (Button) findViewById(R.id.buttonLayer);
        textViewUser = (TextView) findViewById(R.id.textViewUser);


        filter = new IntentFilter(Application.mainActivityBroadcast);
        broadCast = new Receiver();
        registerReceiver(broadCast, filter);

        load_server_cfg();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void Onclick(View v) {
        Intent action = null;
        switch (v.getId()) {
            case R.id.buttonLayer:
                action = new Intent(this, ActivityLayers.class);
                break;
            case R.id.UFDLButton:
                action = new Intent(this, UFDLActivity.class);
                break;
            case R.id.GeneticButton:
                action = new Intent(this, GeneticAlgActivity.class);
                break;
        }

        startActivity(action);
    }



    public void save(String serverIp, int  serverPort){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ServerIP", serverIp);
        editor.putInt("ServerPort", serverPort);
        editor.commit();

    }

    public void load_server_cfg(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SERVER_IP   = sharedPref.getString("ServerIP","192.168.0.157");
        SERVER_PORT = sharedPref.getInt("ServerPort", 8000);
    }

    public void onLogin(View v) {

        if (!isAuth) {


            AlertDialog = new LoginDialogFragment(this, SERVER_IP, SERVER_PORT, network, med);
            AlertDialog.show(getFragmentManager(), "");



        }
        else{

            new android.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Disconnect Gateway")
                    .setMessage("Are you sure?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            network.DisConnect();
                            textViewUser.setText("Not Identified");
                            textViewUser.setTextColor(Color.RED);
                            buttonUFDL.setEnabled(false);
                            buttonLayer.setEnabled(false);
                            isAuth=false;
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();



        }

    }

    private void AuthNotValid() {
        new android.app.AlertDialog.Builder(MainActivity.this)
                .setTitle("Authentication problem")
                .setMessage("Please, check your password")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // calculateSolution.setEnabled(false);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void AuthValid(){
        //Me llama Mediator BroadCast

        getPointLayers();
        String user = AlertDialog.getUser();
        buttonUFDL.setEnabled(true);
        buttonLayer.setEnabled(true);
        textViewUser.setText(user);
        textViewUser.setTextColor(Color.GREEN);


        network = AlertDialog.getNetwork();
        isAuth  = true;

        if (AlertDialog.getServer()!=null){
            save(AlertDialog.getServer(),AlertDialog.getPort());
        }
    }



    private void getPointLayers() {

        HashMap<String, List<Punto>> pointsLayer = Application.getLayersPoint();

        List<Layer> layers = Application.getLayers();
        final List<Layer> layersPto = new ArrayList<>();

        for (int i = 0; i < layers.size(); i++) {
            Layer aux = layers.get(i);
            if (aux.getTipo() == 0) layersPto.add(aux);
        }

        if (pointsLayer.size() != layersPto.size()) {
            (new Thread() {
                public void run() {
                    for (int i = 0; i < layersPto.size(); i++) {
                        med.sendLayer(layersPto.get(i).getName(), 0);
                    }
                }
            }).start();
        }
    }



    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            boolean isAuth  =   arg1.getBooleanExtra(Application.auth,false);
            if (isAuth)     AuthValid();
            else AuthNotValid();

        }
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadCast);
    }


    protected void onResume() {
        super.onResume();
        registerReceiver(broadCast, filter);

    }


}
