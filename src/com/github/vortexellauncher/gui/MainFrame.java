package com.github.vortexellauncher.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.Settings;
import com.github.vortexellauncher.UserPass;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private HelpTextField txtUsername;
	private HelpTextField txtPassword;
	private JCheckBox chkRemember;
	private JButton btnLogin;
	private JTextField statusBar;
	
	private OptionsGui optionsGui;
	private JButton btnDebug;

	public MainFrame() {
		setTitle("Vortexel Launcher");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				Main.attemptExit();
			}
		});
		setFont(Res.mcfont);
		setBounds(100, 100, 279, 267);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{114, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
				Main.launchpad().attemptLogin(getUsername(), getPassword(), chkRemember.isSelected());
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
		gbc_statusBar.insets = new Insets(0, 0, 5, 0);
		gbc_statusBar.gridwidth = 2;
		gbc_statusBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_statusBar.gridx = 0;
		gbc_statusBar.gridy = 4;
		contentPane.add(statusBar, gbc_statusBar);
		statusBar.setColumns(10);
		
		btnDebug = new JButton("Debug");
		btnDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.setDebugMode(true);
				Main.logView().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnDebug = new GridBagConstraints();
		gbc_btnDebug.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDebug.gridwidth = 2;
		gbc_btnDebug.insets = new Insets(0, 0, 0, 5);
		gbc_btnDebug.gridx = 0;
		gbc_btnDebug.gridy = 5;
		contentPane.add(btnDebug, gbc_btnDebug);
		
		//txtUsername.setFont(Res.mcfont.deriveFont(12.0f));
		
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
