package gymRestService;

/*
 * Clase que representa una sesión en la aplicación.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class Sesion {

	private int Id_Sesion, num_Usuarios;
	private byte activa;
    private String user_Monitor, nombre_Sesion, fecha, ip_Monitor, descripcion;
    
    public Sesion() {
    }

    public Sesion(int Id_Sesion, String user_Monitor, String nombre_Sesion, String fecha,
	    int num_Usuarios, String ip_Monitor, byte activa, String descripcion) {
	
    this.setId_Sesion(Id_Sesion);	
    this.setUser_Monitor(user_Monitor);
	this.setNombre_Sesion(nombre_Sesion);
	this.setFecha(fecha);
	this.setNum_Usuarios(num_Usuarios);
	this.setIp_Monitor(ip_Monitor);
	this.setActiva(activa);
	this.setDescripcion(descripcion);
    }

	

	public int getId_Sesion() {
		return Id_Sesion;
	}

	public void setId_Sesion(int id_Sesion) {
		Id_Sesion = id_Sesion;
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

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public int getNum_Usuarios() {
		return num_Usuarios;
	}

	public void setNum_Usuarios(int num_Usuarios) {
		this.num_Usuarios = num_Usuarios;
	}

	public String getIp_Monitor() {
		return ip_Monitor;
	}

	public void setIp_Monitor(String ip_Monitor) {
		this.ip_Monitor = ip_Monitor;
	}

	public byte getActiva() {
		return activa;
	}

	public void setActiva(byte activa) {
		this.activa = activa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
