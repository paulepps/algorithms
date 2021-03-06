import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

public class HopcroftKarp {
    private static final int UNMATCHED = -1;

    private final int V;                 // number of vertices in the graph
    private BipartiteX bipartition;      // the bipartition
    private int cardinality;             // cardinality of current matching
    private int[] mate;                  // mate[v] =  w if v-w is an edge in current matching
                                         //         = -1 if v is not in current matching
    private boolean[] inMinVertexCover;  // inMinVertexCover[v] = true iff v is in min vertex cover
    private boolean[] marked;            // marked[v] = true iff v is reachable via alternating path
    private int[] distTo;                // distTo[v] = number of edges on shortest path to v

    /**
     * Determines a maximum matching (and a minimum vertex cover)
     * in a bipartite graph.
     *
     * @param  G the bipartite graph
     * @throws IllegalArgumentException if {@code G} is not bipartite
     */
    @SuppressWarnings("unchecked")
	public HopcroftKarp(Graph G) {
        bipartition = new BipartiteX(G);
        if (!bipartition.isBipartite()) {
            throw new IllegalArgumentException("graph is not bipartite");
        }

        // initialize empty matching
        this.V = G.V();
        mate = new int[V];
        for (int v = 0; v < V; v++)
            mate[v] = UNMATCHED;

        // the call to hasAugmentingPath() provides enough info to reconstruct level graph
        while (hasAugmentingPath(G)) {

            // to be able to iterate over each adjacency list, keeping track of which
            // vertex in each adjacency list needs to be explored next
            Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[G.V()];
            for (int v = 0; v < G.V(); v++)
                adj[v] = G.adj(v).iterator();

            // for each unmatched vertex s on one side of bipartition
            for (int s = 0; s < V; s++) {
                if (isMatched(s) || !bipartition.color(s)) continue;   // or use distTo[s] == 0

                // find augmenting path from s using nonrecursive DFS
                Stack<Integer> path = new Stack<Integer>();
                path.push(s);
                while (!path.isEmpty()) {
                    int v = path.peek();

                    // retreat, no more edges in level graph leaving v
                    if (!adj[v].hasNext())
                        path.pop();

                    // advance
                    else {
                        // process edge v-w only if it is an edge in level graph
                        int w = adj[v].next();
                        if (!isLevelGraphEdge(v, w)) continue;

                        // add w to augmenting path
                        path.push(w);

                        // augmenting path found: update the matching
                        if (!isMatched(w)) {
                            // StdOut.println("augmenting path: " + toString(path));

                            while (!path.isEmpty()) {
                                int x = path.pop();
                                int y = path.pop();
                                mate[x] = y;
                                mate[y] = x;
                            }
                            cardinality++;
                        }
                    }
                }
            }
        }

        // also find a min vertex cover
//        inMinVertexCover = new boolean[V];
//        for (int v = 0; v < V; v++) {
//            if (bipartition.color(v) && !marked[v]) inMinVertexCover[v] = true;
//            if (!bipartition.color(v) && marked[v]) inMinVertexCover[v] = true;
//        }
//
//        assert certifySolution(G);
    }

    // string representation of augmenting path (chop off last vertex)
    private static String toString(Iterable<Integer> path) {
        StringBuilder sb = new StringBuilder();
        for (int v : path)
            sb.append(v + "-");
        String s = sb.toString();
        s = s.substring(0, s.lastIndexOf('-'));
        return s;
    }

   // is the edge v-w in the level graph?
    private boolean isLevelGraphEdge(int v, int w) {
        return (distTo[w] == distTo[v] + 1) && isResidualGraphEdge(v, w);
    }

   // is the edge v-w a forward edge not in the matching or a reverse edge in the matching?
    private boolean isResidualGraphEdge(int v, int w) {
        if ((mate[v] != w) &&  bipartition.color(v)) return true;
        if ((mate[v] == w) && !bipartition.color(v)) return true;
        return false;
    }

    private boolean hasAugmentingPath(Graph G) {

        // shortest path distances
        marked = new boolean[V];
        distTo = new int[V];
        for (int v = 0; v < V; v++)
            distTo[v] = Integer.MAX_VALUE;

        // breadth-first search (starting from all unmatched vertices on one side of bipartition)
        Queue<Integer> queue = new ArrayDeque<Integer>();
        for (int v = 0; v < V; v++) {
            if (bipartition.color(v) && !isMatched(v)) {
                queue.add(v);
                marked[v] = true;
                distTo[v] = 0;
            }
        }

        // run BFS until an augmenting path is found
        // (and keep going until all vertices at that distance are explored)
        boolean hasAugmentingPath = false;
        while (!queue.isEmpty()) {
            int v = queue.remove();
            for (int w : G.adj(v)) {

                // forward edge not in matching or backwards edge in matching
                if (isResidualGraphEdge(v, w)) {
                    if (!marked[w]) {
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        if (!isMatched(w))
                            hasAugmentingPath = true;

                        // stop enqueuing vertices once an alternating path has been discovered
                        // (no vertex on same side will be marked if its shortest path distance longer)
                        if (!hasAugmentingPath) queue.add(w);
                    }
                }
            }
        }

        return hasAugmentingPath;
    }

    public int mate(int v) {
        return mate[v];
    }

    public boolean isMatched(int v) {
        return mate[v] != UNMATCHED;
    }

    public int size() {
        return cardinality;
    }

    public boolean isPerfect() {
        return cardinality * 2 == V;
    }

    public boolean inMinVertexCover(int v) {
        return inMinVertexCover[v];
    }

    /**************************************************************************
     *   
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

    // check that mate[] and inVertexCover[] define a max matching and min vertex cover, respectively
    private boolean certifySolution(Graph G) {

        // check that mate(v) = w iff mate(w) = v
        for (int v = 0; v < V; v++) {
            if (mate(v) == -1) continue;
            if (mate(mate(v)) != v) return false;
        }

        // check that size() is consistent with mate()
        int matchedVertices = 0;
        for (int v = 0; v < V; v++) {
            if (mate(v) != -1) matchedVertices++;
        }
        if (2*size() != matchedVertices) return false;

        // check that size() is consistent with minVertexCover()
        int sizeOfMinVertexCover = 0;
        for (int v = 0; v < V; v++)
            if (inMinVertexCover(v)) sizeOfMinVertexCover++;
        if (size() != sizeOfMinVertexCover) return false;

        // check that mate() uses each vertex at most once
        boolean[] isMatched = new boolean[V];
        for (int v = 0; v < V; v++) {
            int w = mate[v];
            if (w == -1) continue;
            if (v == w) return false;
            if (v >= w) continue;
            if (isMatched[v] || isMatched[w]) return false;
            isMatched[v] = true;
            isMatched[w] = true;
        }

        // check that mate() uses only edges that appear in the graph
        for (int v = 0; v < V; v++) {
            if (mate(v) == -1) continue;
            boolean isEdge = false;
            for (int w : G.adj(v)) {
                if (mate(v) == w) isEdge = true;
            }
            if (!isEdge) return false;
        }

        // check that inMinVertexCover() is a vertex cover
        for (int v = 0; v < V; v++)
            for (int w : G.adj(v))
                if (!inMinVertexCover(v) && !inMinVertexCover(w)) return false;

        return true;
    }
}

