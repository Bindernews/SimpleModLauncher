package com.github.vortexellauncher.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import com.github.vortexellauncher.Main;

public class GuiUtils {

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
