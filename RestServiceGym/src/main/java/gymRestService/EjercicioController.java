package gymRestService;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;

/*
 * Clase que representa un controlador REST de ejercicios. Mapea las 
 * operaciones sobre recursos REST relacionados con ejercicios y hace uso 
 * del DAO para hacerlas efectivas en la base de datos.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

@RestController
@ImportResource("classpath:spring/beanLocations.xml")
public class EjercicioController {

    // Obtenemos el DAO mediante inyección de dependencias
    @Autowired
    private EjercicioDaoImpl ejercicioDao;
    
    
    // (J,A) Obtiene un ejercicio
    @RequestMapping(value = "/gym/ejercicio/{id_ejercicio}/", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Ejercicio> getEjercicio(@PathVariable("id_ejercicio") int id_ejercicio) {
    	
	return ejercicioDao.getEjercicio(id_ejercicio);
    }
    
    
    // (J) Obtiene todos los ejercicios
    @RequestMapping(value ="/gym/ejercicio//", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Ejercicio> getAllEjercicios() {

	return ejercicioDao.getAllEjercicios();
    }
    
    
    // (J) Obtiene los ejercicios no incluidos en la sesion
    @RequestMapping(value = "/gym/ejercicioNoSesion/{id_sesion}/", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Ejercicio> getEjercicioNoSesion(@PathVariable("id_sesion") int id_sesion) {
    	
	return ejercicioDao.getEjercicioNoSesion(id_sesion);
    }
    
    
    // (J,A) Obtiene los ejercicios incluidos en sesion
    @RequestMapping(value = "/gym/ejercicioSesion/{id_sesion}/", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<EjercicioB> getEjercicioSesion(@PathVariable("id_sesion") int id_sesion) {
    	
	return ejercicioDao.getEjercicioSesion(id_sesion);
    }
    
    
    // (J) Añade un ejercicio a una sesion
    @RequestMapping(value = "/gym/ejercicio/{id_sesion}/{id_ejercicio}/", method = RequestMethod.POST, consumes = "application/json")
    public String addEjercicioSesion(
    		@PathVariable("id_sesion") int id_sesion,
    		@PathVariable("id_ejercicio") int id_ejercicio) {

	return ejercicioDao.addEjercicioSesion(id_sesion, id_ejercicio);
    }
    
    
    // (J) Elimina un ejercicio de sesion
    @RequestMapping(value = "/gym/ejercicio/{id_sesion}/{id_ejercicio}/", method = RequestMethod.DELETE)
    public String deleteSesion(
    		@PathVariable("id_sesion") int id_sesion,
    		@PathVariable("id_ejercicio") int id_ejercicio) {

	return ejercicioDao.deleteEjercicio(id_sesion, id_ejercicio);
    }

}
