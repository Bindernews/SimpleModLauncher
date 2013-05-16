package com.github.modlauncher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import com.github.modlauncher.exceptions.InvalidModpackException;
import com.github.modlauncher.net.NetUtils;
import com.github.modlauncher.pack.Modpack;
import com.github.modlauncher.pack.PackManager;

public class MinecraftLauncher {

	private PackManager pacman;
	
	public MinecraftLauncher(PackManager manager) {
		pacman = manager;
	}
	
	public Process launchMC() throws IOException, InvalidModpackException {
		pacman.setupPack();
		if (!verifyMCDir(pacman.getWorkingDir())) {
			return null; // possibly change to exception
		}
		
		//StringBuilder cpb = new StringBuilder(""); // Classpath builder
		
		return null;
	}
	
	private static final String[] REQUIRED_FILES = {
		"bin/minecraft.jar",
		"bin/lwjgl.jar",
		"bin/lwjgl_util.jar",
		"bin/jinput.jar",
		"bin/",
		"resources/", // names ending with / mean directory
	};

	public static boolean verifyMCDir(File dir) {
		final String separator = File.pathSeparator;
		if (!dir.isDirectory()) {
			return false;
		}
		
		for(int i = 0; i < REQUIRED_FILES.length; i++) {
			File testf = new File(dir.getAbsolutePath() + separator + REQUIRED_FILES[i]);
			if (REQUIRED_FILES[i].endsWith("/") && !testf.isDirectory())
				return false;
			if (!testf.exists())
				return false;
		}
		return true;
	}
}
