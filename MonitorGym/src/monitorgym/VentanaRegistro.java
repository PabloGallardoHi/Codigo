package monitorgym;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/*
 * Ventana de usuario en la que se mostrará un formulario
 * de registro en el sistema. 
 */

@SuppressWarnings("serial")
public class VentanaRegistro extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsuario, txtNombre, txtApellidos, txtDni, txtDireccion, txtMovil;
	private JPasswordField pwdPassword;
	private JLabel label;
	private JButton btnRegistrar, btnContinuar;
	private GestorPeticiones gestor = new GestorPeticiones();
	private String usuario, passwd, nombre, apellidos, dni, direccion, movil;

	/**
	 * Create the frame.
	 */
	public VentanaRegistro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 666);
		setLocationRelativeTo(null); // Ventana centrada
        setTitle("Registro");
        setResizable(false);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Campo para introducir el nombre de usuario
		txtUsuario = new JTextField();
		txtUsuario.setText("usuario");
		txtUsuario.setBounds(284, 228, 86, 28);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		// Campo para introducir la contraseña del usuario
		pwdPassword = new JPasswordField();
		pwdPassword.setText("password");
		pwdPassword.setBounds(284, 267, 86, 28);
		contentPane.add(pwdPassword);
		
		// Campo para introducir nombre del usuario
		txtNombre = new JTextField();
		txtNombre.setText("nombre");
		txtNombre.setBounds(284, 306, 86, 28);
		contentPane.add(txtNombre);
		txtNombre.setColumns(10);
		
		// Campo para introducir apellidos del usuario
		txtApellidos = new JTextField();
		txtApellidos.setText("apellidos");
		txtApellidos.setBounds(284, 345, 86, 28);
		contentPane.add(txtApellidos);
		txtApellidos.setColumns(10);
				
		// Campo para introducir dni del usuario
		txtDni = new JTextField();
		txtDni.setText("dni");
		txtDni.setBounds(284, 384, 86, 28);
		contentPane.add(txtDni);
		txtDni.setColumns(10);
				
		// Campo para introducir la direccion del usuario
		txtDireccion = new JTextField();
		txtDireccion.setText("direccion");
		txtDireccion.setBounds(284, 423, 86, 28);
		contentPane.add(txtDireccion);
		txtDireccion.setColumns(10);
		
		
		// Campo para introducir el movil del usuario
		txtMovil = new JTextField();
		txtMovil.setText("movil");
		txtMovil.setBounds(284, 462, 86, 28);
		contentPane.add(txtMovil);
		txtMovil.setColumns(10);
		
		
		
		label = new JLabel(" ");
		label.setBounds(81, 499, 424, 20);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(label);
		
		
		// Botón para enviar los datos de registro al sistema
		btnRegistrar = new JButton("REGISTRAR");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					// Obtenemos la información introducida por el usuario
					usuario = txtUsuario.getText();
					passwd = new String(pwdPassword.getPassword());
					nombre = txtNombre.getText();
					apellidos = txtApellidos.getText();
					dni = txtDni.getText();
					direccion = txtDireccion.getText();
					movil = txtMovil.getText();
					
					// Consulta al servidor para comprobar si ya hay un usuario con el mismo nombre
					if (gestor.checkUser(usuario).equals("1")){
						// Enviamos los datos para registrarlos en la base de datos
						if (gestor.register(usuario, passwd, nombre, apellidos, dni, direccion, movil).equals("1")){						
			
						label.setText("Registro completo. Pulse CONTINUAR para poder acceder");
						
						btnContinuar = new JButton("CONTINUAR");
						btnContinuar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try{
									// Lanzamos de nuevo la ventana login para acceder
									VentanaLogin ventana = new VentanaLogin();
					                ventana.setVisible(true);
					                dispose();
						
								} catch(Exception ex){
									// Si no es posible conectar con el servidor se lo indicamos al usuario
									System.err.println("Obtenido el siguiente error: " + ex.getMessage());
									label.setText("Problemas con el servidor. Vuelva a intentarlo en unos minutos");
								}
							}
						});
						btnContinuar.setBounds(228, 570, 112, 28);
						contentPane.add(btnContinuar);
		              
						} else{
			                // Le mostramos al usuario que ha habido algún problema al efectur el registro
			                label.setText("No ha sido posible completar el registro");
			            }
						
		            } else{
		                // Le mostramos al usuario el usuario introducido no está disponible
		                label.setText("Usuario repetido. Introduzca uno nuevo por favor");
		            }
				} catch(Exception ex){
					// Si no es posible conectar con el servidor se lo indicamos al usuario
					System.err.println("Obtenido el siguiente error: " + ex.getMessage());
					label.setText("Problemas con el servidor. Vuelva a intentarlo en unos minutos");
				}
			}
		});
		btnRegistrar.setBounds(228, 530, 112, 28);
		contentPane.add(btnRegistrar);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(197, 235, 60, 14);
		contentPane.add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setBounds(197, 274, 76, 14);
		contentPane.add(lblContrasea);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(197, 313, 76, 14);
		contentPane.add(lblNombre);
		
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setBounds(197, 352, 76, 14);
		contentPane.add(lblApellidos);
		
		JLabel lblDni = new JLabel("DNI:");
		lblDni.setBounds(197, 391, 76, 14);
		contentPane.add(lblDni);
		
		JLabel lblDireccion = new JLabel("Direccion:");
		lblDireccion.setBounds(197, 430, 76, 14);
		contentPane.add(lblDireccion);
		
		JLabel lblMovil = new JLabel("Movil:");
		lblMovil.setBounds(197, 469, 76, 14);
		contentPane.add(lblMovil);
		
		
			
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(VentanaLogin.class.getResource("/icons/logo.png")));
		label_1.setBounds(189, 5, 192, 189);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("Introduzca sus datos para registrarse");
		label_2.setBounds(135, 111, 369, 189);
		Font font = label_2.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, 18);
		label_2.setFont(boldFont);
		contentPane.add(label_2);
		
	}
}
