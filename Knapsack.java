import static java.lang.Math.*;

public class Knapsack {
	
	private final int N;		// maximum number of items
	private final int weight;	// maximum weight
	private int d = 0;
	private int[][] data;
	
	public Knapsack(int N, int weight) {
		
		this.N = N;
		this.weight = weight;
		
		data = new int[N][2];
	}
	
	public void addItem(int profit, int weight) {
		data[d][0] = profit;
		data[d][1] = weight;
		d++;
	}
	
	public int getBest() {
		removeDominated();
		
		int[] best = new int[weight+1];
		int ans = 0;
		
		for(int i = 0; i <= weight; i++)
		{
			for(int j = 0; j < data.length;j++)
			{
				if(i-weight(j) >= 0)
					best[i] = max(best[i],best[i-weight(j)]+profit(j));
			}
			ans = max(ans,best[i]);
		}
		
		return ans;
	}
	
	private int profit(int i) {
		return data[i][0];
	}
	
	private int weight(int i) {
		return data[i][1];
	}
	
	private void removeDominated() {
		boolean[] remove = new boolean[N];
		int count = 0;

		for(int i = 0; i < N; i++)
		{
			if(remove[i]) continue;
			for(int j = 0; j < N;j++)
			{
				if(weight(j) > weight(i) && profit(j) < profit(i))
				{
					if(remove[j] == false)
					{
						remove[j] = true;
						count++;
					}
				}
			}
		}

		int[][] newDat = new int[N-count][2];
		int at = 0;		
		
		for(int i = 0; i < N; i++)
		{			
			if(remove[i]) continue;
			newDat[at] = data[i];
		
			at++;
		}
		
		data = newDat;
	}
	
}
