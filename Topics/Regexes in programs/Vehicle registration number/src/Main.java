import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String regNum = scanner.nextLine(); // a valid or invalid registration number


        String regex = "[ABEKMHOPCTYX]\\d{3}[ABEKMHOPCTYX][ABEKMHOPCTYX]"; // Example regex pattern, replace with your desired pattern
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(regNum);

        if (matcher.matches()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}