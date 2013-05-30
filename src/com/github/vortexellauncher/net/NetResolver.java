package com.github.vortexellauncher.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class NetResolver {
	public NetResolver() {
		
	}	
	
	public static URLConnection resolveConnection(URL url, Proxy prox) throws IOException {
		URLConnection nextcon = proxyOpen(url,prox);
		URLConnection prevcon = null;
		do {
			setupHttpConnection(nextcon);
			prevcon = nextcon;
			nextcon = followRedirect(prevcon, prox);
		} while(prevcon != nextcon);
		return nextcon;
	}
	
	protected static URLConnection proxyOpen(URL url, Proxy prox) throws IOException {
		if (prox == null) {
			return url.openConnection();
		} else {
			return url.openConnection(prox);
		}
	}
	
	protected static URLConnection followRedirect(URLConnection urlcon, Proxy prox) throws IOException {
		if (urlcon instanceof HttpURLConnection) {
			HttpURLConnection htcon = (HttpURLConnection)urlcon;
			if (htcon.getResponseCode() == 307) {
				return proxyOpen(new URL(htcon.getHeaderField("Location")), prox);
			}
		}
		return urlcon;
	}
	
	protected static void setupHttpConnection(URLConnection urlcon) {
		if (urlcon instanceof HttpURLConnection) {
			HttpURLConnection htcon = (HttpURLConnection)urlcon;
			htcon.setInstanceFollowRedirects(true);
			htcon.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		}
		urlcon.setDoInput(true);
		urlcon.setAllowUserInteraction(true);
	}

}
