package com.github.vortexellauncher.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.text.NumberFormatter;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.Settings;
import com.github.vortexellauncher.gui.ItemClickListener;

public class OptionsPanel extends JPanel {
	
	private static final String NEW_SETTINGS_STR = "New...";
	private static final String DEL_SETTINGS_STR = "Delete...";
	
	private static final long serialVersionUID = 1L;
	private JTextField vmargsField;
	private JFormattedTextField ramMaxField;
	private JTextField socksHost;
	private JCheckBox chkDoNotValidate;

	public OptionsPanel() {
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
//		setLocationByPlatform(true);
//		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{76, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		add(comboBox, gbc_comboBox);
		
		ItemClickListener icl = new ItemClickListener();
		icl.addWatchedItem(NEW_SETTINGS_STR);
		icl.addWatchedItem(DEL_SETTINGS_STR);
		icl.addClickListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getItem() == NEW_SETTINGS_STR) {
					//TODO implement
				} else if (e.getItem() == DEL_SETTINGS_STR) {
					//TODO implement
				}
			}
		});
		comboBox.addItemListener(icl);
		
		JLabel lblJvmArgs = new JLabel("JVM Args");
		GridBagConstraints gbc_lblJvmArgs = new GridBagConstraints();
		gbc_lblJvmArgs.insets = new Insets(0, 0, 5, 5);
		gbc_lblJvmArgs.anchor = GridBagConstraints.EAST;
		gbc_lblJvmArgs.gridx = 0;
		gbc_lblJvmArgs.gridy = 1;
		add(lblJvmArgs, gbc_lblJvmArgs);
		
		vmargsField = new JTextField();
		lblJvmArgs.setLabelFor(vmargsField);
		GridBagConstraints gbc_vmargsField = new GridBagConstraints();
		gbc_vmargsField.insets = new Insets(0, 0, 5, 0);
		gbc_vmargsField.anchor = GridBagConstraints.NORTH;
		gbc_vmargsField.fill = GridBagConstraints.HORIZONTAL;
		gbc_vmargsField.gridx = 1;
		gbc_vmargsField.gridy = 1;
		add(vmargsField, gbc_vmargsField);
		vmargsField.setColumns(10);
		
		JLabel lblMaxRam = new JLabel("Max Ram");
		GridBagConstraints gbc_lblMaxRam = new GridBagConstraints();
		gbc_lblMaxRam.anchor = GridBagConstraints.EAST;
		gbc_lblMaxRam.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxRam.gridx = 0;
		gbc_lblMaxRam.gridy = 2;
		add(lblMaxRam, gbc_lblMaxRam);
		
		ramMaxField = new JFormattedTextField(new NumberFormatter());
		lblMaxRam.setLabelFor(ramMaxField);
		ramMaxField.setText("768");
		GridBagConstraints gbc_ramMaxField = new GridBagConstraints();
		gbc_ramMaxField.insets = new Insets(0, 0, 5, 0);
		gbc_ramMaxField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ramMaxField.gridx = 1;
		gbc_ramMaxField.gridy = 2;
		add(ramMaxField, gbc_ramMaxField);
		
		JLabel lblSocksHost = new JLabel("Socks Host");
		GridBagConstraints gbc_lblSocksHost = new GridBagConstraints();
		gbc_lblSocksHost.anchor = GridBagConstraints.EAST;
		gbc_lblSocksHost.insets = new Insets(0, 0, 5, 5);
		gbc_lblSocksHost.gridx = 0;
		gbc_lblSocksHost.gridy = 3;
		add(lblSocksHost, gbc_lblSocksHost);
		
		socksHost = new JTextField();
		lblSocksHost.setLabelFor(socksHost);
		GridBagConstraints gbc_socksHost = new GridBagConstraints();
		gbc_socksHost.insets = new Insets(0, 0, 5, 0);
		gbc_socksHost.fill = GridBagConstraints.HORIZONTAL;
		gbc_socksHost.gridx = 1;
		gbc_socksHost.gridy = 3;
		add(socksHost, gbc_socksHost);
		socksHost.setColumns(10);
		
		JLabel lblSocksPort = new JLabel("Socks Port");
		GridBagConstraints gbc_lblSocksPort = new GridBagConstraints();
		gbc_lblSocksPort.anchor = GridBagConstraints.EAST;
		gbc_lblSocksPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblSocksPort.gridx = 0;
		gbc_lblSocksPort.gridy = 4;
		add(lblSocksPort, gbc_lblSocksPort);
		
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(false);
		JFormattedTextField socksPort = new JFormattedTextField(new NumberFormatter(nf));
		lblSocksPort.setLabelFor(socksPort);
		GridBagConstraints gbc_socksPort = new GridBagConstraints();
		gbc_socksPort.insets = new Insets(0, 0, 5, 0);
		gbc_socksPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_socksPort.gridx = 1;
		gbc_socksPort.gridy = 4;
		add(socksPort, gbc_socksPort);
		
		chkDoNotValidate = new JCheckBox("Do not validate");
		GridBagConstraints gbc_chkDoNotValidate = new GridBagConstraints();
		gbc_chkDoNotValidate.gridwidth = 2;
		gbc_chkDoNotValidate.gridx = 0;
		gbc_chkDoNotValidate.gridy = 5;
		add(chkDoNotValidate, gbc_chkDoNotValidate);
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
		Settings sets = Main.settings();
		sets.setRamMax(getRamMax());
		sets.setVMParams(getVmargs());
		sets.setShouldValidate(!chkDoNotValidate.isSelected());
	}
	public JCheckBox getChkDoNotValidate() {
		return chkDoNotValidate;
	}
}
