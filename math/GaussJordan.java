package math;

public class GaussJordan {
	// Gauss-Jordan elimination with full pivoting.
	//
	// Uses:
	// (1) solving systems of linear equations (AX=B)
	// (2) inverting matrices (AX=I)
	// (3) computing determinants of square matrices
	//
	// Running time: O(n^3)
	//
	// INPUT: a[][] = an nxn matrix
	// b[][] = an nxm matrix
	//
	// OUTPUT: X = an nxm matrix (stored in b[][])
	// A^{-1} = an nxn matrix (stored in a[][])
	// returns determinant of a[][]

	static final double EPS = 1e-10;

	static double GJ(double[][] a, double[][] b) {
		final int n = a.length;
		final int m = b[0].length;

		int[] irow = new int[n];
		int[] icol = new int[n];
		int[] ipiv = new int[n];

		double det = 1;

		for (int i = 0; i < n; i++) {
			int pj = -1, pk = -1;

			for (int j = 0; j < n; j++)
				if (ipiv[j] == 0)
					for (int k = 0; k < n; k++)
						if (ipiv[k] == 0)
							if (pj == -1 || Math.abs(a[j][k]) > Math.abs(a[pj][pk])) {
								pj = j;
								pk = k;
							}
			if (Math.abs(a[pj][pk]) < EPS) {
				System.err.println("Matrix is singular.");
				return 0;
			}

			ipiv[pk]++;

			double[] temp;

			temp = a[pj];
			a[pj] = a[pk];
			a[pk] = temp;

			temp = b[pj];
			b[pj] = b[pk];
			b[pk] = temp;

			if (pj != pk)
				det *= -1;

			irow[i] = pj;
			icol[i] = pk;

			double c = 1.0 / a[pk][pk];
			det *= a[pk][pk];
			a[pk][pk] = 1.0;

			for (int p = 0; p < n; p++)
				a[pk][p] *= c;
			for (int p = 0; p < m; p++)
				b[pk][p] *= c;
			for (int p = 0; p < n; p++)
				if (p != pk) {
					c = a[p][pk];
					a[p][pk] = 0;
					for (int q = 0; q < n; q++)
						a[p][q] -= a[pk][q] * c;
					for (int q = 0; q < m; q++)
						b[p][q] -= b[pk][q] * c;
				}
		}

		for (int p = n - 1; p >= 0; p--)
			if (irow[p] != icol[p]) {
				for (int k = 0; k < n; k++) {
					double tmp;

					tmp = a[k][irow[p]];
					a[k][irow[p]] = a[k][icol[p]];
					a[k][icol[p]] = tmp;
				}
			}

		return det;
	}

	public static void main(String[] args) {

		final int n = 4;
		final int m = 2;
		double A[][] = new double[][] { { 1, 2, 3, 4 }, { 1, 0, 1, 0 }, { 5, 3, 2, 4 }, { 6, 1, 4, 6 } };
		double B[][] = new double[][] { { 1, 2 }, { 4, 3 }, { 5, 6 }, { 8, 7 } };

		double[][] a = new double[n][];
		double[][] b = new double[n][];

		for (int i = 0; i < n; i++) {
			a[i] = A[i];
			b[i] = B[i];
		}

		double det = GJ(a, b);

		// expected: 60
		System.out.println("Determinant: " + det);

		// expected: -0.233333 0.166667 0.133333 0.0666667
		// 0.166667 0.166667 0.333333 -0.333333
		// 0.233333 0.833333 -0.133333 -0.0666667
		// 0.05 -0.75 -0.1 0.2
		System.out.println("Inverse: ");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				System.out.print(a[i][j] + " ");
			System.out.println();
		}

		// expected: 1.63333 1.3
		// -0.166667 0.5
		// 2.36667 1.7
		// -1.85 -1.35
		System.out.println("Solution: ");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++)
				System.out.print(b[i][j] + " ");
			System.out.println();
		}
	}
}
