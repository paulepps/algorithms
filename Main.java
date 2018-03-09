
/*
ID: paul9
LANG: JAVA
TASK: job
 */
import java.io.*;
import java.util.*;
import static java.lang.Math.*;

public class Main
//class job 
{
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("job.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("job.out")));

		int J = sc.nextInt();
		int A = sc.nextInt();
		int B = sc.nextInt();
		int s = 0, t = A*2 + 1;
		FlowNetwork G = new FlowNetwork(A*2 + B*2 + 3);

		int currNode = 1;
		
		for (int i = 0; i < A; i++) {
			FlowEdge e = new FlowEdge(0, currNode, Double.POSITIVE_INFINITY);
			G.addEdge(e);

			e = new FlowEdge(currNode, currNode + 1, 1.0 / sc.nextInt());
			G.addEdge(e);
			
			e = new FlowEdge(currNode + 1, A*2 + 1, Double.POSITIVE_INFINITY);
			G.addEdge(e);
			
			currNode += 2;
		}

		currNode = A*2 + 2;
		
		for (int i = 0; i < B; i++) {
			FlowEdge e = new FlowEdge(A*2 + 1, currNode, Double.POSITIVE_INFINITY);
			G.addEdge(e);

			e = new FlowEdge(currNode, currNode + 1, 1.0 / sc.nextInt());
			G.addEdge(e);
			
			e = new FlowEdge(currNode + 1, A*2 + B*2 + 2, Double.POSITIVE_INFINITY);
			G.addEdge(e);
			
			currNode += 2;
		}
		
		// compute maximum flow and minimum cut
		FordFulkerson maxflow = new FordFulkerson(G, s, t);
//		for (int v = 0; v < G.V(); v++) {
//			for (FlowEdge e : G.adj(v)) {
//				if ((v == e.from()) && e.flow() > 0)
//					System.out.println("   " + e);
//			}
//		}
//
//		out.println(maxflow.value());
//		
//		// compute maximum flow and minimum cut
//		maxflow = new FordFulkerson(G, s, A*2 + B*2 + 2);
		for (int v = 0; v < G.V(); v++) {
			for (FlowEdge e : G.adj(v)) {
				if ((v == e.from()) && e.flow() > 0)
					System.out.println("   " + e);
			}
		}

		out.println(maxflow.value());
		out.close();
		System.out.println("$:" + (System.currentTimeMillis() - start));
		System.exit(0);
	}
}

