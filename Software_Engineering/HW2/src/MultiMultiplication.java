/**
 * a multi addition operation of an expression
 */
public class MultiMultiplication extends MultiArithmeticOperation {

    /**
     * constructor for MultiAddition. there are a lest 2 expression in action
     * @param expression1 first expression
     * @param expression2 second expression
     * @param values all other expression in arithmetic operation
     */
    public MultiMultiplication(Expression expression1, Expression expression2, Expression... values) {
        super("*", expression1,expression2, values);
    }
}

