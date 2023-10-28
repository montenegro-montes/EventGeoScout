package es.uma.mapas;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.appcompat.app.AlertDialog;
import es.uma.Kernel.Mediator;
import es.uma.Kernel.NetworkManager;

/**
 * Created by joseamontenegromontes on 19/12/16.
 */

public class LoginDialogFragment extends DialogFragment {

    String pwdS,userS,serverS,portS,serverAux;
    MainActivity _parent;

    String  SERVER_IP;
    int SERVER_PORT;

    NetworkManager network;
    Mediator _med;

    EditText server,user,pwd;
    public LoginDialogFragment (MainActivity parent, String host, int port, NetworkManager networkManager, Mediator med){
        super();
        _parent         = parent;
        SERVER_IP       = host;
        SERVER_PORT     = port;
        network         = networkManager;
        _med            = med;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_signin, null);
        builder.setView(view);

        server = (EditText) view.findViewById(R.id.server);
        user = (EditText) view.findViewById(R.id.username);
        pwd = (EditText) view.findViewById(R.id.password);

        server.setHint("Server: ("+ SERVER_IP +":"+SERVER_PORT+")");


        builder.setPositiveButton("signin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                         serverAux = server.getText().toString();
                         pwdS   = pwd.getText().toString();
                         userS  = user.getText().toString();

                        if (serverAux.length()>0) {
                            if (serverAux.contains(":")){
                                String[] parts = serverAux.split(":");
                                serverS = parts[0];
                                SERVER_IP= serverS;
                                portS = parts[1];
                                SERVER_PORT=Integer.parseInt(portS);
                            }
                        }

                       checkLogin();

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginDialogFragment.this.getDialog().cancel();
                    }
                });


        AlertDialog dialog = builder.create();


        return dialog;
    }

    public String getUser(){
        return userS;
    }


    public String getServer(){
      return serverS;
    }

    public int getPort(){
        return Integer.parseInt(portS);
    }

    public String getPortS(){
        return portS;
    }

    private void checkLogin(){





        if (userS.isEmpty() | pwdS.isEmpty()){

            new android.app.AlertDialog.Builder(_parent)
                    .setTitle("")
                    .setMessage("Please, introduce user and password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{
            new AsyncTask<Void, Void, Boolean>() {
                protected Boolean doInBackground(Void... params) {

                    int timeoutMS = 100;

                    boolean connected   =   isHostReachable(SERVER_IP, SERVER_PORT,timeoutMS);
                    return connected;
                }



                protected void onPostExecute(Boolean msg) {
                    Log.d("INFO", "" + msg);

                    if (!msg) {
                        new android.app.AlertDialog.Builder(_parent)
                                .setTitle("Server not Reacheable")
                                .setMessage("Please, check server configuration")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    else {


                        (new Thread() {
                            public void run() {
                                network = new NetworkManager(SERVER_IP, SERVER_PORT, _med);

                                try {
                                    network.Connect();

                                    //userS="monte";
                                    //pwdS ="123";
                                    network.sendAuthentication(userS, codifPwd(pwdS));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
            }.execute();
        }



    }

    public  boolean isHostReachable(String serverAddress, int serverTCPport, int timeoutMS) {
        boolean connected = false;
        Socket socket;
        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(serverAddress, serverTCPport);
            socket.connect(socketAddress, timeoutMS);
            if (socket.isConnected()) {
                connected = true;
                socket.close();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            socket = null;
        }
        return connected;
    }



    private String codifPwd(String pwd) {


        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pwd.getBytes());
            return stringHexa(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

    }

    private static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }

    public NetworkManager getNetwork (){
        return network;
    }
}
