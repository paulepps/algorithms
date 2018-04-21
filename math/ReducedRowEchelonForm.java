package math;

public class ReducedRowEchelonForm {
	// Reduced row echelon form via Gauss-Jordan elimination
	// with partial pivoting. This can be used for computing
	// the rank of a matrix.
	//
	// Running time: O(n^3)
	//
	// INPUT: a[][] = an nxm matrix
	//
	// OUTPUT: rref[][] = an nxm matrix (stored in a[][])
	// returns rank of a[][]

	static final double EPSILON = 1e-10;

	static int rref(double[][] a) {
		int n = a.length;
		int m = a[0].length;
		int r = 0;

		for (int c = 0; c < m && r < n; c++) {
			int j = r;

			for (int i = r + 1; i < n; i++)
				if (Math.abs(a[i][c]) > Math.abs(a[j][c]))
					j = i;

			if (Math.abs(a[j][c]) < EPSILON)
				continue;

			double[] temp = a[j];
			a[j] = a[r];
			a[r] = temp;

			double s = 1.0 / a[r][c];

			for (j = 0; j < m; j++)
				a[r][j] *= s;

			for (int i = 0; i < n; i++)
				if (i != r) {
					double t = a[i][c];
					for (j = 0; j < m; j++)
						a[i][j] -= t * a[r][j];
				}
			r++;
		}
		return r;
	}

	public static void main(String[] args) {

		final int n = 5;
		final int m = 4;
		double[][] A = new double[][] { { 16, 2, 3, 13 }, { 5, 11, 10, 8 }, { 9, 7, 6, 12 }, { 4, 14, 15, 1 },
				{ 13, 21, 21, 13 } };

		double[][] a = new double[n][];
		for (int i = 0; i < n; i++)
			a[i] = A[i];

		int rank = rref(a);

		// expected: 4
		System.out.println("Rank: " + rank);

		// expected: 1 0 0 1
		// 0 1 0 3
		// 0 0 1 -3
		// 0 0 0 2.78206e-15
		// 0 0 0 3.22398e-15
		System.out.println("rref: ");
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++)
				System.out.print(a[i][j] + " ");
			System.out.println();
		}
	}
}
