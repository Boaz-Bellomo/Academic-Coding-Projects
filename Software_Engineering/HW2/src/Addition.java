/**
 * an addition operation of an expression
 */
public class Addition extends ArithmeticOperation {

    /**
     * constructor for Addition
     * @param expression1 first component in arithmetic operation
     * @param expression2 second component in arithmetic operation
     */
    public Addition (Expression expression1, Expression expression2){
        super("+", expression1, expression2);
    }
}
