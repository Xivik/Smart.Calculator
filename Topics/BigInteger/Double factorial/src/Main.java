import java.math.BigInteger;

class DoubleFactorial {
    public static BigInteger calcDoubleFactorial(int n) {
        if (n <= 0) {
            return BigInteger.ONE; // Double factorial of 0 or negative numbers is 1
        }

        BigInteger result = BigInteger.ONE;
        for (int i = n; i >= 1; i -= 2) {
            result = result.multiply(BigInteger.valueOf(i));
        }

        return result;
    }
}