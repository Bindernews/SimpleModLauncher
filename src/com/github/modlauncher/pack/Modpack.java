package com.github.modlauncher.pack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.github.modlauncher.exceptions.InvalidModpackException;
import com.github.modlauncher.json.JsonType;
import com.github.modlauncher.json.JsonVal;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Modpack {
	
	private JsonObject obj;

	public String name;
	public String mcversion;
	public String version;
	public String folder;
	
	@JsonVal(type=JsonType.Remainder, required=true, classType=ModFile.class)
	private List<ModFile> modList;
	
	public Modpack() {
		reset();
	}
	
	public JsonObject getJson() {
		return obj;
	}
	
	public List<ModFile> getMods() {
		return modList;
	}
	
	public void loadJson(JsonObject jobj, String fname) throws InvalidModpackException {
		obj = jobj;
		try {
			name = validateString(obj,"name");
			version = validateString(obj,"version");
			mcversion = validateString(obj,"mcversion");
			
			if (obj.has("folder"))
				folder = makeFolderName(validateString(obj, "folder"));
			
			if (!obj.has("mods"))
				throw new InvalidModpackException("No mods defined");
			if (!obj.get("mods").isJsonObject())
				throw new InvalidModpackException("\"mods\" is not a Json Object");
			
			JsonObject mods = obj.get("mods").getAsJsonObject();
			modList = new ArrayList<ModFile>();
			for(Entry<String,JsonElement> ee : mods.entrySet()) {
				ModFile mm = new ModFile();
				if (!ee.getValue().isJsonObject())
					throw new InvalidModpackException("Mod " + ee.getKey() + " is not a Json Object");
				mm.loadJson(ee.getValue().getAsJsonObject(), ee.getKey());
				modList.add(mm);
			}
		}
		catch(InvalidModpackException ime) {
			reset();
			throw new InvalidModpackException("[" + fname + "] " + ime.getMessage());
		}
		
	}
	
	private void reset() {
		name = null;
		version = null;
		mcversion = null;
		modList = null;
	}
	
	private static String validateString(JsonObject obj, String ename) throws InvalidModpackException {
		if (!obj.has(ename))
			throw new InvalidModpackException("\"" + ename + "\" not defined");
		if (!obj.get(ename).isJsonPrimitive())
			throw new InvalidModpackException("\"" + ename + "\" is not a string");
		return obj.get(ename).getAsString();
	}
	
	private static String makeFolderName(String name) {
		return name;
	}
}
