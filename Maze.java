/*
ID: paul9
LANG: JAVA
TASK: snail
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * Longest non-intersecting path through a grid with obstacles.
 * All this problem requires is basically a straightforward depth-first search. 
 * We have to make sure that we check whether we have the best path visited so 
 * far whenever we hit an obstruction or when we re-cross our path. If we hit 
 * a wall/obstruction, we invert (0->1, 1->0) the row and column changes to 
 * get the new column changes.
 */

class snail{
//	public class Maze {

	static boolean[][] grid, visited;
	static int dim, bar, best, cur;

	static boolean ispoint(int row, int col) {
		return (row >= 0 && col >= 0 && row < dim && col < dim);
	}

	static void mark(int a, int b) {
		visited[a][b] = true;
		cur++;
	}

	static void unmark(int a, int b) {
		visited[a][b] = false;
		cur--;
	}

	static void check() {
		if (cur > best)
			best = cur;
	}

	static void search(int row, int col, int rc, int cc) {
		if (visited[row][col]) {
			check();
			return;
		}

		mark(row, col);

		if (!ispoint(row + rc, col + cc) || grid[row + rc][col + cc]) {
			check();
			
			// change directions
			rc = rc == 0 ? 1 : 0;
			cc = cc == 0 ? 1 : 0;

			// turn right and  left
			if (ispoint(row + rc, col + cc) && !grid[row + rc][col + cc])
				search(row + rc, col + cc, rc, cc);
			if (ispoint(row - rc, col - cc) && !grid[row - rc][col - cc])
				search(row - rc, col - cc, -rc, -cc);
		} else
			// keep going straight ahead
			search(row + rc, col + cc, rc, cc);
		
		unmark(row, col);
	}

	public static void main(String[] args) throws IOException {
	
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("snail.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("snail.out")));

		int a;
	    
	    dim = sc.nextInt();
	    bar = sc.nextInt();	// how many barriers?
	    
	    grid = new boolean[dim][dim];
	    visited = new boolean[dim][dim];
	    
	    // add barriers to grid
	    for (a = 0; a < bar; a++) {
	    	String s = sc.next();
			grid[Integer.parseInt(s.substring(1)) - 1][s.charAt(0) - 'A'] = true;
	    }

	    sc.close();
	    
	    // start in top left. search right and search down.
	    search (0, 0, 0, 1);
	    search (0, 0, 1, 0);
	    
	    out.println(best);
	    out.close();

		System.out.println("$:" + (System.currentTimeMillis() - start));
	}
}