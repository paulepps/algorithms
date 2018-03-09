/*
ID: paul9
LANG: JAVA
TASK: heritage
 */
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//public class PrintPostOrderFromPreOrderInOrder
class heritage 
{	
	public static void main(String[] args) throws IOException 
	{
		long start = System.currentTimeMillis();
		Scanner sc = new Scanner(new File("heritage.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("heritage.out")));

		String s = sc.next();
		String[] inOrder = s.split("");
		
		s = sc.next();
		String[] preOrder = s.split("");
		
//        PrintPostOrderFromPreOrderInOrder ppp = new PrintPostOrderFromPreOrderInOrder();
        String postorder[] = postOrder(preOrder, inOrder);
        for(String c : postorder) {
            out.print(c);
        }	
        out.println();
        
        sc.close();
		out.close();
		System.out.println("$:"+(System.currentTimeMillis()-start));
	}


    static String[] postOrder(String[] preorder, String[] inorder) {
        String[] post = new String[inorder.length];
        AtomicInteger postIndex = new AtomicInteger(post.length - 1);
        postOrder(preorder, inorder, post, 0, inorder.length -1, 0, postIndex);
        return post;
    }
    
    static void postOrder(String[] preorder, String[] inorder, String post[], int low, int high, int preIndex, AtomicInteger postIndex) {
        if(low > high) {
            return;
        }
        post[postIndex.getAndDecrement()] = preorder[preIndex];
        int i;
        for(i=0; i < inorder.length; i++) {
            if(preorder[preIndex].equals(inorder[i])) {
                break;
            }
        }
        postOrder(preorder, inorder, post, i+1, high, preIndex + (i - low) + 1, postIndex);
        postOrder(preorder, inorder, post, low, i-1, preIndex + 1, postIndex);
    }
}