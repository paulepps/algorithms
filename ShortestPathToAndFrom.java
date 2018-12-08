
	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;


public class ShortestPathToAndFrom {

	// ===== Implementation of DinicGraph =====
	static class MinCostFlow {

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

}
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

