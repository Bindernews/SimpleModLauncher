package com.github.vortexellauncher.gui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Styler {

	private Styler() {}
	
	public static void setStyle() {
//		UIManager.put("nimbusBase", new Color(128,216,101));
//		UIManager.put("nimbusBlueGrey", new Color(151,255,119));
//		UIManager.put("control", new Color(128,216,101));
	}
	
	public static int setLookAndFeel() {
		boolean hasSet = false;
		try {
			for(LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
				if (lafi.getName().equals("Nimbus")) {
					UIManager.setLookAndFeel(lafi.getClassName());
					hasSet = true;
					break;
				}
			}
		} catch (Exception e) {
		}
		if (hasSet)
			return 0;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			return 1;
		} catch (Exception e1) {
			return 2;
		}
	}

}
