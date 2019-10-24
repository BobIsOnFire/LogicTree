public enum Operation {
    NEGATION, CONJUNCTION, DISJUNCTION, IMPLICATION, NONE;

    public String getSign() {
        switch (this) {
            case NEGATION:
                return "!";
            case CONJUNCTION:
                return "&";
            case DISJUNCTION:
                return "|";
            case IMPLICATION:
                return "->";
            default:
                return "";
        }
    }
}
