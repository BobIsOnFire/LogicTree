import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpressionParser {
    private int bracketCounter;
    private int pointer;
    private boolean readingVariable;
    private String input;

    private List<Expression> expressions;
    private List<Operation> operations;

    public ExpressionParser(String input) {
        this.input = input;
        bracketCounter = 0;
        pointer = -1;
        readingVariable = false;
        expressions = new ArrayList<>();
        operations = new ArrayList<>();
    }

    public Expression parse() {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '(':
                    if (bracketCounter == 0) pointer = i;
                    bracketCounter++;
                    break;
                case ')':
                    bracketCounter--;
                    if (bracketCounter == 0) {
                        String token = input.substring(pointer + 1, i);
                        ExpressionParser op = new ExpressionParser(token);
                        expressions.add( op.parse() );
                    }
                    break;
                case '!':
                    if (readingVariable) {
                        readingVariable = false;
                        String name = input.substring(pointer, i);
                        expressions.add( new Variable(name) );
                    }
                    if (bracketCounter == 0) operations.add(Operation.NEGATION);
                    break;
                case '&':
                    if (readingVariable) {
                        readingVariable = false;
                        String name = input.substring(pointer, i);
                        expressions.add( new Variable(name) );
                    }
                    if (bracketCounter == 0) operations.add(Operation.CONJUNCTION);
                    break;
                case '|':
                    if (readingVariable) {
                        readingVariable = false;
                        String name = input.substring(pointer, i);
                        expressions.add( new Variable(name) );
                    }
                    if (bracketCounter == 0) operations.add(Operation.DISJUNCTION);
                    break;
                case '-':
                    if (readingVariable) {
                        readingVariable = false;
                        String name = input.substring(pointer, i);
                        expressions.add( new Variable(name) );
                    }
                    break;
                case '>':
                    if (bracketCounter == 0 && chars[i-1] == '-') operations.add(Operation.IMPLICATION);
                    break;
                default:
                    if (bracketCounter == 0 && !readingVariable) {
                        readingVariable = true;
                        pointer = i;
                    }
            }
        }

        if (readingVariable) {
            String name = input.substring(pointer);
            expressions.add( new Variable(name) );
        }

        excludeNegations();
        excludeConjunctions();
        excludeDisjunctions();
        excludeImplications();

        return expressions.get(0);
    }

    private void excludeNegations() {
        int pointer = 0;
        Iterator<Operation> iter = operations.iterator();

        while (iter.hasNext()) {
            Operation op = iter.next();

            if (op == Operation.NEGATION) {
                Expression negate = new Expression(
                        Operation.NEGATION,
                        expressions.get(pointer)
                );
                expressions.set(pointer, negate);
                iter.remove();
            } else pointer++;
        }
    }

    private void excludeConjunctions() {
        int pointer = 0;
        Iterator<Operation> iter = operations.iterator();
        while (iter.hasNext()) {
            Operation op = iter.next();

            if (op == Operation.CONJUNCTION) {
                Expression conjugate = new Expression(
                        Operation.CONJUNCTION,
                        expressions.get(pointer),
                        expressions.get(pointer + 1)
                );
                expressions.set(pointer, conjugate);
                expressions.remove(pointer + 1);
                iter.remove();
            } else pointer++;
        }
    }

    private void excludeDisjunctions() {
        int pointer = 0;
        Iterator<Operation> iter = operations.iterator();
        while (iter.hasNext()) {
            Operation op = iter.next();

            if (op == Operation.DISJUNCTION) {
                Expression disjugate = new Expression(
                        Operation.DISJUNCTION,
                        expressions.get(pointer),
                        expressions.get(pointer + 1)
                );
                expressions.set(pointer, disjugate);
                expressions.remove(pointer + 1);
                iter.remove();
            } else pointer++;
        }
    }

    private void excludeImplications() {
        int pointer = expressions.size() - 1;
        for(Operation op: operations) {
            Expression implicate = new Expression(
                    Operation.IMPLICATION,
                    expressions.get(pointer - 1),
                    expressions.get(pointer)
            );
            expressions.set(pointer, implicate);
            expressions.remove(pointer - 1);
            pointer--;
        }
    }
}
