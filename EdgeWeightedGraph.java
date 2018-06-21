package graph_algorithms;
import java.util.Arrays;

public class EdgeWeightedGraph {

	private final int V;
	private int E;
	private double[][] adj;

	public EdgeWeightedGraph(int V) {
		if (V < 0)
			throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.V = V;
		adj = new double[V][V];
		for (int i = 0; i < V; i++)
			Arrays.fill(adj[i], -1);
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public double adj(int v, int w) {
		return adj[v][w];
	}

	public void addEdge(int a, int b, double weight) {
		if (adj[a][b] == -1 || weight < adj[a][b]) {
			adj[a][b] = weight;
			adj[b][a] = weight;
		}
		E++;
	}

}
