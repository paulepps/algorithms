package graph_algorithms;
import java.util.Scanner;

public class PrimMSTAdjacencyMatrix {

	// Number of vertices in the graph
	private static int V;

	// A utility function to find the vertex with minimum key
	// value, from the set of vertices not yet included in MST
	int minKey(int key[], Boolean mstSet[]) {
		// Initialize min value
		int min = Integer.MAX_VALUE, min_index = -1;

		for (int v = 0; v < V; v++)
			if (mstSet[v] == false && key[v] < min) {
				min = key[v];
				min_index = v;
			}

		return min_index;
	}

	// A utility function to calculate the weight of the MST
	int weight(int parent[], int graph[][]) {
		int weight = 0;
		for (int i = 1; i < V; i++)
			weight += graph[i][parent[i]];
		return weight;
	}

	// A utility function to print the constructed MST stored in
	// parent[]
	void printMST(int parent[], int n, int graph[][]) {
		System.out.println("Edge   Weight");
		for (int i = 1; i < V; i++)
			System.out.println(parent[i] + " - " + i + "    " + graph[i][parent[i]]);
	}

	// Function to construct and print MST for a graph represented
	// using adjacency matrix representation
	void primMST(int graph[][]) {
		V = graph.length;

		// Array to store constructed MST
		int parent[] = new int[V];

		// Key values used to pick minimum weight edge in cut
		int key[] = new int[V];

		// To represent set of vertices not yet included in MST
		Boolean mstSet[] = new Boolean[V];

		// Initialize all keys as INFINITE
		for (int i = 0; i < V; i++) {
			key[i] = Integer.MAX_VALUE;
			mstSet[i] = false;
		}

		// Always include first 1st vertex in MST.
		key[0] = 0; // Make key 0 so that this vertex is
					// picked as first vertex
		parent[0] = -1; // First node is always root of MST

		// The MST will have V vertices
		for (int count = 0; count < V - 1; count++) {
			// Pick the minimum key vertex from the set of vertices
			// not yet included in MST
			int u = minKey(key, mstSet);

			// Add the picked vertex to the MST Set
			mstSet[u] = true;

			// Update key value and parent index of the adjacent
			// vertices of the picked vertex. Consider only those
			// vertices which are not yet included in MST
			for (int v = 0; v < V; v++)

				// graph[u][v] is non zero only for adjacent vertices of m
				// mstSet[v] is false for vertices not yet included in MST
				// Update the key only if graph[u][v] is smaller than key[v]
				if (graph[u][v] != 0 && mstSet[v] == false && graph[u][v] < key[v]) {
					parent[v] = u;
					key[v] = graph[u][v];
				}
		}

		// print the constructed MST
		// printMST(parent, V, graph);
		System.out.println(weight(parent, graph));
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		while (sc.hasNext()) {
			int N = sc.nextInt();

			PrimMSTAdjacencyMatrix t = new PrimMSTAdjacencyMatrix();

			int graph[][] = new int[N][N];

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					graph[i][j] = sc.nextInt();
				}
			}

			// Print the solution
			t.primMST(graph);
		}

		sc.close();
	}
}