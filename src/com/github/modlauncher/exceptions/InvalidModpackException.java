package com.github.modlauncher.exceptions;

public class InvalidModpackException extends Exception {
	private static final long serialVersionUID = 1992197842629577135L;

	public InvalidModpackException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public InvalidModpackException(String msg) {
		super(msg);
	}
	
	public InvalidModpackException(Throwable cause) {
		super(cause);
	}
}
