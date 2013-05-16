package com.github.modlauncher;

public abstract class OSInfo {

	public static final String LAUNCHER_DIR = "modlauncher";
	
	private static OSInfo impl = null;
	
	public static void init() throws IllegalArgumentException {
		String name = System.getProperty("os.name").toLowerCase();
		if (name.contains("win")) impl = new OSWin();
		else if (name.contains("linux")) impl = new OSLinux();
		else if (name.contains("mac")) impl = new OSMac();
		else throw new IllegalArgumentException("Unknown OS");
	}
	
	public static String dataDir() {
		return impl.getDataDir();
	}
	
	public static String nativesJar() {
		return impl.getNatives();
	}
	
	protected abstract String getDataDir();
	protected abstract String getNatives();
	
	public static class OSWin extends OSInfo {
		protected String getDataDir() {
			return System.getenv("AppData") + "\\Roaming\\." + LAUNCHER_DIR;
		}
		protected String getNatives() {
			return "windows_natives.jar";
		}
	}
	public static class OSLinux extends OSInfo {
		protected String getDataDir() {
			return "~/."+LAUNCHER_DIR;
		}
		protected String getNatives() {
			return "linux_natives.jar";
		}
	}
	public static class OSMac extends OSInfo {
		protected String getDataDir() {
			return "~/Library/Application Support/"+LAUNCHER_DIR;
		}
		protected String getNatives() {
			return "macosx_natives.jar";
		}
	}
}
