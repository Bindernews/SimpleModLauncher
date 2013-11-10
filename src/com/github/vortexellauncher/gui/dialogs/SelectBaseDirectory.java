package com.github.vortexellauncher.gui.dialogs;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.github.vortexellauncher.OSInfo;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SelectBaseDirectory extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JTextField textField;
	private SelectBaseDirectory self;

	public SelectBaseDirectory() {
		self = this;
		setBounds(100, 100, 231, 106);
		
		textField = new JTextField(OSInfo.defaultDataDir());
		getContentPane().add(textField, BorderLayout.CENTER);
		textField.setColumns(14);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(textField.getText());
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = jfc.showOpenDialog(self);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					textField.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		getContentPane().add(btnBrowse, BorderLayout.EAST);
		
		JLabel lblSelectBaseInstall = new JLabel("Select base install directory...");
		getContentPane().add(lblSelectBaseInstall, BorderLayout.NORTH);

		pack();
	}

}
