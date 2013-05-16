package com.github.modlauncher.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.github.modlauncher.workers.LoginWorker;

public class LoginPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private HelpTextField txtUsername;
	private HelpTextField txtPassword;
	private JCheckBox chckbxRemember;
	private LoginWorker loginWorker;

	/**
	 * Create the panel.
	 */
	public LoginPanel() {

		setMaximumSize(new Dimension(32767, 200));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {94, 70, 0};
		gridBagLayout.rowHeights = new int[] {24, 24, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		txtUsername = new HelpTextField();
		txtUsername.setHelpText("Username");
		txtUsername.setEchoChar((char)0);
		txtUsername.setToolTipText("");
		txtUsername.setName("");
		txtUsername.setMaximumSize(new Dimension(2147483647, 28));
		txtUsername.setAlignmentY(Component.TOP_ALIGNMENT);
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.gridwidth = 2;
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.gridx = 0;
		gbc_txtUsername.gridy = 0;
		add(txtUsername, gbc_txtUsername);
		txtUsername.setColumns(10);
		
		
		txtPassword = new HelpTextField();
		txtPassword.setHelpText("Password");
		txtPassword.setEchoChar(HelpTextField.PASSWORD_ECHO);
		txtPassword.setMaximumSize(new Dimension(2147483647, 28));
		txtPassword.setAlignmentY(Component.TOP_ALIGNMENT);
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.gridwidth = 2;
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 1;
		add(txtPassword, gbc_txtPassword);
		
		chckbxRemember = new JCheckBox("Remember");
		GridBagConstraints gbc_chckbxRemember = new GridBagConstraints();
		gbc_chckbxRemember.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxRemember.gridx = 0;
		gbc_chckbxRemember.gridy = 2;
		add(chckbxRemember, gbc_chckbxRemember);
		
		JButton btnNewButton_1 = new JButton("Login");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginWorker = new LoginWorker(txtUsername.getText(), txtPassword.getText());
				loginWorker.execute();
			}
		});
		btnNewButton_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNewButton_1.setAlignmentY(Component.TOP_ALIGNMENT);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 2;
		add(btnNewButton_1, gbc_btnNewButton_1);

		/*
		Font mcFont = Res.mcfont.deriveFont(16.0f);
		txtUsername.setFont(mcFont);
		txtPassword.setFont(mcFont);
		*/
	}

}
