public class Note {
    private boolean isMP;
    private int i;
    private int j;
    private String type;

    public Note(String type, int... nums) {
        this.type = type;
        this.isMP = type.equals("M.P.") && nums.length > 1;
        i = nums[0];
        if (isMP) j = nums[1];
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

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() {
        if (!isMP) return type + " " + (i + 1);
        return type + " " + (i + 1) + ", " + (j + 1);
    }
}
