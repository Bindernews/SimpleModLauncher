package com.github.vortexellauncher.util;

import java.io.PrintStream;
import java.util.Map.Entry;

public class DebugUtils {

	public static void printJavaProperties(PrintStream out) {
		for(Entry<Object,Object> ent : System.getProperties().entrySet()) {
			out.println(ent.getKey() + ": " + ent.getValue());
		}
	}
	
	public static void printEnvironment(PrintStream out) {
		for(Entry<String, String> ent : System.getenv().entrySet()) {
			out.println(ent.getKey() + "=" + ent.getValue());
		}
	}

}
