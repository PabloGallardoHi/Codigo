package gymRestService.security;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

/*
 * Clase que inicializa el ServletContext nada más ejecutarse el programa 
 * para que la configuración de seguridad pueda disponer de el inmediatamente.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

@Configuration
public class Initializer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.err.println("Iniciando...");
    }
    
}
