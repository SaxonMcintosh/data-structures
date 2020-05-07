import java.util.*;

public class IndexNode  {

    String word;
    int occurences;
    List<Integer> list;
	
    IndexNode left;
    IndexNode right;

    public IndexNode(String word, int lineNumber) {
	this.word = word;
	list = new ArrayList<Integer>();
	list.add(lineNumber);
	occurences = 1;
    }	
	
    public String toString(){
	return word + ", " + occurences + " on line(s):" + list;
    }
}
