import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int[][] fabric = new int[1000][1000];
		
		while (true) {
			String s = sc.nextLine();

			if (s.equals("x"))
				break;

			int idx1 = s.indexOf('@');
			int idx2 = s.indexOf(',');
			int idx3 = s.indexOf(':');
			int idx4 = s.indexOf('x');

			int left = Integer.parseInt(s.substring(idx1 + 2, idx2));
			int top = Integer.parseInt(s.substring(idx2 + 1, idx3));
			int width = Integer.parseInt(s.substring(idx3 + 2, idx4));
			int height = Integer.parseInt(s.substring(idx4 + 1));

			for (int i = top; i < top + height; i++) {
				for (int j = left; j < left + width; j++) {
					fabric[i][j]++;
				}
			}
		//	System.out.println(left + " " + top + " " + width + " " + height);
		}
		
		int answer = -0;
		
		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 1000; j++) {
				if (fabric[i][j] > 1)
					answer++;
			}
		}
		
		System.out.println(answer);
		
		sc.close();
	}

}
