import java.util.List;

public class ModusPonensMatcher {
    private Expression current;

    public ModusPonensMatcher(Expression current) {
        this.current = current;
    }

    public int[] findArguments(List<Expression> expressions) {
        for (int i = expressions.size() - 1; i >= 0; i--) {
            Expression exp = expressions.get(i);

            if (exp.getOperation() == Operation.IMPLICATION && exp.getOperands()[1].equals(current))
                for (int j = expressions.size() - 1; j >= 0; j--)
                    if (expressions.get(j).equals(exp.getOperands()[0])) return new int[]{i, j};
        }

        return null;
    }
}
