package math;

public class ModuloLarge {

	int moduloLarge(String num, int mod) {
		int ans = 0;
		for (int i = 0; i < num.length(); i++) {
			int digit = num.charAt(i) - 48;
			ans = (ans * 10 + digit) % mod;
		}
		return ans;
	}
	
	public static void main(String[] args) {
		ModuloLarge ml = new ModuloLarge();
		System.out.println(ml.moduloLarge("12345", 100));
	}
}
