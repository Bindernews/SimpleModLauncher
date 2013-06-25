package com.github.vortexellauncher.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.github.vortexellauncher.VersionData;
import com.github.vortexellauncher.exceptions.JsonValidationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {
	
	private JsonUtils() {}
	
	private static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
	
	public static Gson getPrettyGson() {
		return prettyGson;
	}
	
	public static class TL<T extends Enum<T>> {
		Class<T> ctype;
		public TL(Class<T> cls) { ctype = cls; }
		public T safeGetEnum(JsonObject obj, String ename) throws JsonValidationException {
			String mtype = validateString(obj,ename,true);
			for(T mt : ctype.getEnumConstants()) {
				if (mt.name().equalsIgnoreCase(mtype)) {
					return mt;
				}
			}
			throw new JsonValidationException("Invalid enum string").setProperty(ename);
		}
	}
	
	public static String validateString(JsonObject obj, String ename, boolean required) throws JsonValidationException {
		if (!checkHas(obj,ename,required))
			return null;
		if (!obj.get(ename).isJsonPrimitive())
			throw new JsonValidationException("not a string").setProperty(ename);
		return obj.get(ename).getAsString();
	}
	
	public static VersionData validateVersion(JsonObject obj, String ename, boolean required) throws JsonValidationException {
		return VersionData.create(validateString(obj,ename,required));
	}
	
	public static JsonArray validateArray(JsonObject obj, String ename, boolean required) throws JsonValidationException {
		if (!checkHas(obj,ename,required))
			return null;
		if (!obj.get(ename).isJsonArray())
			throw new JsonValidationException("not a Json Array").setProperty(ename);
		return obj.get(ename).getAsJsonArray();
	}
	
	public static JsonObject validateObject(JsonObject obj, String ename, boolean required) throws JsonValidationException {
		if (!checkHas(obj,ename,required))
			return null;
		if (!obj.get(ename).isJsonObject())
			throw new JsonValidationException("not a Json Object").setProperty(ename);
		return obj.get(ename).getAsJsonObject();
	}
	
	public static List<String> validateStringArray(JsonObject obj, String ename, boolean required) throws JsonValidationException {
		JsonArray jsa = validateArray(obj,ename,required);
		if (jsa == null)
			return null;
		List<String> l = new ArrayList<String>();
		for(JsonElement e : jsa) {
			if (!e.isJsonPrimitive())
				throw new JsonValidationException("cannot convert to string").setProperty(ename);
			l.add(e.getAsString());
		}
		return l;
	}
	
	private static boolean checkHas(JsonObject obj, String ename, boolean required) throws JsonValidationException {
		if (!obj.has(ename)) {
			if (required)
				throw new JsonValidationException("not defined").setProperty(ename);
			else
				return false;
		}
		return true;
	}
	
	public static JsonElement readJsonURL(URL url) throws IOException {
		InputStreamReader r = new InputStreamReader(url.openStream());
		JsonParser parser = new JsonParser();
		JsonElement elem = parser.parse(r);
		r.close();
		return elem;
	}
	
	public static JsonElement readJsonURLConnection(URLConnection urlcon) throws IOException {
		InputStreamReader r = new InputStreamReader(urlcon.getInputStream());
		JsonParser parser = new JsonParser();
		JsonElement elem = parser.parse(r);
		r.close();
		return elem;
	}
	
	public static JsonElement readJsonFile(File file) throws IOException {
		FileReader r = new FileReader(file);
		JsonParser parser = new JsonParser();
		JsonElement elem = parser.parse(r);
		r.close();
		return elem;
	}
}
