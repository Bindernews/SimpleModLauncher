package com.github.vortexellauncher.util;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class OSUtils {

	private OSUtils() {}
	
	
	public static void openBrowser(URI uri) throws IOException {
		if (!Desktop.isDesktopSupported())
			return;
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE))
		{
			desktop.browse(uri);
		}
	}
	
	public static void openBrowser(URL url) throws IOException {
		try {
			openBrowser(url.toURI());
		} catch (URISyntaxException e) {
			ErrorUtils.printException(e,false);
		}
	}

}
