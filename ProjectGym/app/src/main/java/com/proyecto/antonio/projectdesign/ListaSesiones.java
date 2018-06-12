package com.proyecto.antonio.projectdesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import com.proyecto.antonio.projectdesign.library.Httppostaux;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 22/07/2014.
 */
public class ListaSesiones extends Activity {

    SwipeRefreshLayout swipeRefreshLayout;
    ListView lv;
    List<String> array_list = new ArrayList<String>();
    String URL_connect = "";
    JSONArray jdata;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listasesiones);
        // Carga la IP del servidor almacenada en sharedpreferences
        SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
        if(settings != null) {
            URL_connect = settings.getString("IP", "");
        }
        // Carga el listview que será modificado cada vez que se realice la consulta al servidor
        lv = (ListView) findViewById(R.id.listasesiones);
        // Establece el listener y los colores del swiperefreshlayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setColorScheme(R.color.c1, R.color.c2, R.color.c3, R.color.c4);

        // Realiza en segundo plano la consulta para obtener la lista de sesiones activas
        new asyncsql().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Añade las opciones del menú con el archivo my.xml
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    /*
     *  Especifica las funciones a realizar en caso de que se pulse una opción u otra
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences settings = getSharedPreferences("ProjectGym",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("pass");
                editor.commit();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     *  Establece las funciones a realizar por el listener del swiperefreshlayout.
     *  Cuando detecta un evento realiza una nueva peticion al servidor
     *  El evento se disparará cuando desplacemos hacia abajo el swiperefreshlayout
     */
    OnRefreshListener onRefreshListener = new OnRefreshListener(){

        @Override
        public void onRefresh() {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    // Realiza de nuevo la consulta para obtener las sesiones activas
                    new asyncsql().execute();
                }

            }, 2000);
        }
    };


     /*		CLASE ASYNCTASK
     *
     *  Clase asíncrona que se encarga de lanzar la consulta y de imprimir el resultado en el
     *  listview de la actividad. Donde se muestran las sesiones activas en el servidor.
     *  Esta clase debe ser asíncrona para no provocar problemas de inconsistencia en la
     *  app debido a los tiempos de respuesta del servidor.
    */

    class asyncsql extends AsyncTask< String, String, String > {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {

            SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
            String accessToken = settings.getString("access_token", "");
            String parametros = "GET";

            // Realizamos la peticion y como respuesta se obtiene un array JSON
            Httppostaux post =new Httppostaux();
            jdata=post.getserverdata(parametros, accessToken, "https://"+URL_connect+"/gym/sesionesActivas/");
            array_list = new ArrayList<String>();
            // Si lo que recibimos no es null y no está vacío
            if (jdata!=null && jdata.length() > 0){
                String nom_sesion,user_monitor;

                // Bucle para obtener todos los JSONObject contenidos en el JSONArray recibido
                for(int i=0;i<jdata.length();i++){
                    try {
                        // Se crea un objeto JSONObject y se rellena con
                        // el elemento i del JSONArray recibido
                        JSONObject json_data = jdata.getJSONObject(i); //Aquí está el fallo

                        // Se obtienen los nombres de la sesion y del monitor
                        nom_sesion = json_data.getString("nombre_Sesion");
                        user_monitor = json_data.getString("user_Monitor");
                        // Se añaden al array de string, que es lo que se mostrará en el listview
                        array_list.add("Sesión: "+nom_sesion+"\nMonitor: "+user_monitor+"\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{	// Se devuelve "err", se produjo un error al obtener el JSONArray del servidor
                return "err";
            }
            return "ok";

        }

        /*
         *  Función que se ejecuta tras doInBackground(), en ella se comprueba el resultado de la
         *  consulta. Si es positivo se actualiza el listview con el nuevo array de string donde
         *  están almacenadas las sesiones y los monitores. Si es negativo se muestra un aviso
         *  de que no existen sesiones activas.
         */
        protected void onPostExecute(String result) {

            if (result.equals("ok")) {
                // Actualiza el listview
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListaSesiones.this,
                        android.R.layout.simple_list_item_1, array_list );
                lv.setAdapter(arrayAdapter);
                // Gestiona clicks en la lista de sesiones
                lv.setClickable(true);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    /*
                     *  Detecta la posición en la que se ha hecho click, se accede al objeto JSON
                     *  que se corresponde con esa posicion en el JSONArray. Obtiene los datos de
                     *  nombre de la sesion, del monitor y la IP del monitor. Y lanza la actividad
                     *  ListaEjercicios añadiendo los datos obtenidos de la sesión.
                     */
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        try {
                            JSONObject json_data = jdata.getJSONObject(position);
                            String id_sesion = Integer.toString(json_data.getInt("id_Sesion"));
                            String nom_sesion = json_data.getString("nombre_Sesion");
                            String user_monitor = json_data.getString("user_Monitor");
                            Intent myIntent = new Intent(ListaSesiones.this, ListaEjercicios.class);
                            myIntent.putExtra("id_Sesion", id_sesion);
                            myIntent.putExtra("nom_Sesion",nom_sesion);
                            myIntent.putExtra("user_Monitor",user_monitor);
                            startActivity(myIntent);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                array_list.add("No hay sesiones activas");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListaSesiones.this,
                        android.R.layout.simple_list_item_1, array_list );
                lv.setAdapter(arrayAdapter);
            }
        }
    }
}
