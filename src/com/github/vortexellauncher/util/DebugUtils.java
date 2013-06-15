package com.github.vortexellauncher.util;

import java.io.PrintStream;
import java.util.Map.Entry;

public class DebugUtils {
	
	public static void printEnvironment(PrintStream out) {
		for(Entry<String, String> ent : System.getenv().entrySet()) {
			out.println(ent.getKey() + "=" + ent.getValue());
		}
	}

}
