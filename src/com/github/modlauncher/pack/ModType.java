package com.github.modlauncher.pack;

import java.io.File;

public enum ModType {
	Jar("jarmods"){
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
	
	private final String directoryName;

	public File getDir(File parent) {
		return new File(parent, directoryName);
	}
}
