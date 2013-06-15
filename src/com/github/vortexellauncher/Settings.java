package com.github.vortexellauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import com.github.vortexellauncher.pack.Modpack;

public class Settings extends Properties {
	private static final long serialVersionUID = 69226487626269165L;
	
	private static List<Settings> allSettings;
			
	private String name;
	
	private String vmargs = "";
	/** Ram in MB */
	private int ramMax;
	private Proxy proxy = null;
	private Modpack modpack;
	private boolean redirectOutputToFile = false;
	private String modpackName;
	private boolean shouldValidate = true;
	
	private static boolean debugMode = false;
	
	private static File settingsFile = new File(OSUtils.dataDir(), "settings.xml");
	
	public Settings(String settingsName) {
		name = settingsName;
	}
	
	public void updateToProperties() {
		put("hasBeenWritten","hasBeenWritten");
		put("vmargs", getVMParams());
		put("ram.max", ""+ramMax);
		put("modpack.name", getModpackName());
		put("shouldValidate", ""+shouldValidate);
		put("debugMode", ""+debugMode);
		if (proxy != null) {
			put("proxy.use", "true");
			InetSocketAddress addr = (InetSocketAddress)proxy.address();
			put("proxy.host", addr.getHostName());
			put("proxy.port", addr.getPort());
			put("proxy.type", proxy.type().name());
		} else {
			put("proxy.use", "false");
		}
	}
	
	public void updateFromProperties() {
		if (!super.contains("hasBeenWritten")) 
			return;
		vmargs = getProperty("vmargs", "");
		ramMax = getInteger("ram.max", 768);
		modpackName = getProperty("modpack.name", "");
		shouldValidate = getBoolean("shouldValidate");
//		debugMode = getBoolean("debugMode");
		if (getBoolean("proxy.use")) {
			InetSocketAddress isa = new InetSocketAddress(getProperty("proxy.host"), getInteger("proxy.port", -1));
			proxy = new Proxy(Proxy.Type.valueOf(getProperty("proxy.type")), isa);
		} else {
			proxy = null;
		}
	}
	
	public boolean getBoolean(String key) {
		try {
			return Boolean.parseBoolean(getProperty(key));
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public int getInteger(String key, int def) {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (NullPointerException e) {
			return def;
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public void setVMParams(String params) {
		vmargs = params;
	}
	public String getVMParams() {
		return vmargs != null ? vmargs : "";
	}
	public void setProxy(Proxy aproxy) {
		proxy = aproxy;
	}
	public Proxy getProxy() {
		return proxy;
	}
	public Modpack getModpack() {
		return modpack;
	}
	public void setModpack(Modpack mp) {
		modpack = mp;
	}
	public String getModpackName() {
		return modpackName != null ? modpackName : "";
	}
	public void setModpackName(String nname) {
		modpackName = nname;
	}
	public void setRamMax(int ram) {
		ramMax = ram;
	}
	public int getRamMax() {
		return ramMax;
	}
	public boolean getRedirectOutput() {
		return redirectOutputToFile;
	}
	public boolean shouldValidate() {
		return shouldValidate;
	}
	public void setShouldValidate(boolean v) {
		shouldValidate = v;
	}
	public static boolean isDebugMode() {
		return debugMode;
	}
	public static void setDebugMode(boolean debug) {
		debugMode = debug;	
	}

	public void doLoad() throws InvalidPropertiesFormatException, IOException {
		loadFromXML(new FileInputStream(settingsFile));
		updateFromProperties();
	}
	
	public void doSave() throws IOException {
		updateToProperties();
		storeToXML(new FileOutputStream(settingsFile), "Vortexel Launcher Settings", "UTF-8");
	}
}
