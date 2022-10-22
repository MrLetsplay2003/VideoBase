package me.mrletsplay.videobase.exception;

public class LibraryLoadException extends Exception {

	private static final long serialVersionUID = -8687336563029326213L;

	public LibraryLoadException() {
		super();
	}

	public LibraryLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public LibraryLoadException(String message) {
		super(message);
	}

	public LibraryLoadException(Throwable cause) {
		super(cause);
	}

}
