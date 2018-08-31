/*
ID: paul9
LANG: JAVA
TASK: milk2
*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MainUsaco {
//class milk2 {

  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws IOException {

    BufferedReader f = new BufferedReader(new FileReader("milk2.in"));
    
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("milk2.out")));
    
    int N = Integer.parseInt(f.readLine());

    Pair<Integer,Integer>[] times = new Pair[N];
    
    for (int i = 0; i < N; i++) {
      StringTokenizer st = new StringTokenizer(f.readLine());
      int s = Integer.parseInt(st.nextToken());
      int e = Integer.parseInt(st.nextToken());
      times[i] = new Pair<Integer, Integer>(s, e);
    }
    
    f.close();
    
    Arrays.sort(times);
    
    int start = times[0].e1;
    int end = times[0].e2;
    
    int maxOn = end - start;
    int maxOff = 0;
    
    for (int i = 1; i < times.length; i++) {
      int s = times[i].e1;
      int e = times[i].e2;
      
      int on, off;
      
      if (e <= end) {
        continue; 
      }
      
      if (s <= end) {
        on = e - start;
        maxOn = Math.max(on, maxOn);
        end = e;
      } else {
        off = s - end;
        maxOff = Math.max(off, maxOff);
        start = s;
        end = e;
        maxOn = Math.max(e - s, maxOn);
      }
    }
    
    out.println(maxOn + " " + maxOff);
    out.close();
    
  }
}
 