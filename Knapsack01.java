import static java.lang.Math.*;

public class Knapsack01 {
	
	private final int N;		// maximum number of items
	private final int W;	// maximum weight
	private int[] profit;
	private int[] weight;
	private boolean[] take;
	private int d = 1;
	
	public Knapsack01(int N, int W) {
		
		this.N = N;
		this.W = W;
		
		profit = new int[N+1];
		weight = new int[N+1];
	}
	
	public void addItem(int p, int w) {
		profit[d] = p;
		weight[d] = w;
		d++;
	}
	
	public boolean[] getSelected() {
		return take;
	}
	
	public void pack() {

        // opt[n][w] = max profit of packing items 1..n with weight limit w
        // sol[n][w] = does opt solution to pack items 1..n with weight limit w include item n?
        int[][] opt = new int[N+1][W+1];
        boolean[][] sol = new boolean[N+1][W+1];

        for (int n = 1; n <= N; n++) {
            for (int w = 1; w <= W; w++) {

                // don't take item n
                int option1 = opt[n-1][w];

                // take item n
                int option2 = Integer.MIN_VALUE;
                if (weight[n] <= w) option2 = profit[n] + opt[n-1][w-weight[n]];

                // select better of two options
                opt[n][w] = Math.max(option1, option2);
                sol[n][w] = (option2 > option1);
            }
        }

        // determine which items to take
        take = new boolean[N+1];
        for (int n = N, w = W; n > 0; n--) {
            if (sol[n][w]) {
                take[n] = true;
                w = w - weight[n];
            }
            else {
                take[n] = false;
            }
        }
	}
}
