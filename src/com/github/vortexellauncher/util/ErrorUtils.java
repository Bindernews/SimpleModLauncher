package com.github.vortexellauncher.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.gui.Res;

public class ErrorUtils {
	
	public static void showErrorGui(Window owner, String message, String error, final boolean attemptExit) {
		JDialog d = new JDialog(owner, "Error", Dialog.ModalityType.APPLICATION_MODAL);
		d.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (attemptExit)
					Main.attemptExit();
			}
		});
		JLabel lbl = new JLabel(message);
		lbl.setIcon(Res.iconError);
		JTextArea jta = new JTextArea(error, 6, 16);
		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);
		jta.setEnabled(false);
		jta.setDisabledTextColor(Color.BLACK);
		d.getContentPane().add(lbl, BorderLayout.NORTH);
		d.getContentPane().add(jta, BorderLayout.CENTER);
		d.pack();
		d.setVisible(true);
	}
	
	public static void printException(Throwable e, boolean fatal) {
		printException(null, e, fatal);
	}
	
	public static void printException(String message, Throwable e, boolean fatal) {
		if (message != null)
			System.err.println(message);
		e.printStackTrace();
		Main.logView().setVisible(true);
	}
	
	public static String getExceptionString(Throwable e) {
		StringWriter sw = new StringWriter(500);
		e.printStackTrace(new PrintWriter(sw));
		return sw.getBuffer().toString();
	}

	
}
