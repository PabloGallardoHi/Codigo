package monitorgym;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import monitorgym.AuthTokenInfo;

/*
 * Clase que gestionar� todas las conexiones con el servidor.
 * Cada vez que se realice una petici�n al servidor, deber�
 * utilizar un m�todo de esta clase.
 * 
 * Dise�o por Pablo Gallardo Hiraldo
 * Versi�n: 1.0
 * 
 */

public class GestorPeticiones {

	private String SERVER_PATH;
	private final String SERVER_PORT="9999";
	
	/*
     * Env�a un POST request a '/oauth/token' para conseguir un token de acceso, 
	 * el cual ser� mandado en cada request posterior
     */
    public String sendTokenRequest (String username, String password) throws Exception{
    
    	StringBuffer response = null;
		JSONArray object = null;
    	
    	try{
    		// Actualizamos la IP del servidor por si ha habido algun cambio
    		actualizaIp();
    		// Establecemos conexi�n con el servidor
    		String urlString = SERVER_PATH+":"+SERVER_PORT+"/oauth/token";//?grant_type=password&username="+username+"&password="+password;
    		URL urlObj = new URL(urlString);    		
			HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
			
			//Esto solo lo hacemos para que Java no de problemas con el certificado self-signed.
			con.setHostnameVerifier(new HostnameVerifier(){
				public boolean verify(String hostname, SSLSession session){
					return true;
				}
			});
			
			// Credenciales con las que solicitamos el token de acceso	
			String userCredentials = "trusted-monitor:topsecret";
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
			// Mandamos las credenciales en el header http
			con.setRequestProperty ("Authorization", basicAuth);			
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			
			// Enviamos la petici�n por POST con las credenciales de usuario
			OutputStream os = con.getOutputStream();
			String input = "grant_type=password&username="+username+"&password="+password;
			os.write(input.getBytes());
			os.flush();		
			
			int responseCode = con.getResponseCode();
            System.out.println("\n Sending request to URL : " + urlString);
            System.out.println("Response Code : " + responseCode);
    		
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
            AuthTokenInfo tokenInfo = null;
            
            // Guardamos toda la informaci�n relativa al token en un objeto de tipo AuthTokenInfo 
	        if(object!=null){
	            tokenInfo = new AuthTokenInfo();
	            tokenInfo.setAccess_token(object.getJSONObject(0).getString("access_token"));
	            tokenInfo.setToken_type(object.getJSONObject(0).getString("token_type"));
	            tokenInfo.setRefresh_token(object.getJSONObject(0).getString("refresh_token"));
	            tokenInfo.setExpires_in(object.getJSONObject(0).getInt("expires_in"));
	            tokenInfo.setScope(object.getJSONObject(0).getString("scope"));
	            System.out.println(tokenInfo);
	        }else{
	            System.out.println("No user exist----------");
	        }
	      //Cerramos la conexi�n
            in.close();  
            
    	} catch (Exception e){
			throw e;
		}
		
    	//Devolvemos el token de acceso para para usarlo en cada request que hagamos
		return object.getJSONObject(0).getString("access_token");
	}
    
