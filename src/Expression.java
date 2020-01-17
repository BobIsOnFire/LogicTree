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
    protected String description;
    private Note note;

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Expression(Operation operation, Expression... operands) {
        this.operation = operation;
        this.operands = operands;

        if (operation == Operation.NONE) return;

        String sign = operation.getSign();
        if (operation == Operation.NEGATION)
            this.description = sign + operands[0].toString();
        else
            this.description = String.format("(%s %s %s)", operands[0], sign, operands[1]);
    }

    public Expression[] getOperands() {
        return operands;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return this.description;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Expression)) return false;
        return ((Expression) obj).description.equals(this.description);
    }
}
