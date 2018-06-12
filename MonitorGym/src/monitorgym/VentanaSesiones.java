package monitorgym;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;

import com.toedter.calendar.JDateChooser;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * Clase que muestra una ventana al monitor desde la que
 * gestionar todo lo relativo a las sesiones que haya creado.
 */
public class VentanaSesiones extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JSpinner spinnerNumUser;
	private JDateChooser dateChooser;
	private JComboBox<String> comboBox;
	private JSpinner timeSpinner;
	private DynamicListSesiones listaSesiones;
	private GestorPeticiones gestor = new GestorPeticiones();
	
	private JButton buttonAdd;
	private JButton buttonSave;
	private JButton buttonDelete;
	private JButton buttonEdit;
	private JButton buttonCancelar;
	
	private String mMonitor;


	/*
	 * Recibiremos el nombre del monitor que ha iniciado la sesi�n
	 * para poder obtener del servidor todas las sesiones que haya 
	 * creado
	 */
	public VentanaSesiones(final String monitor, final String access_token) throws Exception {
			
		mMonitor = monitor;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 420);
		setLocationRelativeTo(null); // Ventana centrada
        setTitle("Sesiones");
        setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelNombreSesion = new JLabel("Nombre de la sesi\u00F3n:");
		labelNombreSesion.setBounds(243, 99, 125, 25);
		contentPane.add(labelNombreSesion);
		
		JLabel labelFecha = new JLabel("Fecha:");
		labelFecha.setBounds(243, 159, 63, 25);
		contentPane.add(labelFecha);
		
		JLabel labelHora = new JLabel("Hora:");
		labelHora.setBounds(243, 216, 46, 25);
		contentPane.add(labelHora);
		
		JLabel labelMaximoUsuarios = new JLabel("N\u00BA m\u00E1ximo de usuarios:");
		labelMaximoUsuarios.setBounds(243, 273, 151, 25);
		contentPane.add(labelMaximoUsuarios);
		
		// Campo para introducir el nombre de una nueva sesi�n
		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(376, 97, 178, 28);
		contentPane.add(textFieldNombre);
		textFieldNombre.setColumns(10);
		textFieldNombre.setEnabled(false);	
		
		// Campo para introducir el n�mero m�ximo de usuarios de una sesi�n
		spinnerNumUser = new JSpinner();
		spinnerNumUser.setBounds(447, 273, 41, 25);
		contentPane.add(spinnerNumUser);
		spinnerNumUser.setEnabled(false);
		
		// Campo para seleccionar la fecha de una sesi�n
		dateChooser = new JDateChooser();
		dateChooser.setBackground(Color.white);
		dateChooser.setBounds(376, 156, 178, 28);
		contentPane.add(dateChooser);
		dateChooser.setEnabled(false);
		
		// Campo para seleccionar las sesiones a mostrar.
		// Podremos mostrar las activas, no activas o todas a la vez.
		comboBox = new JComboBox<String>();
		comboBox.setBounds(55, 83, 91, 20);
		comboBox.setAlignmentY(CENTER_ALIGNMENT);
		comboBox.addItem("activas");
		comboBox.addItem("no activas");
		comboBox.addItem("todas");
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				listaSesiones.cambiaDatosLista(comboBox.getSelectedIndex(), access_token);
			}
		});
		contentPane.add(comboBox);
		
		JLabel labelMostrar = new JLabel("Mostrar:");
		labelMostrar.setBounds(10, 86, 46, 14);
		contentPane.add(labelMostrar);
		
		// Lista con todas las sesiones creadas por el monitor
		listaSesiones = new DynamicListSesiones(gestor.sesionesMonitor(monitor, access_token), this, access_token);
		JScrollPane scrollPaneSesiones = new JScrollPane(listaSesiones);
		scrollPaneSesiones.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneSesiones.setBounds(10, 114, 210, 235);
		contentPane.add(scrollPaneSesiones);
		
		// Spinner para seleccionar la hora de comienzo de una sesi�n
		timeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
		timeSpinner.setEditor(timeEditor);
		timeSpinner.setBounds(427, 217, 79, 23);
		contentPane.add(timeSpinner);
		timeSpinner.setEnabled(false);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 146, 28);
		contentPane.add(toolBar);
		
		// Bot�n para cancelar la creaci�n de una nueva sesi�n
		buttonCancelar = new JButton("Cancelar");
		buttonCancelar.setBounds(364, 326, 89, 23);
		contentPane.add(buttonCancelar);
		buttonCancelar.setVisible(false);
		buttonCancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//Desactivamos los campos
				desactivaCampos();
				//Mostramos solo los botones que deben estar activos en este momento
				buttonCancelar.setVisible(false);
				buttonSave.setEnabled(false);
				buttonAdd.setEnabled(true);
				listaSesiones.setEnabled(true);
			}
		});
		
		// Bot�n para crear una nueva sesi�n
		buttonAdd = new JButton("");
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Vaciamos todos los campos necesairos para a�adir nuevas sesiones...
				vaciaCampos();
				//...y los activamos
				activaCampos();
				//Mostramos solo los botones que deben estar activos en este momento
				buttonSave.setEnabled(true);
				buttonAdd.setEnabled(false);
				buttonDelete.setEnabled(false);
				buttonEdit.setEnabled(false);
				listaSesiones.setEnabled(false);
				buttonCancelar.setVisible(true);
				
			}
		});
		buttonAdd.setIcon(new ImageIcon(VentanaSesiones.class.getResource("/icons/add_icon-24.png")));
		toolBar.add(buttonAdd);
		
		// Bot�n para guardar una nueva sesi�n
		buttonSave = new JButton("");
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardaSesion(access_token);
			}
		});
		buttonSave.setIcon(new ImageIcon(VentanaSesiones.class.getResource("/icons/save_icon-26.png")));
		toolBar.add(buttonSave);
		//Solo estar� activo cuando vayamos a crear una nueva sesi�n
		buttonSave.setEnabled(false);
		
		// Bot�n para eliminar una sesi�n seleccionada
		buttonDelete = new JButton("");
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UIManager.put("OptionPane.background", new Color(0xFFA44D));
				
				eliminarSesion(access_token);
			}
		});
		buttonDelete.setIcon(new ImageIcon(VentanaSesiones.class.getResource("/icons/bin_icon-24.png")));
		toolBar.add(buttonDelete);
		//Solo estar� activo despu�s de seleccionar una sesi�n
		buttonDelete.setEnabled(false);
		
		// Bot�n para editar una sesi�n seleccionada
		buttonEdit = new JButton("");
		buttonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editarSesion(access_token);
			}
		});
		buttonEdit.setIcon(new ImageIcon(VentanaSesiones.class.getResource("/icons/pencil_icon-24.png")));
		toolBar.add(buttonEdit);
		buttonEdit.setEnabled(false);		
	}
	
	/*
	 * M�todo para obtener el campo de texto del nombre de la sesi�n.
	 */
	public JTextField getTextFieldNombre(){
		return this.textFieldNombre;
	}
	
	/*
	 * M�todo para obtener el spinner para el n�mero m�ximo de usuarios
	 * de la sesi�n.
	 */
	public JSpinner getSpinnerNumUser(){
		return this.spinnerNumUser;
	}
	
	/*
	 * M�todo para obtener el selector de fecha de la sesi�n
	 */
	public JDateChooser getDateChooser(){
		return this.dateChooser;
	}
	
	/*
	 * M�todo para obtener el JComboBox de selecci�n del tipo de
	 * sesiones a mostrar.
	 */
	public JComboBox<String> getComboBox(){
		return this.comboBox;
	}
	
	/*
	 * M�todo para obtener el spinner de selecci�n de hora de 
	 * inicio de una sesi�n.
	 */
	public JSpinner getTimeSpinner(){
		return this.timeSpinner;
	}
	
	/*
	 * M�todo para obtener el bot�n de eliminaci�n de sesi�n
	 */
	public JButton getButtonDelete(){
		return this.buttonDelete;
	}
	
	/*
	 * M�todo para obtener el bot�n de edici�n de sesi�n
	 */
	public JButton getButtonEdit(){
		return this.buttonEdit;
	}
	
	/*
	 * M�todo para vaciar todos los campos en los que el monitor
	 * introduce la informaci�n de la sesi�n.
	 */
	private void vaciaCampos(){
		textFieldNombre.setText("");
		spinnerNumUser.setValue(0);
		dateChooser.setDate(null);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		timeSpinner.getModel().setValue(calendar.getTime());
	}
	
	/*
	 * M�todo para desactivar todos los campos de introducci�n de 
	 * informaci�n de la sesi�n.
	 */
	private void desactivaCampos(){
		textFieldNombre.setEnabled(false);
		spinnerNumUser.setEnabled(false);
		dateChooser.setEnabled(false);
		timeSpinner.setEnabled(false);
	}
	
	/*
	 * M�todo para activar todos los campos de introducci�n de 
	 * informaci�n de la sesi�n.
	 */
	private void activaCampos(){
		textFieldNombre.setEnabled(true);
		spinnerNumUser.setEnabled(true);
		dateChooser.setEnabled(true);
		timeSpinner.setEnabled(true);
	}
	
	/*
	 * Metodo para guardar una nueva sesion
	 */
	private void guardaSesion(final String access_token){
		try{
			// Obtenemos los valores introducidos por el usuario en todos los campos
			String nomSesion = textFieldNombre.getText();
			String numUsuarios = spinnerNumUser.getValue().toString();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String stringFecha = dateFormat.format(dateChooser.getDate());
			String rawHora = timeSpinner.getValue().toString();
			int index = rawHora.indexOf(":");
			String stringHora = rawHora.substring(index-2, index+6);
			
			// Creamos la nueva sesi�n
            if(listaSesiones.nuevaSesion(nomSesion, mMonitor, stringFecha+" "+stringHora, numUsuarios, access_token)) {
            	// Actualizamos la lista con la nueva sesi�n
                listaSesiones.cambiaDatosLista(comboBox.getSelectedIndex(), access_token);
                //Vaciamos todos los campos
                vaciaCampos();
				//Y los desactivamos
                desactivaCampos();
                //Mostramos solo los botones que deben estar activos en este momento
				buttonSave.setEnabled(false);
    			buttonAdd.setEnabled(true);
				listaSesiones.setEnabled(true);
				buttonCancelar.setVisible(false);
            } else {
                System.out.println("Ha habido alg�n problema creando la sesion");
            }
        } catch(Exception ex){
        	UIManager.put("OptionPane.background", new Color(0xFFA44D));
            System.err.println("Obtenido el siguiente error: " + ex.getMessage());
            String mensaje = "Complete todos los campos para crear la sesi�n";
            JOptionPane.showMessageDialog(null, mensaje);
        }
	}
	
	/*
	 * Metodo para eliminar la sesion seleccionada
	 */
	private void eliminarSesion(final String access_token){
		// Mostramos al usuario el mensaje de confirmaci�n
		String[] buttons = {"Aceptar", "Cancelar"};
		String mensaje = "�Seguro que desea eliminar la sesi�n?";
		String titulo = "Confirmar eliminaci�n";
		int confirmar = JOptionPane.showOptionDialog(null, mensaje, titulo, JOptionPane.WARNING_MESSAGE, 
				0, null, buttons, buttons[1]);
		if(confirmar == 0){
			try{
				// Si el usuario lo confirma, eliminamos la sesi�n
				if(listaSesiones.eliminarSesionMostrada(comboBox.getSelectedIndex(), access_token)){
					// Actualizamos la lista de sesiones
					listaSesiones.cambiaDatosLista(comboBox.getSelectedIndex(), access_token);
					//Mostramos solo los botones que deben estar activos en este momento
					buttonDelete.setEnabled(false);
					buttonEdit.setEnabled(false);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Metodo para editar la sesion seleccionada
	 */
	private void editarSesion(final String access_token){
		// Si el monitor desea editar la sesi�n, se le mostrar� una nueva ventana desde
		// la que editar todo lo relativo a la sesi�n
		VentanaEjercicios ventana = new VentanaEjercicios(listaSesiones.getIdSesionMostrada(), access_token);
		ventana.setVisible(true);
        dispose();
	}
}
