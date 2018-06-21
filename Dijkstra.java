
import java.util.Scanner;

public class Dijkstra {

	static final int MaxN = 1100;
	static final int MaxInt = 200000000;

	static int[][] map = new int[MaxN][MaxN];
	static int[] dist = new int[MaxN];
	static boolean[] mark = new boolean[MaxN];

	static int n, start, end;

	static void dij() {
		int i, j, mini, minip = 0;

		for (i = 1; i <= n; i++)
			dist[i] = MaxInt;

		dist[start] = 0;

		for (i = 1; i <= n; i++) {
			mini = MaxInt;
			for (j = 1; j <= n; j++) {
				if (!mark[j] && dist[j] < mini) {
					mini = dist[j];
					minip = j;
				}
			}

			mark[minip] = true;

			for (j = 1; j <= n; j++) {
				if (!mark[j] && dist[j] > dist[minip] + map[minip][j])
					dist[j] = dist[minip] + map[minip][j];
			}
		}
	}

	public static void main(String[] args) {

		int a, b;

		Scanner sc = new Scanner(System.in);

		n = sc.nextInt();
		start = sc.nextInt();
		end = sc.nextInt();

		for (int i = 1; i <= n; i++)
			for (int j = 1; j <= n; j++)
				map[i][j] = map[j][i] = MaxInt;
		
		// add edges and weights
		for (int i = 1; i <= n; i++) {
			a = sc.nextInt();
			for (int j = 0; j < a; j++) {
				b = sc.nextInt();
				if (j == 0)
					map[i][b] = 0;
				else
					map[i][b] = 1;
			}
		}
		
		sc.close();
		
		dij();

		if (dist[end] != MaxInt)
			System.out.println(dist[end]);
		else
			System.out.println(-1);
	}
}