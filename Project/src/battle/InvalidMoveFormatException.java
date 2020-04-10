package battle;

public class InvalidMoveFormatException extends Exception{
	public InvalidMoveFormatException() {
	}

	public InvalidMoveFormatException(String message) {
		super(message);
	}

	public InvalidMoveFormatException(Throwable cause) {
		super(cause);
	}

	public InvalidMoveFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMoveFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
