
/*
ID: paul9
LANG: JAVA
TASK: buylow
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LIS
// class buylow
{
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("buylow.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("buylow.out")));

		int N = sc.nextInt();
		int data[] = new int[N];

		for (int i = 0; i < N; i++) {
			data[i] = sc.nextInt();
		}

		int[] len = new int[N];
		for (int i = 0; i < data.length; i++) {
			len[i] = 1;
			for (int j = i - 1; j >= 0; j--)
				if (data[j] > data[i] && len[j] + 1 > len[i])
					len[i] = len[j] + 1;
		}

		int max = 0;
		for (int i = 0; i < len.length; i++) {
			if (len[i] > max)
				max = len[i];
		}
		out.print(max + " ");

		ArrayList<Integer> sofar = new ArrayList<>();
		;
		print_all(data, len, max, data.length, sofar);

		out.println(count);
		
		out.close();
		sc.close();
		System.out.println("$:" + (System.currentTimeMillis() - start));
	}

	static int count = 0;

	static void print_all(int[] seq, int[] len, int max, int pos, ArrayList<Integer> sofar) {
		if (max == 0) {
			count++;
			for (int i = sofar.size() - 1; i >= 0; i--)
				System.out.print(sofar.get(i) + " ");
			System.out.println();
			return;
		}
		int val = pos < seq.length ? seq[pos] : -1;
		for (int i = pos - 1; i >= 0; i--) {
			if (len[i] == max && seq[i] > val) {
				sofar.add(seq[i]);
				print_all(seq, len, max - 1, i, sofar);
				sofar.remove(sofar.size() - 1);
			}
		}
	}
}