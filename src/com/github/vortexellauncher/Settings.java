package com.github.vortexellauncher;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.github.vortexellauncher.pack.Modpack;

public class Settings {

	private List<String> vmargs;
	private int ramMax;
	private Proxy proxy;
	private Modpack modpack;
	private boolean redirectOutputToFile = false;
	private String modpackName;
	private boolean shouldValidate = true;
	
	private static boolean debugMode = false;
	
	public Settings() {
		vmargs = new ArrayList<String>();
	}
	
	public void setVMParams(List<String> params) {
		vmargs.clear();
		vmargs.addAll(params);
	}
	public List<String> getVMParams() {
		return vmargs;
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
		return modpackName;
	}
	public void setModpackName(String nname) {
		modpackName = nname;
	}
	
	/**
	 * @param ram The ram in MB
	 */
	public void setRamMax(int ram) {
		ramMax = ram;
	}
	
	/**
	 * @return The maximum ram to allow Java to use in MB
	 */
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
}
