import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class BWT {

  private void computeLShift(LinkedList<Integer>[] arr, int[] sortedBwt,
      int index, int[] lShift) {
    LinkedList<Integer> head = arr[sortedBwt[index]];
    lShift[index] = head.getFirst();
    head.removeFirst();
  }

  public void invert(int bwtArr[]) {
    int i;
    int len = bwtArr.length;
    int[] lShift = new int[len];

    int[] sortedBwt = bwtArr.clone();

    // Index at which original string appears
    // in the sorted rotations list
    int x = 0;

    Arrays.sort(sortedBwt);

    @SuppressWarnings("unchecked")
    LinkedList<Integer>[] arr = new LinkedList[2];
    for (int j = 0; j < arr.length; j++) {
      arr[j] = new LinkedList<Integer>();
    }

    for (i = 0; i < len; i++) {
      arr[bwtArr[i]].addLast(i);
    }

    for (i = 0; i < len; i++)
      computeLShift(arr, sortedBwt, i, lShift);

    // Inverts the BWT
    for (i = 0; i < len; i++) {
      x = lShift[x];
      System.out.print(bwtArr[x] + " ");
    }
  }

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();

    int[] bwtArr = new int[N];

    for (int i = 0; i < N; i++) {
      bwtArr[i] = sc.nextInt();
    }

    sc.close();

    BWT bwt = new BWT();
    bwt.invert(bwtArr);
  }
}

