package com.github.vortexellauncher.workers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.SwingWorker;

import com.github.vortexellauncher.net.NetUtils;

public class LoginWorker extends SwingWorker<String, Void> {

	private String username;
	private String password;
	
	public LoginWorker(String uname, String pword) {
		username = uname;
		password = pword;
	}
	
	@Override
	protected String doInBackground() throws Exception {
		URL url = new URL("https://login.minecraft.net/?user=" 
				+ URLEncoder.encode(username, "UTF-8") + "&password=" 
				+ URLEncoder.encode(password, "UTF-8") + "&version=13");
		HttpURLConnection urlcon = (HttpURLConnection)url.openConnection();
		urlcon.setReadTimeout(2000);
		return NetUtils.downloadString(urlcon);
	}
}
