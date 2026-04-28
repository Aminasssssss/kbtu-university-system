package exceptions;

/**
 * Thrown when a non-researcher tries to join a research project.
 */
public class NotResearcherException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception with the given message.
     * @param message the detail message
     */
    public NotResearcherException(String message) {
        super(message);
    }
}