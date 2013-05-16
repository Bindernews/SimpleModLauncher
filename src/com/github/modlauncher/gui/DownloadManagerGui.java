package com.github.modlauncher.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import com.github.modlauncher.net.FileDownloader;

public class DownloadManagerGui extends JDialog {
	private static final long serialVersionUID = 3486143573821346283L;
	
	private final JPanel contentPanel = new JPanel();
	private final JList<FileDownloader> downloadList = new JList<FileDownloader>();

	public DownloadManagerGui() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				
		downloadList.setCellRenderer(new DownloadDrawer());
		contentPanel.add(downloadList, BorderLayout.CENTER);
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
	}
	
	public JList<FileDownloader> getList() {
		return downloadList;
	}

	public static class DownloadDrawer implements ListCellRenderer<FileDownloader> {
		private JPanel panel = new JPanel();
		private JProgressBar progressBar = new JProgressBar();
		private JLabel fileName = new JLabel();
		
		public DownloadDrawer() {
			panel.setLayout(new BorderLayout());
			panel.add(progressBar, BorderLayout.CENTER);
			panel.add(fileName, BorderLayout.NORTH);
		}
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends FileDownloader> list, FileDownloader value,
				int index, boolean isSelected, boolean cellHasFocus) {
			fileName.setText(value.getFile().getName());
			progressBar.setValue((int)(value.getPercentDone() * 100));
			return panel;
		}
	}
}
