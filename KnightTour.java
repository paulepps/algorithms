import java.util.Scanner;

// Java program for Knight Tour problem
class KnightTour {
	static int H, W;

	/*
	 * A utility function to check if i,j are valid indexes for H*W chessboard
	 */
	static boolean isSafe(int x, int y, int sol[][]) {
		return (x >= 0 && x < H && y >= 0 && y < W && sol[x][y] == -1);
	}

	/*
	 * A utility function to print solution matrix sol[N][N]
	 */
	static void printSolution(int sol[][]) {
		String[] solution = new String[H * W];

		/*
		 * The solution matrix (sol[][]) contains the solution as a
		 * sequence of numbers from 0 to H * W -1, showing the order
		 * in which each square is visited. We need to convert that 
		 * to the output format specified by the problem description.
		 */
		for (int x = 0; x < H; x++) {
			for (int y = 0; y < W; y++) {
				char row = (char) ('A' + y);
				int col = x + 1;
				solution[sol[x][y]] = "" + row + col;
			}
		}

		StringBuilder sb = new StringBuilder();

		for (String s : solution) {
			sb.append(s);
		}

		System.out.println(sb.toString());
	}

	/*
	 * This function solves the Knight Tour problem using Backtracking. This
	 * function mainly uses solveKTUtil() to solve the problem. It returns false if
	 * no complete tour is possible, otherwise return true and prints the tour.
	 * Please note that there may be more than one solutions, this function prints
	 * one of the feasible solutions.
	 */
	static boolean solveKT(int test) {
		int sol[][] = new int[H][W];

		/* Initialization of solution matrix */
		for (int x = 0; x < H; x++)
			for (int y = 0; y < W; y++)
				sol[x][y] = -1;

		/*
		 * xMove[] and yMove[] define next move of Knight. xMove[] is for next value of
		 * x coordinate yMove[] is for next value of y coordinate
		 */
		int xMove[] = { -1, 1, -2, 2, -2, 2, -1, 1 };
		int yMove[] = { -2, -2, -1, -1, 1, 1, 2, 2 };

		// Since the Knight is initially at the first block
		sol[0][0] = 0;

		System.out.println("Scenario #" + test + ":");

		/*
		 * Start from 0,0 and explore all tours using solveKTUtil()
		 */
		if (!solveKTUtil(0, 0, 1, sol, xMove, yMove)) {
			System.out.println("impossible");
			return false;
		} else
			printSolution(sol);

		return true;
	}

	/*
	 * A recursive utility function to solve Knight Tour problem
	 */
	static boolean solveKTUtil(int x, int y, int movei, int sol[][], int xMove[], int yMove[]) {
		int k, next_x, next_y;
		if (movei == H * W)
			return true;

		/*
		 * Try all next moves from the current coordinate x, y
		 */
		for (k = 0; k < 8; k++) {
			next_x = x + xMove[k];
			next_y = y + yMove[k];
			if (isSafe(next_x, next_y, sol)) {
				sol[next_x][next_y] = movei;
				if (solveKTUtil(next_x, next_y, movei + 1, sol, xMove, yMove))
					return true;
				else
					sol[next_x][next_y] = -1;// backtracking
			}
		}

		return false;
	}

	/* Driver program to test above functions */
	public static void main(String args[]) {

		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		int test = 1;

		while (test <= T) {
			H = sc.nextInt();
			W = sc.nextInt();

			solveKT(test);

			if (test < T)
				System.out.println();

			test++;
		}
	}
}