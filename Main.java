
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
    public static FasterScanner in = new FasterScanner();
    public static Writer out = new Writer();

    static ArrayList<Point2D> list = new ArrayList<Point2D>();

	public static void main(String[] args) throws IOException 
	{
		long start = System.currentTimeMillis();

		int N = in.nextInt();
		Scanner sc = new Scanner(new File("job.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("job.out")));

		int J = sc.nextInt();
		int A = sc.nextInt();
		int B = sc.nextInt();
		int s = 0, t = A*2 + 1;
		FlowNetwork G = new FlowNetwork(A*2 + B*2 + 3);

		int currNode = 1;

		for (int i = 0; i < N; i++) {
			int L = in.nextInt();
			Point2D p = new Point2D(L, 0);
			list.add(p);
		}
		
		for (int i = 0; i < N; i++) {
			int H = in.nextInt();
			Point2D p = list.get(i);
			p.y = H;
			list.set(i, p);
		}
		
		Collections.sort(list);;
		
		int[][] sums = new int[N][3];
		sums[N - 1][0] = list.get(N - 1).x;
		sums[N - 1][1] = list.get(N - 1).y;
		sums[N - 1][2] = list.get(N - 1).y;
		
		for (int i = N - 2; i >= 0; i--) {
			Point2D p = list.get(i);
			sums[i][0] = p.x;			
			sums[i][1] = p.y;			
			sums[i][2] = Math.max(sums[i+1][2], p.y);	
		}

		int energy = 0;
		
		for (int i = 0; i < N; i++) {
			int y = sums[i][1];
			int j = i+1;
			
			while (j < N && y >= sums[j][0])
				j++;
			
			if (j < N)
				energy = (energy + sums[j][2]) % 1000000007;
			else
				energy = (energy + sums[i][2]) % 1000000007;
		}

		out.println(energy);

		out.close();
		System.out.println("$:" + (System.currentTimeMillis() - start));
		System.exit(0);
	}
}

class Point2D implements Comparable<Point2D> {

    public int x;    // x coordinate
    public int y;    // y coordinate

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int compareTo(Point2D that) {
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        return 0;
    }

}

