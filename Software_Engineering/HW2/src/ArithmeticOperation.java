/**
 * an abstract arithmetic operation of an expression
 */
public abstract class ArithmeticOperation extends Expression{
    private final Expression expression1;
    private final Expression expression2;
    private final String operation;

    /**
     * constructor for all arithmetic operation
     * @param operation type of operation in String
     * @param expression1 first component in operation
     * @param expression2 second component in operation
     */
    public ArithmeticOperation(String operation, Expression expression1, Expression expression2){
        this.operation = operation;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    /**
     * for etch case of operation, make the fitting calculation
     * @return evaluation base on operation type
     * @throws DivisionByZeroException when trying to divide by zero
     */
    public double evaluate() throws DivisionByZeroException {
        switch (operation) {
            case "+":
                return expression1.evaluate() + expression2.evaluate();
            case "-":
                return expression1.evaluate() - expression2.evaluate();
            case "*":
                return expression1.evaluate() * expression2.evaluate();
            case "/": {
                if (expression2.evaluate() == 0) { // trying to divide by zero
                    throw new DivisionByZeroException();
                } else {
                    return expression1.evaluate() / expression2.evaluate();}
            }
            default:
                return -1;
        }
    }

    /**
     * present the calculation that led to the evaluation
     * @return a string of all components in calculation
     */
    @Override
    public String toString() {
        return "(" + expression1.toString() + " " + operation + " " + expression2.toString() + ")";
    }

}
