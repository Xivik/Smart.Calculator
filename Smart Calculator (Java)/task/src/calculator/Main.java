package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern LATIN_LETTERS_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Pattern NUMERIC_VALUE_PATTERN = Pattern.compile("^[+-]?\\d+");
    private static final Pattern EMPTY_SPACES_PATTERN = Pattern.compile("\\s+");
    private static final Pattern VARIABLE_ASSIGNMENT_PATTERN = Pattern.compile(".+=.+");
    private static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Map<String, Integer> intVariableMap = new HashMap<>();
    private static final Map<String, BigInteger> bigIntVariableMap = new HashMap<>();

    public static void main(String[] args) {

        while (true) {
            String input = getInput();

            if (input.startsWith("/")) {
                if (input.equals("/exit")) {
                    break;
                } else if (input.equals("/help")) {
                    System.out.println("""
                            The program is used for calculations.\s
                            You can use +,-,*,/.\s
                            The program uses operator precedence and supports the use of parentheses.""");
                } else {
                    System.out.println("Unknown command");
                }
            } else if (isVariableAssignment(input)) {
                createVariable(input);
            } else if (!input.isEmpty()) {
                if (inputContainsVariablePattern(input)) {
                    input = getVariableValue(input);
                }

                if (input == null) {
                    System.out.println("Unknown variable");
                } else {
                    String processedInput = processInput(input);
                    evaluateInput(processedInput);
                }
            }
        }

        System.out.println("Bye!");
    }

    private static boolean inputContainsBigInteger(String input) {
        String[] splitInput = input.split("\\s");

        boolean containsBigInt = false;

        for (String s : splitInput) {
            if (stringNumberIsBigInt(s)) {
                containsBigInt = true;
                break;
            }
        }

        return containsBigInt;
    }

    private static boolean stringNumberIsBigInt(String number) {
        try {
            Integer.parseInt(number);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static boolean inputContainsVariablePattern(String input) {
        Matcher matcher = LATIN_LETTERS_PATTERN.matcher(input);
        return matcher.find();
    }

    private static boolean isVariableAssignment(String input) {
        Matcher matcher = VARIABLE_ASSIGNMENT_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static boolean isVariableValid(String variableName) {
        Matcher matcher = VARIABLE_NAME_PATTERN.matcher(variableName);
        return matcher.matches();
    }

    private static void createVariable(String input) {
        input = input.replaceAll("\\s", "");
        String[] splitInput = input.split("=");
        String variableName = splitInput[0];
        String variableValue = splitInput[1];

        if (!isVariableValid(variableName)) {
            System.out.println("Invalid identifier");
            return;
        }

        if (isNumber(variableValue)) {
            handleNumberVariableAssignment(variableName, variableValue);
        } else if (isLatinLetter(variableValue)) {
            handleVariableAssignmentFromOtherVariable(variableName, variableValue);
        } else {
            System.out.println("Invalid assignment");
        }
    }

    private static void handleNumberVariableAssignment(String variableName, String variableValue) {
        if (stringNumberIsBigInt(variableValue)) {
            updateBigIntVariableMap(variableName, new BigInteger(variableValue));
        } else {
            updateIntVariableMap(variableName, Integer.parseInt(variableValue));
        }
    }

    private static void handleVariableAssignmentFromOtherVariable(String variableName, String variableValue) {
        if (bigIntVariableMap.containsKey(variableValue)) {
            BigInteger value = bigIntVariableMap.get(variableValue);
            if (value != null) {
                updateBigIntVariableMap(variableName, value);
            } else {
                System.out.println("Invalid assignment");
            }
        } else {
            Integer value = intVariableMap.get(variableValue);
            if (value != null) {
                updateIntVariableMap(variableName, value);
            } else {
                System.out.println("Invalid assignment");
            }
        }
    }

    private static boolean isNumber(String input) {
        Matcher matcher = NUMERIC_VALUE_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static boolean isLatinLetter(String input) {
        Matcher matcher = LATIN_LETTERS_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static void updateIntVariableMap(String key, int value) {
        bigIntVariableMap.remove(key);
        intVariableMap.put(key, value);
    }

    private static void updateBigIntVariableMap(String key, BigInteger value) {
        intVariableMap.remove(key);
        bigIntVariableMap.put(key, value);
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return removeEmptySpaces(scanner.nextLine());
    }

    private static String removeEmptySpaces(String text) {
        Matcher matcher = EMPTY_SPACES_PATTERN.matcher(text);
        text = matcher.replaceAll(" ");
        return text;
    }

    private static String getVariableValue(String input) {
        String[] splitInput = input.replaceAll("[-+*/()\\s\\d]+", " ")
                .trim()
                .split("\\s");

        for (String character : splitInput) {
            if (intVariableMap.containsKey(character)) {
                int value = intVariableMap.get(character);
                input = input.replaceAll(character, String.valueOf(value));
            } else if (bigIntVariableMap.containsKey(character)) {
                BigInteger value = bigIntVariableMap.get(character);
                input = input.replaceAll(character, String.valueOf(value));
            } else {
                return null;
            }
        }

        return input;
    }

    private static String processInput(String input) {
        String[] splitInput = input.replaceAll("\\s", "").split("");
        StringBuilder output = new StringBuilder();
        StringBuilder plusMinusSequence = new StringBuilder();

        for (int i = 0; i < splitInput.length; i++) {
            String character = splitInput[i];

            if (character.equals("+") || character.equals("-")) {
                plusMinusSequence.append(character);

                if (i != splitInput.length - 1 && !splitInput[i + 1].equals("+") && !splitInput[i + 1].equals("-")) {
                    output.append(evaluateOperation(plusMinusSequence.toString()));
                }
            } else {
                output.append(character);
                plusMinusSequence.setLength(0);
            }
        }

        return output.toString();
    }

    private static String evaluateOperation(String operation) {
        String[] splitOperations = operation.split("");
        int minusCount = 0;

        for (String op : splitOperations) {
            if (op.equals("-")) {
                minusCount++;
            }
        }

        String evaluatedOperation = "+";

        if (minusCount > 0 && minusCount % 2 != 0) {
            evaluatedOperation = "-";
        }

        return evaluatedOperation;
    }

    private static void evaluateInput(String input) {
        try {
            String postfixExpression = infixToPostfixConverter(input);
            if (inputContainsBigInteger(input)) {
                BigInteger result = evaluatePostfixMathExpressionBigInt(postfixExpression);
                System.out.println(result);
            } else {
                int result = evaluatePostfixMathExpression(postfixExpression);
                System.out.println(result);
            }
        } catch (EmptyStackException e) {
            System.out.println("Invalid expression");
        }
    }

    private static String infixToPostfixConverter(String expression) {
        StringBuilder output = new StringBuilder();
        StringBuilder digits = new StringBuilder();
        Stack<String> stack = new Stack<>();
        String[] splitExpression = expression.replaceAll("\\s", "").split("");

        HashMap<String, Integer> operatorPrecedence = new HashMap<>();
        operatorPrecedence.put("*", 2);
        operatorPrecedence.put("/", 2);
        operatorPrecedence.put("+", 1);
        operatorPrecedence.put("-", 1);

        for (int i = 0; i < splitExpression.length; i++) {
            String op = splitExpression[i];

            if (!op.isBlank()) {
                if (isNumber(op) || (op.equals("-") && i == 0)) {
                    digits.append(op);

                    if (i == splitExpression.length - 1 || !isNumber(splitExpression[i + 1])) {
                        output.append(digits).append(" ");
                        digits.setLength(0);
                    }
                } else {
                    if (stack.isEmpty() || stack.peek().equals("(") || op.equals("(")) {
                        stack.push(op);
                    } else if (op.equals(")")) {
                        while (!stack.peek().equals("(") && !stack.isEmpty()) {
                            output.append(stack.pop()).append(" ");
                        }

                        if (!stack.isEmpty()) {
                            stack.pop();
                        }
                    } else if (operatorPrecedence.get(op) > operatorPrecedence.getOrDefault(stack.peek(), 0)) {
                        stack.push(op);
                    } else {
                        while (!stack.isEmpty() && operatorPrecedence.get(op) <= operatorPrecedence.getOrDefault(stack.peek(), 0)) {
                            output.append(stack.pop()).append(" ");
                        }

                        stack.push(op);
                    }
                }
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }

        return output.toString();
    }

    private static int evaluatePostfixMathExpression(String expression) {
        Stack<Integer> numbers = new Stack<>();
        String[] splitExpression = expression.split(" ");

        for (String op : splitExpression) {
            if (isNumber(op)) {
                numbers.push(Integer.parseInt(op));
            } else {
                int rhs = numbers.pop();
                int lhs = numbers.pop();
                int result = switch (op) {
                    case "+" -> lhs + rhs;
                    case "-" -> lhs - rhs;
                    case "*" -> lhs * rhs;
                    case "/" -> lhs / rhs;
                    default -> 0;
                };

                numbers.push(result);
            }
        }

        return numbers.pop();
    }

    private static BigInteger evaluatePostfixMathExpressionBigInt(String expression) {
        Stack<BigInteger> numbers = new Stack<>();
        String[] splitExpression = expression.split(" ");

        for (String op : splitExpression) {
            if (isNumber(op)) {
                numbers.push(new BigInteger(op));
            } else {
                BigInteger rhs = numbers.pop();
                BigInteger lhs = numbers.pop();
                BigInteger result = switch (op) {
                    case "+" -> lhs.add(rhs);
                    case "-" -> lhs.subtract(rhs);
                    case "*" -> lhs.multiply(rhs);
                    case "/" -> lhs.divide(rhs);
                    default -> BigInteger.ZERO;
                };

                numbers.push(result);
            }
        }

        return numbers.pop();
    }
}