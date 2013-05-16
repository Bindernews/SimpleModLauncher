package com.github.modlauncher.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	
	private static MainFrame instance;

	public MainFrame() {
		instance = this;
		setTitle("Vortexel Launcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFont(Res.mcfont);
		setBounds(100, 100, 176, 94);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		LoginPanel panel = new LoginPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JButton btnOptions = new JButton("Options");
		contentPane.add(btnOptions, BorderLayout.NORTH);
		
		pack();
	}
	
	public static MainFrame i() {
		return instance;
	}

}
