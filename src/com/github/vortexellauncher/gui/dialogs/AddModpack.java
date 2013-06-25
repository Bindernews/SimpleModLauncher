package com.github.vortexellauncher.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.exceptions.JsonValidationException;
import com.github.vortexellauncher.gui.Res;
import com.github.vortexellauncher.util.ErrorUtils;
import com.github.vortexellauncher.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

public class AddModpack extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public static final int RETURN_FILE = 1,
			RETURN_URL = 2,
			RETURN_CANCEL = 3;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField fileField;
	private JTextField urlField;
	private AddModpack self;
	private int retVal = RETURN_CANCEL;
	
	private FocusAdapter focusLostValidator = new FocusAdapter() {
		@Override public void focusLost(FocusEvent e) {
			validateFields();
		}
	};
	
	private JLabel lblStatus;
	
	private static final String DEFAULT_STATUS = "Select a modpack";
	private JButton btnOk;
	private JButton btnCancel;

	public AddModpack(Frame owner, boolean modal) {
		super(owner, "Add New Mod Pack", modal);
		self = this;
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				retVal = RETURN_CANCEL;
				setVisible(false);
			}
		});
		setBounds(100, 100, 351, 258);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			panel.setLayout(new GridLayout(0, 2, 0, 0));
			{
				btnOk = new JButton("Ok");
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (validateFields()) {
							dialogClose();
						}
					}
				});
				panel.add(btnOk);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						retVal = RETURN_CANCEL;
						dialogClose();
					}
				});
				panel.add(btnCancel);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0, 0};
			gbl_panel.rowHeights = new int[]{28, 16, 0, 0, 0, 0, 0, 0};
			gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				lblStatus = new JLabel(DEFAULT_STATUS);
				lblStatus.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
				GridBagConstraints gbc_lblStatus = new GridBagConstraints();
				gbc_lblStatus.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblStatus.gridwidth = 2;
				gbc_lblStatus.insets = new Insets(0, 0, 5, 0);
				gbc_lblStatus.gridx = 0;
				gbc_lblStatus.gridy = 0;
				panel.add(lblStatus, gbc_lblStatus);
			}
			{
				Component rigidArea = Box.createRigidArea(new Dimension(20, 10));
				rigidArea.setPreferredSize(new Dimension(20, 10));
				rigidArea.setMinimumSize(new Dimension(20, 10));
				GridBagConstraints gbc_rigidArea = new GridBagConstraints();
				gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
				gbc_rigidArea.gridx = 0;
				gbc_rigidArea.gridy = 1;
				panel.add(rigidArea, gbc_rigidArea);
			}
			{
				JLabel lblSelectAUrl = new JLabel("Select a URL...");
				lblSelectAUrl.setFont(new Font("Arial", Font.PLAIN, 12));
				GridBagConstraints gbc_lblSelectAUrl = new GridBagConstraints();
				gbc_lblSelectAUrl.anchor = GridBagConstraints.WEST;
				gbc_lblSelectAUrl.insets = new Insets(0, 0, 5, 5);
				gbc_lblSelectAUrl.gridx = 0;
				gbc_lblSelectAUrl.gridy = 2;
				panel.add(lblSelectAUrl, gbc_lblSelectAUrl);
			}
			{
				urlField = new JTextField();
				GridBagConstraints gbc_urlField = new GridBagConstraints();
				gbc_urlField.gridwidth = 2;
				gbc_urlField.insets = new Insets(0, 0, 5, 0);
				gbc_urlField.fill = GridBagConstraints.HORIZONTAL;
				gbc_urlField.gridx = 0;
				gbc_urlField.gridy = 3;
				panel.add(urlField, gbc_urlField);
				urlField.setColumns(10);
				urlField.addFocusListener(focusLostValidator);
			}
			{
				JLabel lblOr = new JLabel("OR");
				lblOr.setFont(new Font("Arial", Font.BOLD, 12));
				GridBagConstraints gbc_lblOr = new GridBagConstraints();
				gbc_lblOr.anchor = GridBagConstraints.WEST;
				gbc_lblOr.insets = new Insets(0, 0, 5, 5);
				gbc_lblOr.gridx = 0;
				gbc_lblOr.gridy = 4;
				panel.add(lblOr, gbc_lblOr);
			}
			{
				JLabel lblBrowseForA = new JLabel("Select a file...");
				lblBrowseForA.setFont(new Font("Arial", Font.PLAIN, 12));
				GridBagConstraints gbc_lblBrowseForA = new GridBagConstraints();
				gbc_lblBrowseForA.anchor = GridBagConstraints.WEST;
				gbc_lblBrowseForA.insets = new Insets(0, 0, 5, 5);
				gbc_lblBrowseForA.gridx = 0;
				gbc_lblBrowseForA.gridy = 5;
				panel.add(lblBrowseForA, gbc_lblBrowseForA);
			}
			{
				fileField = new JTextField();
				GridBagConstraints gbc_fileField = new GridBagConstraints();
				gbc_fileField.insets = new Insets(0, 0, 0, 5);
				gbc_fileField.fill = GridBagConstraints.HORIZONTAL;
				gbc_fileField.gridx = 0;
				gbc_fileField.gridy = 6;
				panel.add(fileField, gbc_fileField);
				fileField.setColumns(10);
				fileField.addFocusListener(focusLostValidator);
			}
			{
				JButton btnBrowse = new JButton("Browse");
				btnBrowse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fc = new JFileChooser();
						fc.setDialogType(JFileChooser.OPEN_DIALOG);
						fc.setAcceptAllFileFilterUsed(true);
						fc.setMultiSelectionEnabled(false);
						fc.showOpenDialog(self);
						File selFile = fc.getSelectedFile();
						if (selFile != null) {
							fileField.setText(selFile.getAbsolutePath());
						}
						validateFields();
					}
				});
				GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
				gbc_btnBrowse.gridx = 1;
				gbc_btnBrowse.gridy = 6;
				panel.add(btnBrowse, gbc_btnBrowse);
			}
		}
		btnOk.requestFocusInWindow();
	}
	
	/**
	 * Handles the return value, etc.
	 */
	public void dialogClose() {
		try {
			String fname = null;
			JsonElement elem = null;
			switch(getReturnValue()) {
			case AddModpack.RETURN_CANCEL:
				return; // do nothing
			case AddModpack.RETURN_FILE:
				File f = new File(getFile());
				elem = JsonUtils.readJsonFile(f);
				fname = f.getName();
				break;
			case AddModpack.RETURN_URL:
				URL url = new URL(getURL());
				elem = JsonUtils.readJsonURL(url);
				fname = url.getPath();
				break;
			}
			Main.metaManager().updatePack(elem.getAsJsonObject(), fname);
			Main.frame().getMainPanel().updateModpackList();
		} catch (IOException ex) {
			ErrorUtils.showErrorGui(self, "I/O Error:", ex.getMessage(), false);
		} catch (JsonValidationException ex) {
			ErrorUtils.showErrorGui(self, "Invalid Modpack: ", ex.getMessage(), false);
		} catch (JsonSyntaxException ex) {
			ErrorUtils.showErrorGui(self, "Invalid Modpack: ", ex.getMessage(), false);
		} catch (Exception ex) {
			ErrorUtils.printException("An unknown error occured", ex, false);
		}
		setVisible(false);
	}
	
	protected boolean validateFields() {
		retVal = RETURN_CANCEL;
		if (!urlField.getText().equals("")) {
			try {
				@SuppressWarnings("unused")
				URL url = new URL(urlField.getText());
				retVal = RETURN_URL;
			} catch (MalformedURLException e) {
				lblStatus.setIcon(Res.iconError);
				lblStatus.setText("Error: Bad URL");
				return false;
			}
		} else if (!fileField.getText().equals("")) {
			File f = new File(fileField.getText());
			if (!f.exists()) {
				lblStatus.setIcon(Res.iconError);
				lblStatus.setText("Error: File doesn't exist");
				return false;
			}
			retVal = RETURN_FILE;
		} else {
			lblStatus.setIcon(Res.iconError);
			lblStatus.setText("Error: No file or URL selected");
			return false;
		}
		lblStatus.setIcon(null);
		lblStatus.setText(DEFAULT_STATUS);
		return true;
	}
	
	public void showSelf() {
		fileField.setText("");
		urlField.setText("");
		retVal = RETURN_CANCEL;
		validateFields();
		setVisible(true);
	}
	
	public int getReturnValue() {
		return retVal;
	}
	public String getURL() {
		return urlField.getText();
	}
	public String getFile() {
		return fileField.getText();
	}

}
