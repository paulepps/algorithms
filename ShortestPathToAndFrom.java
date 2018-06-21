package graph_algorithms;
// http://poj.org/problem?id=2135
	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

// ===== Implementation of DinicGraph =====
class MinCostFlow {

	private int V;
	private List<Edge>[] g;
	private int[] prevv;
	private int[] preve;

	@SuppressWarnings("unchecked")
	public MinCostFlow(int v) {
		V = v;

		g = new ArrayList[V];
		for (int i = 0; i < g.length; i++) {
			g[i] = new ArrayList<Edge>();
		}

		prevv = new int[V];
		preve = new int[V];

		Arrays.fill(prevv, -1);
		Arrays.fill(preve, -1);
	}

	public void addEdge(int u, int v, int cap, long cost) {
		g[u].add(new Edge(v, cap, cost, g[v].size()));
		g[v].add(new Edge(u, 0, -cost, g[u].size() - 1));
	}

	public long minCostFlow(int s, int t, int f) {
		long res = 0;
		long[] h = new long[V];
		long[] d = new long[V];
	
		Arrays.fill(h,  0);
		Arrays.fill(d, Long.MAX_VALUE);

		while (f > 0) {
			Arrays.fill(d, Long.MAX_VALUE);
			
			PriorityQueue<Pair<Long, Integer>> pq = new PriorityQueue<Pair<Long, Integer>>();
			
			d[s] = 0;
			pq.add(new Pair<Long, Integer>(d[s], s));

	        while (!pq.isEmpty()) {
	        	Pair<Long, Integer> p = pq.poll();
	            int v = p.e2;
	            
	            if (d[v] < p.e1) continue;
	            
	            for (int i = 0; i < g[v].size(); i++) {
	                final Edge e = g[v].get(i);
	                
	                if (e.cap > 0 && d[e.to] > d[v] + e.cost + h[v] - h[e.to]) {
	                    d[e.to] = d[v] + e.cost + h[v] - h[e.to];
	                    prevv[e.to] = v;
	                    preve[e.to] = i;
	                    pq.add(new Pair<Long, Integer>(d[e.to], e.to));
	                }
	            }
	        }

	        if (d[t] == Long.MAX_VALUE)
	            return -1;

	        for (int v = 0; v < V; v++)
	            h[v] += d[v];

	        int bn = f;
	        for (int v = t; v != s; v = prevv[v])
	            bn = Math.min(bn, g[prevv[v]].get(preve[v]).cap);

	        f -= bn;
	        res += bn * h[t];

	        for (int v = t; v != s; v = prevv[v]) {
	            Edge e = g[prevv[v]].get(preve[v]);
	            e.cap -= bn;
	            g[v].get(e.rev).cap += bn;
	        }
		}
		return res;
	}
}

public class ShortestPathToAndFrom {

	static MinCostFlow g;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int N = sc.nextInt();
		int M = sc.nextInt();

		g = new MinCostFlow(N);

		while (M-- > 0) {
			int u = sc.nextInt() - 1;
			int v = sc.nextInt() - 1;
			long w = sc.nextLong();
			
			g.addEdge(u,v,1,w);
			g.addEdge(v,u,1,w);
		}

		System.out.println(g.minCostFlow(0,N-1,2));

		sc.close();
	}
}

class Edge {
    int to, cap; 
    long cost; 
    int rev;
    
    public Edge(int to, int cap, long cost, int rev) {
    	this.to = to;
    	this.cap = cap;
    	this.cost = cost;
    	this.rev = rev;
    }
}

/**
 * Represent a pair of elements.
 */
class Pair<S,T>
        implements Comparable<Pair<S,T>>
{
    /** first element in the pair */
    final S e1;

    /** second element in the pair */
    final T e2;

    /** is the first element an instance of Comparable? */
    final boolean e1Comparable;

    /** is the second element an instance of Comparable? */
    final boolean e2Comparable;


    /**
    * Constructor.
    *
    * @param e1
    *      first element in the pair
    * @param e2
    *      second element in the pair.
    */
    Pair(
            final S e1,
            final T e2)
    {
        this.e1 = e1;
        this.e2 = e2;

        this.e1Comparable = (e1 instanceof Comparable);
        this.e2Comparable = (e2 instanceof Comparable);
    }


    /**
    * Compares this pair of elements to the specified pair.
    * The comparison is performed in element-order, i.e. the first element followed by the second.
    */
    @SuppressWarnings("unchecked")
	public int compareTo(
            Pair<S,T> o)
    {
        if (e1Comparable)
        {
            final int k = ((Comparable<S>) e1).compareTo(o.e1);
            if (k > 0) return 1;
            if (k < 0) return -1;
        }

        if (e2Comparable)
        {
            final int k = ((Comparable<T>) e2).compareTo(o.e2);
            if (k > 0) return 1;
            if (k < 0) return -1;
        }

        return 0;
    }


    /**
    * Check if this pair of elements is equal to the specified object.
    * The comparison is performed on both elements in the pair.
    */
    @SuppressWarnings("unchecked")
	@Override
    public boolean equals(
            Object obj)
    {
        if (obj instanceof Pair)
        {
            final Pair<S,T> o = (Pair<S,T>) obj;
            return (e1.equals(o.e1) && e2.equals(o.e2));
        }
        else
        {
            return false;
        }
    }


    /**
    * Hash code for this pair of elements.
    */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 19 * hash + (e1 != null ? e1.hashCode() : 0);
        hash = 19 * hash + (e2 != null ? e2.hashCode() : 0);
        return hash;
    }
}