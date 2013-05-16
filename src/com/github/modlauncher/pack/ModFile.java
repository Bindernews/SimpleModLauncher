package com.github.modlauncher.pack;

import com.github.modlauncher.exceptions.InvalidModpackException;
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
	public Download download = Download.Direct;
	
	/** The filename to save to */
	public String filename;

	public ModFile() {
	}
	
	public void loadJson(JsonObject obj, String oname) throws InvalidModpackException {
		name = oname;
		url = validateString(obj,"url",true);
		md5 = validateString(obj,"md5",false);
		type = new TL<ModType>().safeGetEnum(obj, "type");
		if (obj.has("download"))
			download = new TL<Download>().safeGetEnum(obj, "download");
		filename = validateString(obj,"filename",false);
	}
	
	protected class TL<T extends Enum<T>> {
		Class<T> ctype;
		public T safeGetEnum(JsonObject obj, String ename) throws InvalidModpackException {
			String mtype = validateString(obj,ename,true);
			for(T mt : ctype.getEnumConstants()) {
				if (mt.name().equalsIgnoreCase(mtype)) {
					return mt;
				}
			}
			throw new InvalidModpackException("Mod \"" + name + "\" has an invalid mod type");
		}
	}
	
	private String validateString(JsonObject obj, String ename, boolean required) throws InvalidModpackException {
		if (!obj.has(ename)) {
			if (required)
				throw new InvalidModpackException("Mod " + name + ": \"" + ename + "\" not defined");
			else
				return null;
		}
		if (!obj.get(ename).isJsonPrimitive())
			throw new InvalidModpackException("Mod " + name + "\"" + ename + "\" is not a string");
		return obj.get(ename).getAsString();
	}
}
