/*
    Nicholas Yamamoto
    Homework 5
    CS3100 - Programming Languages
    April 24, 2020
 */
import java.util.*;

public class Interpreter {

    // Global DEBUG boolean to activate console outputs for debugging/function tracing
    private static boolean DEBUG = false;

    // Global variable for number of iterations to run expression, to be overridden in a For Loop
    private static int numberOfIterations = 1;

    // Global variable to store variables assigned in evalAssign() by user
    private static HashMap<String, Integer> variables = new HashMap<>();

    //  <S> --> <assign> | <evaluate> | <isPrint> | <isForLoop>
    static boolean interpret(String s) {

        if (DEBUG) {
            System.out.println("Running interpret() with " + s);
        }

        ArrayList<String> tokens = Tokenizer.tokenize(s);

        // Perform the action based on if any of these == true
        return (
                isAssign(tokens, 0) ||
                isPrint(tokens, 0) ||
                isForLoop(tokens, 0) ||
                isEvaluate(tokens, 0)
        );
    }

    //  <assign> --> <id> = <expr>
    //  <id> = tokens.start
    //  '=' = tokens.start + 1
    //  <expr> = (tokens.start + 1) to tokens.end (everything else after the equals sign)
    static boolean isAssign(ArrayList<String> tokens, int start) {

        if (DEBUG) {
            System.out.println("Determining if " + tokens + " is an Assignment.");
        }

        // If the second token is an equals sign, it is an assignment!
        if (tokens.size() != 1 && TokenIdentifier.isEquals(tokens.get(start + 1))) {
            evalAssign(tokens);
            return true;
        }

        return false;
    }

    //  <expr> --> <expr> + <term> | <term>
    // <id> = tokens.start
    // '=' = tokens.start + 1
    // <expr> = (tokens.start + 1) to tokens.end (everything else after the equals sign)
    static boolean isEvaluate(ArrayList<String> tokens, int start) {

        if (DEBUG) {
            System.out.println("Determining if " + tokens + " is an Evaluation.");
        }

        if (tokens.size() == 2) return false;

        // If input is only 1 token and a Number, isPrint to screen
        if (tokens.size() == 1 && TokenIdentifier.isNumber(tokens.get(start))) {
            for(int i = 0; i < numberOfIterations; i++) {
                System.out.println(tokens.get(start));
            }

            return true;
        }

        // If input is only one token and it is a Variable name, see if it exists
        else if (tokens.size() == 1 && TokenIdentifier.isVariable(tokens.get(start))) {
            Integer variable = evalVariable(tokens, start);

            if (variable != null) {
                for(int i = 0; i < numberOfIterations; i++) {
                    System.out.println(variable);
                }
            }

            return true;
        }

        // If input is a more than one token and first token is a Variable, evaluate modification to Variable and replace value in tokens
        else if (tokens.size() != 1 && TokenIdentifier.isVariable(tokens.get(start))) {
            Integer variable = evalVariable(tokens, start);

            if (variable != null) {
                for(int i = 0; i < numberOfIterations; i++) {
                    tokens.set(0, variable.toString());
                    System.out.println(Evaluator.evalExpressionHelper(tokens, 0, tokens.size() - 1));
                }
            }

            return true;
        }

        // If input is more than 1 token and first token is a Number, assume this is an evaluation and pass to evalExpressionHelper()
        else if (tokens.size() != 1) {
            for(int i = 0; i < numberOfIterations; i++) {
                System.out.println(Evaluator.evalExpressionHelper(tokens, 0, tokens.size() - 1));
            }

            return true;
        }

        return false;
    }


     //  <isPrint> --> isPrint ( ) | isPrint ( <expr> )
     static boolean isPrint(ArrayList<String> tokens, int start) {

         if (DEBUG) {
             System.out.println("Determining if " + tokens + " is a Print statement.");
         }

         if (TokenIdentifier.isPrint(tokens.get(start)) &&
                TokenIdentifier.isLeftParenthesis(tokens.get(start + 1))) {

             StringBuilder extractedExpression = new StringBuilder();
             StringBuilder printLiteralExpression = new StringBuilder();

             // Basically, this is a recursion of interpret()
             // Store tokens as a String to transform using .split()
             for (String s : tokens) {
                 extractedExpression.append(s);
             }

             String expressionToPrint = extractedExpression.toString().split("[\\(\\)]")[1];

             if (DEBUG) {
                 System.out.println("\nHere is the extracted expression: " + expressionToPrint);
             }

             // Checks if the first token is a ", denoting a String Literal, and simply prints to screen if true
             if (TokenIdentifier.isQuotationMark(tokens.get(start + 2))) {
                 for (String s : tokens) {
                     printLiteralExpression.append(s);
                 }

                 String printLiteralString = printLiteralExpression.toString().split("[\"]")[1];
                 for(int i = 0; i < numberOfIterations; i++) {
                     System.out.println(printLiteralString);
                 }

                 return true;
             }

             return isEvaluate(Tokenizer.tokenize(expressionToPrint), start);
         }

         return false;
     }

