import java.io.* ;
import java.util.* ;

public class GCDOfAllSubarrays {
	public static void main(String[] args) throws IOException{
		Solver Machine = new Solver() ;
		Machine.Solve() ;
	}	
}

class Mod{
	static long mod=1000000007 ;
	static long d(long a,long b){ return (a*ModInverse(b))%mod ; }
	static long m(long a , long b){ return (a*b)%mod ; }
	static private long ModInverse(long a ){ return pow(a,mod-2) ; }
	static long pow(long a,long b){
		long val=a ; long ans=1 ;
		while(b!=0){
			if((b&1)==1) ans = m(ans,val) ; 
			val = m(val,val) ; 
			b>>=1 ;
		}
		return ans ;
	}	
}

class pair implements Comparable<pair>{
	long x ; int y ;
	pair(long a,int b){ x=a ; y=b ; }
	public int compareTo(pair p){
		return (this.x<p.x ? -1 : (this.x>p.x ? 1 : (this.y<p.y ? -1 : (this.y>p.y ? 1 : 0)))) ; 
	}
}

class Solver{

	void Solve() throws IOException{
		long a[] = {1, 3, 6};
		
	    TreeMap<Long,Long> map = new TreeMap<Long,Long>() ;
	    TreeMap<Long,Long> suff = new TreeMap<Long,Long>() ;

	    for(long elem : a){
	    	TreeMap<Long,Long> cur_suff = new TreeMap<Long,Long>() ;
	    	Set<Long> vals = suff.keySet() ;
	    	for(long val : vals){ 
	    		long curGCD = gcd(val,elem) ; 
	    		cur_suff.put(curGCD,cur_suff.getOrDefault(curGCD,0l)+suff.getOrDefault(val,1l));
	    	}
	    	cur_suff.put(elem,cur_suff.getOrDefault(elem,0l)+1) ;
	    	vals = cur_suff.keySet() ;
	    	for(long val : vals) map.put(val,map.getOrDefault(val,0l)+cur_suff.get(val)) ;
	    	suff = cur_suff ;
	    }
	    
	    // map will map array value to the frequency with which it occurs as 
	    // GCD of a subarray
	    Set<Long> keyval = map.keySet() ;
	    
	    // this might help processing results
	    long keys[] = new long[keyval.size()+1] ;
	    long psum[] = new long[keyval.size()+1] ;
	    int idx=1 ;
	    for(long key : keyval){
	    	psum[idx]=psum[idx-1]+map.get(key) ;
	    	keys[idx++]=key ;
	    }
	} 
	long gcd(long a,long b){
		while(a%b!=0){long c=b ; b=a%b ; a=c ;}
		return b ;
	}
}
@SuppressWarnings("serial")
class mylist extends ArrayList<Integer>{
	
}
@SuppressWarnings("serial")
class MySet extends TreeSet<pair>{
}
