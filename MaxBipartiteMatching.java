import java.util.Arrays;
import java.util.Scanner;

// This code performs maximum bipartite matching.
//
// Running time: O(|E| |V|) -- often much faster in practice
//
//   INPUT: w[i][j] = edge between row node i and column node j
//   OUTPUT: mr[i] = assignment for row node i, -1 if unassigned
//	         mc[j] = assignment for column node j, -1 if unassigned
//	         function returns number of matches made

public class MaxBipartiteMatching {

	boolean FindMatch(int i, final boolean[][] w, int[] mr, int[] mc, boolean[] seen) {
		for (int j = 0; j < w[i].length; j++) {
			if (w[i][j] && !seen[j]) {
				seen[j] = true;
				if (mc[j] < 0 || FindMatch(mc[j], w, mr, mc, seen)) {
					mr[i] = j;
					mc[j] = i;
					return true;
				}
			}
		}
		return false;
	}

	int BipartiteMatching(final boolean[][] w) {

		int[] mr = new int[w.length];
		int[] mc = new int[w[0].length];

		Arrays.fill(mr, -1);
		Arrays.fill(mc, -1);

		int ct = 0;
		for (int i = 0; i < w.length; i++) {
			boolean[] seen = new boolean[w[0].length];
			if (FindMatch(i, w, mr, mc, seen))
				ct++;
		}
		return ct;
	}
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int[] aa = {2,3,4,5,6,7};
		
		while (sc.hasNext()) {
			int N = sc.nextInt();
			int M = sc.nextInt();
			
			boolean[][] w = new boolean[N][M];
			
			for (int i = 0; i < N; i++) {
				int stalls = sc.nextInt();
				
				for (int j = 0; j < stalls; j++) {
					int stall = sc.nextInt() - 1;
					w[i][stall] = true;
				}
			}
			
			MaxBipartiteMatching m = new MaxBipartiteMatching();
			
			System.out.println(m.BipartiteMatching(w));
		}
		
		sc.close();
	}
}
