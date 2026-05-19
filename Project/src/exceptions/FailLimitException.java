package exceptions;

/**
 * Thrown when a student fails a course more than 3 times.
 */
public class FailLimitException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception with the given message.
     * @param message the detail message
     */
    public FailLimitException(String message) {
        super(message);
    }
}