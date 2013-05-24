package com.github.modlauncher.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Res {

	private static ClassLoader sysloader = ClassLoader.getSystemClassLoader(); 
	
	public static Font mcfont;
	
	public static void init() throws IOException {
		// load font
		try {
			mcfont = Font.createFont(Font.TRUETYPE_FONT, sysloader.getResourceAsStream("res/minecraft.ttf"));
		} catch (FontFormatException e) {
			throw new IOException(e);
		}
	}
	
	public static InputStream getResourceStream(String name) {
		return sysloader.getResourceAsStream(name);
	}
	
	public static InputStreamReader getResourceReader(String name) {
		return new InputStreamReader(getResourceStream(name));
	}
	
	public static URL getURL(String name) {
		return sysloader.getResource(name);
	}
}
