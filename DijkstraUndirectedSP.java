import java.util.Arrays;
import java.util.PriorityQueue;

public class DijkstraUndirectedSP {

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
