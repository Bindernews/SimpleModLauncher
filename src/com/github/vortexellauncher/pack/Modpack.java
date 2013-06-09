package com.github.vortexellauncher.pack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.github.vortexellauncher.MCVersion;
import com.github.vortexellauncher.OSUtils;
import com.github.vortexellauncher.VersionData;
import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Modpack {
	
	private JsonObject obj;

	public String name;
	public MCVersion mcversion;
	public VersionData version;
	public String folder;
	private String updateURL;
	private List<String> authors = new ArrayList<String>();
	private List<ModFile> modList = new ArrayList<ModFile>();
	
	public Modpack() {
		reset();
	}
	
	public Modpack(JsonObject jobj, String fname) throws InvalidModpackException {
		reset();
		loadJson(jobj, fname);
	}
	
	public void loadJson(JsonObject jobj, String fname) throws InvalidModpackException {
		obj = jobj;
		try {
			name = JsonUtils.validateString(obj,"name",true);
			version = VersionData.create(JsonUtils.validateString(obj,"version",true));
			mcversion = MCVersion.fromString(JsonUtils.validateString(obj,"mcversion",true));
			if (mcversion == null)
				throw new InvalidModpackException("Invalid minecraft version");
			
			updateURL = JsonUtils.validateString(jobj, "updateurl", false);
			folder = makeFolderName(JsonUtils.validateString(obj, "folder",false));
			
			if (!obj.has("mods"))
				throw new InvalidModpackException("No mods defined");
			if (!obj.get("mods").isJsonObject())
				throw new InvalidModpackException("\"mods\" is not a Json Object");
			
			JsonObject mods = obj.get("mods").getAsJsonObject();
			for(Entry<String,JsonElement> ee : mods.entrySet()) {
				ModFile mm = new ModFile();
				if (!ee.getValue().isJsonObject())
					throw new InvalidModpackException("Mod " + ee.getKey() + " is not a Json Object");
				try {
					mm.loadJson(ee.getValue().getAsJsonObject(), ee.getKey());
				}
				catch (InvalidModpackException e) {
					throw e.setModFile(mm);
				}
				modList.add(mm);
			}
		}
		catch(InvalidModpackException ime) {
			String mpname = name != null ? name : fname;
			reset();
			throw ime.setModpackName(mpname);
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
