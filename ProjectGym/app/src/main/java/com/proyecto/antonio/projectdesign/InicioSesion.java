package com.proyecto.antonio.projectdesign;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.proyecto.antonio.projectdesign.library.Httppostaux;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InicioSesion extends Activity {

    String URL_connect = "192.168.1.36:9999"; // Valor por defecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciosesion);

        // Carga la configuración desde SharedPreferences (almacena IP del servidor, y usuario y
        // contraseña si se especifica en el checkbox)
        SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
        if(settings != null) {
            URL_connect = settings.getString("IP", "");
            String usuario = settings.getString("user", "");
            String password = settings.getString("pass", "");
            if(!password.equals("")){
                // Si se especifica recordar, se inicia sesión automáticamente
                new asynclogin().execute(usuario,password);
            }
        }
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
            case R.id.ip:
                optionIP();
                return true;
            case R.id.logout:
                SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("password");
                editor.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     *  Muestra un mensaje para introducir la IP y el puerto del servidor
     *  Almacena la configuración en SharedPreferences de la app
     */
    public void optionIP(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Introduce la IP y el puerto del servidor");
        alert.setMessage("Formato: "+URL_connect);

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("IP", value);
                editor.commit();
                URL_connect = value;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    /*
     *  Función que se ejecuta cuando el usuario pulsa en el botón del formulario
     *  Desde esta funcion se lanza la consulta al servidor, y que si los datos
     *  son correctos se lanza la siguiente actividad
     */
    public void ir_ListaSesiones(View view){
        // Extreamos los datos de usuario y contraseña introducidos en el formulario
        EditText user = (EditText) findViewById(R.id.email);
        EditText pass = (EditText) findViewById(R.id.password);
        String usuario = user.getText().toString();
        String passw = pass.getText().toString();

        // Se verifica si los datos introducidos están en blanco (nos ahorramos la consulta)
        if( checklogindata(usuario, passw)==true){

            // Ejecuta asíncronamente la función que realiza la consulta al servidor
            new asynclogin().execute(usuario,passw);

        }else{
            // Si los datos están vacíos se muestra mensaje de error
            err_login();
        }

    }

    // Vibra y muestra un mensaje de error de color rojo en el formulario
    public void err_login(){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(400);
        TextView tv = (TextView) findViewById(R.id.error);
        tv.setVisibility(View.VISIBLE); // Muestra texto de color rojo indicando el error
    }

    // Vibra y elimina el mensaje de error, en caso de que estuviera visible
    public void login_ok(){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        TextView tv = (TextView) findViewById(R.id.error);
        tv.setVisibility(View.INVISIBLE);
    }

    /*
     *  Esta función es la que realiza la petición al servidor y comprueba el resultado obtenido
     *  Es lanzadada desde el método doInBackground de la clase asíncrona
     *  Recibe como parámetros el usuario y la contraseña (del formulario o de SharedPreferences)
     *  Devuelve false si el login es erroneo y true si es correcto
     */
    public boolean loginstatus(String username ,String password ) {
        int logstatus=0;

        /*
         *  Creamos una cadena para agregar los datos recibidos por los
         *  parametros anteriores y enviarlo mediante POST a nuestro sistema para relizar la
         *  validacion
         */
        String parametros = "{\"username\":\""+username+"\", \"passw\":\""+password+"\" }";
        Httppostaux post =new Httppostaux();
        String accessToken = post.sendTokenRequest(username, password, URL_connect);

        SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("access_token", accessToken);
        editor.commit();

        String value = settings.getString("acces_token", "");
        JSONArray jdata=post.getserverdata(parametros, accessToken,
                "https://"+URL_connect+"/gym/login/");

        SystemClock.sleep(500); // Para simular que tarda más en iniciar sesión

        // Si lo que recibimos no es null y no está vacío
        if (jdata != null && jdata.length() > 0){
            // Se crea un objeto JSONObject y se rellena con el unico elemento del JSONArray recibido
            JSONObject json_data;
            try {
                json_data = jdata.getJSONObject(0);
                logstatus=json_data.getInt("logstatus");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Si recibimos un 0 significa que hemos tenido un error, devuelve false
            if (logstatus==0){
                Log.e("loginstatus ", "invalido");
                return false;
            }
            else{ //Si recibimos un 1 significa que no se produjeron errores, devuelve true
                return true;
            }

        }else{	// Se devuelve false, se produjo un error al obtener el JSONArray del servidor
            return false;
        }

    }


    // Función que comprueba que ningún campo del formulario ha quedado en blanco
    public boolean checklogindata(String username ,String password ){

        if 	(username.equals("") || password.equals("")){ // Si alguno de los campos está vacio devuelve false
            return false;
        }else{ // Si ninguno de los dos campos está vacio devuelve true
            return true;
        }
    }

    /*		CLASE ASYNCTASK
     *
     *  Clase asíncrona que se encarga de lanzar la consulta y de reaccionar a la respuesta obtenida
     *  Ya sea mostrando el mensaje de error o lanzando la nueva actividad si los datos son
     *  correctos. Esta clase debe ser asíncrona para no provocar problemas de inconsistencia en la
     *  app debido a los tiempos de respuesta del servidor.
     */
    class asynclogin extends AsyncTask< String, String, String > {

        String user,pass;
        private ProgressDialog pDialog;

        // Muestra un cartel para informar al usuario de que se esta llevando a cabo la autenticacion
        protected void onPreExecute() {
            pDialog = new ProgressDialog(InicioSesion.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // Función que se ejecuta en segundo plano, en ella se llama a la función loginstatus()
        // que es la encargada de realizar la consulta al servidor y de obtener la respuesta
        protected String doInBackground(String... params) {
            user=params[0];
            pass=params[1];

            // Realizamos la consulta, recibimos la respuesta y analizamos los datos en segundo plano.
            if (loginstatus(user,pass)==true){
                return "ok"; // Login valido
            }else{
                return "err"; // Login invalido
            }

        }

        /*
         *  Función que se ejecuta tras doInBackground(), en ella se comprueba el resultado
         *  y según haya sido erroneo o válido, muestra el mensaje de error en el formulario
         *  o lanza la nueva actividad (almacenando los datos necesarios en SharedPreferences)
         */
        protected void onPostExecute(String result) {

            pDialog.dismiss(); // Eliminamos el cartel que informa de la autenticación

            if (result.equals("ok")){
                SharedPreferences settings = getSharedPreferences("ProjectGym", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user", user);
                CheckBox cb = (CheckBox) findViewById(R.id.recordar);
                if(cb.isChecked()){
                    editor.putString("pass",pass); // Si se ha pulsado recordar se almacena la contraseña
                }
                editor.commit();
                Intent i=new Intent(InicioSesion.this, ListaSesiones.class);
                login_ok();
                startActivity(i);
            }else{
                err_login();
            }

        }

    }
}




