/**
 * a abstract class for an arithmetic expression
 */
public abstract class Expression {
    /**
     * abstract evaluate method
     * @return double
     */
    public abstract double evaluate();

    /**
     * abstract toString method
     * @return string
     */
    @Override
    public abstract String toString();
}
