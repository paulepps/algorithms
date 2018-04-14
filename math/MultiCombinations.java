package math;
import java.util.HashSet;

public class MultiCombinations {
 
    private HashSet<String> set = new HashSet<String>();
    private Combinations comb = null;
    private Object[] nextElem = null;
 
    public MultiCombinations(Object[] objects, int k) throws CombinatoricException {
        k = Math.max(0, k);
        Object[] myObjects = new Object[objects.length * k];
        for (int i = 0; i < objects.length; i++) {
            for (int j = 0; j < k; j++) {
                myObjects[i * k + j] = objects[i];
            }
        }
        comb = new Combinations(myObjects, k);
    } // constructor
 
    boolean hasMoreElements() {
        boolean ret = false;
        nextElem = null;
        int oldCount = set.size();
        while (comb.hasMoreElements()) {
            Object[] elem = (Object[]) comb.nextElement();
            String str = "";
            for (int i = 0; i < elem.length; i++) {
                str += ("%" + elem[i].toString() + "~");
            }
            set.add(str);
            if (set.size() > oldCount) {
                nextElem = elem;
                ret = true;
                break;
            }
        }
        return ret;
    } // hasMoreElements()
 
    Object[] nextElement() {
        return nextElem;
    }
 
    static java.math.BigInteger c(int n, int k) throws CombinatoricException {
        return Combinatoric.c(n + k - 1, k);
    }
    
    public static void main(String[] args) throws CombinatoricException {
        Object[] objects = {"iced", "jam", "plain"};
        //Object[] objects = {"abba", "baba", "ab"};
        //Object[] objects = {"aaa", "aa", "a"};
        //Object[] objects = {(Integer)1, (Integer)2, (Integer)3, (Integer)4};
        MultiCombinations mc = new MultiCombinations(objects, 2);
        while (mc.hasMoreElements()) {
            for (int i = 0; i < mc.nextElement().length; i++) {
                System.out.print(mc.nextElement()[i].toString() + " ");
            }
            System.out.println();
        }
 
        // Extra credit:
        System.out.println("----------");
        System.out.println("The ways to choose 3 items from 10 with replacement = " + MultiCombinations.c(10, 3));
	}
} // class
 