package graph_algorithms;
import java.util.ArrayList;

public class Graph {

	private final int V;
	private ArrayList<Integer>[] adj;
	
	@SuppressWarnings("unchecked")
	public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        adj = (ArrayList<Integer>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<Integer>();
        }
	}
	
    public int V() {
        return V;
    }

    public ArrayList<Integer> adj(int v) {
        return adj[v];
    }

    public int degree(int v) {
        return adj[v].size();
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }
}