    // <isForLoop> --> for <id> in ( <integer> ) :
    static boolean isForLoop(ArrayList<String> tokens, int start) {

        if (DEBUG) {
            System.out.println("Determining if expression " + tokens + " is a For Loop.");
        }

        if (TokenIdentifier.isLoopFor(tokens.get(start)) &&
                TokenIdentifier.isVariable(tokens.get(start + 1)) &&
                TokenIdentifier.isLoopIn(tokens.get(start + 2)) &&
                TokenIdentifier.isLoopRange(tokens.get(start + 3)) &&
                TokenIdentifier.isLeftParenthesis(tokens.get(start + 4)) &&
                start == 0) {

            if (DEBUG) {
                System.out.println("\nThis is in proper For Loop syntax!");
            }

            // Override numberOfIterations
            numberOfIterations = Integer.parseInt(tokens.get(start + 5));

            StringBuilder extractedExpression = new StringBuilder();

            // Basically, this is a recursion of interpret()
            // Store tokens as a String to transform using .split()
            for (String s : tokens) {
                extractedExpression.append(s);
            }

            String loopedExpression = extractedExpression.toString().split(":")[1];

            return (
                    isPrint(Tokenizer.tokenize(loopedExpression), start) ||
                    isEvaluate(Tokenizer.tokenize(loopedExpression), start)
            );
        }

        return false;
    }

    //  <id> --> <number>
    // This evaluates the input if it is a variable name, then checks to see if it exists
    static Integer evalVariable(ArrayList<String> tokens, int start) {

        if (DEBUG) {
            System.out.println("Running evalVariable() with " + tokens);
        }

        // If it is a valid Variable name that is in the Variables Hash Table, find and return it
        if (variables.containsKey(tokens.get(start))) {
            return variables.get(tokens.get(start));
        }

        else {
            System.out.println("ERROR: Undefined local variable or method `" + tokens.get(start) + "'");
            return null;
        }
    }


    // This function performs the Assignment of a Variable
    // Set the variable to the value given and return null;
    static void evalAssign(ArrayList<String> tokens) {

        if (DEBUG) {
            System.out.println("Running evalAssign() with " + tokens);
        }

        // Create an ArrayList to temporarily store the value on the right of the equals sign
        ArrayList<String> temp = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("=")) {

                // (i + 1) = Values to the right of '='
                // Get all of the values as an Expression to be evaluated
                for(int j = i + 1; j < tokens.size(); j++) {
                    temp.add(tokens.get(j));
                }

                for(int k = 0; k < temp.size(); k++) {

                    if (variables.containsKey(temp.get(k))) {
                        temp.set(k, variables.get(temp.get(k)).toString());
                    }
                }

                // Evaluate token variables after the equal sign and put into the variable name the result of the evaluated Expression
                // (i - 1) = Value to left of '='
                variables.put(tokens.get(i - 1), Evaluator.evalExpressionHelper(temp, 0, temp.size() - 1));
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("Nick's Ruby Interpreter - v1.0\n" +
                           "Based on Ruby v2.4\n\n" +
                           "Type \"local-variables\" to list local variables!\n" +
                           "Type \"exit\" to Quit!\n");

        Scanner userInput = new Scanner(System.in);
        String currentUserInput;

        while(true) {
            System.out.print(">>> ");
            currentUserInput = userInput.nextLine().trim();

            if (currentUserInput.equals(""))
                continue;

            else if (currentUserInput.equals("exit"))
                break;

            else if (currentUserInput.equals("local-variables"))
                System.out.println(variables.toString());

            else if (!interpret(currentUserInput))
                System.out.println("Invalid: Could not be interpreted!");

        }

        userInput.close();
    }
}
