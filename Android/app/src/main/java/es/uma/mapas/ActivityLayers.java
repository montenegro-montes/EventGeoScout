package es.uma.mapas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import MapFrame.Layer;
import androidx.appcompat.app.AppCompatActivity;
import es.uma.Kernel.Mediator;
import es.uma.Utils.Row;

public class ActivityLayers extends AppCompatActivity {
    private static final int REQUEST_CODE = 10;
    GeoCapture Application;

    CustomArrayAdapter adapter;

    String layerSelected=null;
    int tipo=0;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_layers);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Application= (GeoCapture)getApplicationContext();
        Application.setLayerActivity(this);

        ListView list = (ListView) findViewById(R.id.LAYER);
        list.setItemsCanFocus(false);

        GeoCapture Application= (GeoCapture)getApplicationContext();
        List<Layer> layers= Application.getLayers();


        if (layers==null)   return;

        int sizeLayers=layers.size();
        if (sizeLayers>0){
            final List<Row> rows= new ArrayList<Row>(sizeLayers);

            for (Layer layer :layers){

                Row row = new Row();
                row.setlayer(layer.getName());
                row.setType(layer.getTipo());
                row.setNelements(layer.getNelements());
                rows.add(row);
            }
            adapter =new CustomArrayAdapter(this, rows);
            list.setAdapter(adapter);
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        }

    }

    /*********************************************************
     * Button para añadir nuevas coordenadas
     *
     * @param v
     */

     public void clickAdd(View v){

        ArrayList<Row> countryList = adapter.layerList;
        for(int i=0;i<countryList.size();i++){
            Row layer = countryList.get(i);
            if(layer.isChecked()){
                layerSelected =layer.getLayer();
                tipo=layer.getType();
            }
        }

        if (layerSelected!=null) {

            Intent map = new Intent(this, GetLocalizationActivity.class);
            map.putExtra("layerName", layerSelected);
            map.putExtra("type",tipo); //TYPE
            startActivityForResult(map, REQUEST_CODE);

        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Add features layer")
                    .setMessage("Please select a layer")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    /*********************************************************
     *
     * @param v
     */
    public void clickView(View v){


        ArrayList<Row> countryList = adapter.layerList;
        for(int i=0;i<countryList.size();i++){
            Row layer = countryList.get(i);
            if(layer.isChecked()){
                layerSelected =layer.getLayer();
                tipo=layer.getType();
            }
        }

      if (layerSelected!=null) {

          Mediator med= Application.getMediator();
          med.sendLayer(layerSelected, tipo);

      }
      else{
          new AlertDialog.Builder(this)
                  .setTitle("View features layer")
                  .setMessage("Please select a layer")
                  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                      }
                  })
                  .setIcon(android.R.drawable.ic_dialog_alert)
                  .show();
      }
    }

    /*********************************************************
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
        if (data.hasExtra("valorRetorno1")) {
            String uno=data.getExtras().getString("valorRetorno1");
            String dos=data.getExtras().getString("valorRetorno2");
        }}
    }

    /*********************************************************
     *
     */


    public void ShowData(){
        Intent map = new Intent(this, ViewLocalizationActivity.class);
        map.putExtra("layerName", layerSelected);
        map.putExtra("type",tipo); //TYPE
        startActivityForResult(map, REQUEST_CODE);
    }

}
