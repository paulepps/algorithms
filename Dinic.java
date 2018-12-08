import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Dinic {

  private final int MAXM = 25000;
  private final int MAXN = 25000;

  private int start, end;
  private int edgeCount = 0;

  private int[] head;
  private int[] vertex;
  private int[] next;
  private int[] capacity;
  private int[] flow;
  private int[] dist;
  private int[] curr;

  public Dinic() {
    head = new int[MAXN];
    Arrays.fill(head, -1);

    vertex = new int[MAXM];
    next = new int[MAXM];
    capacity = new int[MAXM];
    flow = new int[MAXM];

    dist = new int[MAXN];
    curr = new int[MAXN];
  }

  public void addEdge(int uu, int vv, int ca) {
    vertex[edgeCount] = vv;
    capacity[edgeCount] = ca;
    flow[edgeCount] = 0;
    next[edgeCount] = head[uu];
    head[uu] = edgeCount++;

    vertex[edgeCount] = uu;
    capacity[edgeCount] = 0;
    flow[edgeCount] = 0;
    next[edgeCount] = head[vv];
    head[vv] = edgeCount++;
  }

  private boolean bfs() {
    Arrays.fill(dist, -1);
    dist[start] = 0;

    Queue<Integer> qu = new LinkedList<Integer>();
    qu.add(start);

    while (!qu.isEmpty()) {
      int u = qu.poll();

      for (int e = head[u]; e != -1; e = next[e])
        if (dist[vertex[e]] == -1 && capacity[e] > flow[e]) {
          dist[vertex[e]] = dist[u] + 1;
          qu.add(vertex[e]);
        }
    }
    return dist[end] != -1;
  }

  private int dfs(int u, int a) {
    if (u == end || a == 0)
      return a;

    int f, Flow = 0;

    for (int e = curr[u]; e != -1; e = next[e]) {
      curr[u] = e;
      if (dist[vertex[e]] == dist[u] + 1
          && (f = dfs(vertex[e], Math.min(a, capacity[e] - flow[e]))) > 0) {
        flow[e] += f;
        flow[e ^ 1] -= f;
        Flow += f;
        a -= f;

        if (a == 0)
          break;
      }
    }
    return Flow;
  }

  public int maxflow(int start, int end) {
    this.start = start;
    this.end = end;

    int Flow = 0;

    while (bfs()) {
      curr = head.clone();
      Flow += dfs(start, Integer.MAX_VALUE);
    }
    return Flow;
  }
}
