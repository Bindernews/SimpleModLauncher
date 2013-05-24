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
import javax.swing.JTextField;

import com.github.modlauncher.Launchpad;
import com.github.modlauncher.UserPass;

public class LoginPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private HelpTextField txtUsername;
	private HelpTextField txtPassword;
	private JCheckBox chkRemember;
	private JTextField statusBar;
	private JButton loginButton;

	/**
	 * Create the panel.
	 */
	public LoginPanel() {

		setMaximumSize(new Dimension(32767, 200));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {94, 70, 0};
		gridBagLayout.rowHeights = new int[] {24, 24, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		txtUsername = new HelpTextField();
		txtUsername.setHelpText("Username");
		txtUsername.setEchoChar((char)0);
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
		
		chkRemember = new JCheckBox("Remember");
		GridBagConstraints gbc_chkRemember = new GridBagConstraints();
		gbc_chkRemember.insets = new Insets(0, 0, 5, 5);
		gbc_chkRemember.gridx = 0;
		gbc_chkRemember.gridy = 2;
		add(chkRemember, gbc_chkRemember);
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Launchpad.i().attemptLogin(txtUsername.getText(), txtPassword.getText(), chkRemember.isSelected());
			}
		});
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setAlignmentY(Component.TOP_ALIGNMENT);
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.insets = new Insets(0, 0, 5, 0);
		gbc_loginButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginButton.gridx = 1;
		gbc_loginButton.gridy = 2;
		add(loginButton, gbc_loginButton);
		
		statusBar = new JTextField(" ");
		statusBar.setEnabled(false);
		statusBar.setEditable(false);
		GridBagConstraints gbc_statusBar = new GridBagConstraints();
		gbc_statusBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_statusBar.gridwidth = 2;
		gbc_statusBar.insets = new Insets(0, 0, 0, 5);
		gbc_statusBar.gridx = 0;
		gbc_statusBar.gridy = 3;
		add(statusBar, gbc_statusBar);
		
		/*
		Font mcFont = Res.mcfont.deriveFont(16.0f);
		txtUsername.setFont(mcFont);
		txtPassword.setFont(mcFont);
		*/
	}
	
	public String getUsername() {
		return txtUsername.getText();
	}
	
	public String getPassword() {
		return txtPassword.getText();
	}
	public JTextField getStatusBar() {
		return statusBar;
	}
	public void setStatus(String text) {
		statusBar.setText(text);
	}
	public void setUserPass(String user, String pass) {
		txtUsername.setText(user);
		txtPassword.setText(pass);
	}
	public void setUserPass(UserPass up) {
		setUserPass(up.getUsername(), up.getPassword());
	}
	
	public void setStatusIcon(StatusIcon ico) {
		//TODO implement
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		txtUsername.setEnabled(enabled);
		txtPassword.setEnabled(enabled);
		loginButton.setEnabled(enabled);
	}
	public JCheckBox getChkRemember() {
		return chkRemember;
	}
}
