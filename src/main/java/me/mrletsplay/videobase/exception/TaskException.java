package me.mrletsplay.videobase.exception;

public class TaskException extends RuntimeException {

	private static final long serialVersionUID = 9138351232713900090L;

	public TaskException() {
		super();
	}

	public TaskException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskException(String message) {
		super(message);
	}

	public TaskException(Throwable cause) {
		super(cause);
	}

}
