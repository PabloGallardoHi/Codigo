package monitorgym;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;


/*
 * Clase principal de nuestra aplicaci�n, en la que se define
 * el aspecto de la misma y se lanza la primera ventana de
 * usuario
 */
public class Monitorgym {
	public static void main(String[] args) {
		// TODO code application logic here
		try {
			// Definimos el aspecto de nuestra aplicaci�n
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.getLookAndFeelDefaults().put("Panel.background", new Color(0xFFA44D));
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, fall back to cross-platform
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		// Lanzamos la primera ventana de usuario
		VentanaLogin ventana = new VentanaLogin();
		ventana.setVisible(true);
	}
}
