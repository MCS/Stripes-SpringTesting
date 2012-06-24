package myproj.exception;

public class ImplementedLaterException extends RuntimeException {

    public ImplementedLaterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ImplementedLaterException(Throwable cause) {
        super(cause);
    }

    public ImplementedLaterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImplementedLaterException(String message) {
        super(message);
    }

    public ImplementedLaterException() {
    }
}
