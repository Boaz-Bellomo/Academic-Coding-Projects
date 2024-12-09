/**
 * a runtime exception class for division by zero
 */
public class TaskAlreadyExistsException extends RuntimeException{
    /**
     * basic exception constructor
     */
    public TaskAlreadyExistsException(){}

    /**
     * message exception constructor
     * @param massage for user
     */
    public TaskAlreadyExistsException(String massage){
        super(massage);
    }

    /**
     * extended exception constructor
     * @param massage for user
     * @param cause of exception
     */
    public TaskAlreadyExistsException(String massage, Throwable cause){
        super(massage, cause);
    }
}
