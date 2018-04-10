package math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecurringDecimals {

//	  Computes the decimal representation of the fraction a / b in three parts:
//	  integer part, non-recurring fractional part, and recurring part.
	
	static int integer;
	static String fractional;
	static String recurring;
	
	static void divide(int a, int b) {
		  assert b > 0;
		  
		  fractional = "";
		  recurring = "";
		  
		  integer = a / b;
		  int remainder = a % b;
		  
		  Map<Integer,Integer> seen = new HashMap<>();
		  seen.put(remainder, 0);

		  List<Integer> digits = new ArrayList<>();
		  
		  while (true) {  // Loop executed at most b times (as remainders must be distinct)
			  remainder *= 10;
			  digits.add(remainder / b);
			  remainder %= b;
			  
			  
			  if (seen.containsKey(remainder)) {  // Digits have begun to recur
				  int where = seen.get(remainder);
				  
				  int i, j;
				  
				  for (i = 0, j = 0; i < where; i++, j++) {
					  fractional += digits.get(j);
				  }
				  
				  for (i = 0; i < digits.size() - where; i++, j++) {
					  recurring += digits.get(j);
				  }
			      return;
			  } else {
				  seen.put(remainder, digits.size());
				  
			  }
			  
		  }
	}

	public static void main(String[] args) {
		
//		Some examples.
//		
//		5/4 = 1.25(0)
//		1/6 = 0.1(6)
//		17/7 = 2.(428571)
//		22/11 = 2.(0)
//		100/17 = 5.(8823529411764705)
		
		int[][] testCases = {{5,4}, {1,6}, {17,7}, {22,11}, {100,17}};
		
		for (int j = 0; j < testCases.length; j++) {
			int a = testCases[j][0];
			int b = testCases[j][1];
			
			divide(a, b);
			System.out.printf("%d/%d = %d.%s(%s)\n", a, b, integer, fractional, recurring);
		}
	}
}
