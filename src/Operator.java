public class Operator {
    private Operation operation;
    private Operator[] operators;

    public Operator(Operation operation, Operator... operators) {
        this.operation = operation;
        this.operators = operators;
    }

    @Override
    public String toString() {
        String sign = operation.getSign();
        if (operation == Operation.NEGATION)
            return "(" + sign + operators[0].toString() + ")";
        if (operation == Operation.NONE)
            return operators[0].toString();
        StringBuilder sb = new StringBuilder();
        sb.append('(').append(sign);

        for (Operator operator: operators)
            sb.append(',').append(operator.toString());

        return sb.append(')').toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Operator)) return false;

        Operator op = (Operator) obj;
        if (op.operation != this.operation) return false;
        if (op.operators.length != this.operators.length) return false;

        for (int i = 0; i < this.operators.length; i++)
            if ( !this.operators[i].equals( op.operators[i] ) ) return false;
        return true;
    }
}
