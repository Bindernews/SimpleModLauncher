package com.github.vortexellauncher.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.vortexellauncher.Main;

@SuppressWarnings("serial")
public class OfflineDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public OfflineDialog(Frame owner) {
		super(owner, "Play Offline?", true);
		setBounds(100, 100, 251, 104);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblWouldYouLike = new JLabel("Would you like to play in offline mode?");
			contentPanel.add(lblWouldYouLike);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnYes = new JButton("Yes");
				btnYes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Main.launchpad().playOffline();
						dispose();
					}
				});
				buttonPane.add(btnYes);
				getRootPane().setDefaultButton(btnYes);
			}
			{
				JButton btnNo = new JButton("No");
				btnNo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Main.launchpad().resetLogin();
						Main.frame().setEnabled(true);
						dispose();
					}
				});
				buttonPane.add(btnNo);
			}
		}
	}

}
