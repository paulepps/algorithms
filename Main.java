import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	static int numNodes, logNumNodes, root;

	static List<Integer>[] children; // children[i] contains the children of node i

	static int[][] A; // A[i][j] is the 2^j-th ancestor of node i, or -1 if that ancestor does not
				// exist
	static int[] L; // L[i] is the distance between node i and the root

	// floor of the binary logarithm of n
	static int lb(int n) {
		if (n == 0)
			return -1;

		int p = 0;

		if (n >= 1 << 16) {
			n >>= 16;
			p += 16;
		}
		if (n >= 1 << 8) {
			n >>= 8;
			p += 8;
		}
		if (n >= 1 << 4) {
			n >>= 4;
			p += 4;
		}
		if (n >= 1 << 2) {
			n >>= 2;
			p += 2;
		}
		if (n >= 1 << 1) {
			p += 1;
		}

		return p;
	}

	static void DFS(int i, int l) {

		L[i] = l;
		for (int j = 0; j < children[i].size(); j++) {
			DFS(children[i].get(j), l + 1);
		}
	}

	static int LCA(int p, int q) {
		// ensure node p is at least as deep as node q
		if (L[p] < L[q]) {
			int temp = p;
			p = q;
			q = temp;
		}

		// "binary search" for the ancestor of node p situated on the same level as q
		for (int i = logNumNodes; i >= 0; i--)
			if (L[p] - (1 << i) >= L[q])
				p = A[p][i];

		if (p == q)
			return p;

		// "binary search" for the LCA
		for (int i = logNumNodes; i >= 0; i--)
			if (A[p][i] != -1 && A[p][i] != A[q][i]) {
				p = A[p][i];
				q = A[q][i];
			}

		return A[p][0];
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();

		while (T-- > 0) {
			int N = sc.nextInt();

			numNodes = N;
			logNumNodes = lb(numNodes);

			children = new ArrayList[numNodes];

			for (int i = 0; i < numNodes; i++) {
				children[i] = new ArrayList<Integer>();
			}

			A = new int[numNodes][logNumNodes + 1];
			L = new int[numNodes];

			boolean[] hasAncestor = new boolean[numNodes];

			for (int i = 0; i < numNodes - 1; i++) {
				int parent = sc.nextInt() - 1; // convert to 0-based
				int child = sc.nextInt() - 1; // convert to 0-based

				A[child][0] = parent;

				children[parent].add(child);

				hasAncestor[child] = true;
			}

			// whoever doesn't have an ancestor is the root
			for (int i = 0; i < hasAncestor.length; i++) {
				if (!hasAncestor[i]) {
					root = i;
					A[i][0] = -1;
					break;
				}
			}

			// precompute A using dynamic programming
			for (int j = 1; j <= logNumNodes; j++) {
				for (int i = 0; i < numNodes; i++) {
					if (A[i][j - 1] != -1) {
						A[i][j] = A[A[i][j - 1]][j - 1];
					} else {
						A[i][j] = -1;
					}
				}
			}

			// precompute L
			DFS(root, 0);

			
			System.out.println(LCA(sc.nextInt() - 1, sc.nextInt() - 1) + 1);
		}
	}
}
