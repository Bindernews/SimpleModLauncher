package com.github.vortexellauncher.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import com.github.vortexellauncher.Main;

public class GuiUtils {

	
	public static void reportMessage(String title, String message) {
		JDialog jd = new JDialog(Main.frame(), title, true);
		jd.setResizable(false);
		
		JTextArea jta = new JTextArea(message);
		jta.setBackground(new Color(255,255,210));
		jta.setFont(Font.decode("Arial-PLAIN-16"));
		jta.setEditable(false);
		jd.getContentPane().add(jta);
		
		jd.pack();
		jd.setVisible(true);
	}
}
