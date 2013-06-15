package com.github.vortexellauncher.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.io.MultiOutputStream;
import com.github.vortexellauncher.io.TextPaneOutputStream;
import com.github.vortexellauncher.util.DebugUtils;

public class LogView extends JFrame implements ClipboardOwner {
	private static final long serialVersionUID = 5985510213911888380L;

	private static PrintStream normalOut = System.out, normalErr = System.err;
	private PrintStream outPrinter, errPrinter;

	private JTextPane text;
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

		text = new JTextPane();
		text.setFont(Font.decode(Font.MONOSPACED + "-12"));
		text.setBackground(Color.WHITE);
		text.setEditable(false);
		
		JPanel p = new JPanel(new BorderLayout());
		p.add(text, BorderLayout.CENTER);
		JScrollPane jsp = new JScrollPane(p);
		jsp.setPreferredSize(new Dimension(6, 80));
		getContentPane().add(jsp, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		JButton btnCopyToClipboard = new JButton("Copy to Clipboard");
		btnCopyToClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setClipboard(text.getText());
			}
		});
		buttonPanel.setLayout(new BorderLayout(0, 0));
		buttonPanel.add(btnCopyToClipboard, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmPrintSystemInfo = new JMenuItem("Print System Info");
		mntmPrintSystemInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.getProperties().list(System.out);
				System.out.println();
				DebugUtils.printEnvironment(System.out);
			}
		});
		mnTools.add(mntmPrintSystemInfo);

		pack();
		setupStreams();
	}

	private void setupStreams() {
		outPrinter = new PrintStream(new MultiOutputStream(
				new TextPaneOutputStream(text, Color.BLACK), normalOut));
		errPrinter = new PrintStream(new MultiOutputStream(
				new TextPaneOutputStream(text, Color.RED), normalErr));
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
