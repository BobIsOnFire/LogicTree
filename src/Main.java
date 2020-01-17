import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Expression[] axioms = Expression.getAxiomSchemes();
    private static List<Expression> steps = new ArrayList<>();
    private static List<Expression> hypotheses = new ArrayList<>();
    private static List<Boolean> isUsed = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) System.exit(0);

        String input = scanner.nextLine();
        String[] tokens = input.replaceAll("\\s+", "").split("(,|\\|-)");
        for (int i = 0; i < tokens.length - 1; i++)
            if (!tokens[i].isEmpty()) hypotheses.add(new ExpressionParser(tokens[i]).parse());

        Expression result = new ExpressionParser(tokens[tokens.length - 1]).parse();
        // if fails tests => unused step is incorrect (who cares?)
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine().replaceAll("\\s+", "");
            if (s.equals("done")) break;

            Expression exp = new ExpressionParser(s).parse();
            steps.add(exp);
            isUsed.add(false);
            if (exp.equals(result)) break;
        }

        isUsed.set(isUsed.size() - 1, true);
        if (!steps.get(steps.size() - 1).equals(result)) {
            System.out.println("Proof is incorrect");
            System.exit(0);
        }

        setUsage(steps.size() - 1);

        List<Expression> finalSteps = new ArrayList<>();
        int[] stepsMapper = new int[steps.size()];

        int k = 0;
        for (int i = 0; i < steps.size(); i++) {
            if (isUsed.get(i)) {
                stepsMapper[i] = k;

                Expression step = steps.get(i);
                Note note = step.getNote();

                if (note.isMP()) {
                    note.setI(stepsMapper[note.getI()]);
                    note.setJ(stepsMapper[note.getJ()]);
                }

                finalSteps.add(step);
                k++;
            }
        }

        String hype = String.join(", ", hypotheses.stream().map(Expression::toString).toArray(String[]::new));
        System.out.printf("%s|- %s\n", (hype.isEmpty() ? "" : hype + " "), result);
        for (int i = 0; i < finalSteps.size(); i++) {
            Expression step = finalSteps.get(i);
            System.out.printf("[%d. %s] %s\n", (i + 1), step.getNote(), step);
        }
    }

    private static Note defineNote(Expression exp, int sup) {
        int index = hypotheses.indexOf(exp);
        if (index >= 0) return new Note("Hypothesis", index);

        SchemeMatcher em = new SchemeMatcher(exp);
        index = em.matchAll(axioms);

        if (index >= 0) return new Note("Ax. sch.", index);

        ModusPonensMatcher mpm = new ModusPonensMatcher(exp);
        int[] indexes = mpm.findArguments(steps, sup);

        if (indexes != null)
            return new Note("M.P.", indexes);

        return null;
    }

    private static void setUsage(int index) {
        isUsed.set(index, true);
        Expression step = steps.get(index);
        Note note = defineNote(step, index);

        if (note == null) {
            System.out.println("Proof is incorrect");
            System.exit(0);
        }

        step.setNote(note);
        if (note.isMP()) {
            setUsage(note.getI());
            setUsage(note.getJ());
        }
    }
}
