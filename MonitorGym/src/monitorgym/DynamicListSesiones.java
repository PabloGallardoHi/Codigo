package monitorgym;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.json.JSONArray;

/*
 * Clase desde la que se gestionará todo lo relativo a mostrar
 * las sesiones del monitor por pantalla.
 */
public class DynamicListSesiones extends JList<String> {
	
	private ArrayList<String> idSesiones;
	private ArrayList<String> idSesionesActivas;
	private ArrayList<String> idSesionesNoActivas;
	private ArrayList<String> nomSesiones;
	private ArrayList<String> nomSesionesActivas;
	private ArrayList<String> nomSesionesNoActivas;
	private DefaultListModel<String> model;
	private GestorPeticiones gestor;
	private VentanaSesiones ventanaSesiones;
	private String idSesionMostrada;
	private int posSesionMostrada;
	
	public DynamicListSesiones(JSONArray sesiones, VentanaSesiones ventana, final String access_token) throws Exception{
		String nombre, id;
		int activa;
		//Inicializamos las variables
		gestor = new GestorPeticiones();
		ventanaSesiones=ventana;
		idSesiones = new ArrayList<String>();
		idSesionesActivas = new ArrayList<String>();
		idSesionesNoActivas = new ArrayList<String>();
		nomSesiones = new ArrayList<String>();
		nomSesionesActivas = new ArrayList<String>();
		nomSesionesNoActivas = new ArrayList<String>();
		model = new DefaultListModel<String>();
		this.setModel(model);
		this.setBounds(10, 36, 76, 175);
		try{
			//Llenamos la lista con las sesiones obtenidas del servidor
			for(int i=0; i<sesiones.length(); i++){
				//System.out.println("Respuesta del culo: "+ sesiones.getJSONObject(0).toString());
				id = Integer.toString(sesiones.getJSONObject(i).getInt("id_Sesion")) ;
				nombre = sesiones.getJSONObject(i).getString("nombre_Sesion");
				nomSesiones.add(nombre);
				idSesiones.add(id);
				activa = sesiones.getJSONObject(i).getInt("activa");
				// Dependiendo de si la sesión es activa o no irá a una lista diferente
				if(activa==0){
					nomSesionesNoActivas.add(nombre);
					idSesionesNoActivas.add(id);
				} else if(activa==1){
					nomSesionesActivas.add(nombre);
					idSesionesActivas.add(id);
					model.addElement(nombre);
				}
			}
		} catch (Exception e){
			throw e;
		}
		this.addMouseListener(this.nuevoMouseListener(access_token));
	}
	
