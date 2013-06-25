package com.github.vortexellauncher.exceptions;

import java.util.ArrayList;

public class JsonValidationException extends Exception {
	private static final long serialVersionUID = 98604669291916732L;

	private String location = null;
	private String property = null;
	private ArrayList<String> tree = new ArrayList<String>();
	
	public JsonValidationException() {
	}

	public JsonValidationException(String arg0) {
		super(arg0);
	}

	public JsonValidationException(Throwable arg0) {
		super(arg0);
	}
	
	public JsonValidationException(String msg, Throwable t) {
		super(msg, t);
	}
	
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Error ");
		if (location != null) {
			sb.append("at ");
			sb.append(location);
			sb.append(" ");
		}
		sb.append("in ");
		for(int i=0; i<tree.size(); i++) {
			sb.append(tree.get(i));
			if (i < tree.size() - 1)
				sb.append(" > ");
		}
		if (property != null) {
			sb.append("property ");
			sb.append(property);
			sb.append(" ");
		}
		sb.append(" - ");
		sb.append(super.getMessage());
		return sb.toString();
	}

	public String getLocation() {
		return location;
	}

	public JsonValidationException setLocation(String location) {
		this.location = location;
		return this;
	}
	
	public JsonValidationException setProperty(String property) {
		this.property = property;
		return this;
	}
	
	public JsonValidationException addToTree(String tpath) {
		tree.add(tpath);
		return this;
	}

}
