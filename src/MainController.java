
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MainController extends JFrame {

	private static final long serialVersionUID = 1L;
	// Layout Organization
	private JPanel components;
	private JPanel left;
	private JPanel right;
	private JPanel buttonPnl;

	// Layout Components
	private MenuItems menuBar;
	private FilePanel filePanel;
	private OptionsPanel optionsPanel;
	private TextPane textPane;
	private JScrollPane scrollPane;
	private JButton runBtn;
	private JButton resetBtn;

	// Required Variables to Pass
	private String puckFile;
	private String airspaceFile;
	private String outputName;

	// Option Variables to Pass
	private String altBand;
	private double uaSpeed;
	private double uaClimbRate;
	private double uaHeading;
	private double pcol;
	private int keepoutVolumeRadius;
	private int keepoutVolumeHeight;
	private double puckRadius;
	private double puckHeight;
	private int startTOD;
	private int endTOD;
	private int totalObservationTime;
	private boolean header;

	// ArrayLists to pass all modified inputs
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<Object> values = new ArrayList<Object>();

	// This instance of the Main Controller
	private MainController mc;

	/**
	 * Constructor for the main controller of the GUI, all variables will pass
	 * through and be sent from here.
	 * 
	 * @param name is the name of the JFrame.
	 */
	public MainController(String name) {
		super(name);

		// Initialize the main controller instance for hidden method use use;
		mc = this;

		// Initialize Organizers
		components = new JPanel(new BorderLayout());
		left = new JPanel(new BorderLayout());
		right = new JPanel(new BorderLayout());
		buttonPnl = new JPanel(new BorderLayout());

		// Initialize Components
		menuBar = new MenuItems(this, filePanel, optionsPanel);
		filePanel = new FilePanel(this);
		optionsPanel = new OptionsPanel(this);
		textPane = new TextPane();
		runBtn = new JButton("Run");
		resetBtn = new JButton("Reset Options");

		// Initialize Option Variables
		altBand = null;
		uaSpeed = -1.0;
		uaClimbRate = 0.0;
		uaHeading = -1.0;
		pcol = -1.0;
		keepoutVolumeRadius = -1;
		keepoutVolumeHeight = -1;
		puckRadius = -1.0;
		puckHeight = -1;
		startTOD = 0;
		endTOD = 23;
		totalObservationTime = -1;
		header = false;

		// Call to setLayout method to organize the GUI
		setLayout();

		// Run Button action listener takes all entered data and writes it to a .vcra
		// file as well as initializes the VCRA through calling a script.
		runBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Code will not run if errors exist
				if (textPane.getText().contains("Error!")) {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>Errors are detected. Please refer to the display on the right.</html>"
							+ "</div></html>");
					// Show above message in the event of errors
					JOptionPane.showMessageDialog(App.frame, abt);

					// File paths / names must be set before initializing a run
				} else if (puckFile == null || airspaceFile == null || outputName == null) {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>Error: Puck File, Airspace File, and Output Name File <br/>fields must be filled before initiating a run.</html>"
							+ "</div></html>");
					// Show above message in case of file path / names not being set
					JOptionPane.showMessageDialog(App.frame, abt);
				} else {
					if (!areVariablesNull()) {
						try {
							// Saves all modified inputs in JSON format as a .vcra file in the local
							// directory
							JSONObject jObj = menuBar.getOutFormat();
							String saveoption = JOptionPane.showInputDialog(App.frame, "Save file as _.vcra");
							// Adds .vcra in the event that it isn't there
							if (!saveoption.trim().endsWith(".vcra")) {
								saveoption = saveoption.trim().replaceAll(" ", "_") + ".vcra";
							}
							String pathS = MainController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
							pathS = pathS.substring(1);
							int index = pathS.indexOf("vcra-swing-ui");
							pathS = pathS.substring(0, index);
							pathS = pathS + "vcra-swing-ui\\vcra_files\\";
							// File writer to write the JSON file
							FileWriter fw = new FileWriter(pathS + saveoption.trim().replaceAll(" ", "_"), false);
							fw.write(jObj.toString());
							fw.close();

							// ------------RUN CODE HERE-------------------------
							Process p;
							try {
								// Get path of current MainController.java for use in file paths
								String path = MainController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
								path = path.substring(1);

								// Command to be run 'python path-to-GUIRunVCRA.py .vcra-JSON-File'
								
								int ind = path.indexOf("vcra-swing-ui");
								path = path.substring(0, ind);
								
								String[] cmd = { "python",
										path + "\\vcra-swing-ui\\vcra_python\\Scripts\\GUIRunVCRA.py",
										path + "vcra-swing-ui\\vcra_files\\" + saveoption };
								p = Runtime.getRuntime().exec(cmd);
								p.waitFor();

								// Text pane clear and thread creation to handle the input stream and print to
								// the pane
								clearPane();
								Thread paneThread = new Thread(new TextPaneThread(p, mc));
								paneThread.start();

							} catch (IOException eX) {
								eX.printStackTrace();
							} catch (InterruptedException eX) {
								eX.printStackTrace();
							}

						} catch (Exception ex) {
						}
						// Error message if no data has been input
					} else {
						clearPane();
						appendPane("\n\n\n\n\nMUST INPUT SOME DATA BEFORE RUNNING");
					}
				}
			}
		});

		// Reset Button resets all inputs and clears all text fields.
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Resets all private variables.
				setAllNull();
				// Clears text from all JTextFields
				optionsPanel.setEmpty();
				// Clear Header JRadioButton Selected
				filePanel.clearHeader();
				// Clear file fields
				filePanel.clearFields();
				// Clears textPane
				clearPane();
				// Appends confirmation text
				appendPane("Options Reset to Default");

			}
		});

		// Scroll pane adjustment listener for the text pane scroll bar
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			BoundedRangeModel brm = scrollPane.getVerticalScrollBar().getModel();
			boolean wasAtBottom = true;

			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (!brm.getValueIsAdjusting()) {
					if (wasAtBottom)
						brm.setValue(brm.getMaximum());
				} else
					wasAtBottom = ((brm.getValue() + brm.getExtent()) == brm.getMaximum());

			}
		});

	}

	/**
	 * Sets the layout of the GUI
	 */
	public void setLayout() {
		// Create JMenuBar
		menuBar = new MenuItems(this, filePanel, optionsPanel);
		menuBar.setSize(getPreferredSize());
		menuBar.setVisible(true);
		menuBar.setBorder(BorderFactory.createBevelBorder(2));

		// Separates the left and right of the GUI for easier layout.
		left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		right.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		// Container for adding components
		Container c = getContentPane();
		// Sets the size of the left panel (set so that layout doesn't change
		// drastically with resizing)
		left.setPreferredSize(new Dimension(550, 600));

		// Methods to set the left and right panels
		setLeft();
		setRight();

		// Add textArea and lPane to container
		c.add(menuBar, BorderLayout.NORTH);
		c.add(left, BorderLayout.WEST);
		c.add(right, BorderLayout.CENTER);
	}

	/**
	 * Sets the layout of the left panel of the GUI
	 */
	public void setLeft() {
		// Borders for the file and options panels
		filePanel.setBorder(BorderFactory.createBevelBorder(0));
		optionsPanel.setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createEmptyBorder(0, 0, 0, 10)));

		// Sets the size of the file panel so the layout doesn't drastically change with
		// resizing
		filePanel.setPreferredSize(new Dimension(550, 250));

		// Adds the components to the left panel
		left.add(filePanel, BorderLayout.NORTH);
		left.add(optionsPanel, BorderLayout.CENTER);
	}

	/**
	 * Sets the layout of the right panel of the GUI
	 */
	public void setRight() {
		// Method call to set up the button panel
		setBtnPnl();

		// Sets the border of the textPane
		textPane.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane = new JScrollPane(textPane);

		// Adds the components to the right panel
		right.add(scrollPane, BorderLayout.CENTER);
		right.add(buttonPnl, BorderLayout.SOUTH);
	}

	public void autoload() {
		JLabel abt = new JLabel("<html><div style='text-align: center;'>"
				+ "<html>UCRs Completed<br/>Would you like to open the Puck Statistics File?</html>" + "</div></html>");
		if (JOptionPane.showConfirmDialog(App.frame, abt, null,
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
			try {
				// Get path of current MainController.java for use in file paths
				String path = System.getProperty("java.class.path");
				String pathStart = "";

				// Remove ';' and all text before it in the event that the file path first
				// resorts to bin;
				if (path.contains(";")) {
					int semiC = path.indexOf(";");
					String pathNoSemi = path.substring(semiC + 1);
					// Make substring from the end of the ';' to the top level folder of our
					// repository
					int i = pathNoSemi.lastIndexOf("\\vcra-swing-ui\\");
					pathStart = pathNoSemi.substring(0, i);

					// Make substring from beginning to the top level folder of our repository
				} else {
					int i = path.lastIndexOf("\\vcra-swing-ui\\");
					pathStart = path.substring(0, i);
				}

				// Command to launch in default application'
				Desktop d = Desktop.getDesktop();
				d.open(new File(pathStart + "\\vcra-swing-ui\\Output\\" + outputName));
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	/**
	 * Append pane method is used when adding text to the text pane.
	 * 
	 * @param s is the new text to add
	 */
	public void appendPane(String s) {

		// First if statement is to remove unnecessary text in the event of a new append
		// call.
		if (textPane.getText().contains("VCRA GUI BETA")
				|| textPane.getText().contains("MUST INPUT SOME DATA BEFORE SAVING")
				|| textPane.getText().contains("Options Reset") || textPane.getText().contains("Ljava")
				|| textPane.getText().contains("UCRs Completed") || textPane.getText().contains("Errors exist")) {
			StyledDocument doc = textPane.getStyledDocument();
			SimpleAttributeSet left = new SimpleAttributeSet();
			StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
			doc.setParagraphAttributes(0, doc.getLength(), left, false);
			clearPane();
		}
		// If statement is used to add 'Options Set:' as a title at the top
		if (textPane.getText().contentEquals("")) {
			SimpleAttributeSet left = new SimpleAttributeSet();
			StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
			StyledDocument doc = textPane.getStyledDocument();
			doc.setParagraphAttributes(0, 0, left, false);

			Style style = doc.addStyle("largetitle", null);
			StyleConstants.setFontSize(style, 18);
			StyleConstants.setUnderline(style, true);
			StyleConstants.setBold(style, true);
			try {
				doc.insertString(doc.getLength(), "Options Set: \n", style);
			} catch (Exception e) {
				System.out.println("an error has occurred");
			}
			try {
				doc.insertString(doc.getLength(), s + "\n", null);
			} catch (Exception e) {
				System.out.println("Bad Location Exception");
			}
			// Else statement to just add text to the bottom
		} else {
			try {
				StyledDocument doc = textPane.getStyledDocument();
				doc.insertString(doc.getLength(), s + "\n", null);
			} catch (Exception e) {
				System.out.println("Bad Location Exception");
			}
		}
	}

	/**
	 * Strict append method only adds the next string
	 */
	public void strictAppend(String s) {
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyledDocument doc = textPane.getStyledDocument();
		doc.setParagraphAttributes(0, 0, left, false);
		try {
			doc.insertString(doc.getLength(), s, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clear pane method clears the textPane
	 */
	public void clearPane() {
		textPane.setText("");
	}

	/**
	 * Replaces old text with new updated text
	 * 
	 * @param old    is a substring of the old text to be replaced
	 * @param update is the new text used to replace old
	 */
	public void replaceText(String old, String update) {
		String text = textPane.getText();
		String output = "";

		// Gets textPane as a List
		List<String> textAsList = Arrays.asList(text.split("\n"));

		// For loop to find the old text and replace it
		for (int i = 1; i < textAsList.size(); i++) {
			if (textAsList.get(i).contains(old)) {
				if (i < textAsList.size() - 1) {
					output += update + "\n";
				} else {
					output += update;
				}
			} else if (i < textAsList.size() - 1) {
				output += textAsList.get(i) + "\n";
			} else {
				output += textAsList.get(i);
			}
		}
		// Clear pane to avoid print repetition
		clearPane();
		// Adds new text 'update' to the pane
		appendPane(output);
	}

	/**
	 * Set button panel sets the button panel for easier formatting of the GUI
	 */
	public void setBtnPnl() {
		// Set border for panel
		buttonPnl.setBorder(
				new CompoundBorder(BorderFactory.createBevelBorder(0), BorderFactory.createEmptyBorder(15, 5, 15, 5)));

		// Set size for panel to appear larger and take appropriate space
		resetBtn.setPreferredSize(new Dimension(150, 60));
		runBtn.setPreferredSize(new Dimension(100, 60));

		// Add buttons to panel
		buttonPnl.add(resetBtn, BorderLayout.WEST);
		buttonPnl.add(runBtn, BorderLayout.EAST);
	}

	/**
	 * Check method to see if any variable is null, used in saving output
	 * 
	 * @return true if no values have been set.
	 */
	public boolean areVariablesNull() {
		if (puckFile == null && airspaceFile == null && outputName == null && altBand == null && uaSpeed == -1
				&& uaClimbRate == 0 && uaHeading == -1 && pcol == -1.0 && keepoutVolumeRadius == -1
				&& keepoutVolumeHeight == -1 && puckRadius == -1 && puckHeight == -1 && startTOD == 0 && endTOD == 23
				&& totalObservationTime == -1 && header == false) {
			return true;
		}
		return false;
	}

	/**
	 * Sets the names and values arrays for all modified variables.
	 */
	public void setOutput() {
		if (puckFile != null) {
			names.add("puckFile");
			values.add(puckFile);
		}

		if (airspaceFile != null) {
			names.add("airspaceFile");
			values.add(airspaceFile);
		}

		if (outputName != null) {
			names.add("outputName");
			values.add(outputName);
		}

		if (altBand != null) {
			names.add("altBand");
			values.add(altBand);
		}
		if (uaSpeed != -1.0) {
			names.add("uaSpeed");
			values.add(uaSpeed);
		}
		if (uaClimbRate != 0.0) {
			names.add("uaClimbRate");
			values.add(uaClimbRate);
		}
		if (uaHeading != -1.0) {
			names.add("uaHeading");
			values.add(uaHeading);
		}
		if (pcol != -1.0) {
			names.add("pcol");
			values.add(pcol);
		}
		if (keepoutVolumeRadius != -1) {
			names.add("keepoutVolumeRadius");
			values.add(keepoutVolumeRadius);
		}
		if (keepoutVolumeHeight != -1) {
			names.add("keepoutVolumeHeight");
			values.add(keepoutVolumeHeight);
		}
		if (puckRadius != -1) {
			names.add("puckRadius");
			values.add(puckRadius);
		}
		if (puckHeight != -1) {
			names.add("puckHeight");
			values.add(puckHeight);
		}
		if (startTOD != 0) {
			names.add("startTOD");
			values.add(startTOD);
		}
		if (endTOD != 23) {
			names.add("endTOD");
			values.add(endTOD);
		}
		if (totalObservationTime != -1) {
			names.add("totalObservationTime");
			values.add(totalObservationTime);
		}
		if (header != false) {
			names.add("header");
			values.add(header);
		}
	}

	/**
	 * Sets all values to null, used in the open function in MenuItems
	 */
	public void setAllNull() {
		puckFile = null;
		airspaceFile = null;
		outputName = null;
		altBand = null;
		uaSpeed = -1.0;
		uaClimbRate = 0.0;
		uaHeading = -1.0;
		pcol = -1.0;
		keepoutVolumeRadius = -1;
		keepoutVolumeHeight = -1;
		puckRadius = -1.0;
		puckHeight = -1.0;
		startTOD = 0;
		endTOD = 23;
		totalObservationTime = -1;
		header = false;
	}

	// GETTERS AND SETTERS

	public ArrayList<String> getNames() {
		return this.names;
	}

	public ArrayList<Object> getValues() {
		return this.values;
	}

	public JPanel getComponentsPanel() {
		return components;
	}

	public JPanel getLeft() {
		return left;
	}

	public JPanel getRight() {
		return right;
	}

	public JPanel getButtonPnl() {
		return buttonPnl;
	}

	public MenuItems getMenuBarPanel() {
		return menuBar;
	}

	public FilePanel getFilePanel() {
		return filePanel;
	}

	public OptionsPanel getOptionsPanel() {
		return optionsPanel;
	}

	public TextPane getTextPane() {
		return textPane;
	}

	public JButton getRunBtn() {
		return runBtn;
	}

	public JButton getResetBtn() {
		return resetBtn;
	}

	public String getPuckFile() {
		return puckFile;
	}

	public String getAirspaceFile() {
		return airspaceFile;
	}

	public String getOutputName() {
		return outputName;
	}

	public String getAltBand() {
		return altBand;
	}

	public double getUaSpeed() {
		return uaSpeed;
	}

	public double getUaClimbRate() {
		return uaClimbRate;
	}

	public double getUaHeading() {
		return uaHeading;
	}

	public double getPcol() {
		return pcol;
	}

	public int getKeepoutVolumeRadius() {
		return keepoutVolumeRadius;
	}

	public int getKeepoutVolumeHeight() {
		return keepoutVolumeHeight;
	}

	public double getPuckRadius() {
		return puckRadius;
	}

	public double getPuckHeight() {
		return puckHeight;
	}

	public int getStartTOD() {
		return startTOD;
	}

	public int getEndTOD() {
		return endTOD;
	}

	public int getTotalObservationTime() {
		return totalObservationTime;
	}

	public boolean isHeader() {
		return header;
	}

	public void setComponents(JPanel components) {
		this.components = components;
	}

	public void setLeft(JPanel left) {
		this.left = left;
	}

	public void setRight(JPanel right) {
		this.right = right;
	}

	public void setButtonPnl(JPanel buttonPnl) {
		this.buttonPnl = buttonPnl;
	}

	public void setMenuBar(MenuItems menuBar) {
		this.menuBar = menuBar;
	}

	public void setFilePanel(FilePanel filePanel) {
		this.filePanel = filePanel;
	}

	public void setOptionsPanel(OptionsPanel optionsPanel) {
		this.optionsPanel = optionsPanel;
	}

	public void setRunBtn(JButton runBtn) {
		this.runBtn = runBtn;
	}

	public void setResetBtn(JButton resetBtn) {
		this.resetBtn = resetBtn;
	}

	public void setPuckFile(String puckFile) {
		this.puckFile = puckFile;
	}

	public void setAirspaceFile(String airspaceFile) {
		this.airspaceFile = airspaceFile;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public void setAltBand(String altBand) {
		this.altBand = altBand;
	}

	public void setUaSpeed(double uaSpeed) {
		this.uaSpeed = uaSpeed;
	}

	public void setUaClimbRate(double uaClimbRate) {
		this.uaClimbRate = uaClimbRate;
	}

	public void setUaHeading(double uaHeading) {
		this.uaHeading = uaHeading;
	}

	public void setPcol(double pcol) {
		this.pcol = pcol;
	}

	public void setKeepoutVolumeRadius(int keepoutVolumeRadius) {
		this.keepoutVolumeRadius = keepoutVolumeRadius;
	}

	public void setKeepoutVolumeHeight(int keepoutVolumeHeight) {
		this.keepoutVolumeHeight = keepoutVolumeHeight;
	}

	public void setPuckRadius(double puckRadius) {
		this.puckRadius = puckRadius;
	}

	public void setPuckHeight(double puckHeight) {
		this.puckHeight = puckHeight;
	}

	public void setStartTOD(int startTOD) {
		this.startTOD = startTOD;
	}

	public void setEndTOD(int endTOD) {
		this.endTOD = endTOD;
	}

	public void setTotalObservationTime(int totalObservationTime) {
		this.totalObservationTime = totalObservationTime;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

}
