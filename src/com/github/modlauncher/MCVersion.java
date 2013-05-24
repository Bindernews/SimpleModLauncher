package com.github.modlauncher;

public enum MCVersion {

	v1_5_2("1.5.2", "1_5_2"),
	v1_5_1("1.5.1", "1_5_1"),
	v1_5("1.5", "1_5");
	
	public final String versionString;
	private final String assetFolder;
	private MCVersion(String vtext, String assetDir) {
		versionString = vtext;
		assetFolder = assetDir;
	}
	
	public String getJarUrl() {
		return "http://assets.minecraft.net/" + assetFolder + "/minecraft.jar"; 
	}
	
	public static MCVersion fromString(String vstr) {
		for(MCVersion mcv : MCVersion.values()) {
			if (vstr.equals(mcv.versionString)) {
				return mcv;
			}
		}
		return null;
	}
}
