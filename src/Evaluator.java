/*
        Nicholas Yamamoto
        Term Project - Ruby Interpreter
        CS3100 - Programming Languages
        April 23, 2020
 */
import java.util.*;

public class Evaluator {

    // Create a Global DEBUG boolean to activate console outputs for debugging/function tracing
    private static boolean DEBUG = false;

    static Integer evalExpression(String expression) {
        ArrayList<String> tokens = Tokenizer.tokenize(expression);

        return evalExpressionHelper(tokens, 0, tokens.size() - 1);
    }

    static Integer evalModulus(Integer x, Integer y) {
        return x % y;
    }


    static Integer evalExponent(Integer x, Integer y) {
        return (int) Math.pow(x, y);
    }

    static Integer evalFloorDivision(Integer x, Integer y) {
        return Math.floorDiv(x, y);
    }

    //  <expr> -> <expr> + <term> | <term>
    static Integer evalExpressionHelper(ArrayList<String> tokens, int start, int end) {
        if (DEBUG) {
            System.out.println("\nevalExpressionHelper() Tokens: " + tokens);
        }

        if (start > end)
            return null;

        Integer i1 = evalTerm(tokens, start, end);

        if (i1 != null)
            return i1;

        for(int i = start + 1; i <= end; i++) {
            if (tokens.get(i).equals("+")) {
                i1 = evalExpressionHelper(tokens, start, i - 1);

                if (i1 != null) {
                    Integer i2 = evalTerm(tokens, i + 1, end);

                    if (i2 != null)
                        return i1 + i2;
                }
            }

            else if (tokens.get(i).equals("-")) {
                i1 = evalExpressionHelper(tokens, start, i - 1);

                if (i1 != null) {
                    Integer i2 = evalTerm(tokens, i + 1, end);

                    if (i2 != null)
                        return i1 - i2;
                }
            }
        }

        return evalTerm(tokens, start, end);
    }

    //  <term> -> <term> * <factor> | <factor>
    static Integer evalTerm(ArrayList<String> tokens, int start, int end) {
        if (DEBUG) {
            System.out.println("evalTerm() Tokens:             " + tokens);
        }

        if (start > end)
            return null;

        Integer i1 = evalFactor(tokens, start, end);

        if (i1 != null)
            return i1;

        for(int i = start + 1; i <= end; i++) {
            if (tokens.get(i).equals("*")) {
                i1 = evalTerm(tokens, start, i - 1);
                if (i1 != null) {
                    Integer i2 = evalFactor(tokens, i + 1, end);
                    if (i2 != null)
                        return i1 * i2;

                }
            }

            else if (tokens.get(i).equals("/")) {

                i1 = evalTerm(tokens, start, i - 1);

                if (i1 != null) {
                    Integer i2 = evalFactor(tokens, i + 1, end);

                    if (i2 != null)
                        return i1 / i2;
                }
            }

            else if (tokens.get(i).equals("%")) {

                i1 = evalTerm(tokens, start, i - 1);
                if (i1 != null) {
                    Integer i2 = evalFactor(tokens, i + 1, end);

                    if (i2 != null)
                        return evalModulus(i1, i2);
                }
            }

            else if (tokens.get(i).equals("//")) {

                i1 = evalTerm(tokens, start, i - 1);
                if (i1 != null) {
                    Integer i2 = evalFactor(tokens, i + 1, end);

                    if (i2 != null)
                        return evalFloorDivision(i1, i2);
                }
            }

            // Given the example of "2 ** 3"
            // Token at (i) == **
            // Token at (i - 1) == 2
            // Token at (i + 1) == 3
            else if (tokens.get(i).equals("**")) {


                // eg. "2 * 2 ** 3"

                i1 = evalTerm(tokens, start, i - 1); // == (2 * 2) = 4
                if (i1 != null) {
                    Integer i2 = evalFactor(tokens, i + 1, end);

                    if (i2 != null)
                        return evalExponent(i1, i2);
                }
            }
        }

        return evalFactor(tokens, start, end);
    }

    //  <factor> -> (<expr>) | <id>
    static Integer evalFactor(ArrayList<String> tokens, int start, int end) {

        if (DEBUG) {
            System.out.println("evalFactor() Tokens:           " + tokens);
        }

        if (start > end)
            return null;

        Integer i1 = evalId(tokens, start, end);

        if (i1 != null)
            return i1;

        if ((tokens.get(start).equals("(") && tokens.get(end).equals(")")
                && evalExpressionHelper(tokens, start + 1, end - 1) != null)) {
            i1 = evalExpressionHelper(tokens, start + 1, end - 1);

            if (i1 != null)
                return i1;
        }

        return evalId(tokens, start, end);
    }

    //  <id> --> <number>
    static Integer evalId(ArrayList<String> tokens, int start, int end) {

        if (DEBUG) {
            System.out.println("evalVariable() Tokens:         " + tokens);
        }

        if (start != end) return null;

        String s = tokens.get(start);

        if (s.matches("\\d+")) {
            return Integer.parseInt(s);
        }

        return null;
    }


    public static void main(String[] args) {

        // These examples apply specifically to the Ruby programming language syntax
        System.out.println("Testing Evaluator: Addition/Subtraction/Multiplication/Division:");

        System.out.println(evalExpression("234\n")); // 234
        System.out.println(evalExpression("1 + 3\n"));	// 4
        System.out.println(evalExpression("(1 + 3) * 45\n"));	// 180
        System.out.println(evalExpression("(1 + (2 + 1)) * 45\n"));	// 180
        System.out.println(evalExpression("(1 + (2 + 1)) * (78+3*15) +45\n"));	// 537
        System.out.println(evalExpression("(1 +\n")); // null
        System.out.println(evalExpression("1 + * 2\n")); // null
        System.out.println(evalExpression("(1 + (5-2)) * 45\n"));	// 180
        System.out.println(evalExpression("(1 + 6/2) * 45\n"));	// 180
        System.out.println(evalExpression("(1 + 7%2) * 45\n"));	// 90
        System.out.println(evalExpression("(1 + .2) * 10\n"));	// null
        System.out.println(evalExpression("(1 + 8 // 3) * 45\n"));	// 135
        System.out.println(evalExpression("1 + (2 * 2)\n"));	// 5

        System.out.println("\nTesting Double Operands:");

        System.out.println(evalExpression("2 * 2 ** 3\n")); // 16
        System.out.println(evalExpression("2 * 3 ** 2\n")); // 18
        System.out.println(evalExpression("2 ** 3 * 2\n")); // 16
        System.out.println(evalExpression("2 ** 3\n")); // 8
        System.out.println(evalExpression("2 // 2\n")); // 1
    }
}
