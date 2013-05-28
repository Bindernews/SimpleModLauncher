package com.github.vortexellauncher.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import com.github.vortexellauncher.Launchpad;
import com.github.vortexellauncher.UserPass;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private HelpTextField txtUsername;
	private HelpTextField txtPassword;
	private JCheckBox chkRemember;
	private JButton btnLogin;
	private JTextField statusBar;
	
	private OptionsGui optionsGui;

	public MainFrame() {
		setTitle("Vortexel Launcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFont(Res.mcfont);
		setBounds(100, 100, 277, 241);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{114, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		txtUsername = new HelpTextField();
		txtUsername.setEchoChar('\0');
		txtUsername.setHelpText("Username");
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.gridwidth = 2;
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 0;
		gbc_txtUsername.gridy = 0;
		contentPane.add(txtUsername, gbc_txtUsername);
		
		txtPassword = new HelpTextField();
		txtPassword.setEchoChar(HelpTextField.PASSWORD_ECHO);
		txtPassword.setHelpText("Password");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.gridwidth = 2;
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 1;
		contentPane.add(txtPassword, gbc_txtPassword);
		
		chkRemember = new JCheckBox("Remember");
		GridBagConstraints gbc_chkRemember = new GridBagConstraints();
		gbc_chkRemember.insets = new Insets(0, 0, 5, 5);
		gbc_chkRemember.gridx = 0;
		gbc_chkRemember.gridy = 2;
		contentPane.add(chkRemember, gbc_chkRemember);
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Launchpad.i().attemptLogin(getUsername(), getPassword(), chkRemember.isSelected());
			}
		});
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.insets = new Insets(0, 0, 5, 0);
		gbc_btnLogin.gridx = 1;
		gbc_btnLogin.gridy = 2;
		contentPane.add(btnLogin, gbc_btnLogin);
		
		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionsGui.setVisible(true);
			}
		});
		GridBagConstraints gbc_btnOptions = new GridBagConstraints();
		gbc_btnOptions.gridwidth = 2;
		gbc_btnOptions.insets = new Insets(0, 0, 5, 0);
		gbc_btnOptions.anchor = GridBagConstraints.NORTH;
		gbc_btnOptions.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOptions.gridx = 0;
		gbc_btnOptions.gridy = 3;
		contentPane.add(btnOptions, gbc_btnOptions);
		
		statusBar = new JTextField();
		statusBar.setEnabled(false);
		statusBar.setEditable(false);
		GridBagConstraints gbc_statusBar = new GridBagConstraints();
		gbc_statusBar.gridwidth = 2;
		gbc_statusBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_statusBar.gridx = 0;
		gbc_statusBar.gridy = 4;
		contentPane.add(statusBar, gbc_statusBar);
		statusBar.setColumns(10);
		
		pack();
		
		// construct everything else
		optionsGui = new OptionsGui(this);
	}
	
	public String getUsername() {
		return txtUsername.getText();
	}
	
	public String getPassword() {
		return txtPassword.getText();
	}
	
	public void setUserPass(String user, String pass) {
		txtUsername.setText(user);
		txtPassword.setText(pass);
	}
	public void setUserPass(UserPass up) {
		setUserPass(up.getUsername(), up.getPassword());
	}
	public void setLoginStatus(String status) {
		statusBar.setText(status);
	}
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}
	public JCheckBox getChkRemember() {
		return chkRemember;
	}
	public OptionsGui getOptionsGui() {
		return optionsGui;
	}
}
