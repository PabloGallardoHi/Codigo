package gymRestService;

import java.util.List;

/*
 * Clase que representa la interfaz DAO de sesiones. Abstrae las 
 * operaciones necesarias sobre la base de datos relacionadas con los 
 * sesiones a petición del controlador REST de sesiones.
 * 
 * Diseño por Pablo Gallardo
 * Versión: 1.0
 *
 */

public abstract interface SesionDao {
	
    public abstract String addSesion( String user_monitor, String nombre_sesion, String fecha,
    	    int num_usuarios, byte activa);
    public abstract String updateSesion(int id_sesion);
    public abstract String updateSesionMonitor(int id_sesion, String ip_monitor);
    public abstract List<Sesion> getSesionesActivas();
    public abstract List<Sesion> getSesion(int id_sesion);
    public abstract List<Sesion> getSesionMonitor(String monitor);
    public abstract String deleteSesion(int id_sesion);

}
