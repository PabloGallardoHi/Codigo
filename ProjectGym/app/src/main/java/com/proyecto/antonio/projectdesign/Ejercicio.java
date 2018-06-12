package com.proyecto.antonio.projectdesign;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.proyecto.antonio.projectdesign.library.Httppostaux;
import com.proyecto.antonio.projectdesign.library.YouTubeFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;

import java.io.ObjectInputStream;




public class Ejercicio extends FragmentActivity {

    private String URL_connect = "";
    private JSONArray jdatainfo, jdatavideo;
    private String id_ejercicio;
    private String nom_sesion, user_monitor, nom_ejercicio, finalidad, duracion, descripcion;
    private String urlVideo = null;

    // Para recepcion de mensajes del monitor
    private HiloMensajes myDatagramReceiver;
    private DatagramSocket datagramSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Actividad a pantalla completa (sin título ni barra de estado)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_ejercicio);

        // Recoge datos pasados desde la actividad ListaEjercicios
        Intent myIntent = getIntent();
        id_ejercicio =  myIntent.getStringExtra("id_Ejercicio");
        nom_sesion = myIntent.getStringExtra("nom_Sesion");
        user_monitor= myIntent.getStringExtra("user_Monitor");
        URL_connect = myIntent.getStringExtra("ip_Monitor");

        // Crea un hilo nuevo, en el que se escuchará la llegada de mensajes del monitor
        myDatagramReceiver = new HiloMensajes();
        myDatagramReceiver.start();

        // Carga el vídeo desde el servidor
        new asyncvideo().execute();

        // Esperamos para que llegue la respuesta del servidor con la URL del vídeo
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(urlVideo != null){

            //final String videoId = Bundle.getString(KEY_VIDEO_ID);
            final YouTubeFragment fragment = (YouTubeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_youtube);
            fragment.setVideoId(urlVideo);

        }

        // Realiza una consulta asíncrona para obtener la información acerca del ejercicio
        new asyncinfo().execute();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cierra el socket y acaba con el hilo
        datagramSocket.close();
        myDatagramReceiver.kill();
    }


    /*
     *  Esta función muestra un mensaje en el que se describe la información acerca del ejercicio:
     *      · Nombre de la sesión
     *      · Nombre del monitor
     *      · Nombre del ejercicio
     *      · Finalidad del ejercicio
     *      · Duración
     *      · Descripción del ejercicio
     */
    public void mostrar_Descripcion(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // Si se produce algun error al traer la información del ejercicio desde el
        // servidor del monitor, se muestra un mensaje informando sobre ello.
        nom_ejercicio = nom_ejercicio != null ? nom_ejercicio : "no encontrado";
        finalidad = finalidad != null ? finalidad:"no encontrada";
        duracion = duracion != null?duracion:"no encontrada";
        descripcion = descripcion != null ? descripcion :"no encontrada";
        alert.setTitle(nom_sesion+" - "+user_monitor+" - " + nom_ejercicio);
        alert.setMessage("Finalidad: " + finalidad + "\nDuración: " + duracion +
                "\nDescripción: "+ descripcion);
        alert.show();
    }


    /*
     *  Método que crea un AlertDialog y lo muestra por pantalla. Usado tanto para mostrar errores
     *  como para mostrar los mensajes enviados por el monitor desde su panel de chat.
     */
    private void alert(final String msg) {
        final String error = (msg == null) ? "Error desconocido: " : msg;
        if(!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Ejercicio.this);
            builder.setMessage(error).setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }
            );
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    /*		CLASE ASYNCTASK
     *
     *  Clase asíncrona que se encarga de realizar una consulta al servidor del monitor
     *  para obtener información acerca del ejercicio (nombre, finalidad, duracion y descripcion).
     *  Esta clase debe ser asíncrona para no provocar problemas de inconsistencia en la
     *  app debido a los tiempos de respuesta del servidor.
     */
    class asyncinfo extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {

            /*
             *  Creamos un ArrayList del tipo nombre-valor para agregar el id del ejercicio y
             *  enviarlo mediante POST al servidor del monitor para que le devuelva la información
             *  acerca del ejercicio.
             */
            SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
            String accessToken = settings.getString("access_token", "");

            ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
            postparameters2send.add(new BasicNameValuePair("id_ejercicio",id_ejercicio));
            String parametros="GET";
            // Realizamos la peticion HTTP y obtenemos un JSONArray como respuesta
            Httppostaux post =new Httppostaux();
            jdatainfo=post.getserverdata(parametros, accessToken,
                    "https://"+URL_connect+"/gym/ejercicio/"+id_ejercicio+"/");


            // Si lo que recibimos no es null y no está vacío
            if (jdatainfo!=null && jdatainfo.length() > 0){
                try{
                    // Se crea un objeto JSONObject y se rellena con
                    // el unico elemento del JSONArray recibido
                    JSONObject json_data = jdatainfo.getJSONObject(0);
                    // Se almacena la información recibida
                    nom_ejercicio = json_data.getString("nombre_Ejercicio");
                    finalidad = json_data.getString("finalidad");
                    duracion = json_data.getString("duracion");
                    descripcion = json_data.getString("descripcion");
                } catch (JSONException e){
                    e.printStackTrace();
                }
            } else{
                return "err";
            }
            return "ok";
        }

        protected void onPostExecute(String result) {
        }
    }


    /*		CLASE ASYNCTASK
     *
     *  Clase asíncrona que se encarga de realizar una consulta al servidor del monitor
     *  para obtener la URL del vídeo del ejercicio.
     *  Esta clase debe ser asíncrona para no provocar problemas de inconsistencia en la
     *  app debido a los tiempos de respuesta del servidor.
     */
    class asyncvideo extends AsyncTask<String, String, String>{
        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {

            /*
             *  Creamos un ArrayList del tipo nombre-valor para agregar el id del ejercicio y
             *  enviarlo mediante POST al servidor del monitor para que le devuelva la información
             *  acerca del ejercicio.
             */
            SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
            String accessToken = settings.getString("access_token", "");

            ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
            postparameters2send.add(new BasicNameValuePair("id_ejercicio",id_ejercicio));
            String parametros = "GET";
            // Realizamos la peticion HTTP y obtenemos un JSONArray como respuesta
            Httppostaux post =new Httppostaux();
            jdatavideo=post.getserverdata(parametros, accessToken,
                    "https://"+URL_connect+"/gym/ejercicio/"+id_ejercicio+"/");

            // Si lo que recibimos no es null y no está vacío
            if (jdatavideo!=null && jdatavideo.length() > 0){
                try{
                    // Se crea un objeto JSONObject y se rellena con
                    // el unico elemento del JSONArray recibido
                    JSONObject json_data = jdatavideo.getJSONObject(0);
                    // Se almacena la información recibida
                    urlVideo = json_data.getString("url_video");
                } catch (JSONException e){
                    e.printStackTrace();
                }
            } else{
                return "err";
            }
            return "ok";
        }

        protected void onPostExecute(String result) {
        }

        private String compruebaUrlVideo(String url){
            String resulUrl;
            SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);

            if(url.toLowerCase().contains("https://")){
                resulUrl = url;
            } else{
                int index = settings.getString("IP", "").indexOf(":");
                String ip = settings.getString("IP", "").substring(0,index);
                resulUrl = "https://"+ip+url;
            }

            return resulUrl;
        }
    }


    /*
     *  Función que se ejecuta cuando se recibe una trama por el socket abierto para la recepción
     *  de mensajes del monitor. En la función se llama a la función alert() vista anteriormente
     *  pasandole como argumento el mensaje recibido en el socket.
     */
    private Runnable updateTextMessage = new Runnable() {
        public void run() {
            if (myDatagramReceiver == null) return;
            alert(myDatagramReceiver.getLastMessage());
        }
    };


    /*
     *  Clase que hereda de la clase Thread. Nos permite crear otro hilo en nuestra actividad,
     *  este hilo será el encargado de quedar a la escucha en el puerto 12345 de mensajes
     *  que envia el monitor desde su panel de chat. Además, ejecutará la función updateTextMessage
     *  para mostrar en pantalla el mensaje recibido mediante una alerta.
     */
    public class HiloMensajes extends Thread{

        private boolean bKeepRunning = true;
        private String lastMessage = "";
        private static final int PORT = 12345;
        private ServerSocket server;
        private Socket socket;
        private ObjectInputStream input;

        public void run(){
            try{

                server = new ServerSocket(PORT);
                   while(bKeepRunning){
                       socket = server.accept();
                       input = new ObjectInputStream(socket.getInputStream());
                       lastMessage = input.readObject().toString();
                       runOnUiThread(updateTextMessage);
                   }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        // Elimina el hilo que está en continua ejecución, termina el bucle
        public void kill() {
            bKeepRunning = false;
        }
        // Método getter para acceder a la variable lastMessage desde otra clase
        public String getLastMessage() {
            return lastMessage;
        }
    }
}
