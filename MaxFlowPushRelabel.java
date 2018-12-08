import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

//Adjacency list implementation of FIFO push relabel maximum flow
//with the gap relabeling heuristic.  This implementation is
//significantly faster than straight Ford-Fulkerson.  It solves
//random problems with 10000 vertices and 1000000 edges in a few
//seconds, though it is possible to construct test cases that
//achieve the worst-case.
//
//Running time:
//  O(|V|^3)
//
//INPUT: 
//  - graph, constructed using AddEdge()
//  - source
//  - sink
//
//OUTPUT:
//  - maximum flow value
//  - To obtain the actual flow values, look at all edges with
//    capacity > 0 (zero capacity edges are residual edges).


public class MaxFlowPushRelabel {

	class Edge {
		int from, to, cap, flow, index;
		
		Edge(int from, int to, int cap, int flow, int index) {
		 this.from = from;
		 this.to = to;
		 this.cap = cap;
		 this.flow = flow;
		 this.index = index;
		}
	}
	
	int N;
	List<Edge>[] G;
	long[] excess;
	int[] dist, count;
	boolean[] active;
	Queue<Integer> Q;
	
	@SuppressWarnings("unchecked")
	public MaxFlowPushRelabel(int N) {
		this.N = N; 
		
		G = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			G[i] = new ArrayList<Edge>();
		}
		
		excess = new long[N];
		dist = new int[N];
		count = new int[2*N];
		active = new boolean[N];
		
		Q = new LinkedList<Integer>();
	}

	void AddEdge(int from, int to, int cap) {
		G[from].add(new Edge(from, to, cap, 0, G[to].size()));
		if (from == to)
			G[from].get(G[from].size() - 1).index++;
		G[to].add(new Edge(to, from, 0, 0, G[from].size() - 1));
	}

	void Enqueue(int v) {
		if (!active[v] && excess[v] > 0) { 
			active[v] = true; 
			Q.add(v); } 
	}

	void Push(Edge e) {
		int amt = (int) Math.min(excess[e.from], e.cap - e.flow);

		if (dist[e.from] <= dist[e.to] || amt == 0)
			return;
		
		e.flow += amt;
		G[e.to].get(e.index).flow -= amt;
		excess[e.to] += amt;
		excess[e.from] -= amt;
		
		Enqueue(e.to);
	}

	void Gap(int k) {
		for (int v = 0; v < N; v++) {
			if (dist[v] < k)
				continue;

			count[dist[v]]--;
			dist[v] = Math.max(dist[v], N + 1);
			count[dist[v]]++;
			
			Enqueue(v);
		}
	}

	void Relabel(int v) {
		count[dist[v]]--;
		dist[v] = 2 * N;

		for (int i = 0; i < G[v].size(); i++)
			if (G[v].get(i).cap - G[v].get(i).flow > 0)
				dist[v] = Math.min(dist[v], dist[G[v].get(i).to] + 1);
		
		count[dist[v]]++;
		
		Enqueue(v);
	}

	void Discharge(int v) {
		for (int i = 0; excess[v] > 0 && i < G[v].size(); i++)
			Push(G[v].get(i));

		if (excess[v] > 0) {
			if (count[dist[v]] == 1)
				Gap(dist[v]);
			else
				Relabel(v);
		}
	}

	long GetMaxFlow(int s, int t) {
	 count[0] = N-1;
	 count[N] = 1;
	 dist[s] = N;
	 active[s] = active[t] = true;
	 
	 for (int i = 0; i < G[s].size(); i++) {
	   excess[s] += G[s].get(i).cap;
	   Push(G[s].get(i));
	 }
	 
	 while (!Q.isEmpty()) {
	   int v = Q.poll();
	   active[v] = false;

	   Discharge(v);
	 }
	 
	 long totflow = 0;
	 for (int i = 0; i < G[s].size(); i++) 
		 totflow += G[s].get(i).flow;
	 
	 return totflow;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (sc.hasNext()) {
			int N = sc.nextInt();
			int M = sc.nextInt();

			MaxFlowPushRelabel flow = new MaxFlowPushRelabel(M);
			
			for (int i = 0; i < N; i++) {
				int from = sc.nextInt() - 1;
				int to = sc.nextInt() - 1;
				int cap = sc.nextInt();
				
				flow.AddEdge(from, to, cap);
			}
			
			System.out.println(flow.GetMaxFlow(0, M-1));
		}
		
		sc.close();
	}
}