import java.util.List;

public class Annotation {
    private boolean isMP;
    private int i;
    private int j;
    private String type;

    public Annotation(String type, int... nums) {
        this.type = type;
        this.isMP = type.equals("M.P.") && nums.length > 1;
        i = nums[0];
        if (isMP) j = nums[1];
    }

    public String getAnnotation(List<Expression> steps, List<Expression> finalSteps) {
        if (!isMP) return getAnnotation();
        int iFinal = finalSteps.indexOf(steps.get(i));
        int jFinal = finalSteps.indexOf(steps.get(j));

        return type + " " + (iFinal + 1) + ", " + (jFinal + 1);
    }

    public boolean isMP() {
        return isMP;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public String getAnnotation() {
        return type + " " + (i + 1);
    }
}
