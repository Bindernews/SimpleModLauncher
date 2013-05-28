package com.github.vortexellauncher.exceptions;

import com.github.vortexellauncher.pack.ModFile;

public class InvalidModpackException extends Exception {
	private static final long serialVersionUID = 1992197842629577135L;
	
	private String packName = null;
	private ModFile file = null;
	private String property = null;
	
	public InvalidModpackException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public InvalidModpackException(String msg) {
		super(msg);
	}
	
	public InvalidModpackException(Throwable cause) {
		super(cause);
	}
	
	public InvalidModpackException setModpackName(String name) {
		packName = name;
		return this;
	}
	
	public InvalidModpackException setProperty(String prop) {
		property = prop;
		return this;
	}
	
	public InvalidModpackException setModFile(ModFile mf) {
		file = mf;
		return this;
	}
	
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Error ");
		if (packName != null) {
			sb.append("in modpack ");
			sb.append(packName);
			sb.append(" ");
		}
		if (file != null) {
			sb.append("with mod ");
			sb.append(file.name);
			sb.append(" ");
		}
		if (property != null) {
			sb.append("with property ");
			sb.append(property);
			sb.append(" ");
		}
		sb.append(" - ");
		sb.append(super.getMessage());
		return sb.toString();
	}
}
