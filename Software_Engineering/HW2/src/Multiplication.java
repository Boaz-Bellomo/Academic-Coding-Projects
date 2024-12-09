/**
 * a multiplication operation of an expression
 */
public class Multiplication extends ArithmeticOperation {

    /**
     * constructor for Multiplication
     * @param expression1 first component in arithmetic operation
     * @param expression2 second component in arithmetic operation
     */
    public Multiplication (Expression expression1, Expression expression2){
        super("*", expression1, expression2);
    }
}
