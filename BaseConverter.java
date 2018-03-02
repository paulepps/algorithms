
public class BaseConverter {
	
	String baseConverter(String number, int ob, int nb) {

		number = number.toUpperCase();

		String list = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		int dec = 0;

		if (number == "") {
			number = "0";
		} else {
			for (int i = 0; i < number.length(); i++) {
				dec += (list.indexOf(number.charAt(i))) * (Math.pow(ob, (number.length() - i - 1)));
			}
			number = "";
			int magnitude = (int)Math.floor((Math.log(dec)) / (Math.log(nb)));
			
//			for (int i = magnitude; i >= 0; i--) {
			for (int i = magnitude; i > 0; i--) {
				int amount = (int)Math.floor(dec / Math.pow(nb, i));
				number = number + list.charAt(amount);
				dec -= amount * (Math.pow(nb, i));
			}
		}
		return number;
	}
	
	public static void main(String[] args) {
		BaseConverter bc = new BaseConverter();
		System.out.println(bc.baseConverter("37", 10, 37));
	}
}
