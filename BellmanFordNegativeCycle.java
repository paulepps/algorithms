import java.util.Scanner;

public class Main {
	
	static final int EAST = 4;
	static final int SOUTH = 8;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int H = sc.nextInt();
		int W = sc.nextInt();
		
		Graph G = new Graph(H*W);
		
		for (int i = 0; i < H * W; i++) {
			int room = sc.nextInt();
			
			if ((room & EAST) == 0) {
				G.addEdge(i, i+1);
			}

			if ((room & SOUTH) == 0) {
				G.addEdge(i, i+W);
			}
		}
		
		sc.close();
		
		CC cc = new CC(G);
	
		int max = 0;
	    for (int v = 0; v < G.V(); v++) {
	    	max = Math.max(max,  cc.size(v));
	    }

		System.out.println(cc.count());
		System.out.println(max);
		
	}
}

class CC {
    private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components

    /**
     * Computes the connected components of the undirected graph {@code G}.
     *
     * @param G the undirected graph
     */
    public CC(Graph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    // depth-first search for a Graph
    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    /**
     * Returns the component id of the connected component containing vertex {@code v}.
     */
    public int id(int v) {
        return id[v];
    }

    /**
     * Returns the number of vertices in the connected component containing vertex {@code v}.
     */
    public int size(int v) {
        return size[id[v]];
    }

    /**
     * Returns the number of connected components in the graph {@code G}.
     */
    public int count() {
        return count;
    }

    /**
     * Returns true if vertices {@code v} and {@code w} are in the same
     * connected component.
     */
    public boolean connected(int v, int w) {
        return id(v) == id(w);
    }
}
