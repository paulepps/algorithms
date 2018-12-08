import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class PT {
  int x, y;
  PT(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

class GridMarker {
  int point;
  int dist;
  boolean duplicate;

  GridMarker(int p, int d) {
    point = p;
    dist = d;
    duplicate = false;
  }
}


class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int maxX = 0;
    int maxY = 0;

    List<PT> points = new ArrayList<PT>();

    while (true) {
      // while (sc.hasNextLine()) {
      String line = sc.nextLine();

      if (line.equals("x"))
        break;

      String[] arr = line.split(", ");

      int x = Integer.parseInt(arr[0]);
      int y = Integer.parseInt(arr[1]);

      maxX = Math.max(maxX, x);
      maxY = Math.max(maxY, y);

      points.add(new PT(x, y));
    }

    sc.close();

    boolean[] infinite = new boolean[points.size()];
    int[] area = new int[points.size()];
    
    for (int yy = 0; yy <= maxY; yy++) {
      for (int xx = 0; xx <= maxX; xx++) {
        int minPoint = -1;
        int minDist = 9999;
        
        for (int i = 0; i < points.size(); i++) {
          PT pt = points.get(i);

          int dist = Math.abs(pt.x - xx) + Math.abs(pt.y - yy);

          if (dist < minDist) {
            minDist = dist;
            minPoint = i;
          } else if (dist == minDist) {
            minPoint = -1;
          }
        }
        
        if (minPoint != -1) {
          if (xx == maxX || xx == 0 || yy == maxY || yy == 0)
            infinite[minPoint] = true;
          area[minPoint]++;
        }
      }
    }

    int maxArea = 0;

    for (int i = 0; i < area.length; i++) {
      if (area[i] > maxArea && !infinite[i])
        maxArea = area[i];
    }

    System.out.println(maxArea);
  }
}
