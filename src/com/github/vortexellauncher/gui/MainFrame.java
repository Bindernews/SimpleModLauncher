package com.github.vortexellauncher.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.Settings;
import com.github.vortexellauncher.UserPass;
import com.github.vortexellauncher.gui.dialogs.AddModpack;
import com.github.vortexellauncher.gui.dialogs.OptionsGui;
import com.github.vortexellauncher.pack.Modpack;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	public static final ComponentAdapter sizeBoundsListener = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			Dimension min = e.getComponent().getMinimumSize();
			Dimension max = e.getComponent().getMaximumSize();
			int width = clampI(e.getComponent().getWidth(), min.width, max.width);
			int height = clampI(e.getComponent().getHeight(), min.height, max.height);
			e.getComponent().setSize(width, height);
		}
	};
	
	private static int clampI(int value, int min, int max) {
		return value < min ? min : (value > max ? max : value);
	}
	
	private static final String ADD_NEW_STR = "Add New...";

	private JPanel contentPane;
	private HelpTextField txtUsername;
	private HelpTextField txtPassword;
	private JCheckBox chkRemember;
	private JButton btnLogin;
	private JTextField statusBar;
	private JButton btnDebug;
	
	private OptionsGui optionsGui;
	private AddModpack addModpackDialog;
	
	private ArrayListModel<Object> modpackListModel = new ArrayListModel<Object>();
	private JComboBox packSelector;

	public MainFrame() {
		setTitle("Vortexel Launcher");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				Main.attemptExit();
			}
		});
		addComponentListener(sizeBoundsListener);
		setFont(Res.mcfont);
		setBounds(100, 100, 409, 181);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{114, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		packSelector = new JComboBox(modpackListModel);
		packSelector.addItemListener(new ItemListener() {
			Object lastItemSelected = null;
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					lastItemSelected = e.getItem();
				} else {
					if (e.getItem() == ADD_NEW_STR) {
						packSelector.setSelectedItem(lastItemSelected);
						addModpackDialog.showSelf();
					} else {
						Main.settings().setModpackName((String)e.getItem());
					}
				}
			}
		});
		GridBagConstraints gbc_packSelector = new GridBagConstraints();
		gbc_packSelector.gridwidth = 2;
		gbc_packSelector.insets = new Insets(0, 0, 5, 0);
		gbc_packSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_packSelector.gridx = 0;
		gbc_packSelector.gridy = 0;
		contentPane.add(packSelector, gbc_packSelector);
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
		contentPane.add(txtUsername, gbc_txtUsername);
		
		txtPassword = new HelpTextField();
		txtPassword.setEchoChar(HelpTextField.PASSWORD_ECHO);
		txtPassword.setHelpText("Password");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.gridwidth = 2;
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 2;
		contentPane.add(txtPassword, gbc_txtPassword);
		
		chkRemember = new JCheckBox("Remember");
		GridBagConstraints gbc_chkRemember = new GridBagConstraints();
		gbc_chkRemember.insets = new Insets(0, 0, 5, 5);
		gbc_chkRemember.gridx = 0;
		gbc_chkRemember.gridy = 3;
		contentPane.add(chkRemember, gbc_chkRemember);
		
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
		contentPane.add(btnLogin, gbc_btnLogin);
		
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
		gbc_btnOptions.gridy = 5;
		contentPane.add(btnOptions, gbc_btnOptions);
		
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
		contentPane.add(btnDebug, gbc_btnDebug);
		
		//txtUsername.setFont(Res.mcfont.deriveFont(12.0f));
		
		pack();
		
		setMinimumSize(getSize());
		
		// construct everything else
		optionsGui = new OptionsGui(this);
		addModpackDialog = new AddModpack(this, true);
		addModpackDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	
	public void updateModpackList() {
		modpackListModel.clear();
		for(Modpack mp : Main.metaManager().getModpackList()) {
			modpackListModel.add(mp.name);
		}
		modpackListModel.add(ADD_NEW_STR);
		packSelector.setEnabled(true);
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
	public JComboBox getPackSelector() {
		return packSelector;
	}
}
