package math;

import java.util.ArrayList;
import java.util.List;

public class Euclid {

	// This is a collection of useful code for solving problems that
	// involve modular linear equations. Note that all of the
	// algorithms described here work on nonnegative integers.

	// return a % b (positive value)
	static int mod(int a, int b) {
		return ((a % b) + b) % b;
	}

	// computes gcd(a,b)
	static int gcd(int a, int b) {
		while (b != 0) {
			a %= b;
			int temp = a;
			a = b;
			b = temp;
		}
		return a;
	}

	// computes lcm(a,b)
	static int lcm(int a, int b) {
		return a / gcd(a, b) * b;
	}

	// returns d = gcd(a,b); finds x,y such that d = ax + by
	static int extended_euclid(int a, int b, int[] xy) {
		int xx = xy[1] = 0;
		int yy = xy[0] = 1;
		while (b != 0) {
			int q = a / b;
			int t = b;
			b = a % b;
			a = t;
			t = xx;
			xx = xy[0] - q * xx;
			xy[0] = t;
			t = yy;
			yy = xy[1] - q * yy;
			xy[1] = t;
		}
		return a;
	}

	// finds all solutions to ax = b (mod n)
	static List<Integer> modular_linear_equation_solver(int a, int b, int n) {
		int x, y;
		List<Integer> solutions = new ArrayList<Integer>();
		int[] xy = new int[2];
		int d = extended_euclid(a, n, xy);
		x = xy[0];
		y = xy[1];
		if (b % d == 0) {
			x = mod(x * (b / d), n);
			for (int i = 0; i < d; i++)
				solutions.add(mod(x + i * (n / d), n));
		}
		return solutions;
	}

	// computes b such that ab = 1 (mod n), returns -1 on failure
	static int mod_inverse(int a, int n) {
		int x;
		int[] xy = new int[2];
		int d = extended_euclid(a, n, xy);
		x = xy[0];
		if (d > 1)
			return -1;
		return mod(x, n);
	}

	// Chinese remainder theorem (special case): find z such that
	// z % x = a, z % y = b. Here, z is unique modulo M = lcm(x,y).
	// Return (z,M). On failure, M = -1.
	static int[] chinese_remainder_theorem(int x, int a, int y, int b) {
		int[] st = new int[2];
		int d = extended_euclid(x, y, st);
		int s = st[0];
		int t = st[1];
		if (a % d != b % d)
			return new int[] {0, -1};
		return new int[] {mod(s * b * x + t * a * y, x * y) / d, x * y / d};
	}

	// Chinese remainder theorem: find z such that
	// z % x[i] = a[i] for all i. Note that the solution is
	// unique modulo M = lcm_i (x[i]). Return (z,M). On
	// failure, M = -1. Note that we do not require the a[i]'s
	// to be relatively prime.
	static int[] chinese_remainder_theorem(int[] x, int[] a) {
		int[] ret = new int[] {a[0], x[0]};
		for (int i = 1; i < x.length; i++) {
			ret = chinese_remainder_theorem(ret[1], ret[0], x[i], a[i]);
			if (ret[1] == -1)
				break;
		}
		return ret;
	}

	// computes x and y such that ax + by = c; on failure, x = y =-1
	static void linear_diophantine(int a, int b, int c, int[] xy) {
		int d = gcd(a, b);
		if (c % d != 0) {
			xy[0] = xy[1] = -1;
		} else {
			xy[0] = c / d * mod_inverse(a / d, b / d);
			xy[1] = (c - a * xy[0]) / b;
		}
	}

	public static void main(String[] args) {
		
	  // expected: 2
		System.out.println(gcd(14, 30));
	  
	  // expected: 2 -2 1
	  int[] xy = new int[2];
	  int d = extended_euclid(14, 30, xy);
	  int x = xy[0], y = xy[1];
	  System.out.println(d + " " + x + " " + y);
	  
	  // expected: 95 45
	  List<Integer> sols = modular_linear_equation_solver(14, 30, 100);
	  for (int i = 0; i < sols.size(); i++) System.out.print(sols.get(i) + " "); 
	  System.out.println();
	  
	  // expected: 8
	  System.out.println(mod_inverse(8, 9));
	  
	  // expected: 23 56
	  //           11 12
//	  int xs[] = {3, 5, 7, 4, 6};
//	  int as[] = {2, 3, 2, 3, 5};
	  int[] ret = chinese_remainder_theorem(new int[]{3, 5, 7}, new int[]{2, 3, 2});
	  System.out.println(ret[0] + " " + ret[1]);
	  ret = chinese_remainder_theorem (new int[]{4, 6}, new int[]{3, 5});
	  System.out.println(ret[0] + " " + ret[1]);
	  
	  // expected: 5 -15
	  xy = new int[]{x, y};
	  linear_diophantine(7, 2, 5, xy);
	  System.out.println(xy[0] + " " + xy[1]);

	}
}
