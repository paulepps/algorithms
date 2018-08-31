import java.util.Arrays;
import java.util.Scanner;

// Implementation of min cost max flow algorithm using adjacency
// matrix (Edmonds and Karp 1972). This implementation keeps track of
// forward and reverse edges separately (so you can set cap[i][j] !=
// cap[j][i]). For a regular max flow, set all edge costs to 0.
//
// Running time, O(|V|^2) cost per augmentation
// max flow: O(|V|^3) augmentations
// min cost max flow: O(|V|^4 * MAX_EDGE_COST) augmentations
//
// INPUT:
// - graph, constructed using AddEdge()
// - source
// - sink
//
// OUTPUT:
// - (maximum flow value, minimum cost value)
// - To obtain the actual flow, look at positive values only.

public class MinCostMaxFlow {

  private final long INF = Long.MAX_VALUE / 4;

  private int N;
  private long[][] cap, flow, cost;
  private boolean[] found;
  private long[] dist, pi, width;
  private Pair<Long, Long>[] dad;

  @SuppressWarnings("unchecked")
  MinCostMaxFlow(int N) {
    this.N = N;

    cap = new long[N][N];
    flow = new long[N][N];
    cost = new long[N][N];

    found = new boolean[N];

    dist = new long[N];
    pi = new long[N];
    width = new long[N];

    dad = new Pair[N];
  }

  public void AddEdge(int from, int to, long cap, long cost) {
    this.cap[from][to] = cap;
    this.cost[from][to] = cost;
  }

  private void Relax(int s, int k, long cap, long cost, int dir) {
    long val = dist[s] + pi[s] - pi[k] + cost;

    if (cap != 0 && val < dist[k]) {
      dist[k] = val;
      dad[k] = new Pair<>((long)s, (long)dir);
      width[k] = Math.min(cap, width[s]);
    }
  }

  private long Dijkstra(int s, int t) {
    Arrays.fill(found, false);
    Arrays.fill(dist, INF);
    Arrays.fill(width, 0);

    dist[s] = 0;
    width[s] = INF;

    while (s != -1) {
      int best = -1;
      found[s] = true;
      for (int k = 0; k < N; k++) {
        if (found[k])
          continue;
        Relax(s, k, cap[s][k] - flow[s][k], cost[s][k], 1);
        Relax(s, k, flow[k][s], -cost[k][s], -1);
        if (best == -1 || dist[k] < dist[best])
          best = k;
      }
      s = best;
    }

    for (int k = 0; k < N; k++)
      pi[k] = Math.min(pi[k] + dist[k], INF);
    
    return width[t];
  }

  public Pair<Long,Long> GetMaxFlow(int s, int t) {
    long totflow = 0, totcost = 0;
    long amt;
    while ((amt = Dijkstra(s, t)) != 0) {
      totflow += amt;
      for (int x = t; x != s; x = dad[x].e1.intValue()) {
        if (dad[x].e2 == 1) {
          flow[dad[x].e1.intValue()][x] += amt;
          totcost += amt * cost[dad[x].e1.intValue()][x];
        } else {
          flow[x][dad[x].e1.intValue()] -= amt;
          totcost -= amt * cost[x][dad[x].e1.intValue()];
        }
      }
    }
    return new Pair<Long,Long>(totflow, totcost);
  }
  
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int F = sc.nextInt();
    int P = sc.nextInt();

    int start = 0;
    int end = F;
    
    int cows = 0;
    
    MinCostMaxFlow g = new MinCostMaxFlow(25000);

    for (int i = 0; i < F; i++) {
      int ci = sc.nextInt();
      int si = sc.nextInt();

      cows += ci;
      
      g.AddEdge(start, i, ci, 0);
      g.AddEdge(i, end, si, 0);
    }
    

    for (int i = 0; i < P; i++) {
      int f1 = sc.nextInt() - 1;
      int f2 = sc.nextInt() - 1;
      int t = sc.nextInt();

      g.AddEdge(f1, f2, Long.MAX_VALUE / 4, t);
      g.AddEdge(f2, f1, Long.MAX_VALUE / 4, t);
    }

    System.out.println(g.GetMaxFlow(0, F));
  }
}
