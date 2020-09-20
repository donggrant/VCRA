
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OptionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Declaring fields with respective labels for all inputs and variables
	private JTextField altBandField = new JTextField(10);
	private JLabel altBandLabel = new JLabel("Alt Band (ft) [X1, X2..]");
	private JTextField uaSpeedField = new JTextField(10);
	private JLabel uaSpeedLabel = new JLabel("UA Speed (kt)");
	private JTextField uaClimbRateField = new JTextField(10);
	private JLabel uaClimbRateLabel = new JLabel("UA Climb Rate (ft/min)");
	private JTextField uaHeadingField = new JTextField(10);
	private JLabel uaHeadingLabel = new JLabel("UA Heading (deg)");
	private JTextField pcolField = new JTextField(10);
	private JLabel pcolLabel = new JLabel("Probability of Collision");
	private JTextField keepoutVolumeRadiusField = new JTextField(10);
	private JLabel keepoutVolumeRadiusLabel = new JLabel("Keepout Volume Radius (ft)");
	private JTextField keepoutVolumeHeightField = new JTextField(10);
	private JLabel keepoutVolumeHeightLabel = new JLabel("Keepout Volume Height (ft)");
	private JTextField puckRadiusField = new JTextField(10);
	private JLabel puckRadiusLabel = new JLabel("Puck Radius (NM)");
	private JTextField puckHeightField = new JTextField(10);
	private JLabel puckHeightLabel = new JLabel("Puck Height (ft)");
	private JTextField startTODField = new JTextField(10);
	private JLabel startTODLabel = new JLabel("Start Time of Day");
	private JTextField endTODField = new JTextField(10);
	private JLabel endTODLabel = new JLabel("End Time of Day");
	private JTextField totalObservationTimeField = new JTextField(10);
	private JLabel totalObservationTimeLabel = new JLabel(" Total Observation Time (sec)");

	/**
	 * Optional input panel
	 * 
	 * @param mc instance of Main Controller for set methods and textPane
	 *           modification.
	 */
	public OptionsPanel(MainController mc) {
		super();

		// Set layout
		setLayout(new GridBagLayout());

		// Set border
		GridBagConstraints gc = new GridBagConstraints();

		// Anchor east for clear visibility
		gc.anchor = GridBagConstraints.EAST;
		
		// Tool Tip Text
		
		altBandField.setToolTipText("Altitude Band (ft): Integer > 0. Enter lists as int1, int2, int3... Default: 250");
		altBandLabel.setToolTipText("Altitude Band (ft): Integer > 0. Enter lists as int1, int2, int3... Default: 250");
		uaSpeedField.setToolTipText("UA Speed (kt): Double > 0. Default: 30");
		uaSpeedLabel.setToolTipText("UA Speed (kt): Double > 0. Default: 30");
		uaClimbRateField.setToolTipText("UA Climb Rate (ft/min): Double > 0. Null as default.");
		uaClimbRateLabel.setToolTipText("UA Climb Rate (ft/min): Double > 0. Null as default.");
		uaHeadingField.setToolTipText("UA Heading (deg): Double from [0 - 360). Null as default.");
		uaHeadingLabel.setToolTipText("UA Heading (deg): Double from [0 - 360). Null as default.");
		pcolField.setToolTipText("Probability of Collision: Double from [0 - 1]. Default: 0.05");
		pcolLabel.setToolTipText("Probability of Collision: Double from [0 - 1]. Default: 0.05");
		keepoutVolumeRadiusField.setToolTipText("Keepout Volume Radius (ft): Integer > 0. Default: 500");
		keepoutVolumeRadiusLabel.setToolTipText("Keepout Volume Radius (ft): Integer > 0. Default: 500");
		keepoutVolumeHeightField.setToolTipText("Keepout Volume Height (ft): Integer > 0. Default: 200");
		keepoutVolumeHeightLabel.setToolTipText("Keepout Volume Height (ft): Integer > 0. Default: 200");
		puckRadiusField.setToolTipText("Puck Radius (NM): Double > 0. Default: 5");
		puckRadiusLabel.setToolTipText("Puck Radius (NM): Double > 0. Default: 5");
		puckHeightField.setToolTipText("Puck Height (ft): Double > 0. Default: 500");
		puckHeightLabel.setToolTipText("Puck Height (ft): Double > 0. Default: 500");
		startTODField.setToolTipText("Start Time of Day: Integer - Default is 0 (Midnight) Maximum is 23 (11:00pm).");
		startTODLabel.setToolTipText("Start Time of Day: Integer - Default is 0 (Midnight) Maximum is 23 (11:00pm).");
		endTODField.setToolTipText("End Time of Day: Integer - Default is 23 (11:00pm) Minimum is 0 (Midnight).");
		endTODLabel.setToolTipText("End Time of Day: Integer - Default is 23 (11:00pm) Minimum is 0 (Midnight).");
		totalObservationTimeField.setToolTipText("Total Observation Time (sec): Integer > 0. None as default.");
		totalObservationTimeLabel.setToolTipText("Total Observation Time (sec): Integer > 0. None as default.");

		// ACTION LISTENERS FOR EACH FIELD

		altBandField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// altBand action listener converts a ',' separated string into an int array and
					// then back to a string to check for type errors on input

					int[] altBand = null;
					String altBandstr = altBandField.getText();
					// altBandstr = altBandstr.stripTrailing();
					if (altBandstr != null && !altBandstr.contentEquals("")) {
						altBand = Arrays.asList(altBandstr.split(",")).parallelStream().map(String::trim)
								.mapToInt(Integer::parseInt).toArray();
					}
					String str = "";
					for (int i = 0; i < altBand.length; i++) {
						if (i < altBand.length - 1) {
							if (altBand[i] <= 0) {
								throw new InvalidValueException();
							}
							str += altBand[i] + ", ";
						} else {
							if (altBand[i] <= 0) {
								throw new InvalidValueException();
							}
							str += "" + altBand[i];
						}
					}
					// Set Main Controller variable
					mc.setAltBand(str);

					// If block to see if input is new or will be replacing old input
					if (mc.getTextPane().getText().contains("Altitude Band: ")) {
						mc.replaceText("Altitude Band: ", "Altitude Band: " + str);
					} else {
						mc.appendPane("Altitude Band: " + str);
					}
				} catch (InvalidValueException ive) {
					String errorMsg = "[Error! Invalid input received. Altitude Band value must be greater than 0.]";
					if (mc.getTextPane().getText().contains("Altitude Band: ")) {
						mc.replaceText("Altitude Band: ", "Altitude Band: " + errorMsg);
					} else {
						mc.appendPane("Altitude Band: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter integers separated by comas for this field.]";
					if (mc.getTextPane().getText().contains("Altitude Band: ")) {
						mc.replaceText("Altitude Band: ", "Altitude Band: " + errorMsg);
					} else {
						mc.appendPane("Altitude Band: " + errorMsg);
					}
				}
			}
		});

		uaSpeedField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Set Main Controller variable and text pane
					if (Double.parseDouble(uaSpeedField.getText()) <= 0) {
						throw new InvalidValueException();
					}
					mc.setUaSpeed(Double.parseDouble(uaSpeedField.getText()));
					if (mc.getTextPane().getText().contains("UA Speed: ")) {
						mc.replaceText("UA Speed: ", "UA Speed: " + mc.getUaSpeed());
					} else {
						mc.appendPane("UA Speed: " + mc.getUaSpeed());
					}
				} catch (InvalidValueException ive) {
					String errorMsg = "[Error! Invalid input received. UA Speed value must be greater than 0.]";
					if (mc.getTextPane().getText().contains("UA Speed: ")) {
						mc.replaceText("UA Speed: ", "UA Speed: " + errorMsg);
					} else {
						mc.appendPane("UA Speed: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a number for this field.]";
					if (mc.getTextPane().getText().contains("UA Speed: ")) {
						mc.replaceText("UA Speed: ", "UA Speed: " + errorMsg);
					} else {
						mc.appendPane("UA Speed: " + errorMsg);
					}
				}
			}
		});

		uaClimbRateField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Set Main Controller variable and text pane
					mc.setUaClimbRate(Double.parseDouble(uaClimbRateField.getText()));
					if (mc.getTextPane().getText().contains("UA Climb Rate: ")) {
						mc.replaceText("UA Climb Rate: ", "UA Climb Rate: " + mc.getUaClimbRate());
					} else {
						mc.appendPane("UA Climb Rate: " + mc.getUaClimbRate());
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a number for this field.]";
					if (mc.getTextPane().getText().contains("UA Climb Rate: ")) {
						mc.replaceText("UA Climb Rate: ", "UA Climb Rate: " + errorMsg);
					} else {
						mc.appendPane("UA Climb Rate: " + errorMsg);
					}
				}
			}
		});

		uaHeadingField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Double.parseDouble(uaHeadingField.getText()) < 0
							|| Double.parseDouble(uaHeadingField.getText()) >= 360) {
						throw new InvalidValueException();
					}
					// Set Main Controller variable and text pane
					mc.setUaHeading(Double.parseDouble(uaHeadingField.getText()));
					if (mc.getTextPane().getText().contains("UA Heading: ")) {
						mc.replaceText("UA Heading: ", "UA Heading: " + mc.getUaHeading());
					} else {
						mc.appendPane("UA Heading: " + mc.getUaHeading());
					}
				} catch (InvalidValueException ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a number between 0 and 360.]";
					if (mc.getTextPane().getText().contains("UA Heading: ")) {
						mc.replaceText("UA Heading: ", "UA Heading: " + errorMsg);
					} else {
						mc.appendPane("UA Heading: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a number for this field.]";
					if (mc.getTextPane().getText().contains("UA Heading: ")) {
						mc.replaceText("UA Heading: ", "UA Heading: " + errorMsg);
					} else {
						mc.appendPane("UA Heading: " + errorMsg);
					}
				}
			}
		});

		pcolField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Set Main Controller variable and text pane
					if (0 > Double.parseDouble(pcolField.getText()) || Double.parseDouble(pcolField.getText()) > 1) {
						throw new InvalidValueException();
					}
					mc.setPcol(Double.parseDouble(pcolField.getText()));
					if (mc.getTextPane().getText().contains("PCOL: ")) {
						mc.replaceText("PCOL: ", "PCOL: " + mc.getPcol());
					} else {
						mc.appendPane("PCOL: " + mc.getPcol());
					}
				} catch (InvalidValueException ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a number between 0 and 1.]";
					if (mc.getTextPane().getText().contains("PCOL: ")) {
						mc.replaceText("PCOL: ", "PCOL: " + errorMsg);
					} else {
						mc.appendPane("PCOL: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a decimal for this field.]";
					if (mc.getTextPane().getText().contains("PCOL: ")) {
						mc.replaceText("PCOL: ", "PCOL: " + errorMsg);
					} else {
						mc.appendPane("PCOL: " + errorMsg);
					}
				}
			}
		});

		puckRadiusField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Double.parseDouble(puckRadiusField.getText()) <= 0) {
						throw new InvalidValueException();
					}
					// Set Main Controller variable and text pane
					mc.setPuckRadius(Double.parseDouble(puckRadiusField.getText()));
					if (mc.getTextPane().getText().contains("Puck Radius: ")) {
						mc.replaceText("Puck Radius: ", "Puck Radius: " + mc.getPuckRadius());
					} else {
						mc.appendPane("Puck Radius: " + mc.getPuckRadius());
					}
				} catch (InvalidValueException ive) {
					String errorMsg = "[Error! Invalid input received. Puck Radius must have a value greater than 0.]";
					if (mc.getTextPane().getText().contains("Puck Height: ")) {
						mc.replaceText("Puck Height: ", "Puck Height: " + errorMsg);
					} else {
						mc.appendPane("Puck Height: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a number for this field.]";
					if (mc.getTextPane().getText().contains("Puck Radius: ")) {
						mc.replaceText("Puck Radius: ", "Puck Radius: " + errorMsg);
					} else {
						mc.appendPane("Puck Radius: " + errorMsg);
					}
				}
			}
		});

		puckHeightField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Double.parseDouble(puckHeightField.getText()) <= 0) {
						throw new InvalidValueException();
					}
					// Set Main Controller variable and text pane
					mc.setPuckHeight(Double.parseDouble(puckHeightField.getText()));
					if (mc.getTextPane().getText().contains("Puck Height: ")) {
						mc.replaceText("Puck Height: ", "Puck Height: " + mc.getPuckHeight());
					} else {
						mc.appendPane("Puck Height: " + mc.getPuckHeight());
					}
				} catch (InvalidValueException ive) {
					String errorMsg = "[Error! Invalid input received. Puck Height must have a value greater than 0.]";
					if (mc.getTextPane().getText().contains("Puck Height: ")) {
						mc.replaceText("Puck Height: ", "Puck Height: " + errorMsg);
					} else {
						mc.appendPane("Puck Height: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a number for this field.]";
					if (mc.getTextPane().getText().contains("Puck Height: ")) {
						mc.replaceText("Puck Height: ", "Puck Height: " + errorMsg);
					} else {
						mc.appendPane("Puck Height: " + errorMsg);
					}
				}
			}
		});

		keepoutVolumeRadiusField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Integer.parseInt(keepoutVolumeRadiusField.getText()) <= 0) {
						throw new InvalidValueException();
					}
					// Set Main Controller variable and text pane
					mc.setKeepoutVolumeRadius(Integer.parseInt(keepoutVolumeRadiusField.getText()));
					if (mc.getTextPane().getText().contains("Keepout Volume Radius: ")) {
						mc.replaceText("Keepout Volume Radius: ",
								"Keepout Volume Radius: " + mc.getKeepoutVolumeRadius());
					} else {
						mc.appendPane("Keepout Volume Radius: " + mc.getKeepoutVolumeRadius());
					}
				} catch (InvalidValueException ive) {
					String errorMsg = "[Error! Invalid input received. Keepout Volume Radius must have a value greater than 0.]";
					if (mc.getTextPane().getText().contains("Keepout Volume Radius: ")) {
						mc.replaceText("Keepout Volume Radius: ", "Keepout Volume Radius: " + errorMsg);
					} else {
						mc.appendPane("Keepout Volume Radius: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a number for this field.]";
					if (mc.getTextPane().getText().contains("Keepout Volume Radius: ")) {
						mc.replaceText("Keepout Volume Radius: ", "Keepout Volume Radius: " + errorMsg);
					} else {
						mc.appendPane("Keepout Volume Radius: " + errorMsg);
					}
				}
			}
		});

		keepoutVolumeHeightField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Integer.parseInt(keepoutVolumeHeightField.getText()) <= 0) {
						throw new InvalidValueException();
					}
					// Set Main Controller variable and text pane
					mc.setKeepoutVolumeHeight(Integer.parseInt(keepoutVolumeHeightField.getText()));
					if (mc.getTextPane().getText().contains("Keepout Volume Height: ")) {
						mc.replaceText("Keepout Volume Height: ",
								"Keepout Volume Height: " + mc.getKeepoutVolumeHeight());
					} else {
						mc.appendPane("Keepout Volume Height: " + mc.getKeepoutVolumeHeight());
					}
				} catch (InvalidValueException ive) {
					String errorMsg = "[Error! Invalid input received. Keepout Volume Height must have a value greater than 0.]";
					if (mc.getTextPane().getText().contains("Keepout Volume Height: ")) {
						mc.replaceText("Keepout Volume Height: ", "Keepout Volume Height: " + errorMsg);
					} else {
						mc.appendPane("Keepout Volume Height: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter an integer for this field.]";
					if (mc.getTextPane().getText().contains("Keepout Volume Height: ")) {
						mc.replaceText("Keepout Volume Height: ", "Keepout Volume Height: " + errorMsg);
					} else {
						mc.appendPane("Keepout Volume Height: " + errorMsg);
					}
				}
			}
		});

		startTODField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mc.setStartTOD(Integer.parseInt(startTODField.getText()));
					if(Integer.parseInt(startTODField.getText())>mc.getEndTOD()) {
						
						throw new InvalidValueException();
					}
					if(Integer.parseInt(startTODField.getText())<0 || Integer.parseInt(startTODField.getText())>23) {
						throw new InvalidValueException();
					}
					if(mc.getEndTOD()<mc.getStartTOD()) {
						throw new InvalidValueException();
					}
					if(mc.getEndTOD()<0 || mc.getEndTOD()>23) {
						throw new InvalidValueException();
					}
					// Set Main Controller variable and text pane
					if (mc.getTextPane().getText().contains("Start Time of Day: ")) {
						mc.replaceText("Start Time of Day: ", "Start Time of Day: " + mc.getStartTOD());
					} else {
						mc.appendPane("Start Time of Day: " + mc.getStartTOD());
					}
					if (mc.getTextPane().getText().contains("End Time of Day: ")) {
						mc.replaceText("End Time of Day: ", "End Time of Day: " + mc.getEndTOD());
					} else {
						mc.appendPane("End Time of Day: " + mc.getEndTOD());
					}
				} catch (InvalidValueException ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Start time must be less than end time and ranges from 0 to 23.]";
					if (mc.getTextPane().getText().contains("Start Time of Day: ")) {
						mc.replaceText("Start Time of Day: ", "Start Time of Day: " + errorMsg);
					} else {
						mc.appendPane("Start Time of Day: " + errorMsg);
					}	
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter an integer for this field.]";
					if (mc.getTextPane().getText().contains("Start Time of Day: ")) {
						mc.replaceText("Start Time of Day: ", "Start Time of Day: " + errorMsg);
					} else {
						mc.appendPane("Start Time of Day: " + errorMsg);
					}
				}
			}
		});

		endTODField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mc.setEndTOD(Integer.parseInt(endTODField.getText()));
					// Set Main Controller variable and text pane
					if(Integer.parseInt(endTODField.getText())<mc.getStartTOD()) {
						throw new InvalidValueException();
					}
					if(Integer.parseInt(endTODField.getText())<0 || Integer.parseInt(endTODField.getText())>23) {
						throw new InvalidValueException();
					}
					if(mc.getStartTOD()>mc.getEndTOD()) {
						throw new InvalidValueException();
					}
					if(mc.getStartTOD()<0 || mc.getStartTOD()>23) {
						throw new InvalidValueException();
					}
					if (mc.getTextPane().getText().contains("End Time of Day: ")) {
						mc.replaceText("End Time of Day: ", "End Time of Day: " + mc.getEndTOD());
					} else {
						mc.appendPane("End Time of Day: " + mc.getEndTOD());
					}
					if (mc.getTextPane().getText().contains("Start Time of Day: ")) {
						mc.replaceText("Start Time of Day: ", "Start Time of Day: " + mc.getStartTOD());
					} else {
						mc.appendPane("Start Time of Day: " + mc.getStartTOD());
					}
				} catch (InvalidValueException ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. End time must be greater than start time and ranges from 0 to 23.]";
					if (mc.getTextPane().getText().contains("End Time of Day: ")) {
						mc.replaceText("End Time of Day: ", "End Time of Day: " + errorMsg);
					} else {
						mc.appendPane("End Time of Day: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter an integer for this field.]";
					if (mc.getTextPane().getText().contains("End Time of Day: ")) {
						mc.replaceText("End Time of Day: ", "End Time of Day: " + errorMsg);
					} else {
						mc.appendPane("End Time of Day: " + errorMsg);
					}
				}
			}
		});

		totalObservationTimeField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Set Main Controller variable and text pane
					if (Integer.parseInt(totalObservationTimeField.getText()) <= 0) {
						throw new InvalidValueException();
					}
					mc.setTotalObservationTime(Integer.parseInt(totalObservationTimeField.getText()));
					if (mc.getTextPane().getText().contains("Total Observation Time: ")) {
						mc.replaceText("Total Observation Time: ",
								"Total Observation Time: " + mc.getTotalObservationTime());
					} else {
						mc.appendPane("Total Observation Time: " + mc.getTotalObservationTime());
					}
				} catch (InvalidValueException ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter a positive integer.]";
					if (mc.getTextPane().getText().contains("Total Observation Time: ")) {
						mc.replaceText("Total Observation Time: ", "Total Observation Time: " + errorMsg);
					} else {
						mc.appendPane("Total Observation Time: " + errorMsg);
					}
				} catch (Exception ex) {
					// Error message due to incorrect type input
					String errorMsg = "[Error! Invalid input received. Please enter an integer for this field.]";
					if (mc.getTextPane().getText().contains("Total Observation Time: ")) {
						mc.replaceText("Total Observation Time: ", "Total Observation Time: " + errorMsg);
					} else {
						mc.appendPane("Total Observation Time: " + errorMsg);
					}
				}
			}
		});

        //// Constructing the panel gridbag layout ////
		
		// Setting spacing on the grid horizontally and vertically
		gc.weighty = 0.1;
		gc.weightx = 0.2;

		// Starting Column 1 //
		
		// First Row
		gc.gridx = 0;
		gc.gridy = 0;
		add(altBandLabel, gc);
		gc.gridx = 1;
		add(altBandField, gc);
		
		// Second Row
		gc.gridx = 0;
		gc.gridy++;
		add(uaSpeedLabel, gc);
		gc.gridx = 1;
		add(uaSpeedField, gc);

		// Third Row
		gc.gridx = 0;
		gc.gridy++;
		add(uaClimbRateLabel, gc);
		gc.gridx = 1;
		add(uaClimbRateField, gc);

		// Fourth Row
		gc.gridx = 0;
		gc.gridy++;
		add(uaHeadingLabel, gc);
		gc.gridx = 1;
		add(uaHeadingField, gc);

		// Fifth Row
		gc.gridx = 0;
		gc.gridy++;
		add(puckRadiusLabel, gc);
		gc.gridx = 1;
		add(puckRadiusField, gc);

		// Sixth Row
		gc.gridx = 0;
		gc.gridy++;
		add(puckHeightLabel, gc);
		gc.gridx = 1;
		add(puckHeightField, gc);

		// Starting Column 2 //
		
		// First Row
		gc.gridx++;
		gc.gridy = 0;
		add(pcolLabel, gc);
		gc.gridx = 3;
		add(pcolField, gc);

		// Second Row
		gc.gridx = 2;
		gc.gridy++;
		add(keepoutVolumeRadiusLabel, gc);
		gc.gridx = 3;
		add(keepoutVolumeRadiusField, gc);

		//Third Row
		gc.gridx = 2;
		gc.gridy++;
		add(keepoutVolumeHeightLabel, gc);
		gc.gridx = 3;
		add(keepoutVolumeHeightField, gc);

		// Fourth Row
		gc.gridx = 2;
		gc.gridy++;
		add(startTODLabel, gc);
		gc.gridx = 3;
		add(startTODField, gc);

		// Fifth Row
		gc.gridx = 2;
		gc.gridy++;
		add(endTODLabel, gc);
		gc.gridx = 3;
		add(endTODField, gc);

		// Sixth Row
		gc.gridx = 2;
		gc.gridy++;
		add(totalObservationTimeLabel, gc);
		gc.gridx = 3;
		add(totalObservationTimeField, gc);
	}

    // Getter and Setter Methods
	public JTextField getAltBandField() {
		return altBandField;
	}

	public JTextField getUaSpeedField() {
		return uaSpeedField;
	}

	public JTextField getUaClimbRateField() {
		return uaClimbRateField;
	}

	public JTextField getUaHeadingField() {
		return uaHeadingField;
	}

	public JTextField getPcolField() {
		return pcolField;
	}

	public JTextField getKeepoutVolumeRadiusField() {
		return keepoutVolumeRadiusField;
	}

	public JTextField getKeepoutVolumeHeightField() {
		return keepoutVolumeHeightField;
	}

	public JTextField getPuckRadiusField() {
		return puckRadiusField;
	}

	public JTextField getPuckHeightField() {
		return puckHeightField;
	}

	public JTextField getStartTODField() {
		return startTODField;
	}

	public JTextField getEndTODField() {
		return endTODField;
	}

	public JTextField getTotalObservationTimeField() {
		return totalObservationTimeField;
	}

	public void setAltBandField(JTextField altBandField) {
		this.altBandField = altBandField;
	}

	public void setUaSpeedField(JTextField uaSpeedField) {
		this.uaSpeedField = uaSpeedField;
	}

	public void setUaClimbRateField(JTextField uaClimbRateField) {
		this.uaClimbRateField = uaClimbRateField;
	}

	public void setUaHeadingField(JTextField uaHeadingField) {
		this.uaHeadingField = uaHeadingField;
	}

	public void setPcolField(JTextField pcolField) {
		this.pcolField = pcolField;
	}

	public void setKeepoutVolumeRadiusField(JTextField keepoutVolumeRadiusField) {
		this.keepoutVolumeRadiusField = keepoutVolumeRadiusField;
	}

	public void setKeepoutVolumeHeightField(JTextField keepoutVolumeHeightField) {
		this.keepoutVolumeHeightField = keepoutVolumeHeightField;
	}

	public void setPuckRadiusField(JTextField puckRadiusField) {
		this.puckRadiusField = puckRadiusField;
	}

	public void setPuckHeightField(JTextField puckHeightField) {
		this.puckHeightField = puckHeightField;
	}

	public void setStartTODField(JTextField startTODField) {
		this.startTODField = startTODField;
	}

	public void setEndTODField(JTextField endTODField) {
		this.endTODField = endTODField;
	}

	public void setTotalObservationTimeField(JTextField totalObservationTimeField) {
		this.totalObservationTimeField = totalObservationTimeField;
	}

	/**
	 * Sets all text fields to "", used in reset options button
	 */
	public void setEmpty() {
		altBandField.setText("");
		uaSpeedField.setText("");
		uaClimbRateField.setText("");
		uaHeadingField.setText("");
		pcolField.setText("");
		keepoutVolumeRadiusField.setText("");
		keepoutVolumeHeightField.setText("");
		puckRadiusField.setText("");
		puckHeightField.setText("");
		startTODField.setText("");
		endTODField.setText("");
		totalObservationTimeField.setText("");
	}

	// GETTERS

	public String getAltBand() {
		return altBandField.getText();
	}

	public String getUaSpeed() {
		return uaSpeedField.getText();
	}

	public String getUaClimbRate() {
		return uaClimbRateField.getText();
	}

	public String getUaHeading() {
		return uaHeadingField.getText();
	}

	public String getPcol() {
		return pcolField.getText();
	}

	public String getKeepoutVolumeRadius() {
		return keepoutVolumeRadiusField.getText();
	}

	public String getKeepoutVolumeHeight() {
		return keepoutVolumeHeightField.getText();
	}

	public String getPuckRadius() {
		return puckRadiusField.getText();
	}

	public String getPuckHeight() {
		return puckHeightField.getText();
	}

	public String getStartTOD() {
		return startTODField.getText();
	}

	public String getEndTOD() {
		return endTODField.getText();
	}

	public String getTotalObservationTime() {
		return totalObservationTimeField.getText();
	}

}

