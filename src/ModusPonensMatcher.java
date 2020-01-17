import java.util.List;

public class ModusPonensMatcher {
    private Expression current;

    public ModusPonensMatcher(Expression current) {
        this.current = current;
    }

    public int[] findArguments(List<Expression> expressions, int sup) {
        for (int i = 0; i < sup; i++) {
            Expression exp = expressions.get(i);

            if (exp.getOperation() == Operation.IMPLICATION && exp.getOperands()[1].equals(current))
                for (int j = 0; j < sup; j++)
                    if (expressions.get(j).equals(exp.getOperands()[0])) return new int[]{i, j};
        }

        return null;
    }
}
