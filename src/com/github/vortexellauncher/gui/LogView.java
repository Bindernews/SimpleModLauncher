package com.github.vortexellauncher.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.io.MultiOutputStream;
import com.github.vortexellauncher.io.TextAreaOutputStream;
import com.github.vortexellauncher.util.DebugUtils;

public class LogView extends JFrame implements ClipboardOwner {
	private static final long serialVersionUID = 5985510213911888380L;

	private static PrintStream normalOut = System.out,
			normalErr = System.err;
	private PrintStream outPrinter,
						errPrinter;
	
	private JTextArea textArea;
	private LogView self;

	public LogView() {
		super("Log");
		self = this;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				self.setVisible(false);
				Main.attemptExit();
			}
		});
		
		textArea = new JTextArea();
		textArea.setFont(Font.decode(Font.MONOSPACED + "-12"));
		textArea.setRows(2);
		textArea.setTabSize(4);
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnCopyToClipboard = new JButton("Copy to Clipboard");
		btnCopyToClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setClipboard(textArea.getText());
			}
		});
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(btnCopyToClipboard, BorderLayout.WEST);
		
		JButton btnPrintSystemInfo = new JButton("Print System Info");
		btnPrintSystemInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DebugUtils.printJavaProperties(System.out);
				DebugUtils.printEnvironment(System.out);
			}
		});
		panel.add(btnPrintSystemInfo, BorderLayout.EAST);
		
		pack();
		setupStreams();
	}
	
	private void setupStreams() {
		outPrinter = new PrintStream(new MultiOutputStream(new TextAreaOutputStream(textArea, Color.BLACK), normalOut));
		errPrinter = new PrintStream(new MultiOutputStream(new TextAreaOutputStream(textArea, Color.RED), normalErr));
	}

	public void addRedirect() {
		System.setOut(outPrinter);
		System.setErr(errPrinter);
	}
	
	public void setClipboard(String text) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection ss = new StringSelection(text);
		clipboard.setContents(ss, this);
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {		
	}

}
