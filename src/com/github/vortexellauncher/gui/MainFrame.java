package com.github.vortexellauncher.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.gui.dialogs.AddModpack;
import com.github.vortexellauncher.gui.panels.MainPanel;
import com.github.vortexellauncher.gui.panels.OptionsPanel;
import com.github.vortexellauncher.util.GuiUtils;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private AddModpack addModpackDialog;
	private JDialog optionsDialog;
	private OptionsPanel optionsGui;
	private MainPanel contentPane;
	private BufferedImage launcherImage; 
	private JTextField squidTicker;

	public MainFrame() {
		setTitle("Vortexel Launcher");
		try {
			 launcherImage = ImageIO.read(MainFrame.class.getResource("/res/VortexelThumbnail.png"));
			 setIconImage(launcherImage);
		} catch (IOException e) {
		} catch (NullPointerException e) {
		}
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				Main.attemptExit();
			}
		});
		addComponentListener(GuiUtils.sizeBoundsListener);
		setFont(Res.mcfont);
		setBounds(100, 100, 369, 188);
		
		contentPane = new MainPanel(this);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPane, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(launcherImage));
		getContentPane().add(lblNewLabel, BorderLayout.EAST);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(launcherImage));
		getContentPane().add(lblNewLabel_1, BorderLayout.WEST);
		
		squidTicker = new JTextField(" ");
		squidTicker.setFont(new Font("Monospaced", Font.PLAIN, 12));
		squidTicker.setEnabled(false);
		squidTicker.setEditable(false);
		getContentPane().add(squidTicker, BorderLayout.SOUTH);
		pack();
		
		RocketSquidTicker ticker = new RocketSquidTicker();
		ticker.start();
		
		setMinimumSize(getSize());
		setMaximumSize(getSize());
		setResizable(false);
		
		// construct everything else
		optionsGui = new OptionsPanel();
		optionsDialog = new JDialog(this, "Options", true);
		optionsDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		optionsDialog.setContentPane(optionsGui);
		optionsDialog.pack();
		
		addModpackDialog = new AddModpack(this, true);
	}

	public MainPanel getMainPanel() {
		return contentPane;
	}
	
	public OptionsPanel getOptionsGui() {
		return optionsGui;
	}
	
	public JDialog getOptionsDialog() {
		return optionsDialog;
	}
	
	public AddModpack getAddModpackDialog() {
		return addModpackDialog;
	}
	
	public JTextField getSquidTicker() {
		return squidTicker;
	}
}
