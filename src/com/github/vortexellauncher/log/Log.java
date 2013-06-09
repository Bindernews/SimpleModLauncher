package com.github.vortexellauncher.log;

public class Log {

	private static Log globalLog;
	
	public void info(String message) {
		
	}
	
	public void warning(String message) {
		
	}
	
	public void error(Throwable t) {
		
	}
	
	public void error(String message) {
		
	}
	
	public void error(String message, Throwable t) {
		
	}
	
	public static Log getLog() {
		return globalLog;
	}
}
