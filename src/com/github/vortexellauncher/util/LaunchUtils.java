package com.github.vortexellauncher.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.OSUtils;

public class LaunchUtils {

	public static String getCurrentJar() {
		String val = new File(LaunchUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getAbsolutePath();
		try {
			val = URLDecoder.decode(val, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return val;
	}
	
	public static String getJVMPath() {
		String jvmPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		if (OSUtils.getOS() == OSUtils.Windows) {
			jvmPath += "w";
		}
		return jvmPath;
	}
	
	/**
	 * Relaunches the launcher with the -nojvm flag
	 */
	public static void restartWithNoJVM() {
		ArrayList<String> procArgs = new ArrayList<String>();
		procArgs.add(LaunchUtils.getJVMPath());
		procArgs.add("-cp");
		procArgs.add(LaunchUtils.getCurrentJar());
		procArgs.add(Main.class.getName());
		procArgs.add("-nojvm");
		ProcessBuilder pb = new ProcessBuilder(procArgs);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
