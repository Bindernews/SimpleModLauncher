package com.github.modlauncher.net;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class NetUtils {

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @see {@link https://github.com/Slowpoke101/FTBLaunch}
	 */
	public static String downloadString(URL url) throws IOException {
		Scanner scan = new Scanner(url.openStream());
		scan.useDelimiter("\\A");
		String val = scan.hasNext() ? scan.next() : "";
		scan.close();
		return val;
	}
}
