
package es.uma.Kernel;

import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import MapFrame.Layer;
import MapFrame.Poly;
import MapFrame.Punto;
import Packet.PacketModifLayerDataB;
import es.uma.mapas.ActivityLayers;
import es.uma.mapas.GeoCapture;


/**
 *
 * @author Monte
 */
public class Mediator {
    private NetworkManager      network;

    private UserManager         userManager;
    GeoCapture                  mApplication;

    public Mediator ( GeoCapture Application){
        mApplication = Application;
        userManager  = new UserManager(this);

    }



    public void registerComm(NetworkManager networkP){

        network=networkP;
    }


    /***************************
     * Lo utilizo para pedir los datos de las layers.
     *
     * @param layername
     * @param tipo
     */
    public void sendLayer (final String layername, final int tipo){

        (new Thread() {
            public void run() {
                network.sendLayer(layername,tipo);
            }
        }).start();

    }


    public void  ptosReceive(String name,List<Punto> ptos){

        System.out.println("PUNTOS " + ptos.size());
        mApplication.setPuntos(name,ptos);

        ActivityLayers actLayer = mApplication.getLayerActivity();
        if (actLayer!=null)actLayer.ShowData();

    }


    public void  lynesReceive(List<Poly> lynes){

        System.out.println("lynes " + lynes.size());

        mApplication.setLynes(lynes);
        ActivityLayers actLayer = mApplication.getLayerActivity();
        actLayer.ShowData();

    }

    public void severeError(String msg,String info){

        Log.d("ERROR",msg + " " + info);
        //panicMode();
    }



    public void connexionReset (String User){


    }

    public void sendDataLayer (String layername, Punto pto,boolean add) {
            network.sendDataLayer(layername, pto, add);
    }

    public void sendDataLayer (String layername, int tipo, Poly ptos,boolean add) {
        network.sendDataLayer(layername,tipo, ptos, add);
    }

    public void layerDataModif (PacketModifLayerDataB packet){

     /*   if(del.isSelected()) map.setModeDel();
        else map.setModeAdd();

        boolean isAdd    = packet.isADD();
        String layerName = packet.getLayerName();
        BBTable.updateFeatures(layerName,isAdd);

        int tipo= packet.getTypeIntLayer(); //MISMO QUE ANTES PERO EN INT PARA SERIALIZAR EN EL SOCKET
        network.sendLayer(layerName, tipo);*/
    }



    public void authIncorrect (boolean badPwd) {

        Log.d("ERROR","AUTH");
        Intent intent=new Intent();
        intent.setAction(mApplication.mainActivityBroadcast);
        intent.putExtra(mApplication.auth,false);
        mApplication.sendBroadcast(intent);
    }


    public void authValid (List <Layer> layers) {
        try {

           Log.d("MAPA", "LAYERS " + layers.size());

            mApplication.setLayers(layers);

            Intent intent=new Intent();
            intent.setAction(mApplication.mainActivityBroadcast);
            intent.putExtra(mApplication.auth,true);
            mApplication.sendBroadcast(intent);

        } catch (Exception ex) {
            Logger.getLogger(Mediator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

