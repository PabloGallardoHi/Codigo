package monitorgym;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.lang.String;

import weupnp.SelectorPuertoNat;


/*
 * Clase para conseguir la ip del usuario a la cual
 * realizar las peticiones de vídeo
 */
public class HiloGetIpUsuario extends Thread {

	private static final int PORT = 54321;
	private ArrayList<String> nomUsuarios;
	private ArrayList<String> ipUsuarios;
	private ServerSocket server;
	private String ip_puerto, nombreUsuario, mIdSesion, access_Token;
	private SelectorPuertoNat upnp;
	private GestorPeticiones gestor;
	
	public HiloGetIpUsuario(String idSesion, final String access_token){
		mIdSesion = idSesion;
		access_Token = access_token; 
		nomUsuarios = new ArrayList<String>();
		ipUsuarios = new ArrayList<String>();
		gestor = new GestorPeticiones();
	}
	
	public void run(){
		try{
			// Hacemos mapeo del puerto en el NAT
			upnp = new SelectorPuertoNat(PORT);
			
			// Actualizamos la IP de la sesión almacenada en la base de datos
			gestor.actualizaIpMonitorSesion(mIdSesion, upnp.getPortUsed(), access_Token);
			// Creamos el socket a la espera de los usuarios
			server = new ServerSocket(upnp.getPortUsed());
			while(true){
				System.out.println("Esperando conexiones usuarios...");
				Socket socket = server.accept();
				System.out.println("Conexión establecida");
				if(socket.isConnected()){
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					ip_puerto = socket.getRemoteSocketAddress().toString();
					// Almacenamos la IP del usuario y su nombre en dos listas
					ipUsuarios.add(ip_puerto);
					nombreUsuario = input.readObject().toString();
					if(nomUsuarios.indexOf(nombreUsuario) == -1){
						nomUsuarios.add(nombreUsuario);
					}
					socket.close();
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Método para obtener la IP de un usuario
	 */
	public String getIpUsuario(int i){
		return ipUsuarios.get(i);
	}
	
	/* 
	 * Método para obtener el nombre de un usuario
	 */
	public String getNomUsuario(int i){
		return nomUsuarios.get(i);
	}
	
	/*
	 * Método para obtener una lista con todos los nombres
	 * de los usuarios
	 */
	public ArrayList<String> getUsuarios(){
		return nomUsuarios;
	}
	
	/*
	 * Método para liberar el puerto mapeado anteriormente
	 */
	public void releasePort() throws Exception{
		upnp.releasePort();		
	}
}
