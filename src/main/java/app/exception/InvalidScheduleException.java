package app.exception;

/**
 * Thrown when a schedule does not confirm to its constraints.
 */
public class InvalidScheduleException extends RuntimeException {
    public InvalidScheduleException(String message) {
        super(message);
    }
}
