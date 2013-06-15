package com.github.vortexellauncher.io;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class TextPaneOutputStream extends OutputStream {
	
	private JTextPane textPane;
	private Color textColor = null;
	
	
	public TextPaneOutputStream(JTextPane pane) {
		this.textPane = pane;
	}
	
	public TextPaneOutputStream(JTextPane pane, Color color) {
		textPane = pane;
		textColor = color;
	}

	@Override
	public synchronized void write(int b) throws IOException {
//		changeColor();
//		textArea.append(Character.toString((char)b));
//		changeColor();
		write(Character.toString((char)b));
	}
	
	@Override
	public synchronized void write(byte[] b) throws IOException {
//		changeColor();
//		textArea.append(new String(b));
//		changeColor();
		write(new String(b));
	}
	
	@Override
	public synchronized void write(byte[] b, int off, int len) throws IOException {
//		changeColor();
//		textArea.append(new String(b, off, len));
//		changeColor();
		write(new String(b, off, len));
	}
	
	public synchronized void write(final String str) throws IOException {
		Runnable r = new Runnable() {
			public void run() {
				StyleContext sc = StyleContext.getDefaultStyleContext();
				AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, textColor);
				int len = textPane.getDocument().getLength();
				textPane.setCaretPosition(len);
				textPane.setCharacterAttributes(aset, false);
				boolean canEdit = textPane.isEditable();
				textPane.setEditable(true);
				textPane.replaceSelection(str);
				textPane.setEditable(canEdit);
			}
		};
		SwingUtilities.invokeLater(r);
		//r.run();
	}
}
