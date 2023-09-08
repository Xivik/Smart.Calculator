import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{12,}$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(scanner.nextLine());

        if (matcher.matches()) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}