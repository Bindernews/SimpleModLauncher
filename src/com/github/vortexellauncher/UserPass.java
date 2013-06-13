package com.github.vortexellauncher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.github.vortexellauncher.util.Utils;

public class UserPass {

	private static final String lastLoginSepStr = ":`:!@:`:";
	
	private String username;
	private String password;
	
	public UserPass(String text) {
		String[] split = text.split(lastLoginSepStr, 2);
		username = split[0];
		password = split[1];
	}
	
	public UserPass(String user, String pass) {
		username = user;
		password = pass;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUsername(String user) {
		username = user;
	}
	
	public void setPassword(String pass) {
		password = pass;
	}
	
	public String toString() {
		return username;
	}
	
	public String combine() {
		return username + lastLoginSepStr + password;
	}
	
	public static UserPass[] loadFromFile(File f) throws IOException {
		String str = Utils.simpleCryptIn(f);
		StringTokenizer st = new StringTokenizer(str, "\n\r");
		ArrayList<UserPass> ups = new ArrayList<UserPass>();
		while(st.hasMoreTokens()) {
			ups.add(new UserPass(st.nextToken()));
		}
		return ups.toArray(new UserPass[ups.size()]);
	}
	
	public static void saveToFile(File f, UserPass[] ups) {
		
	}
}
