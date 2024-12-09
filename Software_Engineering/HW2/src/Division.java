/**
 * a division operation of an expression
 */
public class Division extends ArithmeticOperation {

    /**
     * constructor for Division
     * @param expression1 first component in arithmetic operation
     * @param expression2 second component in arithmetic operation
     */
    public Division (Expression expression1, Expression expression2){
        super("/", expression1, expression2);
    }
}
