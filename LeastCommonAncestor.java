

import java.util.Scanner;

/*
 * Input
 * 
 * The input consists of T test cases. The number of test cases (T) is given 
 * in the first line of the input file. Each test case starts with a line 
 * containing an integer N , the number of nodes in a tree, 2<=N<=10,000. 
 * The nodes are labeled with integers 1, 2,..., N. Each of the next N -1 
 * lines contains a pair of integers that represent an edge --the first 
 * integer is the parent node of the second integer. Note that a tree with 
 * N nodes has exactly N - 1 edges. The last line of each test case contains 
 * two distinct integers whose nearest common ancestor is to be computed.
 * 
 * Output
 * 
 * Print exactly one line for each test case. The line should contain the 
 * integer that is the nearest common ancestor.
 * 
 * Sample Input
 * 
 * 
 * 2
 * 16
 * 1 14
 * 8 5
 * 10 16
 * 5 9
 * 4 6
 * 8 4
 * 4 10
 * 1 13
 * 6 15
 * 10 11
 * 6 7
 * 10 2
 * 16 3
 * 8 1
 * 16 12
 * 16 7
 * 5
 * 2 3
 * 3 4
 * 3 1
 * 1 5
 * 3 5
 * 
 * Sample Output
 * 
 * 4
 * 3
 * 
 */

public class LeastCommonAncestor {

	static final int MAX = 10001;

	static Node[] hash = new Node[MAX];
	static Node[] Qes = new Node[MAX];

	static int[] f = new int[MAX];
	static int[] r = new int[MAX];
	static int[] indegree = new int[MAX];
	static int[] visit = new int[MAX];
	static int[] ancestor = new int[MAX];

	static void Add(Node hash[], int a, int b)
	{

	    Node p = hash[a];
	    Node q = p.next;

	    Node r = new Node();
	    r.a = b;
	    p.next = r;
	    r.next = q;
	}

	static void init(int n) {
		int i;
		for (i = 1; i <= n; i++) {
			r[i] = 1;
			f[i] = i;
			indegree[i] = 0;
			visit[i] = 0;
			ancestor[i] = 0;
			hash[i] = new Node();
			hash[i].next = null;
			Qes[i] = new Node();
			Qes[i].next = null;
		}
	}

	static int find(int n) {
		if (f[n] == n)
			return n;
		else
			f[n] = find(f[n]);
		return f[n];
	}

	static int Union(int x, int y) {
		int a = find(x);
		int b = find(y);
		if (a == b)
			return 0;
		else if (r[a] <= r[b]) {
			f[a] = b;
			r[b] += r[a];
		} else {
			f[b] = a;
			r[a] += r[b];
		}
		return 1;
	}

	static void LCA(int u)
	{
	    ancestor[u]=u;
	    Node p;
	    for(p=hash[u].next;p!=null;p=p.next)
	    {
	        LCA(p.a);
	        Union(u,p.a);
	        ancestor[find(u)]=u;
	    }
	    visit[u]=1;
	    for(p=Qes[u].next;p!=null;p=p.next)
	    {
	        if(visit[p.a]==1)
	        {
	            System.out.println(ancestor[find(p.a)]);
	            return;
	        }
	    }
	}

	public static void main(String[] args) {
	    int testcase;
	    int n;
	    int i,j;

	    Scanner sc = new Scanner(System.in);
	    
	    testcase = sc.nextInt();
	    for(i=1;i<=testcase;i++)
	    {
	        n = sc.nextInt();
	        init(n);
	        int s,t;
	        for(j=1;j<=n-1;j++)
	        {

	        	s = sc.nextInt();
	        	t = sc.nextInt();

	            Add(hash,s,t);
	            indegree[t]++;
	        }
        	s = sc.nextInt();
        	t = sc.nextInt();

        	Add(Qes,s,t);
	        Add(Qes,t,s);
	        for(j=1;j<=n;j++)
	        {
	            if(indegree[j]==0)
	            {
	                LCA(j);
	                break;
	            }
	        }
	    }
	}
}

class Node {
	int a;
	Node next;
}
