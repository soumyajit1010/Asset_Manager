package assetmamager.exception;

/**
 * Exception thrown when an invalid asset operation occurs.
 */
public class InvalidAssetsException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidAssetsException(String message) {
		super(message);
	}
}
