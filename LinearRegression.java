import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LinearRegression {
	Scanner sc;

	int N; // number of data points entered
	int M; // number of independent variables
	static final int maxN = 20; // maximum number of data points possible
	static final int sigDig = 4; // accuracy

	double[][] X;
	double[] Y;
	static double[] regrCoeff;

	public LinearRegression() throws FileNotFoundException {
		sc = new Scanner(new File("linear.in"));

		N = sc.nextInt();
		M = sc.nextInt();
	}

	public static void main(String[] args) throws FileNotFoundException {
		long start = System.currentTimeMillis();

		LinearRegression linear = new LinearRegression();

		linear.buildXY();
		linear.linRegr();

		String output = "y = " + linear.roundSigDig(regrCoeff[0], sigDig);
		
		for (int i = 1; i <= linear.M; i++)
			output += " + " + linear.roundSigDig(regrCoeff[i], sigDig) + "x" + i ;
		
		System.out.println(output);
		System.out.println("$:" + (System.currentTimeMillis() - start));
	}

	private double roundSigDig(double theNumber, int numDigits) {
		if (theNumber == 0)
			return (0);
		else if (Math.abs(theNumber) < 0.000000000001)
			return (0);
		// warning: ignores numbers less than 10^(-12)
		else {
			int k = (int) (Math.floor(Math.log(Math.abs(theNumber)) / Math.log(10)) - numDigits);
			double k2 = shiftRight(Math.round(shiftRight(Math.abs(theNumber), -k)), k);
			if (theNumber > 0)
				return (k2);
			else
				return (-k2);
		} // end else
	}

	private double shiftRight(double theNumber, int k) {
		int k2 = 1;

		if (k == 0)
			return (theNumber);
		else {
			int num = k;
			if (num < 0)
				num = -num;
			for (int i = 1; i <= num; i++) {
				k2 = k2 * 10;
			}
		}
		if (k > 0) {
			return (k2 * theNumber);
		} else {
			return (theNumber / k2);
		}
	}

	private void linRegr() {
		int i, j, k;
		double sum;

		double[] B = new double[M + 2];
		double[][] P = new double[M + 2][M + 2];
		double[][] invP = new double[M + 2][M + 2];

		for (i = 1; i <= N; i++)
			X[0][i] = 1;
		for (i = 1; i <= M + 1; i++) {
			sum = 0;
			for (k = 1; k <= N; k++)
				sum = sum + X[i - 1][k] * Y[k];
			B[i] = sum;

			for (j = 1; j <= M + 1; j++) {
				sum = 0;
				for (k = 1; k <= N; k++)
					sum = sum + X[i - 1][k] * X[j - 1][k];
				P[i][j] = sum;
			}
		} // i

		invP = inverse(P);

		regrCoeff = new double[M + 1];

		for (k = 0; k <= M; k++) {
			sum = 0;

			for (j = 1; j <= M + 1; j++) {
				sum = sum + invP[k + 1][j] * B[j];
			} // j
			regrCoeff[k] = sum;
		} // k
	} // end of with math }

	private double[][] inverse(double[][] A) {
		int Length = A.length - 1;
		double[][] B = new double[Length + 1][Length + 1]; // inverse
		double d = det(A);
		if (d == 0)
			System.out.println("singular matrix--check data");
		else {
			int i, j;
			for (i = 1; i <= Length; i++) {
				for (j = 1; j <= Length; j++) {
					// create the minor
					double[][] minor = new double[Length][Length];
					int m, n, theColumn, theRow;

					for (m = 1; m <= Length - 1; m++) // columns
					{
						if (m < j)
							theColumn = m;
						else
							theColumn = m + 1;
						for (n = 1; n <= Length - 1; n++) {
							if (n < i)
								theRow = n;
							else
								theRow = n + 1;
							minor[n][m] = A[theRow][theColumn];
						} // n
					} // m
						// inverse entry
					int factor;
					if ((i + j) % 2 == 0)
						factor = 1;
					else
						factor = -1;

					B[j][i] = det(minor) * factor / d;

				} // j

			} // end i
		} // recursion
		return (B);
	}

	private double det(double[][] A) {
		int Length = A.length - 1;
		double sum = 0.0;

		// formal length of a matrix is one bigger
		if (Length == 1)
			return (A[1][1]);
		else {
			int factor = 1;

			for (int i = 1; i <= Length; i++) {
				if (A[1][i] != 0) {
					// create the minor
					double[][] minor = new double[Length][Length];
					int theColumn;

					for (int m = 1; m <= Length - 1; m++) // columns
					{
						if (m < i)
							theColumn = m;
						else
							theColumn = m + 1;
						for (int n = 1; n <= Length - 1; n++) {
							minor[n][m] = A[n + 1][theColumn];
						} // n
					} // m
						// compute its determinant
					sum = sum + A[1][i] * factor * det(minor);
				}
				factor = -factor; // alternating sum
			} // end i
		} // recursion
		return (sum);
	}

	private void buildXY() {

		X = new double[M + 1][N + 1];
		Y = new double[N + 1];

		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= M; j++) {
				X[j][N] = sc.nextDouble();
			}
			Y[N] = sc.nextDouble();
		}
	}
}
