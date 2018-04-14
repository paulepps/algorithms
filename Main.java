/*
ID: paul9
LANG: JAVA
TASK: comehome
 */
import java.io.*;
import java.util.*;

public class Main
//class comehome 
{
    public static FasterScanner in = new FasterScanner();
    public static Writer out = new Writer();

    static ArrayList<Point2D> list = new ArrayList<Point2D>();

	public static void main(String[] args) throws IOException 
	{
		long start = System.currentTimeMillis();

		int N = in.nextInt();
		
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
		
		System.out.println(System.currentTimeMillis() - start);
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

class Writer {

private BufferedWriter output;

public Writer() {
output = new BufferedWriter(new OutputStreamWriter(System.out));
}

public Writer(String s) {
try {
output = new BufferedWriter(new FileWriter(s));
} catch(Exception ex) { ex.printStackTrace(); System.exit(0);}
}

public void println() {
try {
output.append("\n");
} catch(IOException io) { io.printStackTrace(); System.exit(0);}
}

public void print(Object o) {
try {
output.append(o.toString());
} catch(IOException io) { io.printStackTrace(); System.exit(0);}
}

public void println(Object o) {
try {
output.append(o.toString()+"\n");
} catch(IOException io) { io.printStackTrace(); System.exit(0);}
}

public void printf(String format, Object... args) {
try {
output.append(String.format(format, args));
} catch(IOException io) { io.printStackTrace(); System.exit(0);}
}

public void printfln(String format, Object... args) {
try {
output.append(String.format(format, args)+"\n");
} catch(IOException io) { io.printStackTrace(); System.exit(0);}
}

public void flush() {
try {
output.flush();
} catch(IOException io) { io.printStackTrace(); System.exit(0);}
}

public void close() {
try {
output.close();
} catch(IOException io) { io.printStackTrace(); System.exit(0);}
}
}

class FasterScanner {
private InputStream mIs;
private byte[] buf = new byte[1024];
private int curChar;
private int numChars;

public FasterScanner() {
this(System.in);
}

public FasterScanner(InputStream is) {
mIs = is;
}

public int read() {
if (numChars == -1)
throw new InputMismatchException();
if (curChar >= numChars) {
curChar = 0;
try {
numChars = mIs.read(buf);
} catch (IOException e) {
throw new InputMismatchException();
}
if (numChars <= 0)
return -1;
}
return buf[curChar++];
}

public String nextLine() {
int c = read();
while (isSpaceChar(c))
c = read();
StringBuilder res = new StringBuilder();
do {
res.appendCodePoint(c);
c = read();
} while (!isEndOfLine(c));
return res.toString();
}

public String nextString() {
int c = read();
while (isSpaceChar(c))
c = read();
StringBuilder res = new StringBuilder();
do {
res.appendCodePoint(c);
c = read();
} while (!isSpaceChar(c));
return res.toString();
}

public long nextLong() {
int c = read();
while (isSpaceChar(c))
c = read();
int sgn = 1;
if (c == '-') {
sgn = -1;
c = read();
}
long res = 0;
do {
if (c < '0' || c > '9')
throw new InputMismatchException();
res *= 10;
res += c - '0';
c = read();
} while (!isSpaceChar(c));
return res * sgn;
}

public int nextInt() {
int c = read();
while (isSpaceChar(c))
c = read();
int sgn = 1;
if (c == '-') {
sgn = -1;
c = read();
}
int res = 0;
do {
if (c < '0' || c > '9')
throw new InputMismatchException();
res *= 10;
res += c - '0';
c = read();
} while (!isSpaceChar(c));
return res * sgn;
}

public boolean isSpaceChar(int c) {
return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
}

public boolean isEndOfLine(int c) {
return c == '\n' || c == '\r' || c == -1;
}

}