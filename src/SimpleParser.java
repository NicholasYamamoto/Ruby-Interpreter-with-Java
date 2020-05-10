/*
        Nicholas Yamamoto
        Term Project
        CS3100 - Programming Languages
        April 13, 2020
 */
import java.util.*;

public class SimpleParser {
    static boolean isExpr(String expression) {

        ArrayList<String> tokens = Tokenizer.tokenize(expression);

        // Debug line to isPrint out the list of Tokens
        // tokens.forEach(System.out::println);

        for(int i = 0; i < expression.length() - 2; i++) {
            for(int j = i + 1; j <= expression.length() - 1; j++) {

                if (TokenIdentifier.isMultipleOperators(expression))
                    return true;

                // Special case to handle Modulus
                else if (TokenIdentifier.isModulus(expression))
                    return true;

                else if (TokenIdentifier.isManyOperators(expression))
                    return true;

            }
        }

        return isExprHelper(tokens, 0, tokens.size() - 1);
    }


    // <expr> --> <expr> + <term> | <term>
    static boolean isExprHelper(ArrayList<String> tokens, int start, int end) {

        // Return false if the Expression is empty, which makes it invalid
        if (start > end) return false;

        for(int i = start; i <= end; i++) {
            if (tokens.get(i).equals("+") && isExprHelper(tokens, start, i - 1)
                                          && isTerm(tokens, i + 1, end)) {
                return true;
            }

            else if (tokens.get(i).equals("-") && isExprHelper(tokens, start, i - 1)
                                               && isTerm(tokens, i + 1, end)) {
                return true;
            }
        }

        return isTerm(tokens, start, end);
    }


    // <term> --> <term> * <factor> | <factor>
    static boolean isTerm(ArrayList<String> tokens, int start, int end) {

        // Return false if the Expression is empty, which makes it invalid
        if (start > end) return false;

        for(int i = start; i <= end; i++) {
            if (tokens.get(i).equals("*") && isTerm(tokens, start, i - 1)
                                          && isFactor(tokens, i + 1, end)) {
                return true;
            }

            else if (tokens.get(i).equals("/") && isTerm(tokens, start, i - 1)
                                               && isFactor(tokens, i + 1, end)) {
                return true;
            }
        }

        return isFactor(tokens, start, end);
    }


    //  <factor> --> (<expr>) | <id>
    static boolean isFactor(ArrayList<String> tokens, int start, int end) {

        // Return false if the Expression is empty, which makes it invalid
        if (start > end) return false;

        return (tokens.get(start).equals("(") && tokens.get(end).equals(")")
                                              && isExprHelper(tokens, start + 1, end - 1)
                                              || isId(tokens, start, end));
    }

    //  <id> --> <number>
    static boolean isId(ArrayList<String> tokens, int start, int end) {

        if (start != end) return false;

        // return True to isPrint out the number if only a NUMBER is passed
        if (TokenIdentifier.isNumber(tokens.get(start)))
            return true;

        // else treat it as a Variable
        else
            return TokenIdentifier.isVariable(tokens.get(start));

    }

    public static void main(String[] args) {

        // These examples apply specifically to the Ruby programming language syntax
        System.out.println("Testing SimpleParser:\n");
        System.out.println(isExpr("234")); // true
        System.out.println(isExpr("1 + 3"));	// true
        System.out.println(isExpr("(1 + 3) * 45"));	// true
        System.out.println(isExpr("(1 + (2 + 1)) * 45"));	// true
        System.out.println(isExpr("(1 + (2 + 1)) * (78+3*15) +45"));	// true
        System.out.println(isExpr("(1 +")); // false
        System.out.println(isExpr("1 + * 2")); // false
    }
}
