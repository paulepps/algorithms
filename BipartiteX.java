import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class BipartiteX {
    private static final boolean WHITE = false;
    private static final boolean BLACK = true;

    private boolean isBipartite;   // is the graph bipartite?
    private boolean[] color;       // color[v] gives vertices on one side of bipartition
    private boolean[] marked;      // marked[v] = true if v has been visited in DFS
    private int[] edgeTo;          // edgeTo[v] = last edge on path to v
    private Queue<Integer> cycle;  // odd-length cycle

    /**
     * Determines whether an undirected graph is bipartite and finds either a
     * bipartition or an odd-length cycle.
     *
     * @param  G the graph
     */
    public BipartiteX(Graph G) {
        isBipartite = true;
        color  = new boolean[G.V()];
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];

        for (int v = 0; v < G.V() && isBipartite; v++) {
            if (!marked[v]) {
                bfs(G, v);
            }
        }
        assert check(G);
    }

    private void bfs(Graph G, int s) { 
        Queue<Integer> q = new ArrayDeque<Integer>();
        color[s] = WHITE;
        marked[s] = true;
        q.add(s);

        while (!q.isEmpty()) {
            int v = q.remove();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = v;
                    color[w] = !color[v];
                    q.add(w);
                }
                else if (color[w] == color[v]) {
                    isBipartite = false;

                    // to form odd cycle, consider s-v path and s-w path
                    // and let x be closest node to v and w common to two paths
                    // then (w-x path) + (x-v path) + (edge v-w) is an odd-length cycle
                    // Note: distTo[v] == distTo[w];
                    cycle = new ArrayDeque<Integer>();
                    Stack<Integer> stack = new Stack<Integer>();
                    int x = v, y = w;
                    while (x != y) {
                        stack.push(x);
                        cycle.add(y);
                        x = edgeTo[x];
                        y = edgeTo[y];
                    }
                    stack.push(x);
                    while (!stack.isEmpty())
                        cycle.add(stack.pop());
                    cycle.add(w);
                    return;
                }
            }
        }
    }

    public boolean isBipartite() {
        return isBipartite;
    }
 
    public boolean color(int v) {
        if (!isBipartite)
            throw new UnsupportedOperationException("Graph is not bipartite");
        return color[v];
    }


    public Iterable<Integer> oddCycle() {
        return cycle; 
    }

    private boolean check(Graph G) {
        // graph is bipartite
        if (isBipartite) {
            for (int v = 0; v < G.V(); v++) {
                for (int w : G.adj(v)) {
                    if (color[v] == color[w]) {
                        System.err.printf("edge %d-%d with %d and %d in same side of bipartition\n", v, w, v, w);
                        return false;
                    }
                }
            }
        }

        // graph has an odd-length cycle
        else {
            // verify cycle
            int first = -1, last = -1;
            for (int v : oddCycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }
        return true;
    }

}
