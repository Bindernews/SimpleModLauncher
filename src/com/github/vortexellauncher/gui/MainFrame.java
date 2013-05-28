package com.github.vortexellauncher.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private LoginPanel loginPanel;

	public MainFrame() {
		setTitle("Vortexel Launcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFont(Res.mcfont);
		setBounds(100, 100, 176, 94);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		loginPanel = new LoginPanel();
		contentPane.add(loginPanel, BorderLayout.CENTER);
		
		JButton btnOptions = new JButton("Options");
		contentPane.add(btnOptions, BorderLayout.NORTH);
		
		pack();
	}
	
	public void setLoginStatus(String status) {
		loginPanel.getStatusBar().setText(status);
	}

	public LoginPanel getLoginPanel() {
		return loginPanel;
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		loginPanel.setEnabled(enabled);
	}
}
