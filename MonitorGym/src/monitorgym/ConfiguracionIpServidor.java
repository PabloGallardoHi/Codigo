package monitorgym;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfiguracionIpServidor {

	String ipServer;
	
	public ConfiguracionIpServidor(){
		Properties propiedades = new Properties();
	    InputStream entrada = null;
	    
	    try {

	        entrada = new FileInputStream("configuracion.properties");

	        // cargamos el archivo de propiedades
	        propiedades.load(entrada);

	        // obtenemos las propiedades y la almacenamos
	        ipServer = propiedades.getProperty("ip_server");

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        if (entrada != null) {
	            try {
	                entrada.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	public String getIpServer(){
		return ipServer;
	}
}
