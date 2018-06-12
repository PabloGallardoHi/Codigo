package gymRestService;

import java.util.List;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Clase que implementa la interfaz DAO de usuarios. Realiza las 
 * operaciones necesarias sobre la base de datos relacionadas con los 
 * uarios a petición del controlador REST de usuarios. No se 
 * comentan los métodos debido a que se corresponden con las operaciones 
 * del controlador, con lo que ya están descritos previamente.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class UsuarioDaoImpl implements UsuarioDao {

    JdbcTemplate jdbcTemplate;

    // Obtenemos el codificador mediante inyección de dependencias
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Obtiene contraseña de un usuario
    public String getPass(String username) {
    	String pass = jdbcTemplate.queryForObject("SELECT passw FROM usuarios WHERE username =?",
        		new Object[] { username }, String.class);
    	return pass;
    }
    
    // Obtiene rol de un usuario
    public String getRole(String username) {
    	String role = jdbcTemplate.queryForObject("SELECT role FROM roles_usuarios WHERE username =?",
        		new Object[] { username }, String.class);
    	return role;
    }
    
    // Obtiene un usuario
    public List<Usuario> getUsuario(String username){
    	return jdbcTemplate.query("SELECT * FROM usuarios WHERE username =?",
        		new Object[] { username }, new UsuarioRowMapper());   	
    }
    
    
    public String addUser (String nombre, String apellidos, String dni,
			String direccion, String movil, String username, String password) {
  
        String encodedPassword = passwordEncoder.encode(password); // Codificamos la contraseña antes de guardarla 

    	int consulta1 = jdbcTemplate.update("INSERT INTO usuarios "
    			+ "(nombre, apellidos, dni, direccion, movil, username, passw) "
    			+ "VALUES(?,?,?,?,?,?,?)", 
    			new Object[] { nombre, apellidos, dni, direccion, movil, username, encodedPassword });
    	
    	int consulta2 = jdbcTemplate.update("INSERT INTO roles_usuarios (username, role) "
    			+ "VALUES(?,'ROLE_USER')", 
    			new Object[] { username });
    	
    	// Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul ="";
    	if (consulta1==0 || consulta2==0){
    		 resul = "[{\"nuevoRegistro\":\"0\"}]";
    	}else{
    		resul = "[{\"nuevoRegistro\":\"1\"}]";
    	}	
    	return resul;    	
    }
    
       
    public String checkUser(String user) {
   
    	int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usuarios WHERE username=?",
    			new Object[] { user }, Integer.class);
    	
    	// Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul="";
    	if (count==0){
			 resul = "[{\"uniqueUser\":\"1\"}]";		
		}else{
			resul = "[{\"uniqueUser\":\"0\"}]";
		}	    
    	return resul;   	
    }
    
    
    public String login(String user, String passw) {
    	
    	String passBCrypt= jdbcTemplate.queryForObject("SELECT passw FROM usuarios WHERE username=?",
    			new Object[] { user }, String.class);
  
        boolean count = passwordEncoder.matches(passw, passBCrypt); // Comprobamos la contraseña
        
    	// Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul="";
    	if (count==false){
			 resul = "[{\"logstatus\":\"0\"}]";		
		}else{
			resul = "[{\"logstatus\":\"1\"}]";
		}	   
    	return resul;  	
    }
    
    
    public String addUsuarioSesion(int id_sesion, String username) {
    	
    	String resul="";
    	
    	int id_usuario = jdbcTemplate.queryForObject("SELECT Id_Usuario FROM usuarios WHERE username = ?", 
    			new Object[] { username}, Integer.class);
    	
    	int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sesionesdeusuario WHERE Id_Sesion=? AND Id_Usuario=?",
    			new Object[] { id_sesion, id_usuario }, Integer.class);
    	
    	// Comprobamos que el usuario no está subscrito a la sesión antes de añadirlo
    	// Y devolvemos una cadena comunicando si toda la operación ha tenido éxito
    	if (count==1){
    		resul = "[{\"addUserSesion\":\"1\"}]";	
		}else{		
			int consulta = jdbcTemplate.update("INSERT INTO sesionesdeusuario (Id_Sesion, Id_Usuario) VALUES(?, ?)",
					new Object[] { id_sesion, id_usuario });
			
			if(consulta==1){
				resul = "[{\"addUserSesion\":\"1\"}]";
			} else{
				resul = "[{\"addUserSesion\":\"0\"}]";
			}
		}   	
    	return resul;   	
    }
 
    
    // Inyección del dataSource mediante el constructor
    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public UsuarioDaoImpl() {
    }
    
}
