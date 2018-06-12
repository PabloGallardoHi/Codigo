package monitorgym;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import java.awt.SystemColor;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings({ "serial" })
public class VentanaEjercicios extends JFrame {

	private Container contentPane;
	private JLabel buttonLeftArrow;
	private JLabel buttonRightArrow;
	
	private String idSesion;
	private DynamicListEjercicios listaEjercicios, listaEjerciciosSesion;
	private GestorPeticiones gestor = new GestorPeticiones();
	private JLabel lblDescripcion;
	private JLabel lblDuracion;
	private JLabel labelNombreEjercicio;
	private JLabel labelDescripcionEjercicio;
	private JLabel labelDuracionEjercicio;
	
	private HiloGetIpUsuario server;
	
	private int[] numUsuarioMostrado;
	
	private static final int PUERTO_SERVER = 12345;
	
	private JLabel labelBubbles1;
	private JLabel labelBubbles2;
	private JLabel labelBubbles3;
	private JLabel labelBubbles4;
	
	private JLabel labelCross1;
	private JLabel labelCross2;
	private JLabel labelCross3;
	private JLabel labelCross4;


	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public VentanaEjercicios(String sesion, final String access_token) {
		
		try{
			idSesion = sesion;
			numUsuarioMostrado = new int[4];
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Ejercicios");
			// Fijamos la ventana a pantalla completa
			//setExtendedState(Frame.MAXIMIZED_BOTH);
			
			contentPane = getContentPane();
			contentPane.setLayout(null);
			setBounds(0,0,1300,700);
			
			// Lista con los ejercicios que forman parte de la sesión
			listaEjerciciosSesion = new DynamicListEjercicios(gestor.ejerciciosSesion(idSesion, access_token), this, true, access_token);
			//Insertamos la lista en un JScrollPane
			JScrollPane scrollPaneSesion = new JScrollPane(listaEjerciciosSesion);
			scrollPaneSesion.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPaneSesion.setBounds(51, 58, 141, 195);
			contentPane.add(scrollPaneSesion);
			
			// Lista con todos los ejercicios que NO forman parte de la sesión
			listaEjercicios = new DynamicListEjercicios(gestor.ejerciciosNoSesion(idSesion, access_token), this, false, access_token);
			//Insertamos la lista en un JScrollPane
			JScrollPane scrollPaneEjercicios = new JScrollPane(listaEjercicios);
			scrollPaneEjercicios.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPaneEjercicios.setBounds(267, 58, 141, 195);
			contentPane.add(scrollPaneEjercicios);
			
			//Botón para añadir un ejercicio a la sesión
			buttonLeftArrow = new JLabel("");
			buttonLeftArrow.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/left_arrow_icon-48.png")));
			buttonLeftArrow.setBounds(208, 81, 49, 48);
			buttonLeftArrow.setEnabled(false);
			buttonLeftArrow.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					try{
						// Añadimos el ejercicio a la lista correspondiente y actualizamos
						listaEjerciciosSesion.addEjercicioLista(listaEjercicios.getIdEjercicioSeleccionado(), access_token);
						listaEjercicios.removeEjercicioLista();
						// Petición al servidor para añadir el ejercicio a la sesión correspondiente
						gestor.addEjercicioSesion(idSesion, listaEjercicios.getIdEjercicioSeleccionado(), access_token);
						buttonLeftArrow.setEnabled(false);
					} catch(Exception ex){
						ex.printStackTrace();
					}
				}
			});
			contentPane.add(buttonLeftArrow);
			
			//Botón para eliminar un ejercicio de la lista de ejercicios de la sesión
			buttonRightArrow = new JLabel("");
			buttonRightArrow.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/right_arrow-48.png")));
			buttonRightArrow.setBounds(208, 141, 49, 63);
			buttonRightArrow.setEnabled(false);
			buttonRightArrow.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					try{
						// Añadimos el ejercicio a la lista correspondiente y actualizamos
						listaEjercicios.addEjercicioLista(listaEjerciciosSesion.getIdEjercicioSeleccionado(), access_token);
						listaEjerciciosSesion.removeEjercicioLista();
						// Petición al servidor para eliminar el ejercicio de la sesión
						gestor.removeEjercicioSesion(idSesion, listaEjerciciosSesion.getIdEjercicioSeleccionado(),access_token);
						buttonRightArrow.setEnabled(false);
					} catch(Exception ex){
						ex.printStackTrace();
					}
				}
			});
			contentPane.add(buttonRightArrow);
			
			AbstractBorder border = new TextBubbleBorder(Color.BLACK,2,16,16,false);;
			
			// Sección de la pantalla en la que mostraremos toda la información relativa
			// al ejercicio seleccinado
			JPanel panelEjercicio = new JPanel();
			panelEjercicio.setBorder(border);
			panelEjercicio.setBackground(new Color(210, 105, 30));
			panelEjercicio.setBounds(51, 280, 357, 321);
			getContentPane().setLayout(null);
			contentPane.add(panelEjercicio);
			panelEjercicio.setLayout(null);
			
			lblDescripcion = new JLabel("Descripci\u00F3n:");
			lblDescripcion.setBounds(10, 111, 86, 14);
			panelEjercicio.add(lblDescripcion);
			
			lblDuracion = new JLabel("Duraci\u00F3n:");
			lblDuracion.setBounds(10, 35, 64, 14);
			panelEjercicio.add(lblDuracion);
		
			
			labelNombreEjercicio = new JLabel("");
			//Texto centrado
			labelNombreEjercicio.setHorizontalAlignment(SwingConstants.CENTER);
			//Texto en negrita y de mayor tamaño que el definido por defecto
			Font font = labelNombreEjercicio.getFont();
			Font boldFont = new Font(font.getFontName(), Font.BOLD, 20);
			labelNombreEjercicio.setFont(boldFont);
			labelNombreEjercicio.setBounds(10, 11, 337, 30);
			panelEjercicio.add(labelNombreEjercicio);
			
			labelDescripcionEjercicio = new JLabel("");
			Border descripcionBorder = BorderFactory.createEmptyBorder(5,5,5,5);
			labelDescripcionEjercicio.setBorder(descripcionBorder);
			labelDescripcionEjercicio.setBackground(SystemColor.window);
			labelDescripcionEjercicio.setOpaque(true);
			labelDescripcionEjercicio.setVerticalAlignment(SwingConstants.TOP);
			JScrollPane scrollPaneDescripcion = new JScrollPane(labelDescripcionEjercicio);
			scrollPaneDescripcion.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPaneDescripcion.setBounds(10, 130, 337, 140);
			panelEjercicio.add(scrollPaneDescripcion);
			
			labelDuracionEjercicio = new JLabel("");
			labelDuracionEjercicio.setHorizontalAlignment(SwingConstants.CENTER);
			labelDuracionEjercicio.setBackground(SystemColor.window);
			labelDuracionEjercicio.setBounds(10, 60, 86, 20);
			labelDuracionEjercicio.setOpaque(true);
			panelEjercicio.add(labelDuracionEjercicio);
	
			
			
			// Nuevo hilo para conectar con la aplicación del entrenado
			server = new HiloGetIpUsuario(idSesion, access_token);
			server.start();
			
			
			// Preparamos el espacio donde se reproducirán los vídeos
			// Habrá cuatro paneles de vídeo
			JPanel panelVideo1 = new JPanel();
			panelVideo1.setBackground(Color.WHITE);
			panelVideo1.setBounds(533, 104, 336, 222);
			panelVideo1.setLayout(null);
			contentPane.add(panelVideo1);
			
			JPanel panelVideo2 = new JPanel();
			panelVideo2.setBackground(Color.WHITE);
			panelVideo2.setBounds(901, 104, 336, 234);
			contentPane.add(panelVideo2);
			panelVideo2.setLayout(null);
			
			JPanel panelVideo3 = new JPanel();
			panelVideo3.setBackground(Color.WHITE);
			panelVideo3.setBounds(533, 368, 336, 234);
			contentPane.add(panelVideo3);
			panelVideo3.setLayout(null);
			
			JPanel panelVideo4 = new JPanel();
			panelVideo4.setBackground(Color.WHITE);
			panelVideo4.setBounds(901, 368, 336, 234);
			contentPane.add(panelVideo4);
			panelVideo4.setLayout(null);
			
			
			
			// Etiquetas para el nombre de usuario que se muestra en cada reproductor
			JLabel labelNombreVideo1 = new JLabel("");
			labelNombreVideo1.setBounds(533, 81, 262, 24);
			getContentPane().add(labelNombreVideo1);
						
			JLabel labelNombreVideo2 = new JLabel("");
			labelNombreVideo2.setBounds(901, 81, 262, 24);
			getContentPane().add(labelNombreVideo2);
					
			JLabel labelNombreVideo3 = new JLabel("");
			labelNombreVideo3.setBounds(533, 602, 262, 24);
			getContentPane().add(labelNombreVideo3);
					
			JLabel labelNombreVideo4 = new JLabel("");
			labelNombreVideo4.setBounds(901, 602, 262, 24);
			getContentPane().add(labelNombreVideo4);
			
			
			// Botones para enviar mensaje al cliente
			labelBubbles1 = new JLabel("");
			labelBubbles1.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/bubbles_icon-16.png")));
			labelBubbles1.setBounds(820, 79, 21, 14);
			labelBubbles1.addMouseListener(new ClickMensaje(0));
			getContentPane().add(labelBubbles1);
						
			labelBubbles2 = new JLabel("");
			labelBubbles2.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/bubbles_icon-16.png")));
			labelBubbles2.setBounds(1185, 79, 21, 14);
			labelBubbles2.addMouseListener(new ClickMensaje(1));
			getContentPane().add(labelBubbles2);
						
			labelBubbles3 = new JLabel("");
			labelBubbles3.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/bubbles_icon-16.png")));
			labelBubbles3.setBounds(820, 612, 21, 14);
			labelBubbles3.addMouseListener(new ClickMensaje(2));
			getContentPane().add(labelBubbles3);
						
			labelBubbles4 = new JLabel("");
			labelBubbles4.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/bubbles_icon-16.png")));
			labelBubbles4.setBounds(1185, 612, 21, 14);
			labelBubbles4.addMouseListener(new ClickMensaje(3));
			getContentPane().add(labelBubbles4);
						
						
			// Botones para dejar de reproducir el vídeo de un cliente
			labelCross1 = new JLabel("");
			labelCross1.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/cross_icon-16.png")));
			labelCross1.setBounds(849, 81, 21, 14);
			getContentPane().add(labelCross1);
						
			labelCross2 = new JLabel("");
			labelCross2.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/cross_icon-16.png")));
			labelCross2.setBounds(1216, 79, 21, 14);
			getContentPane().add(labelCross2);
								
			labelCross3 = new JLabel("");
			labelCross3.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/cross_icon-16.png")));
			labelCross3.setBounds(849, 612, 21, 14);
			getContentPane().add(labelCross3);
									
			labelCross4 = new JLabel("");
			labelCross4.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/cross_icon-16.png")));
			labelCross4.setBounds(1216, 613, 21, 14);
			getContentPane().add(labelCross4);

			
			// Iconos para comenzar a reproducir el vídeo
			JLabel labelPlay1 = new JLabel("");
			labelPlay1.setBounds(136, 85, 64, 64);
			panelVideo1.add(labelPlay1);
			labelPlay1.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/play_icon-64.png")));
			labelPlay1.addMouseListener(new ClickVideo(labelPlay1, labelNombreVideo1, panelVideo1, 0, labelCross1, this));
			
			JLabel labelPlay2 = new JLabel("");
			labelPlay2.setBounds(141, 86, 64, 63);
			panelVideo2.add(labelPlay2);
			labelPlay2.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/play_icon-64.png")));
			labelPlay2.addMouseListener(new ClickVideo(labelPlay2, labelNombreVideo2, panelVideo2, 1, labelCross2, this));
				
			JLabel labelPlay3 = new JLabel("");
			labelPlay3.setBounds(135, 82, 64, 63);
			panelVideo3.add(labelPlay3);
			labelPlay3.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/play_icon-64.png")));
			labelPlay3.addMouseListener(new ClickVideo(labelPlay3, labelNombreVideo3, panelVideo3, 2, labelCross3, this));
				
			JLabel labelPlay4 = new JLabel("");
			labelPlay4.setBounds(139, 82, 64, 63);
			panelVideo4.add(labelPlay4);
			labelPlay4.setIcon(new ImageIcon(VentanaEjercicios.class.getResource("/icons/play_icon-64.png")));
			labelPlay4.addMouseListener(new ClickVideo(labelPlay4, labelNombreVideo4, panelVideo4, 3, labelCross4, this));
			
			JLabel lblEjerciciosDeLa = new JLabel("Ejercicios de la sesi\u00F3n");
			lblEjerciciosDeLa.setBounds(63, 23, 129, 24);
			getContentPane().add(lblEjerciciosDeLa);
			
			JLabel lblNewLabel = new JLabel("Resto de ejercicios");
			lblNewLabel.setBounds(281, 23, 106, 24);
			getContentPane().add(lblNewLabel);
			
			// Botón para dar por finalizada una sesión
			JButton btnFinalizarSesin = new JButton("Finalizar Sesi\u00F3n");
			btnFinalizarSesin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						// Liberamos el puerto solicitado por UPnP
						server.releasePort();
						// La sesión pasa a no estar activa
						gestor.setSesionNoActiva(idSesion, access_token);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			btnFinalizarSesin.setBounds(833, 24, 118, 23);
			getContentPane().add(btnFinalizarSesin);
			
			// Botones de chat y cerrar vídeo inicialmente desactivados
			desactivarBotones();
			
						
			// Cargamos las librerias necesarias para el funcionamiento de VLCj
			new NativeDiscovery().discover();
			
		} catch (Exception e){
			System.out.println("Excepción VentanaEjercicios");
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * Método para obtener la etiqueta para el nombre del ejercicio seleccionado
	 */
	public JLabel getLabelNombreEjercicio(){
		return this.labelNombreEjercicio;
	}	
	
	/*
	 * Método para obtener la etiqueta para la descripción del ejercicio seleccionado
	 */
	public JLabel getLabelDescripcionEjercicio(){
		return this.labelDescripcionEjercicio;
	}
	
	/*
	 * Método para obtener la etiqueta para la duración del ejercicio seleccionado
	 */
	public JLabel getLabelDuracionEjercicio(){
		return this.labelDuracionEjercicio;
	}
	
	/*
	 * Método para obtener el botón de introducción de ejercicio en la sesión
	 */
	public JLabel getButtonLeftArrow(){
		return this.buttonLeftArrow;
	}
	
	/*
	 * Método para obtener el botón de eliminación de ejercicio de la sesión
	 */
	public JLabel getButtonRightArrow(){
		return this.buttonRightArrow;
	}
	
	/*
	 * Método para activar los botones de chat y cerrar vídeo dependiendo de
	 * qué reproductor se ha activado
	 */
	public void activarBotones(int i){
		switch(i){
			case 0:
				labelBubbles1.setVisible(true);
				labelCross1.setVisible(true);
				break;
			case 1:
				labelBubbles2.setVisible(true);
				labelCross2.setVisible(true);
				break;
			case 2:
				labelBubbles3.setVisible(true);
				labelCross3.setVisible(true);
				break;
			case 3:
				labelBubbles4.setVisible(true);
				labelCross4.setVisible(true);
				break;
		}
	}
	
	/*
	 * Método para desactivar los botones de chat y cerrar vídeo de uno de los
	 * reproductores de vídeo
	 */
	public void desactivarBotones(int i){
		switch(i){
			case 0:
				labelBubbles1.setVisible(false);
				labelCross1.setVisible(false);
				break;
			case 1:
				labelBubbles2.setVisible(false);
				labelCross2.setVisible(false);
				break;
			case 2:
				labelBubbles3.setVisible(false);
				labelCross3.setVisible(false);
				break;
			case 3:
				labelBubbles4.setVisible(false);
				labelCross4.setVisible(false);
				break;
		}
	}
	
	/*
	 * Método para desactivar todos los botones de chat y cerrar vídeo
	 */
	public void desactivarBotones(){
		labelBubbles1.setVisible(false);
		labelBubbles2.setVisible(false);
		labelBubbles3.setVisible(false);
		labelBubbles4.setVisible(false);
		labelCross1.setVisible(false);
		labelCross2.setVisible(false);
		labelCross3.setVisible(false);
		labelCross4.setVisible(false);
	}
	
	/*
	 * Listener para detectar el click sobre el botón de cerrar vídeo
	 */
	public class ClickCierraVideo extends MouseAdapter{
		
		private Thread mThread;
		private int mNumeroBoton;
		
		public ClickCierraVideo(Thread thread, int numeroBoton){
			mThread = thread;
			mNumeroBoton = numeroBoton;
		}
		
		public void mouseClicked(MouseEvent e){
			System.out.println("Cerrando vídeo");
			// Para el hilo del vídeo
			mThread.interrupt();
			// Desactiva los botones del reproductor
			desactivarBotones(mNumeroBoton);
		}
	}
	
	/*
	 * Listener para detectar click sobre el botón de chat
	 */
	public class ClickMensaje extends MouseAdapter{
		
		private int mNumeroBoton;
		private String cartel = "Escriba el mensaje...";
		private String titulo;
		
		public ClickMensaje(int numeroBoton){
			mNumeroBoton = numeroBoton;
		}
		
		public void mouseClicked(MouseEvent e){
			titulo = server.getNomUsuario(numUsuarioMostrado[mNumeroBoton]);
			// Muestra el cuadro de introducción del mensaje
			String mensaje = JOptionPane.showInputDialog(null, cartel, titulo, JOptionPane.QUESTION_MESSAGE);
			if(mensaje != null){
				// Crea un nuevo hilo...
				HiloMensaje hiloMensaje = new HiloMensaje(server.getIpUsuario(numUsuarioMostrado[mNumeroBoton]), PUERTO_SERVER, mensaje);
				// ... y envía el mensaje
				hiloMensaje.start();
			}
		}
	}
	
	/*
	 * Listener para detectar el click sobre el botón de reproducción de vídeo
	 */
	public class ClickVideo extends MouseAdapter{
		
		private JLabel mLabelButton;
		private JLabel mLabelNombre;
		private JLabel mLabelCross;
		private JPanel mPanel;
		private HiloVideo mHilo;
		private Thread mThread;
		private int mNumeroBoton;
		private VentanaEjercicios mVentana;
		
		public ClickVideo(JLabel labelButton, JLabel labelNombre, JPanel panel, int numeroBoton, JLabel labelCross, VentanaEjercicios ventana){
			mLabelButton = labelButton;
			mLabelNombre = labelNombre;
			mPanel = panel;
			mNumeroBoton = numeroBoton;
			mLabelCross = labelCross;
			mVentana = ventana;
		}
		
		public void mouseClicked(MouseEvent e){
			String titulo = "Usuarios";
			String mensaje = "Seleccione un usuario...";
			String[] array = new String[server.getUsuarios().size()];
			array = server.getUsuarios().toArray(array);
			
			// Se le muestra al usuario un mensaje para que seleccione al usuario que desea ver
			UIManager.put("OptionPane.background", new Color(0xFFA44D));
			String input = (String) JOptionPane.showInputDialog(null, mensaje, titulo, 
					JOptionPane.QUESTION_MESSAGE, null, array, server.getUsuarios().toString());
			if(input != null){
				mLabelButton.setVisible(false);
				
				// Realiza todas las operaciones necesarias para comenzar con la reproducción del vídeo
				new NativeDiscovery().discover();
				mHilo = new HiloVideo(mLabelButton, mLabelNombre, mVentana, mNumeroBoton);
				mHilo.setBounds(0, 0, 336, 234);
				mPanel.add(mHilo);
				mPanel.revalidate();
				mPanel.repaint();
				int indexUser = server.getUsuarios().indexOf(input);
				numUsuarioMostrado[mNumeroBoton] = indexUser;
				// Fijamos la dirección del cliente que queremos reproducir
				mHilo.setUrl("http:/"+server.getIpUsuario(indexUser));
				// Reproduce el vídeo
				mThread = new Thread(mHilo);			
				mThread.start();
				mLabelCross.addMouseListener(new ClickCierraVideo(mThread, mNumeroBoton));
				mLabelNombre.setText(server.getNomUsuario(indexUser));
				activarBotones(mNumeroBoton);
			}
		}
	}
}
