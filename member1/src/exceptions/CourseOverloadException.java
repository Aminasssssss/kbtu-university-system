package exceptions;

public class CourseOverloadException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates exception.
     */
    public CourseOverloadException(String message) {
        super(message);
    }
}
