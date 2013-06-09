package com.github.vortexellauncher.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ExceptionDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private Exception except;
	private JLabel msg = new JLabel();
	private JButton btnOk = new JButton("Ok");
	private StringWriter strWriter = new StringWriter(500);
	
	public ExceptionDialog(Window owner, String title, Exception ex) {
		super(owner, title);
		construct();
		setException(ex);
	}
	
	private void construct() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(msg, BorderLayout.CENTER);
		getContentPane().add(btnOk, BorderLayout.SOUTH);
		btnOk.addActionListener(this);
	}
	
	public Exception getException() {
		return except;
	}
	
	public void setException(Exception ex) {
		except = ex;
		strWriter.getBuffer().delete(0, strWriter.getBuffer().length());
		except.printStackTrace(new PrintWriter(strWriter));
		msg.setText(strWriter.getBuffer().toString());
		pack();
	}
	
	public void addOkayListener(ActionListener l) {
		btnOk.addActionListener(l);
	}
	
	public void removeOkayListener(ActionListener l) {
		btnOk.removeActionListener(l);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
	}
}
