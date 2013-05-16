package com.github.modlauncher.pack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PackCache {

	private Gson gson = new Gson();
	private JsonObject root;
	private File cacheFile;
	
	public PackCache(File f) throws IOException {
		cacheFile = f;
		if (cacheFile.exists()) {
			FileReader fr = new FileReader(cacheFile);
			JsonParser parser = new JsonParser();
			root = parser.parse(fr).getAsJsonObject();
			fr.close();
		}
		else {
			root = new JsonObject();
		}
	}
	
	public void saveToFile() throws IOException {
		FileWriter fw = new FileWriter(cacheFile);
		fw.write(gson.toJson(root));
		fw.close();
	}
	
	public boolean isModDefined(ModFile modfile) {
		if (!root.has(modfile.name))
			return false;
		JsonObject obj = root.get(modfile.name).getAsJsonObject();
		return obj.has("filename")
				&& obj.has("md5")
				&& obj.has("type");
	}
	
	public String getFilename(ModFile modfile) {
		return getModProperty(modfile.name, "filename");
	}
	
	public String getMD5(ModFile modfile) {
		return getModProperty(modfile.name, "md5");
	}
	
	public boolean updateMod(ModFile modfile) {
		putIfValid(modfile.name, "filename", modfile.filename);
		putIfValid(modfile.name, "md5", modfile.md5);
		putIfValid(modfile.name, "type", modfile.type.name());
		return true;
	}
	
	protected String getModProperty(String modname, String prop) {
		if (!root.has(modname)) {
			return null;
		}
		JsonObject pmod = root.get(modname).getAsJsonObject();
		if (pmod.has(prop) && pmod.get(prop).isJsonPrimitive()) {
			return pmod.get(prop).getAsString();
		}
		return null;	
	}
	
	protected boolean putIfValid(String mod, String k, String v) {
		if (v != null) {
			root.get(mod).getAsJsonObject().addProperty(k, v);
			return true;
		}
		return false;
	}
}
