package gymRestService;

/*
 * Clase que representa un usuario en la aplicación.
 * 
 * Diseño por Pablo Gallardo Hiraldo
 * Versión: 1.0
 *
 */

public class Usuario {

    private int Id_Usuario;
    private byte enabled;
    private String nombre, apellidos, dni, direccion, movil, username, passw;
    
    public Usuario() {
    }

    public Usuario(int Id_Usuario, String nombre, String apellidos, String dni,
	    String direccion, String movil, String username, String passw, byte enabled) {
    	
    this.setId_Usuario(Id_Usuario);
	this.setNombre(nombre);
	this.setApellidos(apellidos);
	this.setDni(dni);
	this.setDireccion(direccion);
	this.setMovil(movil);
	this.setUsername(username);
	this.setPassw(passw);
	this.setEnabled(enabled);
    }

	public int getId_Usuario() {
		return Id_Usuario;
	}

	public void setId_Usuario(int Id_Usuario) {
		this.Id_Usuario = Id_Usuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassw() {
		return passw;
	}

	public void setPassw(String passw) {
		this.passw = passw;
	}

	public byte getEnabled() {
		return enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}
  
}
