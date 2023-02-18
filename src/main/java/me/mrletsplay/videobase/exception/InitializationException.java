package me.mrletsplay.videobase.exception;

public class InitializationException extends RuntimeException {

	private static final long serialVersionUID = -8044990856159512028L;

	public InitializationException() {
		super();
	}

	public InitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitializationException(String message) {
		super(message);
	}

	public InitializationException(Throwable cause) {
		super(cause);
	}

}
