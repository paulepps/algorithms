import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Given htmlDoc, target, and htmlString, append the htmlString to the end 
 * of the target's text in the htmlDoc.
 * The target can have three formats:
 * - <x>, where <x> represents the tag;
 * - #<x>, where <x> represents element's id;
 * - .<x>, where <x> represents element's class.
 */

public class AppendHtml {

	public static void main(String[] args) {
		System.out.println(appendHTML("<div></div>", "div", "<p>9+ codefriends online</p>"));
		System.out.println(appendHTML("<div id=\"codefight\"></div>", "#codefight", "<span>New comment on my feed item</span>"));
		System.out.println(appendHTML("<div class=\"challenges codefight\"><h3>FAQ</h3></div><div class=\"codefight\"></div>", 
				".codefight", "<h4>Is it valid HTML5 to use a single tag for a div?</h4>"));
	}
	
	static String appendHTML(String d, String t, String s) {
	    String a = "([^<>\"]|\"[^\"]*\")*";
	    String r;
	    
	    if (t.charAt(0) == '#') {
	    	r = String.format(".* id=\"%s\"", t.replaceAll("^[#\\.]+", ""));
	    } else if (t.charAt(0) == '.') {
	    	r = String.format("%s class=\"([^\"]* )?%s\"", a, t.replaceAll("^[#\\.]+", ""));
	    } else {
	    	r = String.format("<%s%s", t.replaceAll("^[#\\.]+", ""), a);
	    }
//	    System.out.println(r);
	    
	    List<Pair<String, Integer>> list = new ArrayList<>();
	    
	    for (MatchResult match : allMatches(Pattern.compile(r), d.replaceAll("\\\\", ""))) {
//	    	  System.out.println(match.group() + " at " + match.start());
	    	  Pair<String, Integer> pair = new Pair(match.group(), match.start());
	    	  list.add(pair);
	    }
	    
	    Collections.reverse(list);
	    
	    for (Pair<String, Integer> pair : list) {
	        int i = pair.e2 + pair.e1.length();
	        int c = 0;
	        
//	        for (; 0 < 1; i++) {
	        while (true) {
	            if (d.substring(i, i+2).equals("</")) {
	                if (c < 1)
	                    break;
	                c--;
	            } else {
//	                var M = Regex.Match(d.substring(i), "^<\\w" + a + ">").Value;

	                Matcher m = Pattern.compile("^<\\w" + a + ">").matcher(d.substring(i));
	                if (m.find( )) {
	                	String M = m.group();

	                    i += M.length() - 1;
	                    c += M.endsWith("/>") ? 0 : 1;
	                }
	            }
	            i++;
	        }
	        d = d.substring(0, i) + s.replaceAll("\\\\", "") + d.substring(i+1);
	    }
	    return d;
	}

	public static Iterable<MatchResult> allMatches(
		      final Pattern p, final CharSequence input) {
		  return new Iterable<MatchResult>() {
		    public Iterator<MatchResult> iterator() {
		      return new Iterator<MatchResult>() {
		        // Use a matcher internally.
		        final Matcher matcher = p.matcher(input);
		        // Keep a match around that supports any interleaving of hasNext/next calls.
		        MatchResult pending;

		        public boolean hasNext() {
		          // Lazily fill pending, and avoid calling find() multiple times if the
		          // clients call hasNext() repeatedly before sampling via next().
		          if (pending == null && matcher.find()) {
		            pending = matcher.toMatchResult();
		          }
		          return pending != null;
		        }

		        public MatchResult next() {
		          // Fill pending if necessary (as when clients call next() without
		          // checking hasNext()), throw if not possible.
		          if (!hasNext()) { throw new NoSuchElementException(); }
		          // Consume pending so next call to hasNext() does a find().
		          MatchResult next = pending;
		          pending = null;
		          return next;
		        }

		        /** Required to satisfy the interface, but unsupported. */
		        public void remove() { throw new UnsupportedOperationException(); }
		      };
		    }
		  };
		}}
