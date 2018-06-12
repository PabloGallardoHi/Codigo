package gymRestService;

import java.util.List;

/*
 * Clase que representa la interfaz DAO de usuarios. Abstrae las 
 * operaciones necesarias sobre la base de datos relacionadas con los 
 * usuarios a petición del controlador REST de usuarios.
 * 
 * Diseño por Pablo Gallardo
 * Versión: 1.0
 *
 */

public abstract interface UsuarioDao {
	
	public abstract String checkUser(String user);
	public abstract String addUser (String nombre, String apellidos, String dni,
			String direccion, String movil, String username, String password);
	
	public abstract String getPass(String username); // Obtiene contraseña de un usuario
	public abstract String getRole(String username); // Obtiene rol de un usuario
	public abstract List<Usuario> getUsuario(String username);
	
    public abstract String login(String user, String passw);
    public abstract String addUsuarioSesion(int id_sesion, String username);

}
