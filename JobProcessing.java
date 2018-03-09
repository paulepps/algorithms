
/*
ID: paul9
LANG: JAVA
TASK: job
http://usacotraining.blogspot.com/2012/12/problem-423-job-processing.html
 */
import java.io.*;
import java.util.*;

//public class JobProcessing
class job
{
	/* when machine A is done with current job/B starts current job */
	static int[] finA = new int[30];
	static int[] finB = new int[30];

	/* amount of time each machine takes to process a job */
	static int[] timA = new int[30];
	static int[] timB = new int[30];

	/* number of A and B machines */
	static int na, nb;
	/* number of tasks to do */
	static int ntask;

	/* how long before the kth task is through the "A" machine */
	static int taskdone[] = new int[1000];

	/*
	 * the last time that a job can be put into a "B" machine and still be done
	 */
	/* the value is with respect to the total end time */
	/* e.g., a value of 17 means 17 time steps before all task are completed */
	static int[] taskst = new int[1000];

	static void make_times() {
		int lv, lv2;
		int min, mloc;

		for (lv = 0; lv < ntask; lv++) { /* for each time */
			min = 80000; /* = infinity */
			mloc = -1;

			for (lv2 = 0; lv2 < na; lv2++) { /* find best A machine */
				if (finA[lv2] + timA[lv2] < min) {
					mloc = lv2;
					min = finA[lv2] + timA[lv2];
				}
			}
			assert (mloc != -1); /* sanity check */

			/* put this job in the mloc "A" machine */
			finA[mloc] = min;
			taskdone[lv] = min;

			min = 80000;
			mloc = -1;
			for (lv2 = 0; lv2 < nb; lv2++) { /* find best B machine */
				if (finB[lv2] + timB[lv2] < min) {
					mloc = lv2;
					min = finB[lv2] + timB[lv2];
				}
			}
			assert (mloc != -1); /* sanity */

			/* put this job through the mloc "B" machine */
			finB[mloc] = min;
			taskst[lv] = min;
		}
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("job.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("job.out")));

		int lv;
		int max, min;

		/* read in data */
		ntask = sc.nextInt();
		na = sc.nextInt();
		nb = sc.nextInt();

		for (lv = 0; lv < na; lv++)
			timA[lv] = sc.nextInt();
		for (lv = 0; lv < nb; lv++)
			timB[lv] = sc.nextInt();

		/* create taskdone & taskst */
		make_times();

		/* determine time that we are done with the "A" machines */
		min = 0;
		for (lv = 0; lv < na; lv++)
			if (finA[lv] > min)
				min = finA[lv];
		out.print(min + " ");

		/* match "A" completed jobs to "B" started jobs greedily */
		max = 0;
		for (lv = 0; lv < ntask; lv++)
			if (taskdone[lv] + taskst[ntask - lv - 1] > max)
				max = taskdone[lv] + taskst[ntask - lv - 1];

		out.println(max);
		out.close();
		sc.close();
		System.out.println("$:"+(System.currentTimeMillis()-start));System.exit(0);
	}
}
