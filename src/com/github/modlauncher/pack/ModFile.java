package com.github.modlauncher.pack;

import com.github.modlauncher.exceptions.InvalidModpackException;
import com.github.modlauncher.util.JsonUtils;
import com.google.gson.JsonObject;

public class ModFile {

	public static enum Download {
		Direct,
		Browser,
	}
	
	public ModType type = null;
	public String name = null;
	public String url = null;
	public String md5 = null;
	public boolean isArchive = false;
	public Download download = Download.Direct;
	
	/** The filename to save to */
	public String filename;

	public ModFile() {
	}
	
	public ModFile(String mname, ModType mtype, String murl) {
		this(mname, mtype, murl, false);
	}
	
	public ModFile(String mname, ModType mtype, String murl, boolean archive) {
		name = mname;
		type = mtype;
		url = murl;
		isArchive = archive;
	}
	
	public void loadJson(JsonObject obj, String oname) throws InvalidModpackException {
		name = oname;
		url = JsonUtils.validateString(obj,"url",true);
		md5 = JsonUtils.validateString(obj,"md5",false);
		type = new JsonUtils.TL<ModType>(ModType.class).safeGetEnum(obj, "type");
		if (obj.has("download"))
			download = new JsonUtils.TL<Download>().safeGetEnum(obj, "download");
		filename = JsonUtils.validateString(obj,"filename",false);
	}
	
	
	
	
}
