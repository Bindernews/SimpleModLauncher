package com.github.vortexellauncher.pack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.github.vortexellauncher.MCVersion;
import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.OSUtils;
import com.github.vortexellauncher.VersionData;
import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.exceptions.JsonValidationException;
import com.github.vortexellauncher.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Modpack {
	
	private JsonObject obj;

	public String name;
	public MCVersion mcversion;
	public VersionData version;
	public VersionData minLauncherVersion;
	public VersionData maxLauncherVersion;
	public String folder;
	private String updateURL;
	private List<String> authors = new ArrayList<String>();
	private List<ModFile> modList = new ArrayList<ModFile>();
	
	public Modpack() {
		reset();
	}
	
	public Modpack(JsonObject jobj, String fname) throws JsonValidationException {
		reset();
		loadJson(jobj, fname);
	}
	
	public void loadJson(JsonObject jobj, String fname) throws JsonValidationException {
		obj = jobj;
		try {
			name = JsonUtils.validateString(obj,"name",true);
			version = JsonUtils.validateVersion(obj,"version",true);
			minLauncherVersion = JsonUtils.validateVersion(obj, "minLauncherVersion", false);
			maxLauncherVersion = JsonUtils.validateVersion(obj, "maxLauncherVersion", false);
			if (!checkLauncherVersion(Main.VERSION)) {
				throw new InvalidModpackException("Incorrect launcher version");
			}
			mcversion = MCVersion.fromString(JsonUtils.validateString(obj, "mcversion", true));
			updateURL = JsonUtils.validateString(jobj, "updateurl", false);
			folder = makeFolderName(JsonUtils.validateString(obj, "folder",false));
			
			JsonObject mods = JsonUtils.validateObject(obj, "mods", true);
			for(Entry<String,JsonElement> ee : mods.entrySet()) {
				if (ee.getKey().startsWith("##")) // lines with keys that start with ## are comments
					continue;
				if (!ee.getValue().isJsonObject())
					throw new JsonValidationException("Mod " + ee.getKey() + " is not a Json Object");
				ModFile mm = new ModFile();
				try {
					mm.loadJson(ee.getValue().getAsJsonObject(), ee.getKey());
				} catch (JsonValidationException e) {
					throw e.addToTree(ee.getKey());
				}
				modList.add(mm);
			}
		}
		catch(JsonValidationException ime) {
			String mpname = name != null ? name : fname;
			reset();
			throw ime.addToTree("mods").setLocation(mpname);
		}
	}
	
	private void reset() {
		name = null;
		version = null;
		mcversion = null;
		updateURL = null;
		authors.clear();
		modList.clear();
	}
	
	public boolean checkLauncherVersion(VersionData v) {
		System.out.println("Min: " + minLauncherVersion + "  max: " + maxLauncherVersion + "  version: " + v);
		if (minLauncherVersion != null && v.compareTo(minLauncherVersion) < 0)
			return false;
		if (maxLauncherVersion != null && v.compareTo(maxLauncherVersion) > 0)
			return false;
		return true;
	}
	
	public JsonObject getJson() {
		return obj;
	}
	
	public List<String> getAuthors() {
		return authors;
	}
	
	public List<ModFile> getMods() {
		return modList;
	}
	
	public File getFolder() {
		return new File(OSUtils.dataDir(), folder + File.separator + OSUtils.mcFolderName());
	}
	
	public String getUpdateURL() {
		return updateURL;
	}
	
	private static String makeFolderName(String name) {
		return name;
	}
}
