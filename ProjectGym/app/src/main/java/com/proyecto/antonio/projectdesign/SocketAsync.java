package com.proyecto.antonio.projectdesign;

import android.os.AsyncTask;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.io.ObjectOutputStream;
import 	java.net.NetworkInterface;
import java.util.Enumeration;
import java.net.SocketAddress;

/**
 * Created by usuario on 29/04/2015.
 */
public class SocketAsync extends AsyncTask<String, String, String>{

    private static final int PORT_USUARIO = 12345;
    private String SERVER_IP, mUsuario;
    private int SERVER_PORT;
    private boolean connection = false;

    public SocketAsync(String ip, String usuario){
        mUsuario = usuario;
        // Para separar la IP del puerto
        int index = ip.indexOf(":");
        SERVER_IP = ip.substring(0,index);
        SERVER_PORT = Integer.parseInt(ip.substring(index+1,ip.length()));
        Log.w("myApp", "cacafuti"+SERVER_IP+SERVER_PORT);
    }

    protected String doInBackground(String... params){
        try{
            Socket socket = new Socket();
            // Permitimos que otros sockets puedan utilizar la misma ip y puerto
            socket.setReuseAddress(true);
            // Fijamos el puerto del socket al 8080
            socket.bind(new InetSocketAddress(getIP(),8080));
            while(!connection){
                socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 1000);
                if(socket.isConnected()){
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    Log.w("myApp", "cacafuti5"+getIP()+SERVER_PORT);
                    // Enviamos en la petición el nombre del usuario...
                    output.writeObject(mUsuario);
                    connection = true;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }


    private String getIP(){
        String res = null;
        try {
            String localhost = InetAddress.getLocalHost().getHostAddress();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e.nextElement();
                if(ni.isLoopback())
                    continue;
                if(ni.isPointToPoint())
                    continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress) addresses.nextElement();
                    if(address instanceof Inet4Address) {
                        String ip = address.getHostAddress();
                        if(!ip.equals(localhost))
                            System.out.println((res = ip));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean getConnection(){
        return connection;
    }
}
