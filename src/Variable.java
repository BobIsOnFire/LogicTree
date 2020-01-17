public class Variable extends Expression implements Comparable<Variable> {
    public Variable(String name) {
        super(Operation.NONE);
        this.description = name;
    }

    @Override
    public int compareTo(Variable o) {
        return this.description.compareTo(o.description);
    }
}
