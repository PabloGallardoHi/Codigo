package gymRestService;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/*
 * Clase que implementa la interfaz DAO de sesiones. Realiza las 
 * operaciones necesarias sobre la base de datos relacionadas con las 
 * sesiones a petición del controlador REST de sesiones. No se 
 * comentan los métodos debido a que se corresponden con las operaciones 
 * del controlador, con lo que ya están descritos previamente.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class SesionDaoImpl implements SesionDao {

    JdbcTemplate jdbcTemplate;

    
    public String addSesion(String user_monitor, String nombre_sesion, String fecha, 
    		int num_usuarios, byte activa) {
	
    	int consulta = jdbcTemplate.update("INSERT INTO sesiones (nombre_Sesion, user_Monitor, fecha, num_Usuarios, "
    			+ "activa, ip_Monitor, descripcion) VALUES (?,?,?,?,?, '', '')",
    			new Object[] { nombre_sesion, user_monitor, fecha, num_usuarios, activa });
	
    	// Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul ="";
    	if (consulta==0){
    		resul = "[{\"nuevaSesion\":\"0\"}]";
    	}else{
    		resul = "[{\"nuevaSesion\":\"1\"}]";
    	}	
    	return resul;
    }
    
    
    public List<Sesion> getSesionesActivas() {
    	return jdbcTemplate.query("SELECT * FROM sesiones WHERE activa=1", 
    			new SesionRowMapper());
    }
   
   
    public List<Sesion> getSesion(int id_sesion) {
    	return jdbcTemplate.query("SELECT * FROM sesiones WHERE Id_Sesion=?", 
    			new Object[] { id_sesion }, new SesionRowMapper());
    }
    
    
    public String deleteSesion(int id_sesion) {
    	int consulta= jdbcTemplate.update("DELETE FROM sesiones WHERE Id_Sesion=?", 
    			new Object[] { id_sesion });
    	
    	// Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul ="";
    	if (consulta==0){
   		 	resul = "[{\"eliminaSesion\":\"0\"}]";
    	}else{
    		resul = "[{\"eliminaSesion\":\"1\"}]";
    	}	
    	return resul;
    }
     
    
    public String updateSesion (int id_sesion) {
    	int consulta = jdbcTemplate.update("UPDATE sesiones SET activa=0 WHERE Id_Sesion=?", 
    			new Object[] { id_sesion });
    	
    	// Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul ="";
    	if (consulta==0){
    		resul = "[{\"setNoActiva\":\"0\"}]";
    	}else{
    		resul = "[{\"setNoActiva\":\"1\"}]";
    	}	
    	return resul;
    }
   
    
    public List<Sesion> getSesionMonitor(String monitor) {
    	return jdbcTemplate.query("SELECT * FROM sesiones WHERE user_Monitor=?",
    		new Object[] { monitor }, new SesionRowMapper());
    }
    
    
    public String updateSesionMonitor(int id_sesion, String ip_monitor) {
    	String ipypuerto = "192.168.1.36:"+ip_monitor;
    	int consulta = jdbcTemplate.update("UPDATE sesiones SET ip_Monitor=? WHERE Id_Sesion=?", 
    			new Object[] { ipypuerto, id_sesion });
    	
    	// Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul ="";
    	if (consulta==0){
    		resul = "[{\"setIpMonitor\":\"0\"}]";
    	}else{
    		resul = "[{\"setIpMonitor\":\"1\"}]";
    	}	
    	return resul;
    }

    
    // Inyección del dataSource mediante el constructor
    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public SesionDaoImpl() {
    }
}
