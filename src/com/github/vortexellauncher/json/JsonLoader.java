package com.github.vortexellauncher.json;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonLoader<T> {

	private Class<T> type;
	
	public T jsonLoad(JsonObject obj, T inst) {
		Gson gson = new Gson();
		HashSet<String> elemNames = new HashSet<String>();
		for(Entry<String, JsonElement> ent : obj.entrySet()) {
			elemNames.add(ent.getKey());
		}
		Field remainderField = null;
		
		for(Field f : type.getFields()) {
			try {
				JsonVal val = f.getAnnotation(JsonVal.class);
				if (val == null)
					continue;
				f.setAccessible(true);
				JsonElement subobj = obj.get(f.getName());
				if (val.type() == JsonType.Class) {
					if (f.getType() == String.class) {
						f.set(inst, obj.get(f.getName()).getAsString());
					}
					else if (f.getType().isEnum()) {
						f.set(inst, gson.fromJson(subobj, f.getType()));
					}
				}
				else if (val.type() == JsonType.String) {
					f.set(inst, subobj.getAsString());
				}
				else if (val.type() == JsonType.Remainder) {
					remainderField = f;
					continue; // skip trying to remove field name
				}
				else if (val.type() == JsonType.Array) {
					f.set(inst, gson.fromJson(subobj, f.getType()));
				}
				// remove the used field from the set
				elemNames.remove(f.getName());
			}
			catch(IllegalAccessException iae) {
				
			}
		}
		if (remainderField != null) {
		}
		return inst;
	}
}
