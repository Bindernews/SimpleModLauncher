package com.github.vortexellauncher.gui.dialogs;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.github.vortexellauncher.gui.panels.OptionsPanel;

@SuppressWarnings("serial")
public class OptionsDialog extends JDialog {

	private OptionsPanel panel;

	public OptionsDialog(Frame f) {
		super(f, "Options", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		panel = new OptionsPanel();
		setContentPane(panel);
		panel.getBtnOk().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			panel.updateFromSettings();
		}
	}

}
