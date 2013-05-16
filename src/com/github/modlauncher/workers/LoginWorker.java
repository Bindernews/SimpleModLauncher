package com.github.modlauncher.workers;

import java.net.URL;
import java.net.URLEncoder;

import javax.swing.SwingWorker;

import com.github.modlauncher.net.NetUtils;

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
		return NetUtils.downloadString(url);
	}

	
}
