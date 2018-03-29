
/*
ID: paul9
LANG: JAVA
TASK: race3
http://train.usaco.org/usacoanal2?a=KtkWUSTxX2E&S=race3
 */
import java.io.*;
import java.util.*;

public class FloodFill {
	static final int MAXL = 50;
	static boolean[][] conn = new boolean[MAXL][MAXL];
	static int nloc;

	static boolean[] flg = new boolean[MAXL];

	static boolean[] touched = new boolean[MAXL];
	static boolean[] otouched;

	/* mark all the points reachable from this location */
	static void floodFill(int l) {
		int lv;

		/* we've already flood fill from this place, no need to do again */
		if (touched[l])
			return;

		/* mark as visited and reachable */
		touched[l] = true;

		/* recurse on each connected location */
		for (lv = 0; lv < nloc; lv++)
			if (conn[l][lv])
				floodFill(lv);
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("race3.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("race3.out")));

		int lv, lv2;
		int p;
		int f;

		nloc = 0;

		while (true) {
			p = sc.nextInt();
			if (p == -1)
				break; /* end of file */
			if (p == -2)
				nloc++; /* new location */
			else
				conn[nloc][p] = true; /* new arc */
		}

		f = 0;
		/* for each location */
		for (lv = 1; lv < nloc - 1; lv++) {
			/* clear reachable array */
			Arrays.fill(touched, false);

			/* don't allow it to flood fill thru this location */
			touched[lv] = true;

			/*
			 * find all the locations reachable from start not using this location
			 */
			floodFill(0);

			if (!touched[nloc - 1]) { /* we can't reach finish, this is unavoidable */
				flg[lv] = true;
				f++;
			} else
				flg[lv] = false;
		}

		/* output count of unavoidable locs */
		out.print(f);

		/* output unavoidable locations */
		for (lv = 1; lv < nloc; lv++)
			if (flg[lv])
				out.print(" " + lv);
		out.println();

		f = 0;
		for (lv = 1; lv < nloc - 1; lv++)
			if (flg[lv]) /* a splitting point must be unavoidable */
			{
				/* determine everything reachable from this location */
				Arrays.fill(touched, false);

				floodFill(lv);
				/* store that data in otouched */
				otouched = Arrays.copyOf(touched, touched.length);

				/*
				 * determine everything reachable from the beginning, not using this location
				 */
				Arrays.fill(touched, false);
				touched[lv] = true;
				floodFill(0);

				/*
				 * if any point is reachable in both flood fills, this location is not a
				 * splitting
				 */
				for (lv2 = 0; lv2 < nloc; lv2++)
					if (touched[lv2] && otouched[lv2] && lv != lv2)
						break;
				if (lv2 >= nloc) /* no point was found */
				{ /* this location is a splitting point */
					flg[lv] = true;
					f++;
				} else
					flg[lv] = false;
			}

		/* count of splitting points */
		out.print(f);

		/* output splitting points themselves */
		for (lv = 1; lv < nloc - 1; lv++)
			if (flg[lv])
				out.print(" " + lv);
		out.println();

		sc.close();
		out.close();
		System.out.println("$:" + (System.currentTimeMillis() - start));
	}

}