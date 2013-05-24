package com.github.modlauncher.util;

import java.awt.BorderLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.github.modlauncher.Launchpad;

public class ErrorUtils {

	public static void showException(Exception e, boolean fatal) {
		JDialog jd = new JDialog(Launchpad.i().getFrame(), "Error", true);
		jd.getContentPane().setLayout(new BorderLayout());
		jd.getContentPane().add(new JLabel("An exception was thrown"), BorderLayout.NORTH);
		JTextArea jta = new JTextArea(getExceptionString(e));
		jta.setEditable(false);
		jd.getContentPane().add(jta, BorderLayout.CENTER);
		jd.pack();
		jd.setVisible(true);
	}
	
	public static String getExceptionString(Exception e) {
		StringWriter sw = new StringWriter(500);
		e.printStackTrace(new PrintWriter(sw));
		return sw.getBuffer().toString();
	}

	
}
