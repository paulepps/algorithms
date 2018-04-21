package math;

import java.math.BigInteger;
import java.util.Scanner;

public class Catalan {

	BigInteger catalan(int n) {

		// dp array containing the sum
		BigInteger[] dpArray = new BigInteger[n + 1];

		dpArray[0] = BigInteger.ONE;
		dpArray[1] = BigInteger.ONE;

		for (int i = 2; i <= n; i++) {
			dpArray[i] = BigInteger.ZERO;

			for (int k = 0; k < i; k++) {
				dpArray[i] = dpArray[i].add(dpArray[k].multiply(dpArray[i - k - 1]));
			}
		}

		return dpArray[n];
	}

	BigInteger catalanClosedForm(int n) {

		if (n <= 1)
			return BigInteger.ONE;

		BigInteger num = BigInteger.ONE;
		BigInteger denom = BigInteger.ONE;
		
		for (int k = 2; k <= n; k++) {
			num = num.multiply(new BigInteger((n + k) + ""));
			denom = denom.multiply(new BigInteger(k + ""));
		}
		
		return num.divide(denom);
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();

		Catalan C = new Catalan();

		while (n != -1) {
			System.out.println(C.catalanClosedForm(n).toString());
			n = sc.nextInt();
		}
		sc.close();
	}
}
