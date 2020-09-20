
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import json.simple.JSONObject;

public class MenuItems extends JMenuBar {

	private static final long serialVersionUID = 1L;

	// Current instance of MenuItems for use in private methods
	private MenuItems mi = this;

	// Instance of Main Controller to set values and edit the text pane
	private MainController mc;

	/**
	 * Constructor creates menu items to add to the created menu bar (parent class)
	 * 
	 * @param mc is the instance of the Main Controller
	 */
	public MenuItems(MainController mc, FilePanel fp, OptionsPanel op) {
		super();
		this.mc = mc;

		// Menu bar tabs
		JMenu file = new JMenu("File");
		JMenu help = new JMenu("Help");

		// File menu items
		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveAs = new JMenuItem("Save As");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem exit = new JMenuItem("Exit");

		// Help menu items
		JMenuItem about = new JMenuItem("About");
		JMenuItem inst = new JMenuItem("Instructions");

		// ACTION LISTENERS FOR EACH MENU ITEM

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mc.getTextPane().getText().contains("Error!")) {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>You cannot save. There are errors.</html>" + "</div></html>");
					JOptionPane.showMessageDialog(App.frame, abt);
				} else if (mc.getPuckFile() == null || mc.getAirspaceFile() == null || mc.getOutputName() == null) {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>You cannot save. Some files are not selected.</html>" + "</div></html>");
					JOptionPane.showMessageDialog(App.frame, abt);
				} else {
					if (!mc.areVariablesNull()) {
						try {
							// Saves all modified inputs in JSON format as a .vcra file in the local
							// directory
							JSONObject jObj = getOutFormat();
							String saveoption = JOptionPane.showInputDialog(App.frame, "Save file as _.vcra");
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

						} catch (Exception ex) {

						}
					} else {
						mc.clearPane();
						mc.appendPane("\n\n\n\n\nMUST INPUT SOME DATA BEFORE SAVING");
					}
				}
			}
		});

		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mc.getTextPane().getText().contains("Error!")) {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>You cannot save. There are errors.</html>" + "</div></html>");
					JOptionPane.showMessageDialog(App.frame, abt);
				} else if (mc.getPuckFile() == null || mc.getAirspaceFile() == null || mc.getOutputName() == null) {
					JLabel abt = new JLabel("<html><div style='text-align: center;'>"
							+ "<html>You cannot save. Some files are not selected.</html>" + "</div></html>");
					JOptionPane.showMessageDialog(App.frame, abt);
				} else {
					if (!mc.areVariablesNull()) {
						try {
							// Saves all modified inputs in JSON format as a .vcra file in a selected
							// directory
							JSONObject jObj = getOutFormat();
							File outputFile = null;
							String location = new File(System.getProperty("user.home")).getAbsolutePath();
							final JFileChooser jfc = new JFileChooser();
							jfc.setCurrentDirectory(new File(location));
							jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
							jfc.setMultiSelectionEnabled(false);
							int returnVal = jfc.showSaveDialog(mi);
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								outputFile = jfc.getSelectedFile();
								if (!outputFile.getName().trim().endsWith(".vcra")) {
									outputFile = new File(outputFile.getName().trim().replaceAll(" ", "_") + ".vcra");
								}
							}

							if (outputFile != null) {
								outputFile = new File(outputFile.getName().trim().replaceAll(" ", "_"));
								Writer fileWriter = new FileWriter(outputFile.getAbsolutePath(), false);
								fileWriter.write(jObj.toString());
								fileWriter.close();
							}

						} catch (Exception ex) {
							mc.appendPane(ex.getStackTrace().toString());
						}
					} else {
						mc.clearPane();
						mc.appendPane("\n\n\n\n\nMUST INPUT SOME DATA BEFORE SAVING");
					}
				}
			}
		});

		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Opens an existing file of saved inputs and sets current values to those
				// inputs
				String location = new File(System.getProperty("user.home")).getAbsolutePath();
				File selectedFile = null;
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File(location));
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				int result = jfc.showOpenDialog(mi);
				if (result == JFileChooser.APPROVE_OPTION) {
					selectedFile = jfc.getSelectedFile();

				}
				if (selectedFile != null && selectedFile.getName().endsWith(".vcra")) {
					try {
						// Reader reads JSON format and sets all appropriate variables to the read in
						// values
						fp.clearFields();
						fp.clearHeader();
						mc.setAllNull();
						mc.clearPane();
						op.setEmpty();
						BufferedReader br = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
						JSONParser jparse = new JSONParser();
						Object obj = jparse.parse(br);
						JSONObject varList = (JSONObject) obj;

						// Code to read JSON file and set variables in fields and main controller

						if (varList.containsKey("puckFile") && (String) varList.get("puckFile") instanceof String
								&& ((String) varList.get("puckFile")).endsWith(".csv")) {
							File tempFile = new File((String) varList.get("puckFile"));
							if (tempFile.exists()) {
								mc.setPuckFile((String) varList.get("puckFile"));
								mc.getFilePanel().setPuckField();
							}
						}
						if (varList.containsKey("airspaceFile")
								&& (String) varList.get("airspaceFile") instanceof String
								&& ((String) varList.get("airspaceFile")).endsWith(".csv")
								|| ((String) varList.get("airspaceFile")).endsWith(".mat")) {
							File tempFile = new File((String) varList.get("airspaceFile"));
							if (tempFile.exists()) {
								mc.setAirspaceFile((String) varList.get("airspaceFile"));
								mc.getFilePanel().setAirspaceField();
							}
						}
						if (varList.containsKey("outputName") && (String) varList.get("outputName") instanceof String
								&& ((String) varList.get("outputName")).endsWith(".csv")) {
							mc.setOutputName((String) varList.get("outputName"));
							mc.getFilePanel().setOutputNameField();
						}
						if (varList.containsKey("altBand")) {
							mc.setAltBand((String) varList.get("altBand"));
							mc.appendPane("Altitude Band: " + mc.getAltBand());
							op.getAltBandField().setText((String) varList.get("altBand"));
							op.getAltBandField().postActionEvent();
						}
						if (varList.containsKey("uaSpeed")) {
							mc.setUaSpeed(((Number) varList.get("uaSpeed")).doubleValue());
							mc.appendPane("UA Speed: " + mc.getUaSpeed());
							op.getUaSpeedField().setText("" + ((Number) varList.get("uaSpeed")).doubleValue());
							op.getUaSpeedField().postActionEvent();
						}
						if (varList.containsKey("uaClimbRate")) {
							mc.setUaClimbRate(((Number) varList.get("uaClimbRate")).doubleValue());
							mc.appendPane("UA Climb Rate: " + mc.getUaClimbRate());
							op.getUaClimbRateField().setText("" + ((Number) varList.get("uaClimbRate")).doubleValue());
							op.getUaClimbRateField().postActionEvent();
						}
						if (varList.containsKey("uaHeading")) {
							mc.setUaHeading(((Number) varList.get("uaHeading")).doubleValue());
							mc.appendPane("UA Heading: " + mc.getUaHeading());
							op.getUaHeadingField().setText("" + ((Number) varList.get("uaHeading")).doubleValue());
							op.getUaHeadingField().postActionEvent();
						}
						if (varList.containsKey("pcol")) {
							mc.setPcol(((Number) varList.get("pcol")).doubleValue());
							mc.appendPane("PCOL: " + mc.getPcol());
							op.getPcolField().setText("" + ((Number) varList.get("pcol")).doubleValue());
							op.getPcolField().postActionEvent();
						}
						if (varList.containsKey("keepoutVolumeRadius")) {
							mc.setKeepoutVolumeRadius(((Number) varList.get("keepoutVolumeRadius")).intValue());
							mc.appendPane("Keepout Volume Radius: " + mc.getKeepoutVolumeRadius());
							op.getKeepoutVolumeRadiusField()
									.setText("" + ((Number) varList.get("keepoutVolumeRadius")).intValue());
							op.getKeepoutVolumeRadiusField().postActionEvent();
						}
						if (varList.containsKey("keepoutVolumeHeight")) {
							mc.setKeepoutVolumeHeight(((Number) varList.get("keepoutVolumeHeight")).intValue());
							mc.appendPane("Keepout Volume Height: " + mc.getKeepoutVolumeHeight());
							op.getKeepoutVolumeHeightField()
									.setText("" + ((Number) varList.get("keepoutVolumeHeight")).intValue());
							op.getKeepoutVolumeHeightField().postActionEvent();
						}
						if (varList.containsKey("puckRadius")) {
							mc.setPuckRadius(((Number) varList.get("puckRadius")).doubleValue());
							mc.appendPane("Puck Radius: " + mc.getPuckRadius());
							op.getPuckRadiusField().setText("" + ((Number) varList.get("puckRadius")).doubleValue());
							op.getPuckRadiusField().postActionEvent();
						}
						if (varList.containsKey("puckHeight")) {
							mc.setPuckHeight(((Number) varList.get("puckHeight")).doubleValue());
							mc.appendPane("Puck Height: " + mc.getPuckHeight());
							op.getPuckHeightField().setText("" + ((Number) varList.get("puckHeight")).doubleValue());
							op.getPuckHeightField().postActionEvent();
						}
						if (varList.containsKey("startTOD")) {
							mc.setStartTOD(((Number) varList.get("startTOD")).intValue());
							mc.appendPane("Start Time of Day: " + mc.getStartTOD());
							op.getStartTODField().setText("" + ((Number) varList.get("startTOD")).intValue());
							op.getStartTODField().postActionEvent();
						}
						if (varList.containsKey("endTOD")) {
							mc.setEndTOD(((Number) varList.get("endTOD")).intValue());
							if(!mc.getTextPane().getText().contains("End Time of Day: ")) {
								mc.appendPane("End Time of Day: " + mc.getEndTOD());
							}
							op.getEndTODField().setText("" + ((Number) varList.get("endTOD")).intValue());
							op.getEndTODField().postActionEvent();
						}
						if (varList.containsKey("totalObservationTime")) {
							mc.setTotalObservationTime(((Number) varList.get("totalObservationTime")).intValue());
							mc.appendPane("Total Observation Time: " + mc.getTotalObservationTime());
							op.getTotalObservationTimeField()
									.setText("" + ((Number) varList.get("totalObservationTime")).intValue());
							op.getTotalObservationTimeField().postActionEvent();
						}
						if (varList.containsKey("header") && varList.get("header") instanceof Boolean) {
							mc.setHeader((boolean) varList.get("header"));
							if ((boolean) varList.get("header") == true) {
								fp.setHeaderTrue();
							}
						}

					} catch (Exception ex) {
						mc.clearPane();
						mc.strictAppend("Errors exist in the .vcra file, please manually input data and overwrite your file to fix this.");
					}
					// Previously checked for .vcra extension, this is an error message in case the
					// file type is wrong
				} else {
					JLabel err = new JLabel("File must have extension '.vcra' to open.");

					JOptionPane.showMessageDialog(App.frame, err);
				}
			}
		});

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Exits the GUI
				System.exit(0);
			}
		});

		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Displays information about the GUI
				JLabel abt = new JLabel("<html><div style='text-align: center;'>" + "<html>VCRA GUI BETA V0.1</html>"
						+ "</div></html>");

				JOptionPane.showMessageDialog(App.frame, abt);
			}
		});

		inst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "1. Enter data into the appropriate fields.<br/><br/>"
						+ "2. Press the enter key on your keyboard after each input to update information. The input data will appear in this text field.<br/><br/>"
						+ "3. Once all data is entered, press the run button.<br/><br/>"
						+ "4. To save all inputs for future use, use the save or save as function under the file menu. The run button will also save all inputs in the project directory.<br/><br/>"
						+ "5. To load previously saved inputs, use the open function under the file menu.<br/></html>";

				JLabel inst = new JLabel("<html><div style='text-align: left;'>" + message + "</div></html>");

				JOptionPane.showMessageDialog(App.frame, inst);
			}
		});

		// Adds menu items to the file menu
		file.add(save);
		file.add(saveAs);
		file.add(open);
		file.add(exit);

		// Adds menu items to the help menu
		help.add(about);
		help.add(inst);

		// Adds menus to the menu bar
		this.add(file);
		this.add(help);
	}

	/**
	 * Creates a JSON Object from the two output arrays created in the main
	 * controller
	 * 
	 * @return a JSON Object
	 */
	public JSONObject getOutFormat() {
		mc.setOutput();
		ArrayList<String> names = mc.getNames();
		ArrayList<Object> values = mc.getValues();
		JSONObject jObj = new JSONObject();
		for (int i = 0; i < names.size(); i++) {
			try {
				jObj.put(names.get(i), values.get(i));
			} catch (Exception e) {
				mc.appendPane(e.getStackTrace().toString());
			}
		}
		return jObj;
	}

}
