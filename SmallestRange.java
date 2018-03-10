import java.util.Comparator;
import java.util.PriorityQueue;

// Smallest range that includes numbers from k lists
public class SmallestRange {

	public static void main(String[] args) {
		int nums[][] = { { 4, 10, 15, 24, 26 }, { 0, 9, 12, 20 }, { 5, 18, 22, 30 } };

		PriorityQueue<Element> pq = new PriorityQueue<Element>(new ElementComparator());
		
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < nums.length; i++) {
			Element e = new Element(i, 0, nums[i][0]);
			pq.offer(e);
			max = Math.max(max, nums[i][0]);
		}
		int range = Integer.MAX_VALUE;
		int start = -1, end = -1;
		while (pq.size() == nums.length) {

			Element curr = pq.poll();
			if (max - curr.val < range) {
				range = max - curr.val;
				start = curr.val;
				end = max;
			}
			if (curr.idx + 1 < nums[curr.row].length) {
				curr.idx = curr.idx + 1;
				curr.val = nums[curr.row][curr.idx];
				pq.offer(curr);
				if (curr.val > max) {
					max = curr.val;
				}
			}
		}

		System.out.println(start + " " + end);
		//return new int[] { start, end };
	}
}

class ElementComparator implements Comparator<Element> {
	@Override
	public int compare(Element a, Element b) {
		return a.val - b.val;
	}
}

class Element {
	int val;
	int idx;
	int row;

	public Element(int r, int i, int v) {
		val = v;
		idx = i;
		row = r;
	}
}