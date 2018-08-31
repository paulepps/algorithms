// http://poj.org/problem?id=2195

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Hungarian {

	// a[1..n][1..m] >= 0, n <= m
	public static int solveAssignmentProblem(int[][] a) {
		int n = a.length - 1;
		int m = a[0].length - 1;

		int[] u = new int[n + 1];
		int[] v = new int[m + 1];
		int[] p = new int[m + 1];
		int[] way = new int[m + 1];

		for (int i = 1; i <= n; ++i) {
			p[0] = i;
			int j0 = 0;
			int[] minv = new int[m + 1];

			Arrays.fill(minv, Integer.MAX_VALUE);

			boolean[] used = new boolean[m + 1];

			do {
				used[j0] = true;

				int i0 = p[j0];
				int delta = Integer.MAX_VALUE;
				int j1 = 0;

				for (int j = 1; j <= m; ++j)
					if (!used[j]) {
						int cur = a[i0][j] - u[i0] - v[j];
						if (cur < minv[j]) {
							minv[j] = cur;
							way[j] = j0;
						}
						if (minv[j] < delta) {
							delta = minv[j];
							j1 = j;
						}
					}
				for (int j = 0; j <= m; ++j)
					if (used[j]) {
						u[p[j]] += delta;
						v[j] -= delta;
					} else
						minv[j] -= delta;
				j0 = j1;
			} while (p[j0] != 0);

			do {
				int j1 = way[j0];
				p[j0] = p[j1];
				j0 = j1;
			} while (j0 != 0);
		}
		return -v[0];
	}

	public static void main(String[] args) {

		List<Pair<Integer, Integer>> men;
		List<Pair<Integer, Integer>> houses;

		Pair<Integer, Integer> p;

		Scanner sc = new Scanner(System.in);

		int N = sc.nextInt();
		int M = sc.nextInt();

		while (N != 0) {

			men = new ArrayList<Pair<Integer, Integer>>();
			houses = new ArrayList<Pair<Integer, Integer>>();

			for (int i = 0; i < N; i++) {
				String s = sc.next();

				for (int j = 0; j < M; j++) {
					char c = s.charAt(j);

					if (c == 'm') {
						p = new Pair<Integer, Integer>(i, j);
						men.add(p);
					}

					if (c == 'H') {
						p = new Pair<Integer, Integer>(i, j);
						houses.add(p);
					}
				}
			}

			int count = houses.size();
			int[][] a = new int[count + 1][count + 1];

			for (int i = 1; i <= count; i++) {
				Pair<Integer, Integer> p1 = houses.get(i - 1);

				for (int j = 1; j <= count; j++) {
					Pair<Integer, Integer> p2 = men.get(j - 1);

					a[i][j] = distance(p1, p2);
				}
			}

			System.out.println(solveAssignmentProblem(a));

			N = sc.nextInt();
			M = sc.nextInt();
		}
		
		sc.close();
	}

	static int distance(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
		return Math.abs(p1.e1 - p2.e1) + Math.abs(p1.e2 - p2.e2);
	}
}
