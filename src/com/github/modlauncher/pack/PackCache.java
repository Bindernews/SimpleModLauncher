package com.github.modlauncher.pack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.github.modlauncher.OS;
import com.github.modlauncher.VersionData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PackCache {

	private Gson gson = new Gson();
	private JsonObject root;
	private File cacheFile;
	private Modpack modpack;
	
	public PackCache(Modpack pack) throws IOException {
		modpack = pack;
		cacheFile = new File(OS.dataDir(), pack.folder + File.separator + "cache.txt");
		if (cacheFile.exists()) {
			FileReader fr = new FileReader(cacheFile);
			JsonParser parser = new JsonParser();
			root = parser.parse(fr).getAsJsonObject();
			fr.close();
		}
		else {
			root = new JsonObject();
		}
		for(ModFile mf : pack.getMods()) {
			if (isModDefined(mf)) {
				mf.setCacheFilename(getFilename(mf));
				mf.setCacheMD5(getMD5(mf));
				int comp = mf.getVersion().compareTo(getVersion(mf));
				if (comp > 0)
					mf.newerThanCache = true;
			}
			else {
				mf.newerThanCache = true;
			}
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
				&& obj.has("url")
				&& obj.has("type")
				&& obj.has("version");
	}
	
	public boolean updateMod(ModFile modfile) {
		putIfValid(modfile.name, "filename", modfile.getFilename());
		putIfValid(modfile.name, "md5", modfile.getMD5());
		putIfValid(modfile.name, "url", modfile.url);
		putIfValid(modfile.name, "type", modfile.type.name());
		VersionData vd = modfile.getVersion();
		vd = vd != null ? vd : new VersionData(0);
		putIfValid(modfile.name, "version", vd.toString());
		return true;
	}
	
	public String getFilename(ModFile modfile) {
		return getModProperty(modfile.name, "filename");
	}
	
	public VersionData getVersion(ModFile modfile) {
		String prop = getModProperty(modfile.name, "version");
		if (prop == null) return null;
		return VersionData.create(prop);
	}
	
	public String getMD5(ModFile modfile) {
		return getModProperty(modfile.name, "md5");
	}
	
	public String getURL(ModFile modfile) {
		return getModProperty(modfile.name, "url");
	}
	
	public Modpack getModpack() {
		return modpack;
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
			if (!root.has(mod)) {
				root.add(mod, new JsonObject());
			}
			root.get(mod).getAsJsonObject().addProperty(k, v);
			return true;
		}
		return false;
	}
}
