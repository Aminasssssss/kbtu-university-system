package exceptions;

public class FailLimitException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates exception.
     */
    public FailLimitException(String message) {
        super(message);
    }
}
