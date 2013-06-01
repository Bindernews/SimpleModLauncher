package com.github.vortexellauncher;

import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.gui.LogView;
import com.github.vortexellauncher.gui.MainFrame;
import com.github.vortexellauncher.gui.Res;
import com.github.vortexellauncher.pack.PackMetaManager;
import com.github.vortexellauncher.util.JsonUtils;
import com.google.gson.JsonElement;

public class Main {
	
	private static LogView logView = null;
	private static MainFrame frame;
	private static Launch instance;
	private static Settings settings;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (NoClassDefFoundError e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Throwable t) {
			}
		}
		
		logView = new LogView();
		logView.addRedirect();
		
		try {
			Res.init();
		} catch (IOException e) {
			//TODO crash message
		}
		
		frame = new MainFrame();
		settings = new Settings();
		instance = new Launch();
		
		frame.setVisible(true);
		
		try {
			File osDir = new File(OS.dataDir()); 
			if (!osDir.exists()) {
				osDir.mkdirs();
			}

			PackMetaManager metaManager = new PackMetaManager();
			if (!metaManager.hasPack("Vortexel Modpack")) {
				JsonElement elem = JsonUtils.readJsonURL(Res.getURL("res/vortexel_pack.json"));
				metaManager.updatePack(elem.getAsJsonObject(), "vortexel_pack.json");
			}
			settings().setModpackName("Vortexel Modpack");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidModpackException e1) {
			e1.printStackTrace();
		}
	}
	
	public static boolean attemptExit() {
		if (frame().isDisplayable() || logView.isVisible()) {
			return false;
		}
		frame().dispose();
		logView.dispose();
		System.exit(0);
		return true;
	}
	
	public static Launch launchpad() {
		return instance;
	}
	public static MainFrame frame() {
		return frame;
	}
	public static Settings settings() {
		return settings;
	}
	public static LogView logView() {
		return logView;
	}
}
