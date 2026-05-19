package exceptions;

/**
 * Thrown when a researcher with h-index less than 3 is assigned as a supervisor.
 */
public class LowHIndexException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception with the given message.
     * @param message the detail message
     */
    public LowHIndexException(String message) {
        super(message);
    }
}