package gymRestService;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;

/*
 * Clase que representa un controlador REST de sesiones. Mapea las 
 * operaciones sobre recursos REST relacionados con sesiones y hace uso 
 * del DAO para hacerlas efectivas en la base de datos.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

@RestController
@ImportResource("classpath:spring/beanLocations.xml")
public class SesionController {

    // Obtenemos el DAO mediante inyección de dependencias
    @Autowired
    private SesionDaoImpl sesionDao;
    

    // (J) Añade una sesión
    @RequestMapping(value = "/gym/sesionAdd/", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String addSesion(@RequestBody Sesion sesion) {

	return sesionDao.addSesion(sesion.getUser_Monitor(), sesion.getNombre_Sesion(),
		sesion.getFecha(), sesion.getNum_Usuarios(), sesion.getActiva());
    }
    
    
    // (A) Obtiene las sesiones activas
    @RequestMapping(value = "/gym/sesionesActivas/", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Sesion> getSesionesactivas() {
    	
	return sesionDao.getSesionesActivas();
    }
    
    
    // (J,A) Obtiene una sesión
    @RequestMapping(value = "/gym/sesion/{id_sesion}/", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Sesion> getSesion(@PathVariable("id_sesion") int id_sesion) {
    	
	return sesionDao.getSesion(id_sesion);
    }
    
    
    // (J) Elimina una sesión
    @RequestMapping(value = "/gym/sesion/{id_sesion}/", method = RequestMethod.DELETE)
    public String deleteSesion(@PathVariable("id_sesion") int id_sesion) {

	return sesionDao.deleteSesion(id_sesion);
    }
    
    
    // (J) Modifica una sesión
    @RequestMapping(value = "/gym/sesion/", method = RequestMethod.POST, consumes = "application/json")
    public String updateSesion(@RequestBody Sesion sesion) {

	return sesionDao.updateSesion(sesion.getId_Sesion());
    }
 
    
    // (J) Obtiene las sesiones de un determinado monitor
    @RequestMapping(value = "/gym/sesionMonitor/{monitor}/", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Sesion> getSesionMonitor(@PathVariable("monitor") String monitor) {
    	
	return sesionDao.getSesionMonitor(monitor);
    }
    
    
    // (J) Añade la IP del monitor para que el cliente Android pueda conectarse más tarde
    @RequestMapping(value = "/gym/sesionMonitor/", method = RequestMethod.POST, consumes = "application/json")
    public String updateSesionMonitor(@RequestBody Sesion sesion) {

	return sesionDao.updateSesionMonitor(sesion.getId_Sesion(), sesion.getIp_Monitor());
    }

}
