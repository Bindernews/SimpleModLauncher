package com.github.modlauncher;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.github.modlauncher.gui.MainFrame;
import com.github.modlauncher.gui.Res;

public class Main {

	public static void initAll() throws IOException {
		Res.init();
		OSInfo.init();
	}
	
	public static void main(String[] args) throws IOException {
		try {
			initAll();
		}
		catch (IOException ioe) {
			// todo crash message
		}
		
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
