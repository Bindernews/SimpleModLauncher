package com.github.vortexellauncher.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.github.vortexellauncher.Main;

public class ErrorUtils {
	
	public static void showException(Throwable e, boolean fatal) {
		showException("", e, fatal);
	}
	
	public static void showException(String message, Throwable e, boolean fatal) {
		System.err.println(message);
		e.printStackTrace();
		Main.logView().setVisible(true);
	}
	
	public static String getExceptionString(Throwable e) {
		StringWriter sw = new StringWriter(500);
		e.printStackTrace(new PrintWriter(sw));
		return sw.getBuffer().toString();
	}

	
}
