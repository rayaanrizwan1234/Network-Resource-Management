package lsi.multinet.binpacking.exceptions;

public class DuplicateElementException extends Exception {
    /**
     * Creates a new DuplicateElementException
     *
     * @param msg a message
     */
    public DuplicateElementException(String msg) {
        super(msg);
    }

    /**
     * Creates a new DuplicateElementException
     *
     * @param cause the cause
     */
    public DuplicateElementException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new DuplicateElementException
     *
     * @param msg   a message
     * @param cause the cause
     */
    public DuplicateElementException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
