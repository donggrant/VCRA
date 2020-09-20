
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {
	
	public static JFrame frame;
	public static String path;
	

	/**
	 * Main method to be run
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Path to find the location of GUI icon
		path = App.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
		int ind = path.indexOf("vcra-swing-ui");
		path = path.substring(0, ind);
		
		//Run method to launch VCRA GUI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new MainController("Volumetric Collision Risk Assessment (VCRA)");
			    frame.setSize(1100, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ImageIcon icon = new ImageIcon(path + "\\vcra-swing-ui\\pic\\VCRA2.png");
				frame.setIconImage(icon.getImage());
				frame.setVisible(true);
			}
		});

	}
	

}

