package assetmamager.exception;

/**
 * Exception thrown when an employee's experience is invalid for a certain operation.
 */
public class InvalidExperienceException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidExperienceException(String message) {
		super(message);
	}
}
