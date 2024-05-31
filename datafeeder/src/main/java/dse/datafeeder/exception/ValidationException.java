package dse.datafeeder.exception;

public class ValidationException extends RuntimeException {
    // Thrown when a validation error occurred.
    public ValidationException(String message) {
        super(message);
    }
}
