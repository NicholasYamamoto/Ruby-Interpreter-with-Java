/*
    Nicholas Yamamoto
    Homework 5
    CS3100 - Programming Languages
    February 21, 2020

    Write a function ‘tokenize()’ that takes a string of expression and
    returns a series of tokens (such as String Array or ArrayList). Write a main() to test it.
 */

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    public static ArrayList<String> tokenize(String s){
        ArrayList<String> tokens = new ArrayList<>();

        Pattern masterPattern = Pattern.compile("[0-9.a-zA-Z_]+|\\+|\\*{2}|\\*|=|\\(|\\)|:|\"|\\/{2}|\\/|-|%");
        Matcher matchString = masterPattern.matcher(s);

        while (matchString.find()) {
            String temp = matchString.group();

            if (temp != null)
                tokens.add(temp);

        }

        return tokens;
    }

    public static void main(String[] args) {

        // These examples apply specifically to the Ruby programming language syntax
        tokenize("102*3**4").forEach(System.out::println);
        tokenize("123+56*num1").forEach(System.out::println);
        tokenize("(1+ 23.0) * 0.9").forEach(System.out::println);
        tokenize("aa1= (14 - 3) *2/a23").forEach(System.out::println);
        tokenize("(1 + (2 + 1)) * (78+3*15) +45").forEach(System.out::println);
        tokenize("aa1= ((14 - 3) / 2)").forEach(System.out::println);
        tokenize("2+ 3.2 - 56**num1").forEach(System.out::println);
    }
}
