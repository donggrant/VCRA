
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public class FilePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// JFileChooser initialization
	final JFileChooser jfc = new JFileChooser();

	// Instance of MainController to access set and textPane related methods
	private MainController mc;

	// Declare header button group and radio buttons
	private ButtonGroup headerButtons = new ButtonGroup();
	private JRadioButton headerTrue = new JRadioButton("True");
	private JRadioButton headerFalse = new JRadioButton("False");

	// Declare private text areas
	private JTextArea puckFileField;
	private JTextArea airspaceFileField;
	private JTextArea outputNameField;

	/**
	 * Constructor for File Panel object
	 * 
	 * @param mc is the instance of the Main Controller
	 */
	public FilePanel(MainController mc) {
		super();

		this.mc = mc;

		// add header buttons to the button group
		headerButtons.add(headerTrue);
		headerButtons.add(headerFalse);

		// Set panel layout
		setLayout(new GridBagLayout());

		// Declare text for JLabels
		String puckText = "Path to the CSV File Containing Puck Locations: ";
		String airspaceText = "Path to the CSV or MAT File Containing Traffic Data: ";
		String outputText = "Name of the puckstats (results) file: ";

		puckText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 120, puckText);
		airspaceText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 120, airspaceText);
		outputText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 120, outputText);

		JLabel puckFileLabel = new JLabel(puckText);
		JLabel airspaceFileLabel = new JLabel(airspaceText);
		JLabel outputNameLabel = new JLabel(outputText);

		// Give the Labels empty borders for spacing
		puckFileLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		airspaceFileLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		outputNameLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

		// Create Fields for Primary Inputs and allow them to wrap
		puckFileField = new JTextArea(3, 20);
		airspaceFileField = new JTextArea(3, 20);
		outputNameField = new JTextArea(3, 20);

		// Set fields to allow line wrapping
		puckFileField.setLineWrap(true);
		airspaceFileField.setLineWrap(true);
		outputNameField.setLineWrap(true);

		// Restrict path fields from direct editing
		puckFileField.setEditable(false);
		airspaceFileField.setEditable(false);

		// Listener for outputNameField
		outputNameField.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (!outputNameField.getText().trim().contentEquals("")) {
					if (!outputNameField.getText().trim().endsWith(".csv")) {
						outputNameField.setText(outputNameField.getText().trim().replaceAll(" ", "_") + ".csv");
						mc.setOutputName(outputNameField.getText());
					} else {
						mc.setOutputName(outputNameField.getText().trim().replaceAll(" ", "_"));
						outputNameField.setText(outputNameField.getText().trim().replaceAll(" ", "_"));
					}
				} else {
					mc.setOutputName(null);
				}
			}

			public void focusGained(FocusEvent e) {

			}
		});

		// Create file chooser buttons for all three inputs
		JButton puckFileChooser = new JButton("Select File");
		JButton airspaceFileChooser = new JButton("Select File");
		JButton outputNameChooser = new JButton("Select File");

		// Set button sizes
		puckFileChooser.setPreferredSize(new Dimension(100, 20));
		airspaceFileChooser.setPreferredSize(new Dimension(100, 20));
		outputNameChooser.setPreferredSize(new Dimension(100, 20));

		// Create action listeners for each file chooser

		puckFileChooser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				File file = openFileSystem();
				if (file != null && file.getName().endsWith(".csv")) {
					puckFileField.setText(file.getAbsolutePath());
					mc.setPuckFile(file.getAbsolutePath());
				} else {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>Incorrect file type: .csv expected</html>" + "</div></html>");
					JOptionPane.showMessageDialog(App.frame, abt);
				}
			}
		});

		airspaceFileChooser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				File file = openFileSystem();
				if (file != null && (file.getName().endsWith(".csv") || file.getName().endsWith(".mat"))) {
					airspaceFileField.setText(file.getAbsolutePath());
					mc.setAirspaceFile(file.getAbsolutePath());
				} else {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>Incorrect file type:  .csv or .mat expected</html>" + "</div></html>");
					JOptionPane.showMessageDialog(App.frame, abt);
				}
			}
		});

		outputNameChooser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				File file = openFileSystem();
				if (file != null && file.getName().endsWith(".csv")) {
					outputNameField.setText(file.getName());
					mc.setOutputName(file.getName());
				} else {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>Incorrect file type:  .csv expected</html>" + "</div></html>");
					JOptionPane.showMessageDialog(App.frame, abt);
				}
			}
		});

		// Header listeners
		headerTrue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (headerTrue.isSelected()) {
					mc.setHeader(true);
				}
			}
		});
      
      	headerFalse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (headerFalse.isSelected()) {
					mc.setHeader(false);
				}
			}
		});

		// Set Layout for LeftPanel
		GridBagConstraints gc = new GridBagConstraints();

		///////// First Row /////////////////////////////

		// anchor
		gc.anchor = GridBagConstraints.LINE_START;

		// x and y
		gc.gridx = 0;
		gc.gridy = 0;

		// weight
		gc.weightx = 0.1;
		gc.weighty = 0.1;

		// add puckFileLabel
		add(puckFileLabel, gc);

		////////// Second Column //////////////

		// x and y
		gc.gridx++;

		// add puckFileField
		add(puckFileField, gc);

		////////// Third Column //////////////

		// x and y
		gc.gridx++;

		// add file chooser button
		add(puckFileChooser, gc);

		///////// Next Row //////////////////////////////

		// x and y
		gc.gridx = 0;
		gc.gridy++;

		// add airspaceFileLabel
		add(airspaceFileLabel, gc);

		////////// Second Column //////////////

		// x and y
		gc.gridx++;

		// add airspaceFileField
		add(airspaceFileField, gc);

		////////// Third Column //////////////

		// x and y
		gc.gridx++;

		// add file chooser button
		add(airspaceFileChooser, gc);

		// Header Row//

		// x and y
		gc.gridx = 0;
		gc.gridy++;

		// add Header
		JLabel header = new JLabel("Header");
      
      	// Add tool tips to header and radio buttons
		header.setToolTipText("True/False: The CSV File containing traffic data has a header line.");
		headerTrue.setToolTipText("True/False: The CSV File containing traffic data has a header line.");
		headerFalse.setToolTipText("True/False: The CSV File containing traffic data has a header line.");
      
		// add radio buttons and header
		JPanel bpanel = new JPanel();

		// Set layout of radio button panel
		bpanel.setLayout(new BorderLayout());

		// Add label and radio buttons
		bpanel.add(header, BorderLayout.WEST);
		headerTrue.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		bpanel.add(headerTrue, BorderLayout.CENTER);
		headerFalse.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		headerFalse.setSelected(true);
		bpanel.add(headerFalse, BorderLayout.EAST);

		gc.weightx = 0;
		gc.gridx = 1;
		add(bpanel, gc);

		///////// Next Row //////////////////////////////

		// x and y
		gc.gridx = 0;
		gc.gridy++;

		// add outputNameLabel
		add(outputNameLabel, gc);

		////////// Second Column //////////////

		// x and y
		gc.gridx++;

		// add outputNameField
		add(outputNameField, gc);

		////////// Third Column //////////////

		// x and y
		gc.gridx++;

		// add file chooser button
		add(outputNameChooser, gc);

	}

	/**
	 * Method to get a file for use with JFileChooser buttons
	 * 
	 * @return the selected file
	 */
	public File openFileSystem() {
		String location = new File(System.getProperty("user.home")).getAbsolutePath();
		jfc.setCurrentDirectory(new File(location));
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		int result = jfc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			return selectedFile;

		}
		return null;
	}

	// SET METHODS FOR FILE FIELDS

	public void setPuckField() {
		puckFileField.setText(mc.getPuckFile());
	}

	public void setAirspaceField() {
		airspaceFileField.setText(mc.getAirspaceFile());
	}

	public void setOutputNameField() {
		outputNameField.setText(mc.getOutputName());
	}
	
	public void setHeaderTrue() {
		headerTrue.setSelected(true);
	}
	
	public void clearHeader() {
		headerButtons.clearSelection();
		headerFalse.setSelected(true);
	}
	
	public void clearFields() {
		puckFileField.setText("");
		airspaceFileField.setText("");
		outputNameField.setText("");
	}

}