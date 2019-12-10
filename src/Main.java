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
            if(!tokens[i].isEmpty()) hypotheses.add( new ExpressionParser(tokens[i]).parse() );

        List<Annotation> notes = new ArrayList<>();
        Expression result = new ExpressionParser(tokens[tokens.length - 1]).parse();

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine().replaceAll("\\s+", "");
            if (s.equals("done")) break;

            Expression exp = new ExpressionParser(s).parse();
            if (steps.indexOf(exp) >= 0) continue;

            Annotation annotation = getNote(exp);
            if (annotation == null) {
                System.out.println("Proof is incorrect");
                System.exit(0);
            }

            steps.add(exp);
            isUsed.add(false);
            notes.add(annotation);

            if (exp.equals(result)) break;
        }

        isUsed.set(isUsed.size() - 1, true);
        if (!steps.get(steps.size() - 1).equals(result)) {
            System.out.println("Proof is incorrect");
            System.exit(0);
        }

        String hype = String.join(", ", hypotheses.stream().map(Expression::toString).toArray(String[]::new) );
        System.out.println( (hype.isEmpty() ? "" : hype + " ") + "|- " + result.toString()  );

        List<Expression> finalSteps = new ArrayList<>();
        List<Annotation> finalNotes = new ArrayList<>();

        for (int i = 0; i < steps.size(); i++) {
            if (isUsed.get(i)) {
                finalSteps.add(steps.get(i));
                finalNotes.add(notes.get(i));
            }
        }

        for (int i = 0; i < finalSteps.size(); i++) {
            String note = finalNotes.get(i).getAnnotation(steps, finalSteps);
            System.out.println("[" + (i + 1) + ". " + note + "] " + finalSteps.get(i).toString());
        }
    }

    private static Annotation getNote(Expression exp) {

        int index = hypotheses.indexOf(exp);
        if (index >= 0) return new Annotation("Hypothesis", index);

        SchemeMatcher em = new SchemeMatcher(exp);
        index = em.matchAll(axioms);

        if (index != -1) return new Annotation("Ax. sch.", index);

        ModusPonensMatcher mpm = new ModusPonensMatcher(exp);
        int[] indexes = mpm.findArguments(steps);

        if (indexes != null) {
            isUsed.set(indexes[0], true);
            isUsed.set(indexes[1], true);
            return new Annotation("M.P.", indexes);
        }
        return null;
    }
}
