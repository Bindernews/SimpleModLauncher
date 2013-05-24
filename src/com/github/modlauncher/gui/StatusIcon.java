package com.github.modlauncher.gui;

import javax.swing.ImageIcon;

public enum StatusIcon {
	Working("res/icon_working.png"),
	Error("res/icon_error.png"),
	Success("res/icon_success.png");
	
	private ImageIcon icon;
	private StatusIcon(String resname) {
		icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(resname));
	}
	
	public ImageIcon getIcon() {
		return icon;
	}
}
