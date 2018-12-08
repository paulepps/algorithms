

/*
ID: paul9
LANG: JAVA
TASK: fence6
http://train.usaco.org/usacoprob2?a=7CIQiKqBrpL&S=fence6
 */
import java.io.*;
import java.util.*;

import static java.lang.Math.*;

//public class MinimumCycle 
class fence6 
{

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("fence6.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("fence6.out")));

		int N = sc.nextInt();
		Fence[] fences = new Fence[N + 1];

		for (int i = 0; i < N; i++) {
			int index = sc.nextInt();
			int length = sc.nextInt();
			int a1 = sc.nextInt();
			int a2 = sc.nextInt();

			Fence f = new Fence();
			f.length = length;
			for (int j = 0; j < a1; j++) {
				f.adj1.add(sc.nextInt());
			}
			for (int j = 0; j < a2; j++) {
				f.adj2.add(sc.nextInt());
			}

			f.v1 = -1;
			f.v2 = -1;

			fences[index] = f;
		}

		int currVertex = 0;

		for (int i = 1; i < fences.length; i++) {
			Fence f = fences[i];
			if (f.v1 == -1) {
				f.v1 = currVertex;

				for (Integer index : f.adj1) {
					Fence ff = fences[index];
					if (ff.adj1.contains(i))
						ff.v1 = currVertex;
					else
						ff.v2 = currVertex;
				}
				currVertex++;
			}
			if (f.v2 == -1) {
				f.v2 = currVertex;

				for (Integer index : f.adj2) {
					Fence ff = fences[index];
					if (ff.adj1.contains(i))
						ff.v1 = currVertex;
					else
						ff.v2 = currVertex;
				}
				currVertex++;
			}
		}

		MinimumCycle g = new MinimumCycle(currVertex);

		for (int i = 1; i < fences.length; i++) {
			Fence f = fences[i];
			g.addEdge(f.v1, f.v2, f.length);			
		}
		
		out.println(g.findMinimumCycle());
		out.close();
		sc.close();
		
		System.out.println("$:" + (System.currentTimeMillis() - start));
		System.exit(0);
	}
}

class Fence {
	int length;
	ArrayList<Integer> adj1 = new ArrayList<>();
	ArrayList<Integer> adj2 = new ArrayList<>();
	int v1;
	int v2;
}

class MinimumCycle {
	final int INF = 0x3f3f3f3f;
	
	int V;
	List<List<Pair<Integer, Integer>>> adj;

	ArrayList<Edge> edge;

	public MinimumCycle(int V) {
		this.V = V;
		adj = new ArrayList<List<Pair<Integer, Integer>>>();
		for (int i = 0; i < V; i++) {
			adj.add(new ArrayList<Pair<Integer,Integer>>());
		}
		edge = new ArrayList<>();
	}

	// function add edge to graph
	void addEdge ( int u, int v, int w )
	{
		try {
			adj.get(u).add(new Pair<Integer,Integer>( v, w ));
		} catch (IndexOutOfBoundsException ex) {
			adj.set(u, new ArrayList<Pair<Integer,Integer>>());
			adj.get(u).add(new Pair<Integer,Integer>( v, w ));
		}
		try {
			adj.get(v).add(new Pair<Integer,Integer>( u, w ));
		} catch (IndexOutOfBoundsException ex) {
			adj.set(v, new ArrayList<Pair<Integer,Integer>>());
			adj.get(v).add(new Pair<Integer,Integer>( u, w ));
		}
	 
	    // add Edge to edge list
	    Edge e = new Edge(u, v, w);
	    edge.add(e);
	}

	// function remove edge from undirected graph
	void removeEdge ( int u, int v, int w )
	{
		adj.get(u).remove(new Pair<Integer,Integer>( v, w ));
		adj.get(v).remove(new Pair<Integer,Integer>( u, w ));
	}

	// find shortest path from source to sink using
	// Dijkstra's shortest path algorithm [ Time complexity
	// O(E logV )]
	int ShortestPath ( int u, int v )
	{
	    // Create a set to store vertices that are being
	    // preprocessed
	    Set< Pair<Integer, Integer> > setds = new TreeSet<>();
	 
	    // Create a vector for distances and initialize all
	    // distances as infinite (INF)
	    int[] dist = new int[V];
	    Arrays.fill(dist, INF);
	 
	    // Insert source itself in Set and initialize its
	    // distance as 0.
	    setds.add(new Pair<Integer,Integer>(0, u));
	    dist[u] = 0;
	 
	    /* Looping till all shortest distance are finalized
	    then setds will become empty */
	    while (!setds.isEmpty())
	    {
	        // The first vertex in Set is the minimum distance
	        // vertex, extract it from set.
	    	Iterator<Pair<Integer,Integer>> iter = setds.iterator(); 
	        Pair<Integer, Integer> tmp = iter.next();
	        iter.remove();
	 
	        // vertex label is stored in second of pair (it
	        // has to be done this way to keep the vertices
	        // sorted distance (distance must be first item
	        // in pair)
	        int uu = tmp.e2;
	 
	        // get all adjacent vertices of a vertex
	        for (Pair<Integer, Integer> p : adj.get(uu)) {
				
	            // Get vertex label and weight of current adjacent
	            // of u.
	            int vv = p.e1;
	            int weight = p.e2;
	 
	            // If there is shorter path to v through u.
	            if (dist[vv] > dist[uu] + weight)
	            {
	                /* If distance of v is not INF then it must be in
	                    our set, so removing it and inserting again
	                    with updated less distance.
	                    Note : We extract only those vertices from Set
	                    for which distance is finalized. So for them,
	                    we would never reach here. */
	                if (dist[vv] != INF)
	                	setds.remove(new Pair<Integer, Integer>(dist[vv], vv));
	 
	                // Updating distance of v
	                dist[vv] = dist[uu] + weight;
	                setds.add(new Pair<Integer, Integer>(dist[vv], vv));
	            }
	        }
	    }
	 
	    // return shortest path from current source to sink
	    return dist[v] ;
	}

	// function return minimum weighted cycle
	int findMinimumCycle ( )
	{
	    int min_cycle = Integer.MAX_VALUE;
	    int E = edge.size();
	    for ( int i = 0 ; i < E  ; i++ )
	    {
	        // current Edge information
	        Edge e = edge.get(i);
	 
	        // get current edge vertices which we currently
	        // remove from graph and then find shortest path
	        // between these two vertex using Dijkstra's
	        // shortest path algorithm .
	        removeEdge( e.v, e.w, e.weight ) ;
	 
	        // minimum distance between these two vertices
	        int distance = ShortestPath( e.v, e.w );
	 
	        // to make a cycle we have to add weight of
	        // currently removed edge if this is the shortest
	        // cycle then update min_cycle
	        min_cycle = min( min_cycle, distance + e.weight );
	 
	        //  add current edge back to the graph
	        addEdge( e.v, e.w, e.weight );
	    }
	 
	    // return shortest cycle
	    return min_cycle ;
	}
}
