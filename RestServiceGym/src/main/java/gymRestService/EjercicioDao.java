package gymRestService;

import java.util.List;

/*
 * Clase que representa la interfaz DAO de ejercicios. Abstrae las 
 * operaciones necesarias sobre la base de datos relacionadas con los 
 * ejercicios a petición del controlador REST de ejercicios.
 * 
 * Diseño por Pablo Gallardo
 * Versión: 1.0
 *
 */

public abstract interface EjercicioDao {

    public abstract String addEjercicioSesion(int id_sesion, int id_ejercicio);
    public abstract List<Ejercicio> getEjercicio (int id_ejercicio);
    public abstract List<Ejercicio> getEjercicioNoSesion (int id_sesion);
    public abstract List<EjercicioB> getEjercicioSesion (int id_sesion);
    public abstract List<Ejercicio> getAllEjercicios();
    public abstract String deleteEjercicio(int id_sesion, int id_ejercicio);
    
}
