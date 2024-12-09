/**
 * a rounded representation of an expression
 */
public class RoundedExpression extends Expression {
    private final Expression expression;
    private Expression roundedExpression;
    private final int decimalAccuracy;

    /**
     * construct roundedNum by setter method
     * @param expression the num to round
     * @param decimalAccuracy how many numbers are wanted after the decimal point
     */
    public RoundedExpression(Expression expression, int decimalAccuracy) {
        this.expression = expression;
        this.decimalAccuracy = decimalAccuracy;
    }

    /**
     * round expression to a wanted decimal accuracy
     * @return the round expression in IntegerLiteral or DoubleLiteral
     */
    @Override
    public double evaluate() {
        if (decimalAccuracy == 0) { // if int wanted
            this.roundedExpression = new IntegerLiteral((int) expression.evaluate());

        } else { // if double wanted
            double rounder = Math.pow(10, decimalAccuracy);
            double rounded = (double) Math.round(expression.evaluate()*rounder)/rounder;
            this.roundedExpression = new DoubleLiteral(rounded);
        }
        return roundedExpression.evaluate();
    }

    /**
     * present the rounded expression
     * @return the rounded expression in string
     */
    @Override
    public String toString() {
        return expression.toString();
    }
}
