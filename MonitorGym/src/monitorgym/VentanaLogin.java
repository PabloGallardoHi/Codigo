package monitorgym;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * Ventana de usuario en la que se mostrará un formulario
 * de inicio de sesión en el sistema. 
 */
public class VentanaLogin extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField pwdPassword;
	private JLabel label;
	private JButton btnEntrar, btnRegistrar;
	private GestorPeticiones gestor = new GestorPeticiones();
	private String usuario, passwd;

	/**
	 * Create the frame.
	 */
	public VentanaLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 420);
		setLocationRelativeTo(null); // Ventana centrada
        setTitle("Login");
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
		
		label = new JLabel(" ");
		label.setBounds(81, 304, 424, 20);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(label);
		
		// Botón para acceder al sistema tras proporcionar las credenciales
		btnEntrar = new JButton("ENTRAR");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					// Obtenemos las credenciales introducidas por el usuario
					usuario = txtUsuario.getText();
					passwd = new String(pwdPassword.getPassword());
					// Solicitamos token con las credenciales para comprobar si tenemos permiso para acceder al servidor					
					String access_token = gestor.sendTokenRequest(usuario, passwd);
					// Consulta al servidor para comprobar si las credenciales del usuario son correctas
					if (gestor.login(usuario, passwd, access_token).equals("1")){
		                // Lanzamos la siguiente ventana
		                VentanaSesiones ventana = new VentanaSesiones(usuario, access_token);
		                ventana.setVisible(true);
		                dispose();
		            } else{
		                // Le mostramos al usuario que ha introducido mal las credenciales
		                label.setText("Usuario o contraseña incorrectos");
		            }
				} catch(Exception ex){
					// Si no es posible conectar con el servidor se lo indicamos al usuario
					System.err.println("Obtenido el siguiente error: " + ex.getMessage());
					label.setText("Problemas con el servidor. Vuelva a intentarlo en unos minutos");
				}
			}
		});
		btnEntrar.setBounds(228, 335, 112, 28);
		contentPane.add(btnEntrar);
		
		
		btnRegistrar = new JButton("REGISTARSE");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					
					VentanaRegistro ventana = new VentanaRegistro();
	                ventana.setVisible(true);
	                dispose();
	                
		
				} catch(Exception ex){
					// Si no es posible conectar con el servidor se lo indicamos al usuario
					System.err.println("Obtenido el siguiente error: " + ex.getMessage());
					label.setText("Problemas con el servidor. Vuelva a intentarlo en unos minutos");
				}
			}
		});
		btnRegistrar.setBounds(421, 335, 112, 28);
		contentPane.add(btnRegistrar);
			
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(197, 235, 60, 14);
		contentPane.add(lblNombre);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setBounds(197, 274, 76, 14);
		contentPane.add(lblContrasea);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(VentanaLogin.class.getResource("/icons/logo.png")));
		label_1.setBounds(189, 28, 192, 189);
		contentPane.add(label_1);
		
		JLabel buttonConfig = new JLabel("");
		buttonConfig.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UIManager.put("OptionPane.background", new Color(0xFFA44D));
				String cartel = "Introduzca la ip del servidor...";
				String titulo = "Configuración de la dirección IP del servidor";
				String ip = JOptionPane.showInputDialog(null, cartel, titulo, JOptionPane.QUESTION_MESSAGE);
				if(ip != null){
					new ConfiguradorIpServidor(ip);
				}
			}
		});
		buttonConfig.setIcon(new ImageIcon(VentanaLogin.class.getResource("/icons/config_icon-16x16.png")));
		buttonConfig.setBounds(525, 11, 29, 28);
		contentPane.add(buttonConfig);
	}
}
