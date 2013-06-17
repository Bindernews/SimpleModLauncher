package com.github.vortexellauncher;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import com.github.vortexellauncher.gui.LogView;
import com.github.vortexellauncher.gui.MainFrame;
import com.github.vortexellauncher.gui.Res;
import com.github.vortexellauncher.gui.Styler;
import com.github.vortexellauncher.launch.Launch;
import com.github.vortexellauncher.pack.PackMetaManager;

public class Main {
	
	public static final VersionData VERSION = new VersionData(1,0,0,0);
	public static final String UPDATE_URL = "https://googledrive.com/host/0Bw00_I2xsVk3WnNQaTRNby1GeHc/launcher_info.txt";
	public static final String LOGGER_NAME = "Vortexel Launcher";
	
	private static LogView logView = null;
	private static MainFrame frame;
	private static Launch instance;
	private static Settings settings;
	private static PackMetaManager metaManager;
	private static Logger logger;
	
	public static final File lastLoginFile = new File(OSUtils.dataDir(), "loginlast");
	
	public static void main(String[] args) {
		System.out.println(Arrays.asList(args));
		Init.doOSXFix(args);
		
		// create the logger
		logger = Logger.getLogger(LOGGER_NAME);
		logger.setLevel(Level.INFO);
		
		// create the launch instance
		instance = new Launch();
		 
		Styler.setLookAndFeel();
		Styler.setStyle();
		
		// logView provides a way to report error so create it before most errors can be thrown
		logView = new LogView();
		logView.addRedirect();
		
		try {
			// this will also create the primary data folder
			if (!Settings.settingsDir.exists()) {
				Settings.settingsDir.mkdirs();
			}
			Res.init();
			settings = new Settings("default");
			try {
				settings.doLoad();
			} catch (IOException e) { // ingore IOExceptions
			}
			SwingUtilities.invokeLater(new Runnable() { // initialize main gui
				public void run() {
					frame = new MainFrame();
					frame.setVisible(true);
					Init.loadLastLogin();
				}
			});
			metaManager = new PackMetaManager();
			Init.loadModpacks();
		} catch (Exception e) {
			fatalException(e);
		}
	}
	
	/**
	 * Exits the program only if all JFrames and JDialogs have been closed.
	 * @return false if the program didn't close. It will exit otherwise so the return value is pointless but it makes calling code more clear. 
	 */
	public static boolean attemptExit() {
		System.err.println("Attempting exit");
		if (frame().isDisplayable() || logView.isVisible()) {
			return false;
		}
		frame().getOptionsGui().updateSettings();
		try {
			settings().doSave();
		} catch (IOException e) {
			log().log(Level.SEVERE, "", e);
		}
		frame().dispose();
		logView.dispose();
		System.exit(0);
		return true;
	}
	
	public static void fatalException(Throwable t) {
		log().log(Level.SEVERE, "", t);
		if (frame() != null) {
			frame().dispose();
		}
		logView().setVisible(true);
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
	public static PackMetaManager metaManager() {
		return metaManager;
	}
	public static Logger log() {
		return logger;
	}
}
