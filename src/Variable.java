public class Variable extends Operator {
    private String name;

    public Variable(String name) {
        super(Operation.NONE);
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
