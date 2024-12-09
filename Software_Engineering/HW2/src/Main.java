public class Main {
    public static void main(String[] args) {
        testPartA();
        testPartB();
    }

    private static void testPartA() {
        System.out.println("-------------------- Tests for part A --------------------");
        Clock c1 = new Clock(10, 30);
        Object o1 = c1;
        AccurateClock ac1 = new AccurateClock(10, 30, 20);
        Clock c2 = ac1;
        Object o2 = c2;
        System.out.println("c1 with o1: " + c1.equals(o1));
        System.out.println("o1 with c1: " + o1.equals(c1));

        System.out.println("c1 with ac1: " + c1.equals(ac1));
        System.out.println("ac1 with c1: " + ac1.equals(c1));
        System.out.println("c1 with c2: " + c1.equals(c2));
        System.out.println("c2 with c1: " + c2.equals(c1));
        System.out.println("c1 with o2: " + c1.equals(o2));
        System.out.println("o2 with c1: " + o2.equals(c1));

        System.out.println("ac1 with c2: " + ac1.equals(c2));
        System.out.println("c2 with ac1: " + c2.equals(ac1));
        System.out.println("ac1 with o2: " + ac1.equals(o2));
        System.out.println("o2 with ac1: " + o2.equals(ac1));
        System.out.println("c2 with o2: " + c2.equals(o2));
        System.out.println("o2 with c2: " + o2.equals(c2));
        System.out.println("c1 with null: " + c1.equals(null));
        System.out.println("o1 with null: " + o1.equals(null));
        System.out.println("ac1 with null: " + ac1.equals(null));

        System.out.println("c1: " + c1);
        System.out.println("o1: " + o1);
        System.out.println("ac1: " + ac1);
        System.out.println("c2: " + c2);
        System.out.println("o2: " + o2);
        System.out.println("new: " + new AccurateClock(5, 6, 3));
    }

    private static void testPartB() {
        System.out.println("-------------------- Tests for part B --------------------");

        Expression intExp1 = new IntegerLiteral(12345);
        printExpression(intExp1, "intExp1");

        Expression doubleExp1 = new DoubleLiteral(7.54321);
        printExpression(doubleExp1, "doubleExp1");

        Expression intExp2 = new IntegerLiteral(-1709);

        Expression exp1 = new Multiplication(
                new Addition(
                        intExp1,
                        doubleExp1),
                intExp2);
        printExpression(exp1, "exp1");

        Expression exp2 = new Multiplication(
                new Addition(
                        intExp1,
                        doubleExp1),
                new UnaryMinus(
                        new UnaryMinus(
                                intExp2)));
        printExpression(exp2, "exp2");

        Expression exp3 = new Subtraction(intExp1, intExp2);
        printExpression(exp3, "exp3");

        Expression divisionExp = new Division(
                new Addition(
                        new Subtraction(
                                new Multiplication(
                                        intExp1,
                                        intExp2),
                                doubleExp1),
                        exp2),
                exp1);
        printExpression(divisionExp, "divisionExp");

        Expression divExp2 = new Division(new IntegerLiteral(1), new IntegerLiteral(0));
        printExpression(divExp2, "divExp2");

        Expression divExp3 = new Division(new IntegerLiteral(1), new DoubleLiteral(0));
        printExpression(divExp3, "divExp3");

        Expression multiAdd1 = new MultiAddition(
                new DoubleLiteral(21.03),
                new IntegerLiteral(3));
        printExpression(multiAdd1, "multiAdd1");

        Expression multiAdd2 = new MultiAddition(
                new DoubleLiteral(21.03),
                new IntegerLiteral(3),
                new IntegerLiteral(5),
                new DoubleLiteral(3.1415));
        printExpression(multiAdd2, "multiAdd2");

        Expression multiMultiplication1 = new MultiMultiplication(new IntegerLiteral(5), new DoubleLiteral(7));
        printExpression(multiMultiplication1, "multiMultiplication1");

        Expression multiMultiplication2 = new MultiMultiplication(
                new IntegerLiteral(15),
                new DoubleLiteral(81),
                new UnaryMinus(
                        new Addition(
                                new DoubleLiteral(21),
                                new IntegerLiteral(3))));
        printExpression(multiMultiplication2, "multiMultiplication2");

        Expression roundExp1 = new RoundedExpression(new DoubleLiteral(5.123456), 2);
        printExpression(roundExp1, "roundExp1");

        Expression roundExp2 = new RoundedExpression(new DoubleLiteral(5.123456), 0);
        printExpression(roundExp2, "roundExp2");

        Expression roundExp3 = new RoundedExpression(new DoubleLiteral(5.1234567), 5);
        printExpression(roundExp3, "roundExp3");

        Expression roundExp4 = new RoundedExpression(multiAdd2, 2);
        printExpression(roundExp4, "roundExp4");

        Expression roundExp5 = new RoundedExpression(multiAdd2, 1);


        Expression roundExp6 = new RoundedExpression(divExp2, 5);
        printExpression(roundExp6, "roundExp6");

        Expression massiveExp = new Multiplication(
                divisionExp,
                new UnaryMinus(
                        new Multiplication(
                                divisionExp,
                                new Division(
                                        new Addition(
                                                new Subtraction(
                                                        intExp1,
                                                        intExp2),
                                                doubleExp1),
                                        new UnaryMinus(
                                                new Division(
                                                        new DoubleLiteral(2.17),
                                                        new Subtraction(divisionExp, exp3)))))));
        printExpression(massiveExp, "massiveExp");




    }

    private static void printExpression(Expression exp, String name) {
        System.out.println(name + ": " + exp);
        System.out.println(name + " with toString: " + exp.toString());
        try {
            System.out.println(name + " value: " + exp.evaluate());
        } catch (DivisionByZeroException e) {
            System.out.println("Division by zero...");
        }

        System.out.println();
    }
}
