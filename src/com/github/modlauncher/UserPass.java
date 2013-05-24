package com.github.modlauncher;

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
}
