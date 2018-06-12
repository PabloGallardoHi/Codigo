package monitorgym;

import javax.swing.JLabel;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.windows.WindowsCanvas;

@SuppressWarnings({ "deprecation", "serial" })

public class HiloVideo extends WindowsCanvas implements Runnable {

	private MediaPlayerFactory mediaPlayerFactory;
	private EmbeddedMediaPlayer mediaPlayer;
	private String url;
	private JLabel mLabelButton;
	private JLabel mLabelNombre;
	private VentanaEjercicios mVentana;
	private int mNumeroHilo;
	
	public HiloVideo(JLabel labelButton, JLabel labelNombre, VentanaEjercicios ventana, int numeroHilo){
		// Los par�metros del MediaPLayerFactory son para girar el reproductor 90�
		mediaPlayerFactory = new MediaPlayerFactory("--video-filter=transform", "--transform-type=90");
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(this);
        mediaPlayer.setVideoSurface(videoSurface);
        mLabelButton = labelButton;
        mLabelNombre = labelNombre;
        mVentana = ventana;
        mNumeroHilo = numeroHilo;
	}
	
	public void run(){
		while(!Thread.interrupted()){
			// Petici�n para recibir el v�deo y reproducirlo
			mediaPlayer.playMedia(url);
			try{
				Thread.sleep(5000);
			} catch(Exception ex){
				Thread.currentThread().interrupt();
			}
			while(!Thread.currentThread().isInterrupted()){
				if(!mediaPlayer.isPlaying()){
					// En el momento en que no recibimos m�s v�deo interrumpimos el hilo
					System.out.println("No recibimos m�s v�deo");
					Thread.currentThread().interrupt();
				}
			}
		}
		System.out.println("Hilo interrumpido");
		this.setVisible(false);
		mLabelButton.setVisible(true);
		mLabelNombre.setText("");
		mVentana.desactivarBotones(mNumeroHilo);
		// Liberamos todos los recursos utilizamos por el reproductor
		mediaPlayer.release();
	}
	
	/*
	 * M�todo para fijar la IP a la que solicitar el v�deo
	 */
	public void setUrl(String string){
		url = string;
	}
}
