package com.github.vortexellauncher.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

import com.github.vortexellauncher.Launch;
import com.github.vortexellauncher.Settings;

public class OptionsGui extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField vmargsField;
	private JFormattedTextField ramMaxField;

	public OptionsGui(Frame owner) {
		super(owner, "Options", true);
		setLocationByPlatform(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblJvmArgs = new JLabel("JVM Args");
		GridBagConstraints gbc_lblJvmArgs = new GridBagConstraints();
		gbc_lblJvmArgs.insets = new Insets(0, 0, 5, 5);
		gbc_lblJvmArgs.anchor = GridBagConstraints.EAST;
		gbc_lblJvmArgs.gridx = 0;
		gbc_lblJvmArgs.gridy = 0;
		getContentPane().add(lblJvmArgs, gbc_lblJvmArgs);
		
		vmargsField = new JTextField();
		GridBagConstraints gbc_vmargsField = new GridBagConstraints();
		gbc_vmargsField.insets = new Insets(0, 0, 5, 0);
		gbc_vmargsField.anchor = GridBagConstraints.NORTH;
		gbc_vmargsField.fill = GridBagConstraints.HORIZONTAL;
		gbc_vmargsField.gridx = 1;
		gbc_vmargsField.gridy = 0;
		getContentPane().add(vmargsField, gbc_vmargsField);
		vmargsField.setColumns(10);
		
		JLabel lblMaxRam = new JLabel("Max Ram");
		GridBagConstraints gbc_lblMaxRam = new GridBagConstraints();
		gbc_lblMaxRam.anchor = GridBagConstraints.EAST;
		gbc_lblMaxRam.insets = new Insets(0, 0, 0, 5);
		gbc_lblMaxRam.gridx = 0;
		gbc_lblMaxRam.gridy = 1;
		getContentPane().add(lblMaxRam, gbc_lblMaxRam);
		
		ramMaxField = new JFormattedTextField(new NumberFormatter());
		ramMaxField.setText("768");
		GridBagConstraints gbc_ramMaxField = new GridBagConstraints();
		gbc_ramMaxField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ramMaxField.gridx = 1;
		gbc_ramMaxField.gridy = 1;
		getContentPane().add(ramMaxField, gbc_ramMaxField);
		
		pack();
	}
	
	public JTextField getVmargsField() {
		return vmargsField;
	}
	
	public String getVmargs() {
		return vmargsField.getText().trim();
	}
	
	public int getRamMax() {
		try {
			return Integer.parseInt(ramMaxField.getText());
		} catch (NumberFormatException e) {
			return 756;
		}
	}
	
	public void updateSettings() {
		Settings sets = Launch.getSettings();
		sets.setRamMax(getRamMax());
		sets.setVMParams(Arrays.asList(getVmargs().split(" ")));
	}
}
