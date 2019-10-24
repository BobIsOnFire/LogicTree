public class Variable extends Expression implements Comparable<Variable> {
    private String name;

    public Variable(String name) {
        super(Operation.NONE);
        this.name = name;
    }

    public String getName() {
        return name;
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

    @Override
    public int compareTo(Variable o) {
        return this.name.compareTo(o.name);
    }
}
