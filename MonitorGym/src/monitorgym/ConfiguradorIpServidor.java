package monitorgym;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ConfiguradorIpServidor {

	public ConfiguradorIpServidor(String ip){
		Properties propiedades = new Properties();
	    OutputStream salida = null;
	    
	    try {
	        salida = new FileOutputStream("configuracion.properties");
	        
	        // asignamos los valores a las propiedades
	        propiedades.setProperty("ip_server", ip);
	        
	        // guardamos el archivo de propiedades en la carpeta de aplicación
	        propiedades.store(salida, null);
	    } catch (IOException io) {
	        io.printStackTrace();
	    } finally {
	        if (salida != null) {
	            try {
	                salida.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	    }
	}
}
