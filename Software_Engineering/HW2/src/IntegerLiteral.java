/**
 * a integer literal class
 */
public class IntegerLiteral extends DoubleLiteral {

    /**
     * constructor of IntegerLiteral
     * @param integerLiteral a given int value
     */
    public IntegerLiteral(int integerLiteral) {
        super(integerLiteral);
    }

    /**
     * present the IntegerLiteral
     * @return the IntegerLiteral in string
     */
    @Override
    public String toString() {
        return "(" + (int)super.evaluate() + ")";
    }
}
