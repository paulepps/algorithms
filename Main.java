/*
ID: paul9
LANG: JAVA
TASK: comehome
 */
import java.io.*;
import java.util.*;

//public class Main
class comehome 
{
	public static void main(String[] args) throws IOException 
	{
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("comehome.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("comehome.out")));
		
		int N = sc.nextInt();
        EdgeWeightedGraph G = new EdgeWeightedGraph(52);
        
        for (int i = 0; i < N; i++) {
			char a1 = sc.next().charAt(0);
			int a = Character.isUpperCase(a1) ? a1-'A'+26 : a1-'a';

			char b1 = sc.next().charAt(0);
			int b = Character.isUpperCase(b1) ? b1-'A'+26 : b1-'a';

			int w = sc.nextInt();
			
        	G.addEdge(a, b, w);
        }

        DijkstraUndirectedSP sp = new DijkstraUndirectedSP(G, 51);

		int maxAt = 26;
		for(int i = 26; i < 51;i++)
		{
			int d = sp.distTo(i);
			if(d == Integer.MAX_VALUE) continue;
			if(sp.distTo(maxAt) > d)
			{
				maxAt = i;
			}
		}

		out.println(((char)((maxAt-26)+'A'))+" "+sp.distTo(maxAt));
		
		sc.close();
		out.close();
		
		System.out.println(System.currentTimeMillis() - start);
	}
}

class DijkstraUndirectedSP {

	private int N;
	private int dist[];
	private PriorityQueue<Long> pq;
	
    public DijkstraUndirectedSP(EdgeWeightedGraph G, int s) {
	    
    	N = G.V();
    	dist = new int[N];
    	Arrays.fill(dist,Integer.MAX_VALUE);
    	
    	pq = new PriorityQueue<>();
    	pq.add((0L<<32)|(N-1));
    	
		while(pq.size() > 0)
		{
			long p = pq.poll();
			int at = (int)(p&0xFFFFFFFFL);
			int cost = (int)(p>>>32);	


			if(cost >= dist[at]) continue;
			dist[at] = cost;

			for(int i = 0; i < N;i++)
			{
				if(i==at) continue;
				
				int adj = G.adj(at, i);
				if(adj == -1) continue;

				if(cost + adj < dist[i])
				{
					pq.add(((long)cost + adj<< 32)|i);
				}
			}			
		}
    }

    public int distTo(int v) {
        return dist[v];
    }
}

class EdgeWeightedGraph {

	private final int V;
	private int[][] adj;
	
	public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        adj = new int[V][V];
		for(int i = 0; i < V; i++)
			Arrays.fill(adj[i],-1);
	}
	
    public int V() {
        return V;
    }

    public int adj(int v, int w) {
        return adj[v][w];
    }


    public void addEdge(int a, int b, int weight) {
		if(adj[a][b] == -1 || weight < adj[a][b])
		{
			adj[a][b] = weight;
			adj[b][a] = weight;
		}
    }

}
