package math;
import java.util.Arrays;

/*
ID: paul9
LANG: JAVA
TASK: nuggets

The coin problem (also referred to as the Frobenius coin problem or 
Frobenius problem, after the mathematician Ferdinand Frobenius) is 
a mathematical problem that asks for the largest monetary amount 
that cannot be obtained using only coins of specified denominations.

Also known as the McNugget number.
*/

public class Frobenius
{
	static final int INFINITY = 2000000001;

	int frobeniusNumber(int[] a) {
		Arrays.sort(a);
		int[] aa = residueTable(a);
		Arrays.sort(aa);
		
		if (aa[aa.length - 1] >= INFINITY) return 0;
		
		return aa[aa.length - 1] - a[0] < 0 ? 0 : aa[aa.length - 1] - a[0];
	}

	int[] residueTable(int[] a) {
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


	int gcd(int a, int b) {
		a = Math.abs(a);
		b = Math.abs(b);
		
		while (a > 0) {
			b = a;
			a = b % a;
		}
		
		return b;
	}
	
	public static void main(String[] args)
	{
		int[] a = new int[] {6, 9, 20};

		Frobenius frob = new Frobenius();
		System.out.println(frob.frobeniusNumber(a));	// 43
	}
}
