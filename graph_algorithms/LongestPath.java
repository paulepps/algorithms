// http://poj.org/problem?id=1985
package graph_algorithms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Edge1 {
	int to;
	int cost;
	int next;

	Edge1() {
	}

	Edge1(int to, int cost, int next) {
		this.to = to;
		this.cost = cost;
		this.next = next;
	}
}

public class LongestPath {

	static final int maxn = 50000 + 5;
	static final int maxm = 100000 + 5;

	static Edge1[] edges = new Edge1[maxm];
	static int[] head = new int[maxn];
	static int[] dist = new int[maxn];
	static int count = 0;

	static void AddEdge(int u, int v, int cost) {
		edges[count] = new Edge1(v, cost, head[u]);
		head[u] = count++;
		edges[count] = new Edge1(u, cost, head[v]);
		head[v] = count++;
	}

	static int BFS(int s) {
		int max_dist = 0;
		int id = s;

		Queue<Integer> Q = new LinkedList<Integer>();

		Arrays.fill(dist, -1);

		dist[s] = 0;
		Q.add(s);

		while (!Q.isEmpty()) {
			int u = Q.remove();
			if (dist[u] > max_dist)
				max_dist = dist[id = u];
			for (int i = head[u]; i != -1; i = edges[i].next) {
				Edge1 e = edges[i];
				if (dist[e.to] == -1) {
					dist[e.to] = dist[u] + e.cost;
					Q.add(e.to);
				}
			}
		}
		return id;
	}

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
	    
		int n = sc.nextInt();
		int m = sc.nextInt();  
	
	    count=0;  

	    Arrays.fill(head, -1);

	    int u = 0,v,cost;  
	    String c;  
	    
	    for(int i=1;i<=m;i++)  
	    {
	    	u = sc.nextInt();
	    	v = sc.nextInt();
	    	cost = sc.nextInt();
	    	c = sc.next();

	    	AddEdge(u,v,cost);  
	    }  
	    
	    sc.close();
	    
	    System.out.println(dist[BFS(BFS(u))]);  
    }
}