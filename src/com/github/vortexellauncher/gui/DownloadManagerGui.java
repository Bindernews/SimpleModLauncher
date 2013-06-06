package com.github.vortexellauncher.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.net.FileDownloader;

public class DownloadManagerGui extends JDialog {
	private static final long serialVersionUID = 3486143573821346283L;
	
	private final JPanel contentPanel = new JPanel();
	private final JList downloadList = new JList();

	public DownloadManagerGui(Frame owner) {
		super(owner, "Downloads", true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Main.frame().setEnabled(true);
			}
		});
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		downloadList.setCellRenderer(new DownloadDrawer());
		JScrollPane jscroll = new JScrollPane(downloadList);
		contentPanel.add(jscroll, BorderLayout.CENTER);
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
	}
	
	public JList getList() {
		return downloadList;
	}

	public static class DownloadDrawer implements ListCellRenderer {
		private JPanel panel = new JPanel();
		private JProgressBar progressBar = new JProgressBar();
		private JLabel fileName = new JLabel();
		
		public DownloadDrawer() {
			panel.setLayout(new BorderLayout());
			panel.add(progressBar, BorderLayout.CENTER);
			panel.add(fileName, BorderLayout.NORTH);
			progressBar.setBackground(Color.GREEN);
		}
		
		@Override
		public Component getListCellRendererComponent(
				JList list, Object valueObj,
				int index, boolean isSelected, boolean cellHasFocus) {
			String fname;
			FileDownloader value = (FileDownloader)valueObj;
			if (value.getFile() != null) {
				fname = value.getFile().getName();
			}
			else {
				fname = value.getURL().getFile();
			}
			fileName.setText(fname);
			progressBar.setValue((int)(value.getProgress() * 100));
			return panel;
		}
	}
}
