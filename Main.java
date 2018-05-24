import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	static final int MAXN = 20000 + 5;
	static final int INF = 1 << 28;

	static List<Integer>[] G = new ArrayList[MAXN];
	static int[] maxs = new int[MAXN];
	static int[] s = new int[MAXN];
	static int T, n;

	static void init() {
		for (int i = 0; i < n; ++i) {
			G[i] = new ArrayList<Integer>();
			
			maxs[i] = 0;
			s[i] = 0;
		}
	}

	static void dfs(int u, int fa) {
		s[u] = 1;
		
		for (int i = 0; i < G[u].size(); ++i) {
			int v = G[u].get(i);
		
			if (v != fa) {
				dfs(v, u);
				s[u] += s[v];
				maxs[u] = Math.max(maxs[u], s[v]);
			}
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		T = sc.nextInt();

		while (T-- > 0) {
			n = sc.nextInt();

			init();

			for (int i = 1; i < n; ++i) {
				int u = sc.nextInt() - 1;
				int v = sc.nextInt() - 1;

				G[u].add(v);
				G[v].add(u);
			}
			dfs(0, -1);
			int ans = INF, pla = 0;

			for (int i = 0; i < n; ++i) {
				int t = Math.max(maxs[i], n - s[i]);
				if (t < ans) {
					ans = t;
					pla = i + 1;
				}
			}
			System.out.println(pla + " " + ans);
		}
		
		sc.close();
	}
}
