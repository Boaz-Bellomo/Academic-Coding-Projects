/**
 * a subtraction operation of an expression
 */
public class Subtraction extends ArithmeticOperation {

    /**
     * constructor for Subtraction
     * @param expression1 first component in arithmetic operation
     * @param expression2 second component in arithmetic operation
     */
    public Subtraction (Expression expression1, Expression expression2){
        super("-", expression1, expression2);
    }
}
