public class PrimMST {
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private int weight;
    
    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public PrimMST(EdgeWeightedGraph G) {
    	
        marked = new boolean[G.V()];
        marked[0] = true;
    
        weight = 0;

        for (int i = 0; i < G.V() - 1; i++) {
			int min = Integer.MAX_VALUE;
			long minAt = 0;
			
			for (int j = 0; j < G.V(); j++) {
				for (int k = 0; k < G.V(); k++) {
					if (marked[j] && !marked[k] && G.adj(j,k) < min) {
						min = G.adj(j, k);
						minAt = ( ((long)j) << 32) | ((long)k);
					}
				}
			}
			
			marked[(int)(minAt & 0xFFFFFFFFL)] = true;
			weight += min;
		}
    }

    public int weight() {
        return weight;
    }

}
