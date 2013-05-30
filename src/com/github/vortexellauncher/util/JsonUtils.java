package com.github.vortexellauncher.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLConnection;

import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
		public TL(Class<T> cls) {
			ctype = cls;
		}
		@SuppressWarnings("unchecked")
		public TL() {
			ctype = (Class<T>)(((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		}
		public T safeGetEnum(JsonObject obj, String ename) throws InvalidModpackException {
			String mtype = validateString(obj,ename,true);
			for(T mt : ctype.getEnumConstants()) {
				if (mt.name().equalsIgnoreCase(mtype)) {
					return mt;
				}
			}
			throw new InvalidModpackException("Invalid enum string");
		}
	}
	
	public static String validateString(JsonObject obj, String ename, boolean required) throws InvalidModpackException {
		if (!obj.has(ename)) {
			if (required)
				throw new InvalidModpackException("not defined").setProperty(ename);
			else
				return null;
		}
		if (!obj.get(ename).isJsonPrimitive())
			throw new InvalidModpackException("not a string").setProperty(ename);
		return obj.get(ename).getAsString();
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
