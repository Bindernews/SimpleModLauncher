package com.github.vortexellauncher.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ImageIcon;

public class Res {

	private static ClassLoader sysloader = ClassLoader.getSystemClassLoader(); 
	
	public static Font mcfont;
	public static ImageIcon defaultModpackIcon = new ImageIcon(getURL("res/default_icon.png"));
	public static ImageIcon iconError = new ImageIcon(getURL("res/error.png"));
	
	
	public static void init() throws IOException, FontFormatException {
		// load font
		mcfont = Font.createFont(Font.TRUETYPE_FONT, sysloader.getResourceAsStream("res/minecraft.ttf"));
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
