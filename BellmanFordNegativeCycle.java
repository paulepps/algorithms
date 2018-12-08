
import java.util.Scanner;

// http://poj.org/problem?id=3259

public class BellmanFordNegativeCycle {

	static final int maxn = 510;
	static final int maxw = 2500 * 2 + 200 + 10;
	static final int INF = 999999999;
	static int[] d = new int[maxn];
	static int n, m;
	static Edge2[] edge = new Edge2[maxw];

	static boolean BellmanFord() {
		for (int i = 1; i <= n; i++)
			d[i] = INF; 
		d[1] = 0; 

		for (int i = 1; i < n; i++) {
			boolean flag = true; 
			for (int j = 0; j < m; j++) {
				int u = edge[j].u;
				int v = edge[j].v;
				int t = edge[j].t;

				if (d[v] > d[u] + t)
				{
					d[v] = d[u] + t;
					flag = false;
				}
			}
			if (flag)
				return false; 
		}

		for (int i = 0; i < m; i++) {
			if (d[edge[i].v] > d[edge[i].u] + edge[i].t)
				return true;
		}
		return false;
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int T;
		int M, W;

		T = sc.nextInt();

		while (T-- > 0) {
			n = sc.nextInt();
			M = sc.nextInt();
			W = sc.nextInt();

			m = 0;

			int u, v, t;
			for (int i = 1; i <= M; i++) {
				u = sc.nextInt();
				v = sc.nextInt();
				t = sc.nextInt();
				
				edge[m] = new Edge2();
				
				edge[m].u = u;
				edge[m].v = v;
				edge[m++].t = t;

				edge[m] = new Edge2();
				
				edge[m].u = v;
				edge[m].v = u;
				edge[m++].t = t;
			}

			for (int i = 1; i <= W; i++) {
				u = sc.nextInt();
				v = sc.nextInt();
				t = sc.nextInt();

				edge[m] = new Edge2();
				
				edge[m].u = u;
				edge[m].v = v;
				edge[m++].t = -t;
			}

			if (BellmanFord())
				System.out.println("YES");
			else
				System.out.println("NO");
		}
	}
}

class Edge2 {
	int u, v;
	int t;
}