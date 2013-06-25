package com.github.vortexellauncher.exceptions;

public class InvalidModpackException extends JsonValidationException {
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
	
	public InvalidModpackException setProperty(String property) {
		super.setProperty(property);
		return this;
	}
	
	public InvalidModpackException setLocation(String location) {
		super.setLocation(location);
		return this;
	}
	
	@Override
	public InvalidModpackException addToTree(String tpath) {
		super.addToTree(tpath);
		return this;
	}
}
