package gymRestService;

/*
 * Clase que representa un ejercicio (que también contiene datos de la sesión) en la aplicación.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class EjercicioB {

    private int Id_Ejercicio;
    private String nombre_Ejercicio, Descripcion, user_Monitor, nombre_Sesion;
    
    public EjercicioB() {
    }

    public EjercicioB(int Id_Ejercicio, String nombre_Ejercicio, String Descripcion, String user_Monitor, String nombre_Sesion) {
    this.setId_Ejercicio(Id_Ejercicio);
	this.setNombre_Ejercicio(nombre_Ejercicio);
	this.setDescripcion(Descripcion);
	this.setUser_Monitor(user_Monitor);
	this.setNombre_Sesion(nombre_Sesion);
    }

	public int getId_Ejercicio() {
		return Id_Ejercicio;
	}

	public void setId_Ejercicio(int Id_Ejercicio) {
		this.Id_Ejercicio = Id_Ejercicio;
	}

	public String getNombre_Ejercicio() {
		return nombre_Ejercicio;
	}

	public void setNombre_Ejercicio(String nombre_Ejercicio) {
		this.nombre_Ejercicio = nombre_Ejercicio;
	}

	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String Descripcion) {
		this.Descripcion = Descripcion;
	}

	public String getUser_Monitor() {
		return user_Monitor;
	}

	public void setUser_Monitor(String user_Monitor) {
		this.user_Monitor = user_Monitor;
	}

	public String getNombre_Sesion() {
		return nombre_Sesion;
	}

	public void setNombre_Sesion(String nombre_Sesion) {
		this.nombre_Sesion = nombre_Sesion;
	}
    
}
