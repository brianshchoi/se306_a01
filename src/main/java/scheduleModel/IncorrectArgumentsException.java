package scheduleModel;

/**
 * This exception is thrown when the user does not enter a valid number of arguments.
 */
public class IncorrectArgumentsException extends RuntimeException {

    public IncorrectArgumentsException(String string) {
        super(string);
    }
}