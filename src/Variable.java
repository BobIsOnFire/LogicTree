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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable)) return false;
        return ((Variable) obj).name.equals(this.name);
    }
}
