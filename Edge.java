public class Edge implements Comparable<Edge> { 

    int v;
    int w;
    int weight;

    public Edge(int v, int w, int weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge that) {
        return Integer.compare(this.weight, that.weight);
    }

}
