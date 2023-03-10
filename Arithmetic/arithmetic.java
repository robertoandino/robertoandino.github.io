/*Jacquelyn Scheck, Roberto Andino, Collin Rijock
 * Group 7
 * COP 3530 RVC 1218
 * Assignment 1
 * 09-19-2021
 * 
 */
package Assignment1;

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class arithmetic implements Constants {

    private Stack<Object> stck;
    private String expression;
    private int length;


    public arithmetic(String expression) {
        stck = new Stack<Object>();
        this.expression = expression;
        this.length = expression.length();
    }
    
    //determine if the parentheses are balanced
    boolean isBalance() {
        int index = 0;
        boolean fail = false;
        //begin try
        try {
            //begin while loop
            while (index < length && !fail) {
                char ch = expression.charAt(index);
                //switch cases
                switch (ch) {
                    case Constants.LEFT_NORMAL:
                        stck.push(new Character(ch));
                        break;
                    case Constants.RIGHT_NORMAL:
                        stck.pop();
                        break;
                    default:
                        break;
                }//end of switch
                index++;
            }//end of while
        }//end of try
        catch (EmptyStackException e) {

            fail = true;
        }
        return (stck.empty() && !fail);
    }

    //convert infixed expression to postfix
    String convert2Postfix() {
        String postfix = "";
        Scanner scan = new Scanner(expression);
        char current;
        boolean fail = false;
        //begin while loop
        while (scan.hasNext() && !fail) {
            String token = scan.next();

            if (isNumber(token)) // Bullet # 1
            {
                postfix = postfix + token + Constants.A_SPACE;
            } else {
                current = token.charAt(0);
                if (current == Constants.LEFT_NORMAL) // Bullet # 2
                {
                    stck.push(new Character(current));
                } else if (current == Constants.RIGHT_NORMAL) // Bullet # 3
                {
                    try {
                        Character topmost = (Character) stck.pop();
                        char top = Character.valueOf(topmost);  //Value on stack top 

                        while (top != Constants.LEFT_NORMAL) {
                            postfix = postfix + top + Constants.A_SPACE;
                            top = (Character) stck.pop();
                        }
                    } catch (EmptyStackException e) {
                        fail = true;
                    }
                } // End bullet # 2 and 3
                else if (isOperator(current)) // Bullet # 4
                {
                    try {
                        char top = (Character) stck.peek();
                        boolean higher = hasHigherPrecedence(top, current);

                        while (top != Constants.LEFT_NORMAL && higher) {
                            postfix = postfix + stck.pop() + Constants.A_SPACE;
                            top = (Character) stck.peek();
                        }
                        stck.push(new Character(current));
                    } catch (EmptyStackException e) {
                        stck.push(new Character(current));
                    }
                }
            }
        }
        try {
            while (!stck.empty()) // Bullet # 5
            {
                postfix = postfix + stck.pop() + Constants.A_SPACE;
            }
        } catch (EmptyStackException e) {
            e.printStackTrace();
        }
        return postfix;
    }// End convert 2 postfix


    int evaluateRPN() //evaluate the post fixed expression
    {
        Stack<Integer> stackRPN = new Stack<Integer>(); //initialized new stack 
        Scanner scan = new Scanner(convert2Postfix()); //read expression from postfix

        boolean fail = false;

        while (scan.hasNext() && !fail) //begin while loop
        {

            if (scan.hasNextInt()) //if operand push to stack
            {
                stackRPN.push(scan.nextInt());
            } else {

                String token = scan.next();
                char value = token.charAt(0); //token at index

                if (isOperator(value)) { //if operator the pop first two operand off stack T1 and T2 
                    int t1 = stackRPN.pop(); //operand 1

                    int t2 = stackRPN.pop(); //operand 2

                    switch (value) //evaluating by operators
                    {
                        case '*': //multiply 
                            stackRPN.push(t2 * t1);
                            break;
                        case '/': //divide
                            stackRPN.push(t2 / t1);
                            break;
                        case '+': //add
                            stackRPN.push(t2 + t1);
                            break;
                        case '-': //subtract
                            stackRPN.push(t2 - t1);
                            break;
                    }

                }
            }

        }
        return stackRPN.pop(); //return evaluation

    }

    boolean isNumber(String s) {
        boolean number = true;

        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            number = false;
        }
        return number;
    }

    boolean isOperator(char ch) {
        boolean operator;
        switch (ch) {
            case Constants.MULTIPLICATION:
            case Constants.DIVISION:
            case Constants.ADDITION:
            case Constants.SUBTRACTION:
                operator = true;
                break;
            default:
                operator = false;
                break;
        }
        return operator;
    }

    boolean hasHigherPrecedence(char top, char current) {
        boolean higher;

        switch (top) {
            case Constants.MULTIPLICATION:
            case Constants.DIVISION:
                switch (current) {
                    case Constants.ADDITION:
                    case Constants.SUBTRACTION:
                        higher = true;
                        break;
                    default:
                        higher = false;
                        break;
                }
                break;
            default:
                higher = false;
                break;
        }
        return higher;
    }

}
