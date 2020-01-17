import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Expression[] axioms = Expression.getAxiomSchemes();
    private static List<Expression> steps = new ArrayList<>();
    private static List<Expression> hypotheses = new ArrayList<>();
    private static List<Boolean> isUsed = new ArrayList<>();

    public static void main(String[] args) {
        Stopwatch globalW = Stopwatch.start("Global stopwatch");
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) System.exit(0);

        String input = scanner.nextLine();
        String[] tokens = input.replaceAll("\\s+", "").split("(,|\\|-)");

        Stopwatch hypotW = Stopwatch.start("Parsing " + input);
        for (int i = 0; i < tokens.length - 1; i++)
            if (!tokens[i].isEmpty()) hypotheses.add(new ExpressionParser(tokens[i]).parse());
        hypotW.stopAndReport();

        Expression result = new ExpressionParser(tokens[tokens.length - 1]).parse();
        // if fails tests => unused step is incorrect (who cares?)
        Stopwatch proofW = Stopwatch.start("Parsing proof");
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine().replaceAll("\\s+", "");
            if (s.equals("done")) break;

            Stopwatch parseW = Stopwatch.start("Parsing " + s);
            Expression exp = new ExpressionParser(s).parse();
            parseW.stopAndReport();

            steps.add(exp);
            isUsed.add(false);
            if (exp.equals(result)) break;
        }
        proofW.stopAndReport();

        isUsed.set(isUsed.size() - 1, true);
        if (!steps.get(steps.size() - 1).equals(result)) {
            System.out.println("Proof is incorrect");
            System.exit(0);
        }

        setUsage(steps.size() - 1);

        List<Expression> finalSteps = new ArrayList<>();
        int[] stepsMapper = new int[steps.size()];

        Stopwatch mapW = Stopwatch.start("Remapping expressions");
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
        mapW.stopAndReport();

        Stopwatch printW = Stopwatch.start("Printing proof");
        String[] hypots = new String[hypotheses.size()];
        for (int i = 0; i < hypotheses.size(); i++) hypots[i] = hypotheses.get(i).toString();
        String hype = String.join(", ", hypots);
        System.out.printf("%s|- %s\n", (hype.isEmpty() ? "" : hype + " "), result);
        printW.stopAndReport();
        printW.clear().resume();
        for (int i = 0; i < finalSteps.size(); i++) {
            Expression step = finalSteps.get(i);
            System.out.printf("[%d. %s] %s\n", (i + 1), step.getNote(), step);
        }
        printW.stopAndReport();
        globalW.stopAndReport();
    }

    private static Note defineNote(Expression exp, int sup) {
        Stopwatch hypotW = Stopwatch.start("\tMatching hypots " + sup);
        int index = hypotheses.indexOf(exp);
        hypotW.stopAndReport();

        if (index >= 0) return new Note("Hypothesis", index);

        Stopwatch schemeW = Stopwatch.start("\tMatching axioms " + sup);
        SchemeMatcher em = new SchemeMatcher(exp);
        index = em.matchAll(axioms);
        schemeW.stopAndReport();

        if (index >= 0) return new Note("Ax. sch.", index);

        Stopwatch modusW = Stopwatch.start("\tMatching MP " + sup);
        ModusPonensMatcher mpm = new ModusPonensMatcher(exp);
        int[] indexes = mpm.findArguments(steps, sup);
        modusW.stopAndReport();

        if (indexes != null)
            return new Note("M.P.", indexes);

        return null;
    }

    private static void setUsage(int index) {
        Stopwatch usageW = Stopwatch.start("Setting usage " + index);
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
        usageW.stopAndReport();
    }
}
