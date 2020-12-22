package org.boy.aoc.twentytwenty.exercises;

import java.math.BigInteger;
import java.util.*;

public class Eighteen extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        BigInteger sumValue = BigInteger.ZERO;

        for (String line: input.split("\n")) {
            sumValue = sumValue.add(
                    eveuateRpn(infixToRpn(
                            line.replace(" ", "").strip(),
                            false
                    ))
            );
        }
        return String.valueOf(sumValue);
    }

    @Override
    public String pointTwo(String input) {
        BigInteger sumValue = BigInteger.ZERO;

        for (String line: input.split("\n")) {
            sumValue = sumValue.add(
                    eveuateRpn(
                        infixToRpn(
                                line.replace(" ", "").strip(),
                                true
                        )
                    )
            );
        }
        return String.valueOf(sumValue);
    }

    private BigInteger eveuateRpn(String rpn) {
        HashSet<String> operators = new HashSet<>();
        operators.add("*"); operators.add("+");
        Stack<BigInteger> stack = new Stack<>();

        for (int i = 0; i < rpn.length(); i ++) {
            String token = rpn.substring(i, i+1);

            if(!operators.contains(token)){
                stack.push(BigInteger.valueOf(Long.parseLong(token)));
            }else{
                BigInteger a = stack.pop();
                BigInteger b = stack.pop();
                switch(token){
                    case "+":
                        stack.push(a.add(b));
                        break;
                    case "*":
                        stack.push(a.multiply(b));
                        break;
                }

            }
        }
        return stack.pop();
    }

    // Method is used to get the precedence of operators
    private static boolean letterOrDigit(char c) {
        // boolean check
        return Character.isLetterOrDigit(c);
    }

    // Operator having higher precedence
    // value will be returned
    private static int getPrecedence(char ch, boolean ordering) {
        if (ch == '+' || (ch == '*' && !ordering))
            return 2;
        else if (ch == '*')
            return 1;
        else
            return -1;
    }

    // Method converts  given infixto postfix expression
    // to illustrate shunting yard algorithm
    private static String infixToRpn(String expression, boolean ordering) {
        // Initalising an empty String
        // (for output) and an empty stack
        Stack<Character> stack = new Stack<>();

        // Initially empty string taken
        String output = new String("");

        // Iterating ovet tokens using inbuilt
        // .length() functon
        for (int i = 0; i < expression.length(); ++i) {
            // Finding character at 'i'th index
            char c = expression.charAt(i);

            // If the scanned Token is an
            // operand, add it to output
            if (letterOrDigit(c))
                output += c;

                // If the scanned Token is an '('
                // push it to the stack
            else if (c == '(')
                stack.push(c);

                // If the scanned Token is an ')' pop and append
                // it to output from the stack until an '(' is
                // encountered
            else if (c == ')') {
                while (!stack.isEmpty()
                        && stack.peek() != '(')
                    output += stack.pop();

                stack.pop();
            }

            // If an operator is encountered then taken the
            // furthur action based on the precedence of the
            // operator

            else {
                while (
                        !stack.isEmpty()
                                && getPrecedence(c, ordering)
                                <= getPrecedence(stack.peek(), ordering)) {
                    // peek() inbuilt stack function to
                    // fetch the top element(token)

                    output += stack.pop();
                }
                stack.push(c);
            }
        }

        // pop all the remaining operators from
        // the stack and append them to output
        while (!stack.isEmpty()) {
            if (stack.peek() == '(')
                return "This expression is invalid";
            output += stack.pop();
        }
        return output;
    }
}
