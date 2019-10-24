import java.util.Map;
import java.util.TreeMap;

public class SchemeMatcher {
    private Map<Variable, Expression> map;
    private Expression current;

    public SchemeMatcher(Expression current) {
        this.current = current;
    }

    public Map<Variable, Expression> match(Expression axiom) {
        map = new TreeMap<>();
        findMatching(current, axiom);
        return map;
    }

    public int matchAll(Expression[] axioms) {
        int i = -1;
        Map<Variable, Expression> map = null;
        while (i < axioms.length - 1 && map == null) {
            i++;
            map = match(axioms[i]);
        }

        if (map == null) return -1;
        else return i;
    }

    private void findMatching(Expression exp, Expression matcher) {
        if (matcher instanceof Variable) {
            Variable v = (Variable) matcher;
            Expression matched = map.putIfAbsent(v, exp);

            if (matched != null && !matched.equals(exp)) map = null;
            return;
        }

        if (exp.getOperation() != matcher.getOperation()) {
            map = null;
            return;
        }

        Expression[] expOperands = exp.getOperands();
        Expression[] matcherOperands = matcher.getOperands();

        if (expOperands.length != matcherOperands.length) {
            map = null;
            return;
        }

        for (int i = 0; i < expOperands.length; i++) {
            findMatching(expOperands[i], matcherOperands[i]);
            if (map == null) return;
        }
    }
}
