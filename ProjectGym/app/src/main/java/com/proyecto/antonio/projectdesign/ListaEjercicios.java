package com.proyecto.antonio.projectdesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.antonio.projectdesign.library.Httppostaux;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;

/**
 * Created by Antonio on 22/07/2014.
 */
public class ListaEjercicios extends Activity {

    SwipeRefreshLayout swipeRefreshLayout;
    ListView lv;
    List<String> array_list = new ArrayList<String>();
    String URL_connect = "";
    JSONArray jdataAdd, jdataLista, jdataSesion;
    String user;
    String user_monitor;
    String id_sesion;
    SocketAsync socket;
    boolean sesionInit = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_listaejercicios);

        // Carga el usuario que hizo el login, que se encuentra almacenado en sharedpreferences
        SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
        if(settings != null) {
            user = settings.getString("user", "");
            Log.d("usuario: ",user);
        }

        // Obtiene los datos de la sesion de ejercicios obtenidos en la actividad ListaSesiones
        Intent myIntent = getIntent();
        String nom_sesion = myIntent.getStringExtra("nom_Sesion");
        user_monitor= myIntent.getStringExtra("user_Monitor");
        URL_connect = settings.getString("IP", "");
        id_sesion = myIntent.getStringExtra("id_Sesion");

        // Modifica los textview para informar al usuario de la sesión de ejercicios en la que
        // se encuentra y del monitor que la creó
        TextView tv = (TextView) findViewById(R.id.nombresesion);
        tv.setText("Sesión: "+nom_sesion);
        tv = (TextView) findViewById(R.id.nombremonitor);
        tv.setText("Monitor: "+user_monitor);

        // Carga el listview que será modificado cada vez que se realice la consulta al servidor
        lv = (ListView) findViewById(R.id.listasesiones);

        // Establece el listener y los colores del swiperefreshlayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setColorScheme(R.color.c1, R.color.c2, R.color.c3, R.color.c4);

        // Realiza en segundo plano la consulta para obtener la lista de ejercicios de la sesión
        new asyncgetejercicios().execute();
    }

    /*
     *  Cuando se destruye la actividad informamos al servidor del monitor de que nos vamos a
     *  desconectar de la sesión, se realiza de forma asíncrona.
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //new asyncborra().execute();
    }

    /*
     *  Establece las funciones a realizar por el listener del swiperefreshlayout.
     *  Cuando detecta un evento realiza una nueva peticion al servidor
     *  El evento se disparará cuando desplacemos hacia abajo el swiperefreshlayout
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener(){

        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    // Realiza de nuevo la consulta para obtener la rutina de ejercicios
                    new asyncgetejercicios().execute(user);
                }

            }, 2000);
        }
    };


     /*		CLASE ASYNCTASK
     *
     *  Clase asíncrona que se encarga de lanzar la consulta y de imprimir el resultado en el
     *  listview de la actividad. Donde se muestran la lista de ejercicios de la sesión
     *  Esta clase debe ser asíncrona para no provocar problemas de inconsistencia en la
     *  app debido a los tiempos de respuesta del servidor.
    */

    class asyncgetejercicios extends AsyncTask< String, String, String > {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {

            SharedPreferences settings = getSharedPreferences("ProjectGym",
                    Context.MODE_PRIVATE);

             /*
              *  Creamos un ArrayList del tipo nombre-valor para agregar los datos del
              *  usuario y enviarlo mediante POST a nuestro sistema para su suscripción
              *  en la sesión seleccionada
            */
           // ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
           // postparameters2send.add(new BasicNameValuePair("Id_Sesion", id_sesion));
           // postparameters2send.add(new BasicNameValuePair("nom_Usuario",
          //          settings.getString("user", "")));


            String accessToken = settings.getString("access_token", "");
            String nombre_usuario = settings.getString("user", "");
            String parametros = "{\"username\":\""+id_sesion+"\", \"passw\":\""+nombre_usuario+"\" }";

            // Realizamos la peticion y como respuesta se obtiene un array JSON
            Httppostaux post =new Httppostaux();
            jdataAdd=post.getserverdata(parametros, accessToken,
                    "https://"+URL_connect+"/gym/addUserSesion/"+id_sesion+"/"+nombre_usuario+"/");
            if (jdataAdd!=null && jdataAdd.length() > 0){
                //Comprobamos que el usuario se ha suscrito correctamente
                try{
                    if(jdataAdd.getJSONObject(0).getString("addUserSesion").equals("1")) {

                        //Se ha suscrito correctamente

                        // Petición para obtener todos los ejercicios de la sesión
                        parametros = "GET";
                        jdataLista=post.getserverdata(parametros, accessToken,
                                "https://"+URL_connect+"/gym/ejercicioSesion/"+id_sesion+"/");

                        // Si lo que recibimos no es null y no está vacío
                        if (jdataLista!=null && jdataLista.length() > 0){
                            String nom_ejercicio;
                            array_list = new ArrayList<String>();

                            // Bucle para obtener todos los JSONObject contenidos en el JSONArray recibido
                            for(int i=0;i<jdataLista.length();i++){
                                try {
                                    // Se crea un objeto JSONObject y se rellena con
                                    // el elemento i del JSONArray recibido
                                    JSONObject json_data = jdataLista.getJSONObject(i);

                                    // Se obtiene el nombre del ejercicio y se añada al array
                                    nom_ejercicio = json_data.getString("nombre_Ejercicio");
                                    array_list.add("Ejercicio: "+nom_ejercicio+"\n");


                                    // Realizamos la petición para obtener los datos de la sesión
                                    parametros = "GET";
                                    jdataSesion= post.getserverdata(parametros, accessToken,
                                            "https://"+URL_connect+"/gym/sesion/"+id_sesion+"/");

                                    // Si lo que recibimos no es null y no está vacío
                                    if(jdataSesion!= null && jdataSesion.length()>0) {

                                        // Comprobamos que la sesión ha comenzado
                                        if(comparaFecha(jdataSesion.getJSONObject(0).getString("fecha"))){

                                            // Ya ha comenzado la sesion
                                            sesionInit = true;
                                            Log.w("myApp", "cacafuti");

                                            // Obtenemos la IP del monitor y conectamos con él
                                            socket = new SocketAsync(
                                                    jdataSesion.getJSONObject(0).getString("ip_Monitor"),
                                                    settings.getString("user", ""));
                                            socket.execute();
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{	// Se devuelve "err", se produjo un error al obtener el JSONArray del servidor
                            array_list = new ArrayList<String>();
                            return "err";
                        }

                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

            return "ok";
        }

        /*
         *  Función que se ejecuta tras doInBackground(), en ella se comprueba el resultado de la
         *  consulta. Si es positivo se actualiza el listview con el nuevo array de string donde
         *  están almacenadas los ejercicios de la sesión. Si es negativo se muestra un aviso
         *  de que se ha producido un error.
         */
        protected void onPostExecute(String result) {

            if (result.equals("ok")) {
                // Actualiza el listview
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListaEjercicios.this,
                        android.R.layout.simple_list_item_1, array_list );
                lv.setAdapter(arrayAdapter);

                // Gestiona clicks en la lista de sesiones
                lv.setClickable(true);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    /*
                     *  Detecta la posición en la que se ha hecho click, se accede al objeto JSON
                     *  que se corresponde con esa posicion en el JSONArray. Obtiene los datos del
                     *  ejercicio y lanza la actividad Ejercicio añadiendo estos datos en el intent.
                     */
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if(sesionInit) {
                            try {
                                JSONObject json_data = jdataLista.getJSONObject(position);
                                String id_ejercicio = Integer.toString(json_data.getInt("id_Ejercicio"));
                                String nom_ejercicio = json_data.getString("descripcion");
                                Intent myIntent = getIntent();
                                String user_monitor= myIntent.getStringExtra("user_Monitor");
                                String nom_sesion = myIntent.getStringExtra("nom_Sesion");
                                myIntent = new Intent(ListaEjercicios.this, Ejercicio.class);
                                myIntent.putExtra("id_Ejercicio",id_ejercicio);
                                myIntent.putExtra("nom_Sesion",nom_sesion);
                                myIntent.putExtra("user_Monitor",user_monitor);
                                myIntent.putExtra("ip_Monitor",URL_connect);
                                myIntent.putExtra("nom_Ejercicio",nom_ejercicio);
                                startActivity(myIntent);
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else{
                            Toast.makeText(getApplicationContext(), "La sesión aún no ha comenzado",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                array_list.add("Lista no disponible. Recargue en unos segundos");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListaEjercicios.this,
                        android.R.layout.simple_list_item_1, array_list );
                lv.setAdapter(arrayAdapter);
            }
        }


        private boolean comparaFecha(String fecha){
            boolean resultado = false;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            if(currentDate.compareTo(fecha) > 0){
                resultado = true;
            }

            return resultado;
        }
    }



     /*		CLASE ASYNCTASK
     *
     *  Clase asíncrona que se encarga de notificar al servidor del monitor de que el usuario
     *  va a salir de la sesión de ejercicios. Se realiza de forma asíncrona para no dejar la
     *  aplicación colgada esperando la respuesta.
    */

    class asyncborra extends AsyncTask< String, String, String > {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {

            /*
             *  Creamos un ArrayList del tipo nombre-valor para agregar el nombre del usuario y
             *  enviarlo mediante POST a nuestro sistema para eliminarlo de la sesión en el
              *  servidor del monitor.
             */
            ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
            postparameters2send.add(new BasicNameValuePair("user",user));
            String parametros="";
            SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
            String accessToken = settings.getString("access_token", "");

            // Realizamos la peticion HTTP
            Httppostaux post =new Httppostaux();
            post.getserverdata(parametros, accessToken,
                    "https://"+URL_connect+"/gymserver/eliminausuario.php");

            return "ok";
        }

        protected void onPostExecute(String result) {
        }
    }
}
