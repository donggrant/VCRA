
import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TextPane extends JTextPane {

	private static final long serialVersionUID = 1L;

	/**
	 * JTextPane class for organization
	 */
	public TextPane() {
		super();

		// Set the look of the textPane
		this.setEditable(false);
		this.setBackground(Color.BLACK);
		this.setForeground(Color.WHITE);
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyledDocument doc = this.getStyledDocument();

		// Initial text to appear on launch
		this.setText(
				"VCRA GUI BETA V0.1 \n\n//////////////////////////////////////  Instructions For Use:  /////////////////////////////////////// \n\n"
						+ "Enter data into the appropriate fields.\n"
						+ "Press the enter key on your keyboard after each input to update information.\n"
						+ "The input data will appear in this text field.\n"
						+ "Once all data is entered, press the run button.\n\n"
						+ "To save all inputs for future use, use the save or save as function under the file menu.\n"
          				+ "The run button will also save all inputs in the project directory.\n"
						+ "To load previously saved inputs, use the open function under the file menu.\n"
						+ "To view this message again after inputting data, see instructions under the help menu.\n");
		doc = this.getStyledDocument();
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
	}

}

