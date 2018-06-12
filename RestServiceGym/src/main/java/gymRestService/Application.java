package gymRestService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import gymRestService.security.Initializer;

/*
 * Clase principal del servicio web, configurada para la inicialización
 * con Spring Boot
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

@SpringBootApplication
@EnableResourceServer
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(new Class[] { Application.class, Initializer.class }, args);
    }
 
}
