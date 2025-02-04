package lsi.multinet.binpacking.exceptions;


public class BinFullException extends Exception {
 
    /**
     * Creates a new BinFullException
     *
     * @param msg a message
     */
    public BinFullException(String msg) {
        super(msg);
    }

    /**
     * Creates a new BinFullException
     *
     * @param cause the cause
     */
    public BinFullException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new BinFullException
     *
     * @param msg   a message
     * @param cause the cause
     */
    public BinFullException(String msg, Throwable cause) {
        super(msg, cause);
    }
}