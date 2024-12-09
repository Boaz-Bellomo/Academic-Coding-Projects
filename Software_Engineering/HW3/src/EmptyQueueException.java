/**
 * a runtime exception class for division by zero
 */
public class EmptyQueueException extends RuntimeException{
    /**
     * basic exception constructor
     */
    public EmptyQueueException(){}

    /**
     * message exception constructor
     * @param massage for user
     */
    public EmptyQueueException(String massage){
        super(massage);
    }

    /**
     * extended exception constructor
     * @param massage for user
     * @param cause of exception
     */
    public EmptyQueueException(String massage, Throwable cause){
        super(massage, cause);
    }
}
