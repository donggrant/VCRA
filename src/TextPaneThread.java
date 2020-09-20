
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextPaneThread implements Runnable {

	// Declare process and main controller
	private Process p;
	private MainController mc;

	// Constructor sets process and main controller to the instances created in main
	// controller
	public TextPaneThread(Process p, MainController mc) {
		this.p = p;
		this.mc = mc;
	}

	// Run method takes the input stream and appends it to the text pane
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				mc.strictAppend(line + "\n");
			}
          	if (mc.getTextPane().getText().contains("UCRs Completed")) {
				mc.autoload();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

