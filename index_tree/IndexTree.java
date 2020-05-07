/**
 * @author Saxon McIntosh
 * @version March 12, 2020
 *
 * This program utilizes a unique binary tree to create an index of
 * every word found in text file containing the compiled works of
 * Shakespeare by line.
 */

import java.io.*;
import java.util.*;

public class IndexTree {
    private IndexNode root;

    private IndexTree() {
	root = null;
    }

    /**
     * A public add method wrapper for the index.
     *
     * @param word the word to be added to the index
     * @param lineNumber the index of the line that the word is from
     */
    public void add(String word, int lineNumber){
	// occurence is introduced to provide of means of evaluating
	// whether or not the word has appeared multiple times on the
	// same line.
	int occurence = -1;
	IndexNode toAdd = search(root, word, false);
	if (toAdd != null) {
	    occurence = toAdd.occurences;
	}
	// Here the wrapper method calls the private one.
	add(root, word, lineNumber, occurence);
    }

    /**
     * The recursive add method. If the word is not in the index, it is
     * added. If it is already there, then its count is incremented
     * and the new line address is appended to its list.
     *
     * @param root the root of the entire tree
     * @param word the word to be added to the index
     * @param lineNumber the index of the line that the word is from
     * @param occurence the number of times the word has been counted
     * thus far
     *
     * @return the address of the resulting node
     */
    private IndexNode add(IndexNode root, String word, int lineNumber,
			  int occurence) {
	// In the event that the list is empty, the new word becomes the
	// root.
	if (root == null) {
	    this.root = new IndexNode(word, lineNumber);
	    return root;
	}

	// value will be used for control flow related to alphabetical
	// order.
	int value = word.compareTo(root.word);

	// If 'word' comes first alphabetically, the left branch is
	// taken.
	if (value < 0) {
	    // Once the bottom-most branch is reached, the new node
	    // is added as a leaf there.
	    if (root.left == null) {
		root.left = new IndexNode(word, lineNumber);
	    }
	    return add(root.left, word, lineNumber, occurence);
	}

	// This is the same thing but for the right branches.
	if (value > 0) {
	    if (root.right == null) {
		root.right = new IndexNode(word, lineNumber);
	    }
	    return add(root.right, word, lineNumber, occurence);
	}

	// If the current IndexNode has already been introduced, its
	// occurences variable is incremented, but only from the
	// bottom-most context.
	if (root.occurences == occurence) {
	    root.list.add(lineNumber);
	    root.occurences++;
	}
	
	return root;
    }
	
    /**
     * contains checks to see if the index contains a specified word.
     *
     * @param word the word to be detected
     *
     * @return the boolean value of the word's status
     */
    public boolean contains(String word){
	IndexNode current = root;
	while (current != null) {
	    if (word.compareTo(current.word) < 0) {
		current = current.left;
	    } else if (word.compareTo(current.word) > 0) {
		current = current.right;
	    } else {
		return true;
	    }
	}
	return false;
    }
	
    /**
     * This is the wrapper method for the delete function.
     *
     * @param word the word to be deleted
     */
    public void delete(String word){
	delete(root, word);
    }
	
    /**
     * The recursive version of the delete function is also an private
     * method, as it must have access to the private root IndexNode. It
     * removes the word argument from the index if it is there.
     *
     * @param root the root node of the index
     * @param word the word to be deleted
     *
     * @return the deleted IndexNode
     */
    private IndexNode delete(IndexNode root, String word){
	// If the word does not exist in the index, a message is printed
	// and the method is exited.
	if (!this.contains(word)) {
	    System.out.println("Index does not contain " + word);
	    return root;
	}

	// Like in the other methods, value will be used for control
	// flow.
	int value = word.compareTo(root.word);
	// If the root that is ultimately selected is also the node that
	// contains word, I will no longer have access to the parent
	// node to assign a new value to left or right, hence the second
	// half of this && statement.
	if (value < 0 && !word.equals(root.left.word)) {
	    return delete(root.left, word);
	}
	if (value > 0 && !word.equals(root.right.word)) {
	    return delete(root.right, word);
	}

	// value could only equal zero at this point in the event that
	// the word which was selected was the this.root, so the value
	// of the argument 'root' could never be reassigned. Therefore,
	// the next block is for reassigning the root in this scenario.
	if (value == 0) {
	    if (root.left != null) {
		// current will eventually be the value that branches
		// right to link the rest of the tree in the event that
		// a left branch exists.
		IndexNode current = root.left;
		// newRoot saves the value of the left branch of the old
		// root to be the new root eventually.
		IndexNode newRoot = current;
		while (current.right != null) {
		    current = current.right;
		}
		current.right = root.right;
		this.root = newRoot;
		return root;
	    }
	    if (root.right != null) {
		// If a left branch of root doesn't exist, there's no
		// other work to be done than to assign this.root to the
		// right branch.
		this.root = root.right;
		return root;
	    }
	    // If root has no children, a new root need not be assigned.
	    this.root = null;
	    return root;
	}

	// current is assigned the value of the node that contains word.
	IndexNode current = new IndexNode("", 0);
	if (value < 0) {
	    current = root.left;
	} else {
	    current = root.right;
	}
	// child is the IndexNode that will ultimately become the child
	// of either root or the right-most value of current.
	IndexNode child = current.right;
	// The left-branching algorithm:
	if (current.left != null) {
	    current = current.left;
	    if (value < 0) {
		root.left = current;
	    } else {
		root.right = current;
	    }
	    while (current.right != null) {
		current = current.right;
	    }
	    current.right = child;
	} else {
	    // The much simpler right-branching one:
	    if (value < 0) {
		root.left = child;
	    } else {
		root.right = child;
	    }
	}
	return root;
    }
	
