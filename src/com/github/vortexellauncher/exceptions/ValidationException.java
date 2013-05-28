package com.github.vortexellauncher.exceptions;

public class ValidationException extends Exception {
	private static final long serialVersionUID = -555417081762717479L;

	public ValidationException() {
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
