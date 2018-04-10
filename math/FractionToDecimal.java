package math;
/*
ID: paul9
LANG: JAVA
TASK: fracdec

Program will accept a fraction of the form N/D, where N is the numerator 
and D is the denominator, and print the decimal representation. 

If the decimal representation has a repeating sequence of digits, 
indicate the sequence by enclosing it in brackets. 

For example, 1/3 = .33333333...is denoted as 0.(3), 
and 41/333 = 0.123123123...is denoted as 0.(123). 
Use xxx.0 to denote an integer. 

Typical conversions are:

1/3     =  0.(3)
22/5    =  4.4
1/7     =  0.(142857)
2/2     =  1.0
3/8     =  0.375
45/56   =  0.803(571428)

*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class FractionToDecimal {

	public static void main(String[] args) throws IOException 
	{
		Scanner sc = new Scanner(new File("fracdec.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("fracdec.out")));

		int N = sc.nextInt();
		int D = sc.nextInt();

		String ans = "";
		int carry = 0;
		int tempAns = 0;
		int numTimes = (N+"").length();
		int count = 0;
		
		while((carry!=0 || N != 0) && count < numTimes)
		{
			int num = (carry*10+((N+"").charAt(0)-'0'))/D;
			tempAns = tempAns*10 + num;
			carry = (carry*10+((N+"").charAt(0)-'0'))%D;
			N = Integer.valueOf("0"+(N+"").substring(1));			
			count++;
		}
		
		ans = tempAns+".";
		int z = 0;
		int[] seen = new int[D];
		Arrays.fill(seen,-1);
		int rep = -1;
		StringBuilder temp = new StringBuilder();
		
		while(carry != 0)
		{
			if(seen[carry] != -1)
			{
				rep = seen[carry];
				break;
			}
			seen[carry] = z;		
			
			int num = (carry*10)/D;			
			carry = (carry*10)%D;
			temp.append(num);
			
			z++;
		}
		ans = ans + temp;
		if(rep != -1)			
			ans = ans.substring(0,ans.indexOf('.')+1+rep)+"("+ans.substring(ans.indexOf('.')+1+rep)+")";

		if(ans.indexOf('.')+1 == ans.length())
			ans = ans+"0";

		
		int at = 0;
		
		while(ans.length()-at > 76) // line length = 76
		{
			out.println(ans.substring(at,at+76));
			at+=76;
		}
		
		if(at - ans.length() != 0)
			out.println(ans.substring(at));

		sc.close();
		out.close();
	}
}
