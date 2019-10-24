public class Expression {
    public static Expression[] getAxiomSchemes() {
        return new Expression[] {
                new ExpressionParser("A->B->A").parse(),
                new ExpressionParser("(A->B)->(A->B->C)->(A->C)").parse(),
                new ExpressionParser("A->B->A&B").parse(),
                new ExpressionParser("A&B->A").parse(),
                new ExpressionParser("A&B->B").parse(),
                new ExpressionParser("A->A|B").parse(),
                new ExpressionParser("B->A|B").parse(),
                new ExpressionParser("(A->C)->(B->C)->(A|B->C)").parse(),
                new ExpressionParser("(A->B)->(A->!B)->!A").parse(),
                new ExpressionParser("!!A->A").parse()
        };
    }

    private Operation operation;
    private Expression[] operands;

    public Expression(Operation operation, Expression... operands) {
        this.operation = operation;
        this.operands = operands;
    }

    public Expression[] getOperands() {
        return operands;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        String sign = operation.getSign();
        if (operation == Operation.NEGATION)
            return sign + operands[0].toString();
        if (operation == Operation.NONE)
            return operands[0].toString();

        return "(" + operands[0] + " " + sign + " " + operands[1] + ')';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Expression)) return false;

        Expression op = (Expression) obj;
        if (op.operation != this.operation) return false;
        if (op.operands.length != this.operands.length) return false;

        for (int i = 0; i < this.operands.length; i++)
            if ( !this.operands[i].equals( op.operands[i] ) ) return false;
        return true;
    }
}
