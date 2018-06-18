// http://poj.org/problem?id=2253

package graph_algorithms;

import java.util.Arrays;
import java.util.Scanner;

public class DijkstraUndirectedSPMiniMax {

	static int n, casenum = 0;
	static Point[] k = new Point[200];
	
	static double dist(Point p1, Point p2)
	{
	    return Math.sqrt((double)(p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}
	 
	static double dijstra(int s, int e)
	{
	    double[] d = new double[200];
	    Arrays.fill(d, 0);
	    
	    for (int i = 0; i < n; ++i) {
	        d[i] = dist(k[s], k[i]);
	    }
	 
	    boolean[] vis = new boolean[200];
	    vis[s] = true;
	 
	    for (int i = 0; i < n - 1; ++i)
	    {
	        double minval = 999999999;
	        int index = -1;
	        for (int j = 0; j < n; ++j)
	        {
	            if (!vis[j] && minval > d[j]) 
	            {
	                minval = d[j];
	                index = j;
	            }
	        }
	        vis[index] = true;
	        for (int j = 0; j < n; ++j)
	        {
	            if (!vis[j]) {
	                d[j] = Math.min(d[j], Math.max(d[index], dist(k[index], k[j])));
	            }
	        }
	    }
	    return d[1];
	}
	 
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		
	    while (n != 0)
	    {
	        for (int i = 0; i < n; ++i) {
	        	k[i] = new Point();
	        	k[i].x = sc.nextInt();
	        	k[i].y = sc.nextInt();
	        }
	        System.out.printf("Scenario #%d\nFrog Distance = %.3f\n\n", 
	                ++casenum, dijstra(0, 1));
	        
	        n = sc.nextInt();
	    }
	    sc.close();
	}
}

class Point {
	int x, y;
}