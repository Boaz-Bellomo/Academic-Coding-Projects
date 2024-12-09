/**
 * a runtime exception class for division by zero
 */
public class DivisionByZeroException extends RuntimeException{
    /**
     * basic exception constructor
     */
    public DivisionByZeroException(){}

    /**
     * message exception constructor
     * @param massage for user
     */
    public DivisionByZeroException(String massage){
        super(massage);
    }

    /**
     * extended exception constructor
     * @param massage for user
     * @param cause of exception
     */
    public DivisionByZeroException(String massage, Throwable cause){
        super(massage, cause);
    }
}
