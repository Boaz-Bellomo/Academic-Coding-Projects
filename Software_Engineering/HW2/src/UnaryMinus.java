/**
 * a minus representation of an expression
 */
public class UnaryMinus extends Expression {
    private final Expression expression;

    /**
     * constructor for UnaryMinus
     * @param expression the original expression
     */
    public UnaryMinus(Expression expression) {
        this.expression = expression;
    }

    /**
     * evaluate the negative expression
     * @return the negative expression
     */
    @Override
    public double evaluate() {
     return -expression.evaluate();
    }

    /**
     * present the negative expression
     * @return the negative expression in string
     */
    @Override
    public String toString()
    {
        return "(-" + expression + ")";
    }
}
