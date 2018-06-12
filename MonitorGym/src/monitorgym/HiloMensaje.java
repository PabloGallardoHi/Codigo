package monitorgym;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class HiloMensaje extends Thread {

	private String SERVER_IP, mMensaje;
	private int SERVER_PORT;
	
	public HiloMensaje(String ip, int port, String mensaje){
		System.out.println("IP mensaje: "+ip.substring(1,ip.indexOf(":")));
		SERVER_IP = ip.substring(1,ip.indexOf(":"));
		SERVER_PORT = port;
		mMensaje = mensaje;
	}
	
	public void run(){
		try{
			// Conectamos con el socket abierto en la aplicación del usuario
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			if(socket.isConnected()){
				// Enviamos el mensaje
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				output.writeObject(mMensaje);
				socket.close();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
