package exceptions;

/**
 * Thrown when a student exceeds the maximum allowed credits (21).
 */
public class CourseOverloadException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception with the given message.
     * @param message the detail message
     */
    public CourseOverloadException(String message) {
        super(message);
    }
}