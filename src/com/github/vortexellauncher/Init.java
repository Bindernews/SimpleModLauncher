package com.github.vortexellauncher;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.SwingUtilities;

import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.util.JsonUtils;
import com.github.vortexellauncher.util.LaunchUtils;
import com.github.vortexellauncher.util.Utils;
import com.google.gson.JsonElement;

/**
 * Contains code for various initialization routines used by Main.
 * Helps separate implementation from initialization logic.
 * @author Bindernews
 *
 */
public class Init {

	private Init () {}
	
	public static void doOSXFix(String[] args) {
		if (OSUtils.getOS() == OSUtils.Mac && System.getProperty("java.specification.version").equals("1.7")) {
			boolean foundNoJVM = false;
			for (String s : args) {
				if (s.equals("-nojvm")) {
					foundNoJVM = true;
					break;
				}
			}
			if (!foundNoJVM) {
				System.out.println("Restarting with -nojvm");
				LaunchUtils.restartWithNoJVM();
				System.exit(0);
			}
		}
	}
	
	public static void loadLastLogin() {
		try {			
			String lastLoginStr = Utils.simpleCryptIn(Main.lastLoginFile);
			if (lastLoginStr != null) {
				UserPass up = new UserPass(lastLoginStr);
				Main.frame().getMainPanel().setUserPass(up.getUsername(), up.getPassword());
				Main.frame().getMainPanel().getChkRemember().setSelected(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadModpacks() {
		for(int i=0; i<Defaults.MODPACKS.length; i+=2) {
			String packName = Defaults.MODPACKS[i];
			try {
				if (!Main.metaManager().hasPack(packName)) {
					JsonElement elem = JsonUtils.readJsonURL(new URL(Defaults.MODPACKS[i+1]));
					Main.metaManager().updatePack(elem.getAsJsonObject(), packName);
				}
			} catch (IOException e) {
				Log.log(Level.SEVERE, "Failed to update modpack: " + packName, e);
				Log.setVisible(true);
			} catch (InvalidModpackException e) {
				Log.log(Level.SEVERE, "Invalid modpack: " + packName, e);
				Log.setVisible(true);
			}
		}
		if (Main.settings().getModpackName().equals("")) {
			Main.settings().setModpackName("Vortexel Modpack");
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main.frame().getMainPanel().updateModpackList();
				Main.frame().getMainPanel().updateModpackName();
			}
		});
	}
}
