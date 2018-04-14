import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LeftOrRight {

	static String calcDir(String[] p0, String[] p1, String[]p2) {
	    double v1x = Double.parseDouble(p1[0]) - Double.parseDouble(p0[0]);
		double v1y = Double.parseDouble(p1[1]) - Double.parseDouble(p0[1]);
		double v2x = Double.parseDouble(p2[0]) - Double.parseDouble(p1[0]);
		double v2y = Double.parseDouble(p2[1]) - Double.parseDouble(p1[1]);
		
	    if (v1x * v2y - v1y * v2x > 0.0)
	        return "Left";
	    else
	        return "Right";
	}


	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("LeftOrRight.in"));
		
		int lineIdx = 0;
		String line, line0 = "", line1 = "", line2 = "";
		
		while (sc.hasNextLine()) {
			line = sc.nextLine().trim();
		    lineIdx++;
		    
		    if (lineIdx == 1) {
		        System.out.println(line);
		    } 
		    else if (lineIdx == 2) {
		        line0 = line;
		        System.out.println(line0);	
		    }
		    else if (lineIdx == 3) {
		        line1 = line;
		    }
		    else {
		        line2 = line;
		        String dir = calcDir(line0.split("\\s+"), line1.split("\\s+"), line2.split("\\s+"));
		        System.out.println(line1 + " " + dir);
		        line0 = line1;
		        line1 = line2;
		    }
		}
		
		System.out.println(line2);
		sc.close();
	}
}
