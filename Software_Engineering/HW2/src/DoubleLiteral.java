/**
 * a double literal class
 */
public class DoubleLiteral extends Expression {

    private final Double doubleLiteral;

    /**
     * constructor of IntegerLiteral
     * @param doubleLiteral a given int value
     */
    public DoubleLiteral(double doubleLiteral) {
        this.doubleLiteral = doubleLiteral;
    }

    /**
     * evaluate the doubleLiteral
     * @return the doubleLiteral in double
     */
    @Override
    public double evaluate() {
        return doubleLiteral;
    }

    /**
     * present the doubleLiteral
     * @return the doubleLiteral in string
     */
    @Override
    public String toString() {
        return "(" + doubleLiteral + ")";
    }

}
