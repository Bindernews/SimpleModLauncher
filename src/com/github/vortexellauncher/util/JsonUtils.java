package com.github.vortexellauncher.util;

import java.lang.reflect.ParameterizedType;

import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.google.gson.JsonObject;

public class JsonUtils {
	
	private JsonUtils() {}
	
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
}
