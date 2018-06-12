package gymRestService;

/*
 * Clase que representa un ejercicio en la aplicación.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class Ejercicio {

    private int Id_Ejercicio;
    private String nombre_Ejercicio, Descripcion, Finalidad, Duracion, URL_video;
    
    public Ejercicio() {
    }

    public Ejercicio(int Id_Ejercicio, String nombre_Ejercicio, String Descripcion, String Finalidad,
	    String Duracion, String URL_video) {
    	
    this.setId_Ejercicio(Id_Ejercicio);
	this.setNombre_Ejercicio(nombre_Ejercicio);
	this.setDescripcion(Descripcion);
	this.setFinalidad(Finalidad);
	this.setDuracion(Duracion);
	this.setURL_video(URL_video);
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

	public String getFinalidad() {
		return Finalidad;
	}

	public void setFinalidad(String Finalidad) {
		this.Finalidad = Finalidad;
	}

	public String getDuracion() {
		return Duracion;
	}

	public void setDuracion(String Duracion) {
		this.Duracion = Duracion;
	}

	public String getURL_video() {
		return URL_video;
	}

	public void setURL_video(String URL_video) {
		this.URL_video = URL_video;
	}

}
