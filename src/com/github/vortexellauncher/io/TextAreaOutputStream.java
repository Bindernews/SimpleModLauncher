package com.github.vortexellauncher.io;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {
	
	private JTextArea textPane;
	
	
	public TextAreaOutputStream(JTextArea pane) {
		this.textPane = pane;
	}

	@Override
	public synchronized void write(int b) throws IOException {
		write(Character.toString((char)b));
	}
	
	@Override
	public synchronized void write(byte[] b) throws IOException {
		write(new String(b));
	}
	
	@Override
	public synchronized void write(byte[] b, int off, int len) throws IOException {
		write(new String(b, off, len));
	}
	
	public synchronized void write(final String str) throws IOException {
		textPane.append(str);
	}
}
