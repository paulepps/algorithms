
/*
 * ID: paul9 LANG: JAVA TASK: ditch
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class FordFulkerson {
  private static final double FLOATING_POINT_EPSILON = 1E-11;

  private final int V; // number of vertices
  private boolean[] marked; // marked[v] = true iff s->v path in residual
                            // graph
  private FlowEdge[] edgeTo; // edgeTo[v] = last edge on shortest residual
                             // s->v path
  private double value; // current value of max flow

  public FordFulkerson(FlowNetwork G, int s, int t) {
    V = G.V();
    if (s == t)
      throw new IllegalArgumentException("Source equals sink");
    // if (!isFeasible(G, s, t)) throw new
    // IllegalArgumentException("Initial flow is
    // infeasible");

    // while there exists an augmenting path, use it
    value = excess(G, t);
    while (hasAugmentingPath(G, s, t)) {

      // compute bottleneck capacity
      double bottle = Double.POSITIVE_INFINITY;
      for (int v = t; v != s; v = edgeTo[v].other(v)) {
        bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
      }

      // augment flow
      for (int v = t; v != s; v = edgeTo[v].other(v)) {
        edgeTo[v].addResidualFlowTo(v, bottle);
      }

      value += bottle;
    }

    // check optimality conditions
    // assert check(G, s, t);
  }

  /**
   * Returns the value of the maximum flow.
   *
   * @return the value of the maximum flow
   */
  public double value() {
    return value;
  }

  /**
   * Returns true if the specified vertex is on the {@code s} side of the
   * mincut.
   *
   * @param v vertex
   * @return {@code true} if vertex {@code v} is on the {@code s} side of
   *         the micut; {@code false} otherwise
   * @throws IllegalArgumentException unless {@code 0 <= v < V}
   */
  public boolean inCut(int v) {
    validate(v);
    return marked[v];
  }

  // throw an IllegalArgumentException if v is outside prescibed range
  private void validate(int v) {
    if (v < 0 || v >= V)
      throw new IllegalArgumentException(
          "vertex " + v + " is not between 0 and " + (V - 1));
  }

  // is there an augmenting path?
  // if so, upon termination edgeTo[] will contain a parent-link
  // representation of
  // such a path
  // this implementation finds a shortest augmenting path (fewest number of
  // edges),
  // which performs well both in theory and in practice
  private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
    edgeTo = new FlowEdge[G.V()];
    marked = new boolean[G.V()];

    // breadth-first search
    Queue<Integer> queue = new ArrayDeque<Integer>();
    queue.add(s);
    marked[s] = true;
    while (!queue.isEmpty() && !marked[t]) {
      int v = queue.remove();

      for (FlowEdge e : G.adj(v)) {
        int w = e.other(v);

        // if residual capacity from v to w
        if (e.residualCapacityTo(w) > 0) {
          if (!marked[w]) {
            edgeTo[w] = e;
            marked[w] = true;
            queue.add(w);
          }
        }
      }
    }

    // is there an augmenting path?
    return marked[t];
  }

  // return excess flow at vertex v
  private double excess(FlowNetwork G, int v) {
    double excess = 0.0;
    for (FlowEdge e : G.adj(v)) {
      if (v == e.from())
        excess -= e.flow();
      else
        excess += e.flow();
    }
    return excess;
  }

  // return excess flow at vertex v
  private boolean isFeasible(FlowNetwork G, int s, int t) {

    // check that capacity constraints are satisfied
    for (int v = 0; v < G.V(); v++) {
      for (FlowEdge e : G.adj(v)) {
        if (e.flow() < -FLOATING_POINT_EPSILON
            || e.flow() > e.capacity() + FLOATING_POINT_EPSILON) {
          System.err
              .println("Edge does not satisfy capacity constraints: " + e);
          return false;
        }
      }
    }

    // check that net flow into a vertex equals zero, except at source and
    // sink
    if (Math.abs(value + excess(G, s)) > FLOATING_POINT_EPSILON) {
      System.err.println("Excess at source = " + excess(G, s));
      System.err.println("Max flow         = " + value);
      return false;
    }
    if (Math.abs(value - excess(G, t)) > FLOATING_POINT_EPSILON) {
      System.err.println("Excess at sink   = " + excess(G, t));
      System.err.println("Max flow         = " + value);
      return false;
    }
    for (int v = 0; v < G.V(); v++) {
      if (v == s || v == t)
        continue;
      else if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
        System.err.println("Net flow out of " + v + " doesn't equal zero");
        return false;
      }
    }
    return true;
  }

  // check optimality conditions
  private boolean check(FlowNetwork G, int s, int t) {

    // check that flow is feasible
    if (!isFeasible(G, s, t)) {
      System.err.println("Flow is infeasible");
      return false;
    }

    // check that s is on the source side of min cut and that t is not on
    // source
    // side
    if (!inCut(s)) {
      System.err
          .println("source " + s + " is not on source side of min cut");
      return false;
    }
    if (inCut(t)) {
      System.err.println("sink " + t + " is on source side of min cut");
      return false;
    }

    // check that value of min cut = value of max flow
    double mincutValue = 0.0;
    for (int v = 0; v < G.V(); v++) {
      for (FlowEdge e : G.adj(v)) {
        if ((v == e.from()) && inCut(e.from()) && !inCut(e.to()))
          mincutValue += e.capacity();
      }
    }

    if (Math.abs(mincutValue - value) > FLOATING_POINT_EPSILON) {
      System.err.println("Max flow value = " + value + ", min cut value = "
          + mincutValue);
      return false;
    }

    return true;
  }

  public static void main(String[] args) throws IOException {

    long start = System.currentTimeMillis();
    // Scanner sc = new Scanner(new File("ditch.in"));
    // PrintWriter out = new PrintWriter(new BufferedWriter(new
    // FileWriter("ditch.out")));

    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();

    while (T-- > 0) {
      // create flow network with V vertices and E edges
      int V = sc.nextInt();
      // int E = sc.nextInt();
      FlowNetwork G = new FlowNetwork(V);

      for (int i = 0; i < V; i++) {
        for (int j = 0; j < V; j++) {
          int cap = sc.nextInt();
          if (cap > 0) {
            FlowEdge e = new FlowEdge(i, j, cap);
            G.addEdge(e);
          }
        }
      }
      // compute maximum flow and minimum cut
      int s = sc.nextInt(), t = sc.nextInt();

      FordFulkerson maxflow = new FordFulkerson(G, s, t);
      for (int v = 0; v < G.V(); v++) {
        for (FlowEdge e : G.adj(v)) {
          if ((v == e.from()) && e.flow() > 0)
            if (e.flow() == e.capacity())
              System.out.print(e.from() + " " + e.to() + " ");
        }
      }

//      System.out.println((int) maxflow.value());

      for (int i = 0; i < V; i++) {
//        System.out.println(maxflow.inCut(i) + " ");
      }
      sc.close();

      System.out.println();
//      System.out.println("$:" + (System.currentTimeMillis() - start));
      System.exit(0);
    }
  }
}
