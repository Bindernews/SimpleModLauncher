package com.github.vortexellauncher.gui.panels;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.Settings;
import com.github.vortexellauncher.gui.ArrayListModel;
import com.github.vortexellauncher.gui.HelpTextField;
import com.github.vortexellauncher.gui.ItemClickListener;
import com.github.vortexellauncher.gui.MainFrame;
import com.github.vortexellauncher.pack.Modpack;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	
	private static final String ADD_NEW_STR = "Add New...";

	private HelpTextField txtUsername;
	private HelpTextField txtPassword;
	private JCheckBox chkRemember;
	private JButton btnLogin;
	private JTextField statusBar;
	private JButton btnDebug;
	
	private ArrayListModel<Object> modpackListModel = new ArrayListModel<Object>();
	private JComboBox packSelector;
	private ItemClickListener clickListener;
	
	private MainFrame mainFrame;
	
	public MainPanel() {
		this(null);
	}
	public MainPanel(MainFrame frame) {
		mainFrame = frame;
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{114, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gbl_contentPane);
		
		clickListener = new ItemClickListener().
				addWatchedItem(ADD_NEW_STR).
				addClickListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if (e.getItem() == ADD_NEW_STR) {
					mainFrame.getAddModpackDialog().showSelf();
				}
			}
		});
		packSelector = new JComboBox(modpackListModel);
		packSelector.addItemListener(clickListener);
		packSelector.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Main.settings().setModpackName((String)e.getItem());
				}
			}
		});
		GridBagConstraints gbc_packSelector = new GridBagConstraints();
		gbc_packSelector.gridwidth = 2;
		gbc_packSelector.insets = new Insets(0, 0, 5, 0);
		gbc_packSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_packSelector.gridx = 0;
		gbc_packSelector.gridy = 0;
		add(packSelector, gbc_packSelector);
		packSelector.setEnabled(false);
		
		txtUsername = new HelpTextField();
		txtUsername.setEchoChar('\0');
		txtUsername.setHelpText("Username");
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.gridwidth = 2;
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 0;
		gbc_txtUsername.gridy = 1;
		add(txtUsername, gbc_txtUsername);
		
		txtPassword = new HelpTextField();
		txtPassword.setEchoChar(HelpTextField.PASSWORD_ECHO);
		txtPassword.setHelpText("Password");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.gridwidth = 2;
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 2;
		add(txtPassword, gbc_txtPassword);
		
		chkRemember = new JCheckBox("Remember");
		GridBagConstraints gbc_chkRemember = new GridBagConstraints();
		gbc_chkRemember.insets = new Insets(0, 0, 5, 5);
		gbc_chkRemember.gridx = 0;
		gbc_chkRemember.gridy = 3;
		add(chkRemember, gbc_chkRemember);
		
		btnLogin = new JButton("Login");
		btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.launchpad().attemptLogin(getUsername(), getPassword(), chkRemember.isSelected());
			}
		});
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.insets = new Insets(0, 0, 5, 0);
		gbc_btnLogin.gridx = 1;
		gbc_btnLogin.gridy = 3;
		add(btnLogin, gbc_btnLogin);
		
		statusBar = new JTextField();
		statusBar.setEnabled(false);
		statusBar.setEditable(false);
		GridBagConstraints gbc_statusBar = new GridBagConstraints();
		gbc_statusBar.insets = new Insets(0, 0, 5, 0);
		gbc_statusBar.gridwidth = 2;
		gbc_statusBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_statusBar.gridx = 0;
		gbc_statusBar.gridy = 4;
		add(statusBar, gbc_statusBar);
		statusBar.setColumns(10);
		
		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.getOptionsDialog().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnOptions = new GridBagConstraints();
		gbc_btnOptions.gridwidth = 2;
		gbc_btnOptions.insets = new Insets(0, 0, 5, 0);
		gbc_btnOptions.anchor = GridBagConstraints.NORTH;
		gbc_btnOptions.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOptions.gridx = 0;
		gbc_btnOptions.gridy = 5;
		add(btnOptions, gbc_btnOptions);
		
		btnDebug = new JButton("Debug");
		btnDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.setDebugMode(true);
				Main.logView().setVisible(true);
			}
		});
		GridBagConstraints gbc_btnDebug = new GridBagConstraints();
		gbc_btnDebug.gridwidth = 2;
		gbc_btnDebug.insets = new Insets(0, 0, 5, 0);
		gbc_btnDebug.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDebug.gridx = 0;
		gbc_btnDebug.gridy = 6;
		add(btnDebug, gbc_btnDebug);
		
		//txtUsername.setFont(Res.mcfont.deriveFont(12.0f));
	}
	
	public String getUsername() {
		return txtUsername.getText();
	}
	public String getPassword() {
		return txtPassword.getText();
	}
	public JCheckBox getChkRemember() {
		return chkRemember;
	}
	public JComboBox getPackSelector() {
		return packSelector;
	}
	
	public void setLoginStatus(String status) {
		statusBar.setText(status);
	}
	
	public void setUserPass(String user, String pass) {
		txtUsername.setText(user);
		txtPassword.setText(pass);
	}

	public void updateModpackList() {
		modpackListModel.clear();
		for(Modpack mp : Main.metaManager().getModpackList()) {
			modpackListModel.add(mp.name);
		}
		modpackListModel.add(ADD_NEW_STR);
		packSelector.setEnabled(true);
	}

	public void updateModpackName() {
		for (int i=0; i<packSelector.getModel().getSize(); i++) {
			String s = (String)packSelector.getModel().getElementAt(i);
			if (s.equals(Main.settings().getModpackName())) {
				packSelector.setSelectedItem(Main.settings().getModpackName());
				break;
			}
		}
	}
}
