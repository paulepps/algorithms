import java.util.Arrays;

public class AB {
	public String createString(int N, int K) {
		// This works by creating a string of A's,\
		// then inserting B's and moving them from\
		// left to right until the required pairs are met

		int placesMax = N / 2;

		if (N % 2 == 1) {
			placesMax++;
		}

		String[] letters = new String[N];
		Arrays.fill(letters, "A");

		int pairs = 0;

		for (int i = 0; i < placesMax; i++) {
			pairs -= i; // we lose some pairs when we add a new B

			
			int places = N - i;

			for (int j = 1; j < places; j++) { // B's start from index 1 (AB...)
				if (pairs == K) {
					return String.join("", letters);
				}

				letters[j - 1] = "A"; // Put the A back in its place
				letters[j] = "B"; // Put the B one place further
				pairs++;
			}
			if (pairs == K) {
				return String.join("", letters);
			}
		}

		return "";

	}

	public static void main(String[] args) {
		AB ab = new AB();
//		System.out.println(ab.createString(3, 2));
//		System.out.println(ab.createString(2, 0));
//		System.out.println(ab.createString(5, 8));
		System.out.println(ab.createString(10, 20));
	}
}