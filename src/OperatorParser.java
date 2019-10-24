import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OperatorParser {
    private int bracketCounter;
    private int pointer;
    private boolean readingVariable;
    private String input;

    private List<Operator> operators;
    private List<Operation> operations;

    public OperatorParser(String input) {
        this.input = input;
        bracketCounter = 0;
        pointer = -1;
        readingVariable = false;
        operators = new ArrayList<>();
        operations = new ArrayList<>();
    }

    public Operator parse() {
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
                        OperatorParser op = new OperatorParser(token);
                        operators.add( op.parse() );
                    }
                    break;
                case '!':
                    if (readingVariable) {
                        readingVariable = false;
                        String name = input.substring(pointer, i);
                        operators.add( new Variable(name) );
                    }
                    if (bracketCounter == 0) operations.add(Operation.NEGATION);
                    break;
                case '&':
                    if (readingVariable) {
                        readingVariable = false;
                        String name = input.substring(pointer, i);
                        operators.add( new Variable(name) );
                    }
                    if (bracketCounter == 0) operations.add(Operation.CONJUNCTION);
                    break;
                case '|':
                    if (readingVariable) {
                        readingVariable = false;
                        String name = input.substring(pointer, i);
                        operators.add( new Variable(name) );
                    }
                    if (bracketCounter == 0) operations.add(Operation.DISJUNCTION);
                    break;
                case '-':
                    if (readingVariable) {
                        readingVariable = false;
                        String name = input.substring(pointer, i);
                        operators.add( new Variable(name) );
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
            operators.add( new Variable(name) );
        }

        excludeNegations();
        excludeConjunctions();
        excludeDisjunctions();
        excludeImplications();

        return operators.get(0);
    }

    private void excludeNegations() {
        int pointer = 0;
        Iterator<Operation> iter = operations.iterator();

        while (iter.hasNext()) {
            Operation op = iter.next();

            if (op == Operation.NEGATION) {
                Operator negate = new Operator(
                        Operation.NEGATION,
                        operators.get(pointer)
                );
                operators.set(pointer, negate);
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
                Operator conjugate = new Operator(
                        Operation.CONJUNCTION,
                        operators.get(pointer),
                        operators.get(pointer + 1)
                );
                operators.set(pointer, conjugate);
                operators.remove(pointer + 1);
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
                Operator disjugate = new Operator(
                        Operation.DISJUNCTION,
                        operators.get(pointer),
                        operators.get(pointer + 1)
                );
                operators.set(pointer, disjugate);
                operators.remove(pointer + 1);
                iter.remove();
            } else pointer++;
        }
    }

    private void excludeImplications() {
        int pointer = operators.size() - 1;
        for(Operation op: operations) {
            Operator implicate = new Operator(
                    Operation.IMPLICATION,
                    operators.get(pointer - 1),
                    operators.get(pointer)
            );
            operators.set(pointer, implicate);
            operators.remove(pointer - 1);
            pointer--;
        }
    }
}
