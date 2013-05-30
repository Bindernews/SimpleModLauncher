package com.github.vortexellauncher.pack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.vortexellauncher.OS;
import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PackMetaManager {

	private File metaFile = new File(OS.dataDir(), "ModpackCache.txt");
	private JsonObject metadata;
	private JsonObject cacheJson;
	private Gson gson = new Gson();
	
	public PackMetaManager() throws IOException {
		try {
			cacheJson = JsonUtils.readJsonFile(metaFile).getAsJsonObject();
			metadata = cacheJson.getAsJsonObject("packs");
		} catch (IOException e) {
			cacheJson = new JsonObject();
			metadata = new JsonObject();
			cacheJson.add("packs", metadata);
			saveJson();
		}
	}
	
	public File getFolder(String pack) {
		File fold = new File(asString(getPackElem(pack, "folder")));
		if (!fold.isAbsolute()) {
			fold = new File(OS.dataDir(), fold.getPath());
		}
		return fold;
	}
	
	public File getModpackJsonFile(String pack) {
		return new File(getFolder(pack).getParentFile(), "modpack.json");
	}
	
	public Modpack loadModpack(String pack) throws IOException, InvalidModpackException {
		File file = getModpackJsonFile(pack);
		JsonObject jobj = JsonUtils.readJsonFile(file).getAsJsonObject();
		Modpack modpack = new Modpack(jobj, pack);
		return modpack;
	}
	
	public boolean hasPack(String pack) {
		if (!metadata.has(pack)) {
			return false;
		}
		File packFile = new File(getFolder(pack).getParentFile(), "modpack.json");
		if (!packFile.exists()) {
			return false;
		}
		return true;
	}
	
	public String updatePack(JsonObject jobj, String name) throws IOException, InvalidModpackException {
		Modpack modpack = new Modpack(jobj, name);
		
		JsonObject ob = new JsonObject();
		ob.addProperty("folder", modpack.getFolder().getAbsolutePath());
		ob.addProperty("updateurl", modpack.getUpdateURL());
		metadata.add(modpack.name, ob);
		
		File packFile = getModpackJsonFile(modpack.name);
		packFile.getParentFile().mkdirs();
		FileWriter fwriter = new FileWriter(packFile);
		fwriter.write(JsonUtils.getPrettyGson().toJson(jobj));
		fwriter.close();
		
		saveJson();
		return modpack.name;
	}
	
	private void saveJson() throws IOException {
		FileWriter fwriter = new FileWriter(metaFile);
		fwriter.write(gson.toJson(cacheJson));
		fwriter.close();
	}
	
	private String asString(JsonElement elem) {
		return elem != null ? elem.getAsString() : null; 
	}
	private JsonElement getPackElem(String pack, String member) {
		if (!metadata.has(pack)) {
			return null;
		}
		return metadata.getAsJsonObject(pack).get(member);
	}
}
