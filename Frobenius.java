
/*
ID: paul9
LANG: JAVA
TASK: nuggets
 */
import java.io.*;
import java.util.*;

//public class Frobenius
class nuggets
{
	static final int INFINITY = 2000000001;
	
	public static void main(String[] args) throws IOException
	{
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("nuggets.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("nuggets.out")));

		int N = sc.nextInt();
		int[] a = new int[N];
		
		for (int i = 0; i < N; i++) {
			a[i] = sc.nextInt();
		}
		
	    out.println(frobeniusNumber(a));
		out.close();
		System.out.println("$:"+(System.currentTimeMillis()-start));System.exit(0);
	}

	static int frobeniusNumber(int[] a) {
		Arrays.sort(a);
		int[] aa = residueTable(a);
		Arrays.sort(aa);
		
		if (aa[aa.length - 1] >= INFINITY) return 0;
		
		return aa[aa.length - 1] - a[0] < 0 ? 0 : aa[aa.length - 1] - a[0];
	}

	static int[] residueTable(int[] a) {
		int[] n = new int[a[0]];
		Arrays.fill(n, INFINITY);
		n[0] = 0;
		
		for (int i = 1; i < a.length; i++) {
			int d = gcd(a[0], a[i]);
			int nn;
			
			for (int r = 0; r < d; r++) {
				try {
					nn = INFINITY;
					for (int q = r; q < a[0]; q += d) {
						if (n[q] < nn)
							nn = n[q];
					}
				}
				catch (Exception ex) {
					continue;
				}

				if (nn < INFINITY) {
					for (int c = 0; c <a[0]; c++) {
						nn += a[i];
						int p = nn % a[0];
						n[p] = nn = Math.min(nn, n[p]);
					}
				}
			}
		}
		return n;
	}


	static int gcd(int a, int b) {
		a = Math.abs(a);
		b = Math.abs(b);
		
		while (a > 0) {
			b = a;
			a = b % a;
		}
		
		return b;
	}
}
