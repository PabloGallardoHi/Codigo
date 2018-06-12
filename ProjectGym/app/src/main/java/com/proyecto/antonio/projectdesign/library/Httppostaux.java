package com.proyecto.antonio.projectdesign.library;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONArray;
import org.json.JSONException;

import android.net.SSLCertificateSocketFactory;
import android.util.Base64;
import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

/*
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

/*CLASE AUXILIAR PARA EL ENVIO DE PETICIONES A NUESTRO SISTEMA
 * Y MANEJO DE RESPUESTA.*/

public class Httppostaux{

    InputStream is = null;
    String result = "";

    /*
     * Envía un POST request a '/oauth/token' para conseguir un token de acceso,
     * el cual será mandado en cada request posterior
     */
    public String sendTokenRequest (String username, String password, String URL_connect){

        StringBuffer response = null;
        JSONArray object = null;

        try{
            // Establecemos conexión con el servidor
            String urlString = "https://"+URL_connect+"/oauth/token?grant_type=password&username="+username+"&password="+password;
            URL urlObj = new URL(urlString);
            HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();

            if (con instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConn = (HttpsURLConnection) con;
                httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
            }


            Log.w("myApp", "cacafuti");

            // Credenciales con las que solicitamos el token de acceso
            String userCredentials = "trusted-monitor:topsecret";
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));

            // Mandamos las credenciales en el header http
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);

            // Enviamos la petición por POST
            OutputStream os = con.getOutputStream();
            os.flush();

            // Leemos la respuesta
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            // Convertimos a formato JSON
            String responseSTR = response.toString();
            String responseJson = "["+responseSTR+"]";
            object = new JSONArray(responseJson);
            String token = object.getJSONObject(0).getString("access_token");

            in.close();

            //Devolvemos el token de acceso para para usarlo en cada request que hagamos
            return token;

        } catch (Exception e){
            Log.e("log_tag", "Error CON EL TOKEN "+e.toString());
            return null;
        }

    }



    public JSONArray getserverdata(String parameters, String accessToken, String urlwebserver ){

        // Conecta via http y envia un post.
        httppostconnect(parameters,urlwebserver, accessToken);
        // Convierte el resultado a JSON
        JSONArray resul = getjsonarray();
            return resul;
    }


    /*
     * Envía las peticiones al servidor
     */
    private void httppostconnect(String parametros, String urlwebserver, String accessToken){

        try{
            // Añadimos el token de acceso a todas las peticiones  y extablecemos conexión
            String urlstring = urlwebserver+"?access_token="+accessToken;
            URL urlObj = new URL(urlstring);
            HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();

            if (con instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConn = (HttpsURLConnection) con;
                httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
            }

            // Según el tipo de petición, definimos distintas propiedades en la cabecera
            if(parametros=="GET") {
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("GET");
            }else {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");

                // Enviamos la petición por POST
                OutputStream os = con.getOutputStream();
                os.write(parametros.getBytes());
                os.flush();

            }
            // Leemos la respuesta
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputline;
            StringBuffer response= new StringBuffer();
            while((inputline = in.readLine()) != null){
                response.append(inputline);
            }
            result=response.toString();
            in.close();

        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }

    }


    public JSONArray getjsonarray(){
        //Convierte a JSON
        try{
            JSONArray jArray = new JSONArray(result);
            return jArray;
        }
        catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
            return null;
        }
    }
}




