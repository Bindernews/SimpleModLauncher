package com.github.modlauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LaunchSettings {
	
	public static final List<String> DEFAULT_VMPARAMS = Arrays.asList(new String[]{
		"-XX:+UseConcMarkSweepGC",
		"-XX:+CMSIncrementalMode",
		"-XX:+AggressiveOpts",
		"-XX:+CMSClassUnloadingEnabled",
		"-XX:+MaxPermSize=128M",
	});

	private String workingDir;
	private String username;
	private String password;
	private int ramMax;
	private String socksProxyHost = "";
	private int socksProxyPort = 0;
	private List<String> vmparams;
	
	public LaunchSettings(String workdir, String uname, String passwd, String ram) {
		workingDir = workdir;
		username = uname;
		password = passwd;
		ramMax = 512;
		socksProxyHost = "";
		socksProxyPort = 0;
		vmparams = new ArrayList<String>(DEFAULT_VMPARAMS);
	}
	
	public String getWorkingDir() {
		return workingDir;
	}
	
	public void setVMParams(List<String> params) {
		vmparams = params;
	}
	
	public List<String> getVMParams() {
		return vmparams;
	}
	
	public void setUsername(String uname) {
		username = uname;
	}
	
	public String getUsername() {
		return username;
	}

	public void setPassword(String passwd) {
		password = passwd;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setSocksHost(String host) {
		socksProxyHost = host;
	}
	public String getSocksHost() {
		return socksProxyHost;
	}
	public void setSocksPort(int port) {
		socksProxyPort = port;
	}
	public int getSocksPort() {
		return socksProxyPort;
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
	
	
}
