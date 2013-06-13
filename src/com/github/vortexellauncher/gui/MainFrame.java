package com.github.vortexellauncher.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
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

	public MainFrame() {
		setTitle("Vortexel Launcher");
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
		setContentPane(contentPane);
		pack();
		
		setMinimumSize(getSize());
		
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
	
}
