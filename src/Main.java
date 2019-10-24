import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) System.exit(0);

        String input = scanner.nextLine().replaceAll("\\s+", "");

        OperatorParser op = new OperatorParser(input);
        Operator main = op.parse();
        System.out.println( main.toString() );
    }
}
