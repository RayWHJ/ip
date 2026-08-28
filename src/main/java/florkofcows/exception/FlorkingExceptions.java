package florkofcows.exception;

/**
 * Application-specific checked exception used to report input and runtime
 * errors to the user-facing UI.
 */
public class FlorkingExceptions extends Exception {
    public FlorkingExceptions(String message) {
        super(message);
    }
}
