package com.github.modlauncher;

public class LoginResponse {
	public final String latestVersion, downloadTicket, username, sessionID;
	
	public LoginResponse(String resp) {
		String[] responseValues = resp.split(":");
		if (responseValues.length < 4) {
			throw new IllegalArgumentException("Invalid response string.");
		} else {
			latestVersion = responseValues[0];
			downloadTicket = responseValues[1];
			username = responseValues[2];
			sessionID = responseValues[3];
		}
	}
}
