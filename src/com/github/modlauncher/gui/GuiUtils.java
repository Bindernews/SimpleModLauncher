package com.github.modlauncher.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import com.github.modlauncher.Launchpad;

public class GuiUtils {

	
	public static void reportMessage(String title, String message) {
		JDialog jd = new JDialog(Launchpad.i().getFrame(), title, true);
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
