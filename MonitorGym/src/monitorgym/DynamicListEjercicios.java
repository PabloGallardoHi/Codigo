package monitorgym;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.json.JSONArray;

/*
 * Clase desde la que se gestionará todo lo relativo a mostrar
 * un conjunto de ejercicios por pantalla.
 */
public class DynamicListEjercicios extends JList<String> {

	private ArrayList<String> idEjercicios;
	private ArrayList<String> nomEjercicios;
	private DefaultListModel<String> model;
	private String idEjercicioSeleccionado;
	private int posicionClick;
	private GestorPeticiones gestor;
	private VentanaEjercicios ventanaParent;
	private boolean isSesion;
	
	public DynamicListEjercicios(JSONArray ejercicios, VentanaEjercicios parent, boolean bool, final String access_token) throws Exception{
		String nombre;
		idEjercicios = new ArrayList<String>();
		nomEjercicios = new ArrayList<String>();
		model = new DefaultListModel<String>();
		gestor = new GestorPeticiones();
		ventanaParent = parent;
		isSesion = bool;
		this.setModel(model);
		this.setBounds(10, 36, 76, 175);
		try{
			// Añadimos todos los ejercicios obtenidos del servidor a la lista
			for(int i=0; i<ejercicios.length(); i++){
				idEjercicios.add(Integer.toString(ejercicios.getJSONObject(i).getInt("id_Ejercicio")));
				nombre = ejercicios.getJSONObject(i).getString("nombre_Ejercicio");
				nomEjercicios.add(nombre);
				model.addElement(nombre);
			}
		} catch (Exception e){
			throw e;
		}
		this.addMouseListener(this.nuevoMouseListener(access_token));
	}
	
	
	/*
	 * Listener para detectar el ejercicio sobre el que se hace click.
	 * Almacena la ID de este ejercicio en idEjercicioSeleccionado
	 */
	private MouseListener nuevoMouseListener(final String access_token) throws Exception{
		MouseListener mouseListener = new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				JList<?> list = (JList<?>) e.getSource();
				if (e.getClickCount() == 2){
					posicionClick = list.locationToIndex(e.getPoint());
					idEjercicioSeleccionado = idEjercicios.get(posicionClick);
					try{
						JSONArray consulta = gestor.ejercicio(idEjercicioSeleccionado, access_token);
						if(consulta!= null){
							// Mostramos la información del ejercicio seleccionado por pantalla
							ventanaParent.getLabelNombreEjercicio().setText(consulta.getJSONObject(0).getString("nombre_Ejercicio"));
							ventanaParent.getLabelDuracionEjercicio().setText(consulta.getJSONObject(0).getString("duracion"));
							ventanaParent.getLabelDescripcionEjercicio().setText(consulta.getJSONObject(0).getString("descripcion"));
							if(isSesion == true){
								ventanaParent.getButtonRightArrow().setEnabled(true);
								ventanaParent.getButtonLeftArrow().setEnabled(false);
							} else{
								ventanaParent.getButtonLeftArrow().setEnabled(true);
								ventanaParent.getButtonRightArrow().setEnabled(false);
							}
						}
					} catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		};
		
		return mouseListener;
	}
	
	
	/*
	 * Método que devuelve la ID del ejercicio seleccionado
	 */
	public String getIdEjercicioSeleccionado(){
		return this.idEjercicioSeleccionado;
	}
	
	
	/*
	 * Método para añadir un nuevo ejercicio a la lista
	 */
	public void addEjercicioLista(String idEjercicio, final String access_token) throws Exception{
		JSONArray consulta = gestor.ejercicio(idEjercicio, access_token);
		String nombre;
		for(int i=0; i<consulta.length(); i++){
			idEjercicios.add(Integer.toString(consulta.getJSONObject(i).getInt("id_Ejercicio")));
			nombre = consulta.getJSONObject(i).getString("nombre_Ejercicio");
			nomEjercicios.add(nombre);
			model.addElement(nombre);			
		}
	}
	
	
	/*
	 * Método para eliminar un ejercicio previamente seleccionado
	 */
	public void removeEjercicioLista() throws Exception{
		idEjercicios.remove(posicionClick);
		nomEjercicios.remove(posicionClick);
		model.remove(posicionClick);
	}
	
	
	/*
	 * Método para cambiar los ejercicios que se muestrán en la lista
	 * Añade el último elemento en la lista nomEjercicios, que será el que no aparezca en la lista
	 */
	public void cambiaDatosLista(){
		model.addElement(nomEjercicios.get(nomEjercicios.size()-1));
	}
	
	
	/*
	 * Método que devuelve el array con la id de los ejercicios
	 */
	public ArrayList<String> getIdEjercicios(){
		return this.idEjercicios;
	}
	
	
	/*
	 * Método que devuelve el array con los nombres de los ejercicios
	 */
	public ArrayList<String> getNomEjercicios(){
		return this.nomEjercicios;
	}
}
