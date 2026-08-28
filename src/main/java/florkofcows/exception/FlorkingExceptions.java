package florkofcows.exception;

/**
<<<<<<< HEAD
 * Represents an error specific to FlorkOfCows, such as invalid user input
 * or a failure while saving/loading tasks.
=======
 * Application-specific checked exception used to report input and runtime
 * errors to the user-facing UI.
>>>>>>> branch-A-CodingStandard
 */
public class FlorkingExceptions extends Exception {

    /**
     * Creates an exception with the given user-facing error message.
     *
     * @param message the error message to display.
     */
    public FlorkingExceptions(String message) {
        super(message);
    }
}
