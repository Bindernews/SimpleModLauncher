package com.github.vortexellauncher;

import java.io.File;

public enum OSUtils {
	
	Windows {
		public String getDataDir() {
			return System.getenv("AppData") + "\\." + LAUNCHER_DIR;
		}
		public String getNatives() {
			return "windows_natives.jar";
		}
		public String getNativesURL() {
			return "https://s3.amazonaws.com/MinecraftDownload/" + getNatives(); 
		}
		public String getMCFolderName() {
			return ".minecraft";
		}
	},
	Mac {
		private static final String MC_PATH = "/Library/Application Support/"; 
		public String getDataDir() {
			return System.getenv("HOME") + MC_PATH + LAUNCHER_DIR;
		}
		public String getNatives() {
			return "macosx_natives.jar";
		}
		public String getNativesURL () {
			return "https://s3.amazonaws.com/MinecraftDownload/" + getNatives();
		}
		public String getMCFolderName() {
			return "minecraft";
		}
		public File getMinecraftDirFrom(File basedir) {
			return new File(basedir, MC_PATH + "minecraft");
		}
	},
	Linux {
		public String getDataDir() {
			return "~/."+LAUNCHER_DIR;
		}
		public String getNatives() {
			return "linux_natives.jar";
		}
		public String getNativesURL() {
			return "https://s3.amazonaws.com/MinecraftDownload/" + getNatives();
		}
		public String getMCFolderName() {
			return ".minecraft";
		}
	};

	public static final String LAUNCHER_DIR = "modlauncher";
	
	private static OSUtils currentOS = null;
	
	private static void selectOS() {
		String name = System.getProperty("os.name").toLowerCase();
		if (name.contains("win")) currentOS = Windows;
		else if (name.contains("linux")) currentOS = Linux;
		else if (name.contains("mac")) currentOS = Mac;
		else throw new IllegalArgumentException("Unknown OS");
	}
	
	public static OSUtils getOS() {
		if (currentOS == null)
			selectOS();
		return currentOS;
	}
	
	public static String dataDir() {
		return getOS().getDataDir();
	}
	public static String nativesJar() {
		return getOS().getNatives();
	}
	public static String nativesURL() {
		return getOS().getNativesURL();
	}
	public static String mcFolderName() {
		return getOS().getMCFolderName();
	}
	
	public abstract String getDataDir();
	public abstract String getNatives();
	public abstract String getNativesURL();
	public abstract String getMCFolderName();
	public File getMinecraftDirFrom(File basedir) { return new File(basedir, "minecraft"); }
	
}
