import java.math.BigInteger;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");
        BigInteger a = new BigInteger(input[0]);
        BigInteger b = new BigInteger(input[1]);
        BigInteger c = new BigInteger(input[2]);
        BigInteger d = new BigInteger(input[3]);

        BigInteger result = a.negate().multiply(b);
        result = result.add(c);
        result = result.subtract(d);
        System.out.println(result);
    }
}