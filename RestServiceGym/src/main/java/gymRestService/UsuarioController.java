package gymRestService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;

/*
 * Clase que representa un controlador REST de usuarios. Mapea las 
 * operaciones sobre recursos REST relacionados con usuarios y hace uso 
 * del DAO para hacerlas efectivas en la base de datos.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

@RestController
@ImportResource("classpath:spring/beanLocations.xml")
public class UsuarioController {

    // Obtenemos el DAO mediante inyección de dependencias
    @Autowired
    private UsuarioDaoImpl usuarioDao;
    
    
    // (J) Comprueba si un usuario no está dado de alta aún
    @RequestMapping(value = "/checkUser/", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String checkUser (@RequestBody Usuario usuario) {
   
    return usuarioDao.checkUser(usuario.getUsername());
    }
    
    
    // (J) Añade un usuario
    @RequestMapping(value = "/gymRegister/", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String addUser (@RequestBody Usuario usuario) {

	return usuarioDao.addUser(usuario.getNombre(), usuario.getApellidos(), usuario.getDni(),
			usuario.getDireccion(), usuario.getMovil(),usuario.getUsername(), usuario.getPassw());
    }
    
    
    // (J,A) Comprueba que la información de acceso (user y pass) es correcta
    @RequestMapping(value = "/gym/login/", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String login(@RequestBody Usuario usuario) {
    	
	return usuarioDao.login(usuario.getUsername(), usuario.getPassw());
    }
    
    
    // (A) Añade un usuario a una sesión
    @RequestMapping(value = "/gym/addUserSesion/{id_sesion}/{username}", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody String addUsuarioSesion(
    		@PathVariable("id_sesion") int id_sesion,
    		@PathVariable("username") String username) {
    	
	return usuarioDao.addUsuarioSesion(id_sesion, username);
    }
    
}
