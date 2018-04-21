import java.util.Arrays;
import java.util.Scanner;

/*
 * INPUT FORMAT
 * Line 1	Four space-separated integers: N, M, c1, and c2. 
 * N is the number of vertices (1 <= N <= 100), which are numbered 1..N. 
 * M is the number of connections between pairs of vertices (1 <= M <= 600). 
 * The last two numbers, c1 and c2, are the id numbers of the vertices that 
 * we are trying to disconnect. Each connection is unique and bidirectional (if 
 * c1 is connected to c2, then c2 is connected to c1). There can be at most 
 * one edge between any two given vertices. c1 and c2 will not have a direct connection.
 * Lines 2..M+1	The subsequent M lines contain pairs of vertex id numbers that 
 * have connections between them.
 * 
 * SAMPLE INPUT 
 * 3 2 1 2
 * 1 3
 * 2 3
 * 
 * OUTPUT FORMAT
 * Generate two lines of output. The first line is the minimum number of 
 * vertices that can be cut before c1 & c2 are no longer connected. The 
 * second line is a minimal-length sorted list of vertices that will cause 
 * c1 & c2 to no longer be connected. Note that neither c1 nor c2 can go down. 
 * In case of ties, the program should output the set of vertices that, if 
 * interpreted as a base N number, is the smallest one.
 * 
 * SAMPLE OUTPUT
 * 1
 * 3
 * 
 */
public class MinimumVertexCut {

	static final int BIG = 1000000;
	static final int MAXVERTICES = 1000; // original vertex limit
	static final int MAXNODES = 2 * MAXVERTICES;

	static int vertices;
	static int c1, c2;
	static boolean[][] connected = new boolean[MAXVERTICES][MAXVERTICES];
	static boolean[] routing = new boolean[MAXVERTICES];

	static int nodes;
	static int source;
	static int sink;

	static int augment(int cap[][]) {
		assert (source != sink);
		assert (cap[source][sink] == 0);
		assert (cap[sink][source] == 0);

		int[] flow = new int[MAXNODES];
		int[] last = new int[MAXNODES];

		for (int i = 0; i < nodes; ++i) {
			flow[i] = 0;
			last[i] = -1;
		}

		flow[source] = BIG;

		boolean[] set = new boolean[MAXNODES];

		for (int i = 0; i < nodes; ++i) {
			int best = -1;
			for (int j = 0; j < nodes; ++j) {
				if (!set[j]) {
					if (best == -1 || flow[best] < flow[j]) {
						best = j;
					}
				}
			}
			assert (best != -1);
			assert (!set[best]);

			set[best] = true;

			for (int j = 0; j < nodes; ++j) {
				if (flow[j] < Math.min(flow[best], cap[best][j])) {
					flow[j] = Math.min(flow[best], cap[best][j]);
					last[j] = best;
					assert (!set[j]);
				}
			}
		}

		assert (set[sink]);

		if (flow[sink] == 0) {
			return 0;
		}

		int place;
		for (place = sink; last[place] != -1; place = last[place]) {
			cap[last[place]][place] -= flow[sink];
			cap[place][last[place]] += flow[sink];
		}
		assert (place == source);

		return (flow[sink]);

	}

	static int[][] cap = new int[MAXNODES][MAXNODES];

	static int maxflow() {

		nodes = 2 * vertices;
		// node 2 * k is in - node, node 2 * k + 1 is out node
		source = c1 * 2 + 1;
		sink = c2 * 2;

		for (int i = 0; i < nodes; ++i) {
			for (int j = 0; j < nodes; ++j) {
				cap[i][j] = 0;
			}
		}

		assert (!connected[c1][c2]);
		for (int i = 0; i < vertices; ++i) {
			for (int j = 0; j < vertices; ++j) {
				if (connected[i][j]) {
					cap[2 * i + 1][2 * j] = BIG;
				}
			}
		}

		for (int i = 0; i < vertices; ++i) {
			if (routing[i]) {
				cap[2 * i][2 * i + 1] = 1;
				cap[2 * i + 1][2 * i] = 0;
			}
		}

		int flow = 0;
		int inc = 1;
		while (inc > 0) {
			inc = augment(cap);
			flow += inc;
			assert (inc >= 0);
		}

		return (flow);
	}

	public static void main(String[] args) {
		
	    int connections;

	    Scanner sc = new Scanner(System.in);

	    vertices = sc.nextInt();
	    connections = sc.nextInt();
	    c1 = sc.nextInt() - 1;
	    c2 = sc.nextInt() - 1;

	    for (int i = 0; i < connections; ++i) {
	    	int a = sc.nextInt() - 1;
	    	int b = sc.nextInt() - 1;
		    
	    	connected[a][b] = true;
		    connected[b][a] = true;
	    }

	    sc.close();

	    Arrays.fill(routing, true);

	    int     mincut = maxflow ();
	    int     flow = mincut;

	    for (int i = 0; i < vertices && flow > 0; ++i) {
			if (i != c1 && i != c2) {
			    assert (routing[i]);
	
			    routing[i] = false;
			    int     temp = maxflow ();
			    routing[i] = true;
	
			    if (temp < flow) {
					assert (temp == flow - 1);
					flow = temp;
					routing[i] = false;
			    }
			}
	    }

	    assert (flow == 0);

	    System.out.println(mincut);
	    boolean    any = false;
	    for (int i = 0; i < vertices; ++i) {
			if (!routing[i]) {
			    if (!any) {
					System.out.print(i + 1);
					any = true;
			    }
			    else {
					System.out.print(" " + (i + 1));
			    }
			}
	    }
	    System.out.println();
	}
}
