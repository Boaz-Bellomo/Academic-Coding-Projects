/**
 * a abstract multi arithmetic operation of an expression
 */
public abstract class MultiArithmeticOperation extends  Expression{
    private final Expression expressions1;
    private final Expression expressions2;
    private final Expression[] expressions;
    private final String operation;

    /**
     * constructor for all multi arithmetic operation, there ar at lest 2 expressions in action
     * @param expression1 first expression
     * @param expression2 second expression
     * @param operation type of operation in String
     * @param expressions all other expressions in operation
     */
    public MultiArithmeticOperation(String operation,Expression expression1, Expression expression2, Expression... expressions) {
        this.expressions1 = expression1;
        this.expressions2 = expression2;
        this.expressions = expressions;
        this.operation = operation;
    }

    /**
     * calculate the evaluation of all components for etch operation case
     * @return evaluation base on operation type
     */
    @Override
    public double evaluate() {
        double evaluation = 0;
        // if Addition
        if (operation.equals("+")) {
            evaluation = expressions1.evaluate() + expressions2.evaluate();
            for (Expression value : expressions) {
                evaluation += value.evaluate();
            }
        }

        // if multiplication
        else if (operation.equals("*")){
            evaluation = expressions1.evaluate() * expressions2.evaluate();
            for (Expression value : expressions) {
                evaluation *= value.evaluate();
            }
        }
        return evaluation;
    }

    /**
     * present the calculation that led to the evaluation
     * @return a string of all components in calculation
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("("); // add opening bracket
        str.append(expressions1).append(" ").append(operation).append(" ").append(expressions2);

        // for etch component in expressions
        for (Expression expression: expressions){
            // add operation between components
            str.append(" ").append(operation).append(" ");

            // add component to the expression
            str.append(expression.toString());
        }
        str.append(")"); // add closing bracket
        return str.toString();
    }
}
