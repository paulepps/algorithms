/*
 * We can solve it by building a graph with the constraints as edges, 
 * then finding a path through the graph. The "dislike" constraints
 * are represented as negative-weight edges. 
 * 
 * So in the sample input, we have three edges;
 * 
 * 1 -> 3 with weight 10
 * 2 -> 4 with weight 20 
 * 3 -> 2 with weight -3 (not 2 -> 3)
 * 
 * The path from 1 to 4 is
 * 
 * 1 -> 3 -> 2 -> 4 = 10 - 3 + 20 = 27
 * 
 * The Bellman-Ford path-finding algorithm gives us everything we need.
 * It handles negative edge weights and detects cycles.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *  The BellmanFord class represents a data type for solving 
 *  single-source shortest paths problem in edge-weighted digraphs.
 *  
 *  The edge weights can be positive, negative, or zero.
 *  
 *  This class finds either a shortest path from the source vertex s
 *  to every other vertex or a negative cycle reachable from the 
 *  source vertex.
 */

class BellmanFord {
    private int[] distTo;
    private DirectedEdge[] edgeTo;
    private int n, m;
    private boolean cycle;
    
    private final int INF = 999999999;
    
    public BellmanFord(EdgeWeightedDigraph G, int s) {
    	
    	n = G.V();
    	m = G.E();
    	edgeTo = G.edges();
    	distTo = new int[n+1];
    	cycle = false;
    	
		for (int i = 1; i <= n; i++)
			distTo[i] = INF; 
		distTo[s] = 0; 

		boolean flag = true; 

		for (int i = 1; i < n; i++) {
			for (int j = 0; j < m; j++) {
				int u = edgeTo[j].from();
				int v = edgeTo[j].to();
				int t = edgeTo[j].weight();

				if (distTo[v] > distTo[u] + t)
				{
					distTo[v] = distTo[u] + t;
					flag = false;
				}
			}
		}

		if (!flag) {
			// check for cycle
			for (int i = 0; i < m; i++) {
				if (distTo[edgeTo[i].to()] > distTo[edgeTo[i].from()] + edgeTo[i].weight())
					cycle = true;
			}
		}
    }

    public boolean hasCycle() {
    	return cycle;
    }
    
    public int distTo(int v) {
        return distTo[v];
    }

}

/**
 *  The EdgeWeightedDigraph class represents a edge-weighted
 *  digraph of vertices 0 through V - 1, where each directed 
 *  edge is of type DirectedEdge.
 */
class EdgeWeightedDigraph {
    private final int V;
    private int E;
    private List<DirectedEdge> edges;
    
    public EdgeWeightedDigraph(int V) {
        this.V = V;
        this.E = 0;

        edges = new ArrayList<DirectedEdge>();
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(DirectedEdge e) {
        edges.add(e);
        E++;
    }

    public DirectedEdge[] edges() {
    	return edges.toArray(new DirectedEdge[edges.size()]);
    }
}

/**
 *  The DirectedEdge class represents a weighted edge in an 
 *  EdgeWeightedDigraph.
 */
class DirectedEdge { 
    private final int v;
    private final int w;
    private final int weight;

    public DirectedEdge(int v, int w, int weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public int weight() {
        return weight;
    }
}

public class Main {

    private static final int INF = 999999999;
    
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int N, ML, MD;

		N = sc.nextInt();
		ML = sc.nextInt();
		MD = sc.nextInt();

		EdgeWeightedDigraph G = new EdgeWeightedDigraph(N);
		
		DirectedEdge e;
		
		int v, w, t;

		for (int i = 1; i <= ML; i++) {
			v = sc.nextInt();
			w = sc.nextInt();
			t = sc.nextInt();
		
			e = new DirectedEdge(v, w, t);
			G.addEdge(e);
		}

		for (int i = 1; i <= MD; i++) {
			w = sc.nextInt();
			v = sc.nextInt();
			t = sc.nextInt();
			
			e = new DirectedEdge(v, w, -t);
			G.addEdge(e);
		}

		sc.close();
		
		BellmanFord sp = new BellmanFord(G, 1);
		
		if (sp.hasCycle())
			System.out.println(-1);
		else
			if (sp.distTo(N) == INF)
				System.out.println(-2);
			else
				System.out.println(sp.distTo(N));
	}
}
