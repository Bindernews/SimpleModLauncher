package com.github.vortexellauncher.exceptions;

public class HashComparionException extends RuntimeException {
	private static final long serialVersionUID = 6433270927911974585L;

	public HashComparionException() {
	}

	public HashComparionException(String message) {
		super(message);
	}

	public HashComparionException(Throwable cause) {
		super(cause);
	}

	public HashComparionException(String message, Throwable cause) {
		super(message, cause);
	}
}
