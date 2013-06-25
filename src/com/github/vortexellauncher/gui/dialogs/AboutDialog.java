package com.github.vortexellauncher.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.github.vortexellauncher.Main;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {
	private JTextPane textPane;

	public AboutDialog(Frame owner) {
		super(owner, "About", true);
		setBounds(100, 100, 450, 300);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		try {
			createAboutDocument(textPane.getStyledDocument());
		} catch (BadLocationException e) {
		}
		textPane.setEditable(false);
		textPane.setBorder(null);
		getContentPane().add(textPane, BorderLayout.CENTER);
	}
	
	public static void createAboutDocument(StyledDocument doc) throws BadLocationException {
		StringBuilder sb = new StringBuilder();
		sb.append("Vortexel Launcher\n"
				+ "programmed by: Binder News\n"
				+ "icons by: DJToast3\n"
				+ "\n");
		sb.append("version: ").append(Main.VERSION.toString()).append("\n");
		
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Alignment, StyleConstants.ALIGN_CENTER);
		doc.insertString(doc.getLength(), sb.toString(), aset);
	}

}