    /*
     * Env�a las peticiones al servidor 
	 */
	private JSONArray peticion(String url,String params, String access_token) throws Exception{
		
		StringBuffer response = null;
		JSONArray object = null;
		
		try{
			// Actualizamos la IP del servidor por si ha habido algun cambio
			actualizaIp();
			
			String urlString ="";
			String bearerAuth="";
			// A�adimos el token de acceso a todas las peticiones excepto a las que vayan dirigidas
			// al registro de un nuevo usuario en la aplicaci�n, ya que son de libre acceso
			if(url=="gymRegister/") {
				urlString = SERVER_PATH +":"+SERVER_PORT+"/"+url;
			}else if (url=="checkUser/") {
				urlString = SERVER_PATH +":"+SERVER_PORT+"/"+url;
			}else {
				urlString = SERVER_PATH +":"+SERVER_PORT+"/"+url;//+"?access_token="+ access_token;
				bearerAuth="Bearer "+access_token;
				
			}
			// Establecemos conexi�n con el servidor
			URL urlObj = new URL(urlString);			
			HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
			
			//Esto solo lo hacemos para que Java no de problemas con el certificado self-signed.
			con.setHostnameVerifier(new HostnameVerifier(){
				public boolean verify(String hostname, SSLSession session){
					return true;
				}
			});
			
			if(bearerAuth!="") {
			con.setRequestProperty ("Authorization", bearerAuth);
			}
			
			// Seg�n el tipo de petici�n, definimos distintas propiedades en la cabecera
			if(params=="GET") {
				con.setRequestProperty("Accept", "application/json");
				con.setRequestMethod("GET");	
			}else if(params=="DELETE") {
				con.setRequestMethod("DELETE");				
			}else {
				con.setDoOutput(true);
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json");
				
				// Enviamos la petici�n por POST
				String input = params;
				OutputStream os = con.getOutputStream();
				os.write(input.getBytes());
				os.flush();
			}
			
            int responseCode = con.getResponseCode();
            System.out.println("\nSending request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
    		
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
            object = new JSONArray(responseSTR);
            
            //Mostramos la respuesta del servidor por consola
            if(object.length()>0){
            	System.out.println("Respuesta del servidor: "+ object.getJSONObject(0).toString());	
                System.out.println();
            }         
            //Cerramos la conexi�n
            in.close();
            
		} catch (Exception e){
			throw e;
		}		
		//Devolvemos objeto JSON con la respuesta completa del servidor
		return object;
	}
	
	
	/*
	 * M�todo para actualizar la IP del servidor seg�n la configurada por el usuario
	 */
	private void actualizaIp(){
		ConfiguracionIpServidor config = new ConfiguracionIpServidor();
		SERVER_PATH = "https://"+config.getIpServer();
	}
	
	/*
	 * M�todo para enviar las credenciales de usuario al servidor. �ste nos 
	 * indicar� en la respuestas si son v�lidas o no.
	 */
	public String login(String usuario, String passw, String access_token) throws Exception{
		String url = "gym/login/";
		String params = "{\"username\":\""+usuario+"\", \"passw\":\""+passw+"\" }";
		String codigo = "logstatus";
		return this.peticion(url, params, access_token).getJSONObject(0).getString(codigo);
	}
	
	/*
	 * M�todo para comprobar si un usuario est� disponible. �ste nos 
	 * indicar� en la respuestaa si es v�lido o no.
	 */
	public String checkUser(String usuario) throws Exception{
		String url = "checkUser/";
		String params = "{\"username\":\""+usuario+"\"}";
		String codigo = "uniqueUser";
		return this.peticion(url, params, "").getJSONObject(0).getString(codigo);
	}
	
	/*
	 * M�todo para enviar al servidor la informaci�n relativa a un nuevo usuario del sistema.
	 */
	public String register(String username, String password, String nombre, String apellidos, String dni, 
			String direccion, String movil) throws Exception{
		String url = "gymRegister/";
		String params = "{\"username\":\""+username+"\", \"passw\":\""+password+"\" "
						+",\"nombre\":\""+nombre+"\", \"apellidos\":\""+apellidos+"\" "
						+",\"dni\":\""+dni+"\", \"direccion\":\""+direccion+"\" "
						+",\"movil\":\""+movil+"\" }";
		String codigo = "nuevoRegistro";
		return this.peticion(url, params, "").getJSONObject(0).getString(codigo);
	}
	
	/*
	 * M�todo para enviar al servidor la informaci�n relativa a una sesi�n.
	 */
	public String nuevaSesion(String nomSesion, String monitor, String fecha, String numUsuarios, String access_token) throws Exception{
		String url = "gym/sesionAdd/";
		
		String params = "{\"user_Monitor\":\""+monitor+"\", \"nombre_Sesion\":\""+nomSesion+"\" "
						+",\"fecha\":\""+fecha+"\", \"num_Usuarios\":\""+numUsuarios+"\" "
						+",\"activa\":\""+"1"+"\" }";
		String codigo = "nuevaSesion";
		return this.peticion(url, params, access_token).getJSONObject(0).getString(codigo);
	}
	
	/*
	 * M�todo para solicitar al servidor todas las sesiones creadas por
	 * cierto monitor.
	 */
	public JSONArray sesionesMonitor(String monitor, String access_token) throws Exception{
		String url = "gym/sesionMonitor/"+monitor+"/";
		String params = "GET";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo que solicita al servidor toda la informaci�n de una sesi�n
	 */
	public JSONArray sesion(String id, String access_token) throws Exception{
		String url = "gym/sesion/"+id+"/";
		String params = "GET";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo para indicarle al servidor que debe eliminar cierta sesi�n.
	 */
	public JSONArray eliminaSesion(String id, String access_token) throws Exception{
		String url = "gym/sesion/"+id+"/";
		String params = "DELETE";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo para obtener del servidor toda la informaci�n de cierto ejercicio
	 */
	public JSONArray ejercicio(String idEjercicio, String access_token) throws Exception{
		String url = "gym/ejercicio/"+idEjercicio+"/";
		String params = "GET";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo para obtenet todos los ejercicios almacenados en la base de datos.
	 */
	public JSONArray ejercicios(String access_token) throws Exception{
		String url = "gym/ejercicio/";
		String params = "GET";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo para solicitar al servidor todos los ejercicios que no han sido
	 * incluidos en una sesi�n.
	 */
	public JSONArray ejerciciosNoSesion(String idSesion, String access_token) throws Exception{
		String url = "gym/ejercicioNoSesion/"+idSesion+"/";
		String params = "GET";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo para solicitar al servidor todos los ejercicios que forman
	 * parte de una sesi�n.
	 */
	public JSONArray ejerciciosSesion(String idSesion, String access_token) throws Exception{
		String url = "gym/ejercicioSesion/"+idSesion+"/";
		String params = "GET";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo para a�adir un ejercicio a una sesi�n.
	 */
	public JSONArray addEjercicioSesion(String idSesion, String idEjercicio, String access_token) throws Exception{
		String url = "gym/ejercicio/"+idSesion+"/"+idEjercicio+"/";
		String params = "{\"id_Ejercicio\":\""+idEjercicio+"\"}";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo para eliminar un ejercicio de una sesi�n.
	 */
	public JSONArray removeEjercicioSesion(String idSesion, String idEjercicio, String access_token) throws Exception{
		String url = "gym/ejercicio/"+idSesion+"/"+idEjercicio+"/";
		String params = "DELETE";
		return this.peticion(url, params, access_token);
	}
	
	/*
	 * M�todo utilizado para actualizar la IP del monitor asociada a una sesi�n.
	 * Con este m�todo evitamos problemas si el monitor quiere  monitorizar a los
	 * usuarios desde un PC distinto desde el que cre� la sesi�n.
	 */
	public JSONArray actualizaIpMonitorSesion(String idSesion, int puerto, String access_token) throws Exception{
		String url = "gym/sesionMonitor/";
		String params = "{\"id_Sesion\":\""+idSesion+"\", \"ip_Monitor\":\""+puerto+"\" }";
		return this.peticion(url, params, access_token);				
	}
	
	/*
	 * M�todo para indicar al servidor que una sesi�n ha finalizado, 
	 * pasando a estar en el estado de NO activa
	 */
	public JSONArray setSesionNoActiva(String idSesion, String access_token) throws Exception{
		String url = "gym/sesion/";
		String params = "{\"id_Sesion\":\""+idSesion+"\"}";
		return this.peticion(url, params, access_token);
	}
}
