package gp.e3.autheo.client.exceptions;

public class InvalidStateException extends Exception {
	
	/**
	 * Exception to represent invalid states.
	 */
	private static final long serialVersionUID = 1L;

	public InvalidStateException(String errorMessage) {
		super(errorMessage);
	}
}