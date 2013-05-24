package com.github.modlauncher.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import com.github.modlauncher.Launchpad;
import com.github.modlauncher.net.FileDownloader;

public class DownloadManagerGui extends JDialog implements WindowListener {
	private static final long serialVersionUID = 3486143573821346283L;
	
	private final JPanel contentPanel = new JPanel();
	private final JList<FileDownloader> downloadList = new JList<FileDownloader>();

	public DownloadManagerGui(Frame owner) {
		super(owner, "Downloads", false);
		addWindowListener(this);
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
			String fname;
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

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {
		Launchpad.i().getFrame().setEnabled(true);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
