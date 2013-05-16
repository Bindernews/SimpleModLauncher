package com.github.modlauncher.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

public class Res {

	public static Font mcfont;
	
	public static void init() throws IOException {
		ClassLoader sysloader = ClassLoader.getSystemClassLoader();
		
		// load font
		try {
			mcfont = Font.createFont(Font.TRUETYPE_FONT, sysloader.getResourceAsStream("res/minecraft.ttf"));
		} catch (FontFormatException e) {
			throw new IOException(e);
		}
		
		
	}
}