    /**
     * This is a wrapper for the private method of printIndex.
     */
    public void printIndex() {
	printIndex(root);
    }

    /**
     * printIndex prints the whole index through inorder traversal,
     * with the addition of occurences and line numbers as specified
     * by IndexNode's override of toString.
     *
     * @param node the IndexNode that functions as the root of this
     * method
     */
    private void printIndex(IndexNode node){
	if (node == null) {
	    return;
	}
	printIndex(node.left);
	System.out.println(node);
	printIndex(node.right);
    }

    /**
     * The wrapper for the private search method.
     *
     * @param word the word to be searched for
     *
     * @return the IndexNode if it is found.
     */
    public IndexNode search(String word) {
	return search(root, word, true);
    }

    /**
     * search functions similarly to contains, except it returns the
     * node itself if it is found instead of a boolean.
     *
     * @param root the root of the index
     * @param word the word to be searched for
     * @param tog a toggle that is used to determine if a message should
     * be printed to the console
     *
     * @return the address of the desired IndexNode
     */
    private IndexNode search(IndexNode root, String word, boolean tog) {
	IndexNode current = root;
	while (current != null) {
	    if (word.compareTo(current.word) < 0) {
		current = current.left;
	    } else if (word.compareTo(current.word) > 0) {
		current = current.right;
	    } else {
		break;
	    }
	}
	
	if (tog == true) {
	    if (current == null) {
		System.out.println("Sorry, the index does not contain the word \"" + word + "\".");
	    } else {
		System.out.println(current);
	    }
	}
	return current;
    }

    public int getSize() {
	return getSize(root);
    }
    
    private int getSize(IndexNode root) {
	if (root == null) {
	    return 0;
	} else {
	    return getSize(root.left) + getSize(root.right) + 1;
	}
    }
	
    public static void main(String[] args)
	throws FileNotFoundException {

	/*
	IndexTree index = new IndexTree();
	String alph = "abcdefghijklmnopqrstuvwxyz";
	StringBuilder builder = new StringBuilder();
	Scanner sc = new Scanner(new File("shakespeare.txt"));

	int lineCount = 0;
	String line = "";
	String current = "";
        while (sc.hasNextLine()) {
	    line = sc.nextLine();

	    // This is my algorithm for determining where the words are
	    // in the text. I have no idea what unusual chars or
	    // formatting this text will contain, so the safest thing is
	    // to say that a word is a unit composed of contiguous
	    // alphabetical characters. Unfortunately this rules out
	    // anything that contains a hyphen, but nothing is perfect.
	    for (int i = 0; i < line.length(); i++) {
		current = Character.toString(line.charAt(i));
		// Character names will also end up being lowercase.
		// Whoops.
		current = current.toLowerCase();
		if (alph.contains(current)) {
		    builder.append(current);
		} else {
		    if (builder.toString().length() > 0) {
			index.add(builder.toString(), lineCount);
		    }
		    builder.setLength(0);
		}
	    }
	    lineCount++;
	}

	index.printIndex();
	System.out.println();

	index.delete("zounds");
	index.search("zounds");
	System.out.println();
	
	index.search("forsooth");
	*/

	IndexTree index = new IndexTree();
	index.add("x", 0);
	index.add("l", 0);
	index.add("p", 0);
	index.add("a", 0);
	index.add("e", 0);

	index.printIndex();
	System.out.println(index.getSize());
    }
}
