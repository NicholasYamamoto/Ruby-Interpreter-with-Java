/*
        Nicholas Yamamoto
        Term Project - Ruby Interpreter
        CS3100 - Programming Languages
        April 23, 2020
 */
public class TokenIdentifier {

    // REGEX Patterns for Operators
    private static final String MANY_OPERATORS = "[-+\\/*%]*\\s*[-+\\/*%]\\s*[-+\\/*%]\\s*\\d*";
    private static final String MULTIPLE_OPERATORS = "\\d*\\s*?[-+\\/*%]\\s*?[-+\\/*%]\\d*";
    private static final String MODULUS = "\\d*[%]\\d*";

    // REGEX Patterns for Expression Syntax
    private static final String LEFT_PARENTHESIS = "^\\($";
    private static final String RIGHT_PARENTHESIS = "^\\)$";
    private static final String EQUALS_SIGN = "^\\=$";

    // REGEX Patterns for Values
    private static final String VARIABLE = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";
    private static final String NUMBER = "\\d+";

    // REGEX Patterns for Loops
    private static final String LOOP_FOR = "^for$";
    private static final String LOOP_IN = "^in$";
    private static final String LOOP_RANGE = "^range$";

    // REGEX Patterns for Print Statements
    private static final String PRINT = "^print";
    private static final String QUOTATION_MARK = "^\"$";



    // Functions to determine what kind of Token it is
    static boolean isMultipleOperators(String s) {
        return s.matches(MULTIPLE_OPERATORS);
    }

    static boolean isManyOperators(String s) {
        return s.matches(MANY_OPERATORS);
    }

    static boolean isModulus(String s) {
        return s.matches(MODULUS);
    }



    static boolean isLeftParenthesis(String s) {
        return s.matches(LEFT_PARENTHESIS);
    }

    static boolean isRightParenthesis(String s) { return s.matches(RIGHT_PARENTHESIS); }

    static boolean isEquals(String s) {
        return s.matches(EQUALS_SIGN);
    }



    static boolean isVariable(String s) {
        return s.matches(VARIABLE);
    }

    static boolean isNumber(String s) {
        return s.matches(NUMBER);
    }



    static boolean isLoopFor(String s) {
        return s.matches(LOOP_FOR);
    }

    static boolean isLoopIn(String s) {
        return s.matches(LOOP_IN);
    }

    static boolean isLoopRange(String s) {
        return s.matches(LOOP_RANGE);
    }


    static boolean isPrint(String s) {
        return s.matches(PRINT);
    }

    static boolean isQuotationMark(String s) {
        return s.matches(QUOTATION_MARK);
    }

}
