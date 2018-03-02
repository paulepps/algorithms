import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class EulerianPath {
	private int V;
	private int[][] conn;
	private int[] degree;
	private int[] touched;
	private int[] path;
	private int pathLength = 0;
	private int nconn = 0;
	private boolean sanityCheck = false;
	private final int maxEdges = 1200;

	public EulerianPath(int V) {
		this.V = V;
		conn = new int[V][V];
		degree = new int[V];
		path = new int[maxEdges];
	}

	public int[] path() {
		return this.path;
	}

	public int pathLength() {
		return this.pathLength;
	}

	public void addEdge(int x, int y) {
		conn[x][y]++;
		conn[y][x]++;
		degree[x]++;
		degree[y]++;

		if (x >= nconn)
			nconn = x + 1;
		if (y >= nconn)
			nconn = y + 1;
	}

	public void findPath() {

		int i = 0;

		/* find first node of odd degree */
		for (; i < nconn; i++)
			if (degree[i] % 2 == 1)
				break;

		/* if no odd-degree node, find first node with non-zero degree */
		if (i >= nconn)
			for (i = 0; i < nconn; i++)
				if (degree[i] > 0)
					break;

		if (sanityCheck)
			if (!is_connected(i)) {
				System.out.println("Not connected?!?");
				return;
			}

		findPath(i);
	}

	void findPath(int loc) {
		for (int lv = 0; lv < nconn; lv++)
			if (conn[loc][lv] != 0) {
				/* delete edge */
				conn[loc][lv]--;
				conn[lv][loc]--;
				degree[lv]--;
				degree[loc]--;

				/* find path from new location */
				findPath(lv);
			}

		/* add this node to the `end' of the path */
		path[pathLength++] = loc;
	}

	/* Sanity check routine */
	private void fill(int loc) {
		touched[loc] = 1;
		for (int lv = 0; lv < nconn; lv++)
			if (conn[loc][lv] != 0 && touched[lv] == 0)
				fill(lv);
	}

	/* Sanity check routine */
	private boolean is_connected(int st) {
		touched = new int[V];

		fill(st);

		for (int lv = 0; lv < nconn; lv++)
			if (degree[lv] > 0 && touched[lv] == 0)
				return false;
		return true;
	}

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(new File("fence.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("fence.out")));

		int V = sc.nextInt();

		EulerianPath EP = new EulerianPath(V);

		for (int i = 0; i < V; i++) {
			int x = sc.nextInt();
			int y = sc.nextInt();

			// convert to 0-based
			x--;
			y--;

			EP.addEdge(x, y);
		}

		EP.findPath();

		int[] p = EP.path();

		/* the path is discovered in reverse order */
		for (int lv = EP.pathLength - 1; lv >= 0; lv--)
			out.println(p[lv] + 1);

		sc.close();
		out.close();
	}
}
