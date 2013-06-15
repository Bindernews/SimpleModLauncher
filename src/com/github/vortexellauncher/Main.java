package com.github.vortexellauncher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.gui.LogView;
import com.github.vortexellauncher.gui.MainFrame;
import com.github.vortexellauncher.gui.Res;
import com.github.vortexellauncher.launch.Launch;
import com.github.vortexellauncher.pack.PackMetaManager;
import com.github.vortexellauncher.util.JsonUtils;
import com.github.vortexellauncher.util.Utils;
import com.google.gson.JsonElement;

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
	
	public static File lastLoginFile;
	
	public static void main(String[] args) {
		System.out.println(Arrays.asList(args));
		if (OSUtils.getOS() == OSUtils.Mac) {
			boolean foundNoJVM = false;
			for (String s : args) {
				if (s.equals("-nojvm")) {
					foundNoJVM = true;
					break;
				}
			}
			if (!foundNoJVM) {
//				Runtime.getRuntime().exec(new String[]{System.getProperty())
			}
		}
		
		logger = Logger.getLogger(LOGGER_NAME);
//		logger.setLevel(Level.INFO);
		logger.setLevel(Level.FINE);
		
		lastLoginFile = new File(OSUtils.dataDir(), "loginlast");
		instance = new Launch();
		settings = new Settings("default");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loadGui();
			}
		});
		
		
		try {
			settings.doLoad();
		} catch (Exception e) {
		}
		try {
			File osDir = new File(OSUtils.dataDir()); 
			if (!osDir.exists()) {
				osDir.mkdirs();
			}
			
			metaManager = new PackMetaManager();
			for(int i=0; i<Defaults.MODPACKS.length; i+=2) {
				try {
					String packName = Defaults.MODPACKS[i];
					if (!metaManager.hasPack(packName)) {
						JsonElement elem = JsonUtils.readJsonURL(new URL(Defaults.MODPACKS[i+1]));
						metaManager.updatePack(elem.getAsJsonObject(), packName);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InvalidModpackException e) {
					e.printStackTrace();
				}
			}
			if (settings().getModpackName().equals("")) {
				settings().setModpackName("Vortexel Modpack");
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.getMainPanel().updateModpackList();
					frame.getMainPanel().updateModpackName();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			frame.dispose();
		}
	}
	
	private static void loadGui() {
		try {
			for(LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
				if (lafi.getName().equals("Nimbus")) {
					UIManager.setLookAndFeel(lafi.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
			}
		}
		logView = new LogView();
		logView.addRedirect();
		try {
			Res.init();
		} catch (IOException e) {
			//TODO crash message
			e.printStackTrace();
			logView.setVisible(true);
		}
		frame = new MainFrame();
		frame.setVisible(true);
		try {
			
			String lastLoginStr = Utils.simpleCryptIn(lastLoginFile);
			if (lastLoginStr != null) {
				UserPass up = new UserPass(lastLoginStr);
				Main.frame().getMainPanel().setUserPass(up.getUsername(), up.getPassword());
				Main.frame().getMainPanel().getChkRemember().setSelected(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
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
