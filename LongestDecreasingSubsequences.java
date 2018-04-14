/*
ID: paul9
LANG: JAVA
TASK: buylow

Computes subsequences as well as the number of longest subsequences,
excluding duplicates. The problem of finding the longest decreasing
subsequence by itself is easier.
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;

//public class LongestDecreasingSubsequences 
class buylow 
{
	static int[] num = new int[5000]; /* the numbers (prices) */
	static int[] len = new int[5000]; /* the length of the maximum sequence which ends here */
	static int nlen; /* number of numbers */

	static BigInteger[] cnt = new BigInteger[5000]; /* the number of maximal sequences which end here */

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("buylow.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("buylow.out")));

		int lv, lv2;
		int c;
		int max;
		int l;
		BigInteger ans;

		nlen = sc.nextInt();

		for (lv = 0; lv < nlen; lv++)
			num[lv] = sc.nextInt();

		/* use dynamic programming to determine maximum length */
		for (lv = 0; lv < nlen; lv++) {
			max = 1;

			/*
			 * for each number before this one and greater than it, we create a new sequence
			 * by postpending this one. Find the maximum sequence that can created this way
			 */
			for (lv2 = lv - 1; lv2 >= 0; lv2--)
				if (num[lv2] > num[lv] && len[lv2] + 1 > max)
					max = len[lv2] + 1;

			len[lv] = max;
		}

		/*
		 * calculate the number of unique sequences ending at each location that are the
		 * maximum length that a sequence ending there can be
		 */
		for (lv = 0; lv < nlen; lv++) {
			/* if the length is 1, then there's only the number itself, so 1 seq */
			if (len[lv] == 1)
				cnt[lv] = BigInteger.ONE;
			else {
				cnt[lv] = BigInteger.ZERO;
				l = -1;
				max = len[lv] - 1; /* the length of the sequence without this last num */

				/*
				 * otherwise, for each distinct value before it such that it is both greater
				 * than this value and has maximal length to that location of one less, add it's
				 * count to this one
				 */

				/*
				 * note that for each value, we add the number of sequences to it's MOST RECENT
				 * occurrence in order to get all the sequences
				 */

				for (lv2 = lv - 1; lv2 >= 0; lv2--)
					if (len[lv2] == max && num[lv2] > num[lv]
							&& num[lv2] != l) { /*
												 * if this is of the right length, greater than our number, and not the
												 * same value as the last thing added
												 */
						cnt[lv] = cnt[lv].add(cnt[lv2]);
						l = num[lv2];
					}
			}
		}

		/* compute the maximum number of maximum sequences */

		/* same algorithm as the previous section */

		/* find maximum length */
		max = 0;
		for (lv = 0; lv < nlen; lv++)
			if (len[lv] > max)
				max = len[lv];

		/*
		 * add together number of maximum sequences ending at each location, making sure
		 * not to doubly count sequences (using same trick as before)
		 */
		ans = BigInteger.ZERO;
		l = -1;

		for (lv = nlen - 1; lv >= 0; lv--)
			if (len[lv] == max && num[lv] != l) {
				ans = ans.add(cnt[lv]);
				l = num[lv];
			}

		/* output answer */
		out.println(max + " " + ans.toString());
		
		sc.close();
		out.close();
		System.out.println("$:" + (System.currentTimeMillis() - start));
	}

}