	/*
	 * Listener que atenderá al doble click sobre una sesión.
	 */
	private MouseListener nuevoMouseListener(final String access_token) throws Exception{
		MouseListener mouseListener = new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	mostrarSesion(e, access_token);
		    }
		};
		
		return mouseListener;
	}
	
	
	/*
	 * Método para cambiar las sesiones que se muestrán en la lista
	 * dependiendo de la selección del usuario
	 */
	public void cambiaDatosLista(int option, final String access_token){
		int i;
		// Actualizamos todos los elementos que forman parte de las listas, ya que
		// ha habido una modificación en alguna de ellas.
		model.removeAllElements();
		if(option==0 && nomSesionesActivas.size()>0){
			for(i=0; i<nomSesionesActivas.size(); i++){
				model.addElement(nomSesionesActivas.get(i).toString());
			}
		} else if(option==1 && nomSesionesNoActivas.size()>0){
			for(i=0; i<nomSesionesNoActivas.size(); i++){
				model.addElement(nomSesionesNoActivas.get(i).toString());
			}
		} else if (option==2 && nomSesiones.size()>0){
			for(i=0; i<nomSesiones.size(); i++){
				model.addElement(nomSesiones.get(i).toString());
			}
		}
	}
	
	/*
	 * Método para añadir una nueva sesión tanto a la base de datos como a la lista
	 */
	public boolean nuevaSesion(String nomSesion, String monitor, String fecha, String numUsuarios, final String access_token) throws Exception{
		boolean resul = false;
		
		try{
			// Crea la nueva sesión
			if(gestor.nuevaSesion(nomSesion, monitor, fecha, numUsuarios, access_token).equals("1")){
				// Si no ha habido problemas para crear la sesión...
				resul = true;
				//... añadimos la sesión creada a las listas
				JSONArray sesiones = gestor.sesionesMonitor(monitor, access_token);
		      //idSesiones.add(sesiones.getJSONObject(sesiones.length()-1).getString("Id_Sesion"));
				idSesiones.add(Integer.toString(sesiones.getJSONObject(sesiones.length()-1).getInt("id_Sesion")));
				nomSesiones.add(sesiones.getJSONObject(sesiones.length()-1).getString("nombre_Sesion"));
				idSesionesActivas.add(Integer.toString(sesiones.getJSONObject(sesiones.length()-1).getInt("id_Sesion")));
				nomSesionesActivas.add(sesiones.getJSONObject(sesiones.length()-1).getString("nombre_Sesion"));
				
				
				
			}
		} catch(Exception e){
			throw e;
		}
		
		return resul;
	}
	
	
	/*
	 * Método para eliminar la sesión mostrada por pantalla
	 */
	public boolean eliminarSesionMostrada(int option, final String access_token) throws Exception{
		JSONArray consulta;
		boolean resul = false;
		
		try{
			// Eliminamos la sesión en la base de datos
			consulta = gestor.eliminaSesion(idSesionMostrada, access_token);
			if(consulta.getJSONObject(0).getString("eliminaSesion").equals("1")){
				resul = true;
				// Si no ha habido ningún problema limpiamos los campos mostrados por pantalla
				ventanaSesiones.getTextFieldNombre().setText("");
				ventanaSesiones.getSpinnerNumUser().setValue(0);
				ventanaSesiones.getDateChooser().setDate(null);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, 24);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				ventanaSesiones.getTimeSpinner().getModel().setValue(calendar.getTime());
				//Eliminamos la sesión de los array
				if(option==0 && nomSesionesActivas.size()>0){
					idSesionesActivas.remove(posSesionMostrada);
					nomSesionesActivas.remove(posSesionMostrada);
					int index = idSesiones.indexOf(idSesionMostrada);
					idSesiones.remove(index);
					nomSesiones.remove(index);
				} else if (option==2 && nomSesiones.size()>0){
					idSesiones.remove(posSesionMostrada);
					nomSesiones.remove(posSesionMostrada);
					int index = idSesionesActivas.indexOf(idSesionMostrada);
					idSesionesActivas.remove(index);
					nomSesionesActivas.remove(index);
				}
			}
		} catch(Exception e){
			throw e;
		}
		
		return resul;
		
	}
	
	
	/*
	 * Método que devuelve el ID de la sesión que se está mostrando por pantalla
	 */
	public String getIdSesionMostrada(){
		return this.idSesionMostrada;
	}
	
	/*
	 * Método que se asociará al listener de la lista para mostar la sesion seleccionada
	 * por pantalla
	 */
	private void mostrarSesion(MouseEvent e, final String access_token){
		JSONArray sesion;
    	JList<?> list = (JList<?>)e.getSource();
    	if (e.getClickCount() == 2) {
    		try{
    			// Obtenemos la sesión sobre la que se ha hecho click
    			posSesionMostrada = list.locationToIndex(e.getPoint());
    			switch(ventanaSesiones.getComboBox().getSelectedIndex()){
    			case 0: idSesionMostrada = idSesionesActivas.get(posSesionMostrada);
    					sesion = gestor.sesion(idSesionMostrada, access_token);
    					break;
    			case 1: idSesionMostrada = idSesionesNoActivas.get(posSesionMostrada);
    					sesion = gestor.sesion(idSesionMostrada, access_token);
    					break;
    			default: idSesionMostrada =  idSesiones.get(posSesionMostrada);
    					sesion = gestor.sesion(idSesionMostrada, access_token);
    					break;
    			} 
    			// Modificamos los campos de la ventana para mostrarle al usuari
    			// toda la información de la sesión.
    			ventanaSesiones.getTextFieldNombre().setEnabled(false);
    			ventanaSesiones.getSpinnerNumUser().setEnabled(false);
    			ventanaSesiones.getDateChooser().setEnabled(false);
    			ventanaSesiones.getTimeSpinner().setEnabled(false);
    			ventanaSesiones.getTextFieldNombre().setText(sesion.getJSONObject(0).getString("nombre_Sesion"));
    			String fecha = sesion.getJSONObject(0).getString("fecha");
    			int index = fecha.indexOf(" ");
    			Date date = new SimpleDateFormat("yyyy-mm-dd").parse(String.valueOf(fecha.substring(0, index)));
    			ventanaSesiones.getDateChooser().setDate(date);
    			Calendar calendar = Calendar.getInstance();
    			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fecha.substring(index+1, index+3)));
    			calendar.set(Calendar.MINUTE, Integer.parseInt(fecha.substring(index+4, index+6)));
    			calendar.set(Calendar.SECOND, Integer.parseInt(fecha.substring(index+7, index+9)));
    			ventanaSesiones.getTimeSpinner().setValue(calendar.getTime());
    			ventanaSesiones.getSpinnerNumUser().setValue(Integer.parseInt(Integer.toString(sesion.getJSONObject(0).getInt("num_Usuarios"))));
    			
    			Integer.toString(sesion.getJSONObject(0).getInt("activa"));
    			if(Integer.toString(sesion.getJSONObject(0).getInt("activa")).equals("1")){
    				//Si la sesión seleccionada está activa podremos eliminarla
    				ventanaSesiones.getButtonDelete().setEnabled(true);
    				ventanaSesiones.getButtonEdit().setEnabled(true);
    			} else{
    				// En caso contrario, no se activará el botón para eliminar la sesión
    				ventanaSesiones.getButtonDelete().setEnabled(false);
    				ventanaSesiones.getButtonEdit().setEnabled(false);
    			}
    		} catch (Exception ex){
    			ex.printStackTrace();
    		}
         }
	}
	
}
