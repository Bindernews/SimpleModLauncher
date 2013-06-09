package com.github.vortexellauncher.io;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {
	
	private JTextArea textArea;
	private Color textColor = null;
	private Color colorSave = null;
	
	/**
	 * 
	 * @param textArea
	 */
	public TextAreaOutputStream(JTextArea textArea) {
		this.textArea = textArea;
	}
	
	/**
	 * 
	 * @param area
	 * @param color The color to print the text as
	 */
	public TextAreaOutputStream(JTextArea area, Color color) {
		textArea = area;
		textColor = color;
	}

	@Override
	public synchronized void write(int b) throws IOException {
		changeColor();
		textArea.append(Character.toString((char)b));
		changeColor();
	}
	
	@Override
	public synchronized void write(byte[] b) throws IOException {
		changeColor();
		textArea.append(new String(b));
		changeColor();
	}
	
	@Override
	public synchronized void write(byte[] b, int off, int len) throws IOException {
		changeColor();
		textArea.append(new String(b, off, len));
		changeColor();
	}

	protected void changeColor() {
		if (textColor != null) {
			if (colorSave == null) {
				colorSave = textArea.getForeground();
				textArea.setForeground(textColor);
			} else {
				textArea.setForeground(colorSave);
				colorSave = null;
			}
		}
	}
}
