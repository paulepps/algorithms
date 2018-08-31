import java.util.Scanner;

/******************************************************************************
 * Compilation: javac SuffixArrayX.java Execution: java SuffixArrayX <
 * input.txt Dependencies: StdIn.java StdOut.java Data files:
 * https://algs4.cs.princeton.edu/63suffix/abra.txt
 * 
 * A data type that computes the suffix array of a string using 3-way radix
 * quicksort.
 *
 * % java SuffixArrayX < abra.txt i ind lcp rnk select
 * --------------------------- 0 11 - 0 ! 1 10 0 1 A! 2 7 1 2 ABRA! 3 0 4 3
 * ABRACADABRA! 4 3 1 4 ACADABRA! 5 5 1 5 ADABRA! 6 8 0 6 BRA! 7 1 3 7
 * BRACADABRA! 8 4 0 8 CADABRA! 9 6 0 9 DABRA! 10 9 0 10 RA! 11 2 2 11
 * RACADABRA!
 *
 *
 ******************************************************************************/

public class SuffixArrayX {
  private static final int CUTOFF = 5; // cutoff to insertion sort (any
                                       // value between 0 and 12)

  private final int[] text;
  private final int[] index; // index[i] = j means text.substring(j) is ith
                             // largest suffix
  private final int n; // number of characters in text

  /**
   * Initializes a suffix array for the given {@code text} string.
   * 
   * @param text the input string
   */
  public SuffixArrayX() {

    Scanner sc = new Scanner(System.in);

    n = sc.nextInt();
    this.text = new int[n + 1];
    this.index = new int[n];
    for (int i = 0; i < n; i++) {
      text[i] = sc.nextInt();
      index[i] = i;
    }

    sc.close();

    text[n] = -1;

    sort(0, n - 1, 0);
  }

  // 3-way string quicksort lo..hi starting at dth character
  private void sort(int lo, int hi, int d) {

    // cutoff to insertion sort for small subarrays
    if (hi <= lo + CUTOFF) {
      insertion(lo, hi, d);
      return;
    }

    int lt = lo, gt = hi;
    int v = text[index[lo] + d];
    int i = lo + 1;
    while (i <= gt) {
      int t = text[index[i] + d];
      if (t < v)
        exch(lt++, i++);
      else if (t > v)
        exch(i, gt--);
      else
        i++;
    }

    // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
    sort(lo, lt - 1, d);
    if (v > 0)
      sort(lt, gt, d + 1);
    sort(gt + 1, hi, d);
  }

  // sort from a[lo] to a[hi], starting at the dth character
  private void insertion(int lo, int hi, int d) {
    for (int i = lo; i <= hi; i++)
      for (int j = i; j > lo && less(index[j], index[j - 1], d); j--)
        exch(j, j - 1);
  }

  // is text[i+d..n) < text[j+d..n) ?
  private boolean less(int i, int j, int d) {
    if (i == j)
      return false;
    i = i + d;
    j = j + d;
    while (i < n && j < n) {
      if (text[i] < text[j])
        return true;
      if (text[i] > text[j])
        return false;
      i++;
      j++;
    }
    return i > j;
  }

  // exchange index[i] and index[j]
  private void exch(int i, int j) {
    int swap = index[i];
    index[i] = index[j];
    index[j] = swap;
  }

  /**
   * Returns the length of the input string.
   * 
   * @return the length of the input string
   */
  public int length() {
    return n;
  }


  /**
   * Returns the index into the original string of the <em>i</em>th
   * smallest suffix. That is, {@code text.substring(sa.index(i))} is the
   * <em>i</em> smallest suffix.
   * 
   * @param i an integer between 0 and <em>n</em>-1
   * @return the index into the original string of the <em>i</em>th
   *         smallest suffix
   * @throws java.lang.IllegalArgumentException unless {@code 0 <=i < n}
   */
  public int index(int i) {
    if (i < 0 || i >= n)
      throw new IllegalArgumentException();
    return index[i];
  }

  /**
   * Returns the length of the longest common prefix of the <em>i</em>th
   * smallest suffix and the <em>i</em>-1st smallest suffix.
   * 
   * @param i an integer between 1 and <em>n</em>-1
   * @return the length of the longest common prefix of the <em>i</em>th
   *         smallest suffix and the <em>i</em>-1st smallest suffix.
   * @throws java.lang.IllegalArgumentException unless {@code 1 <= i < n}
   */
  public int lcp(int i) {
    if (i < 1 || i >= n)
      throw new IllegalArgumentException();
    return lcp(index[i], index[i - 1]);
  }

  // longest common prefix of text[i..n) and text[j..n)
  private int lcp(int i, int j) {
    int length = 0;
    while (i < n && j < n) {
      if (text[i] != text[j])
        return length;
      i++;
      j++;
      length++;
    }
    return length;
  }

  /**
   * Returns the <em>i</em>th smallest suffix as a string.
   * 
   * @param i the index
   * @return the <em>i</em> smallest suffix as a string
   * @throws java.lang.IllegalArgumentException unless {@code 0 <= i < n}
   */
  public String select(int i) {
    if (i < 0 || i >= n)
      throw new IllegalArgumentException();
    return new String(text, index[i], n - index[i]);
  }

  /**
   * Returns the number of suffixes strictly less than the {@code query}
   * string. We note that {@code rank(select(i))} equals {@code i} for each
   * {@code i} between 0 and <em>n</em>-1.
   * 
   * @param query the query string
   * @return the number of suffixes strictly less than {@code query}
   */
  public int rank(String query) {
    int lo = 0, hi = n - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      int cmp = compare(query, index[mid]);
      if (cmp < 0)
        hi = mid - 1;
      else if (cmp > 0)
        lo = mid + 1;
      else
        return mid;
    }
    return lo;
  }

  // is query < text[i..n) ?
  private int compare(String query, int i) {
    int m = query.length();
    int j = 0;
    while (i < n && j < m) {
      if (query.charAt(j) != text[i])
        return query.charAt(j) - text[i];
      i++;
      j++;

    }
    if (i < n)
      return -1;
    if (j < m)
      return +1;
    return 0;
  }


  public static void main(String[] args) {
    SuffixArrayX suffix2 = new SuffixArrayX();

    for (int i = 0; i <= 6; i++) {
      int lcp = suffix2.lcp(suffix2.index(i), suffix2.index(i + 1));
      System.out.println(lcp);
    }
  }
}


