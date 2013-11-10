package com.github.vortexellauncher.pack;

import java.io.File;

import com.github.vortexellauncher.MCVersion;

public enum ModType {
	Jar("../jarmods") {
	},
	Core("coremods") {
	},
	Mod("mods") {
	},
	Config("config") {
	},
	Bin("bin") {
	},
	Native("bin/natives") {
	},
	Resource("resources/mod"){
	},
	Custom("") {
	};
	
	private ModType(String dirName) {
		directoryName = dirName;
	}
	
	protected String directoryName;

	public File getDir(MCVersion ver, File parent) {
		return new File(parent, directoryName);
	}
	
	public static ModType fromString(String vstr) {
		for(ModType mcv : ModType.values()) {
			if (vstr.equalsIgnoreCase(mcv.name())) {
				return mcv;
			}
		}
		return null;
	}
}
