package gymRestService;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/*
 * Clase que implementa la interfaz DAO de ejercicios. Realiza las 
 * operaciones necesarias sobre la base de datos relacionadas con los 
 * ejercicios a petición del controlador REST de ejercicios. No se 
 * comentan los métodos debido a que se corresponden con las operaciones 
 * del controlador, con lo que ya están descritos previamente.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class EjercicioDaoImpl implements EjercicioDao {

    JdbcTemplate jdbcTemplate;

    
    public List<Ejercicio> getEjercicio(int id_ejercicio) {
    	return jdbcTemplate.query("SELECT * FROM ejercicios WHERE id_ejercicio =?",
    		new Object[] { id_ejercicio }, new EjercicioRowMapper());
    }
    
    
    public List<Ejercicio> getAllEjercicios() {
    	return jdbcTemplate.query("SELECT * FROM ejercicios",
    		new EjercicioRowMapper());
    }
    
    
    public List<Ejercicio> getEjercicioNoSesion(int id_sesion) {
    	return jdbcTemplate.query("SELECT * FROM ejercicios WHERE ejercicios.Id_Ejercicio NOT IN "
    			+ "(SELECT ejerciciosdesesion.Id_Ejercicio FROM ejerciciosdesesion WHERE ejerciciosdesesion.Id_Sesion=?)",
    		new Object[] { id_sesion }, new EjercicioRowMapper());
    }
    
    
    public List<EjercicioB> getEjercicioSesion(int id_sesion) {
    	return jdbcTemplate.query("SELECT ejercicios.Id_Ejercicio, ejercicios.nombre_Ejercicio, ejercicios.Descripcion, "
    			+ "sesiones.user_Monitor,sesiones.nombre_Sesion FROM (sesiones INNER JOIN ejerciciosdesesion ON "
    			+ "sesiones.Id_Sesion=ejerciciosdesesion.Id_Sesion INNER JOIN ejercicios ON "
    			+ "ejercicios.Id_Ejercicio=ejerciciosdesesion.Id_Ejercicio) WHERE ejerciciosdesesion.Id_Sesion=?",
    		new Object[] { id_sesion }, new EjercicioBRowMapper());
    }
 
    
    public String addEjercicioSesion(int id_sesion, int id_ejercicio) {
    	int consulta = jdbcTemplate.update("INSERT INTO ejerciciosdesesion (Id_Ejercicio, Id_Sesion) VALUES(?,?)",
			new Object[] { id_ejercicio, id_sesion });
	
	    // Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul ="";
		if (consulta==0){
			resul = "[{\"addEjercicioSesion\":\"0\"}]";
		}else{
			resul = "[{\"addEjercicioSesion\":\"1\"}]";
		}	
		return resul;
    }
    
      
    public String deleteEjercicio(int id_sesion, int id_ejercicio) {
    	int consulta= jdbcTemplate.update("DELETE FROM ejerciciosdesesion WHERE Id_Sesion=? AND Id_Ejercicio=?",
    		new Object[] { id_sesion, id_ejercicio });
    	
    	// Devolvemos una cadena comunicando si la operación ha tenido éxito
    	String resul ="";
    	if (consulta==0){
    		resul = "[{\"eliminaEjercicioSesion\":\"0\"}]";
    	}else{
    		resul = "[{\"eliminaEjercicioSesion\":\"1\"}]";
    	}	
    	return resul;
    }
        
    
    // Inyección del dataSource mediante el constructor
    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public EjercicioDaoImpl() {
    }
}
