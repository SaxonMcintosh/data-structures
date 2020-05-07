/**
 * Implementing the CircularLinkedList class to solve a variant of the
 * Josephus problem.
 *
 * @author Saxon McIntosh
 * @version February 3, 2020
 */

import java.util.Iterator;

public class CircularLinkedList<E> implements Iterable<E> {
    Node<E> head;
    Node<E> tail;
    int size;

    /**
     * A constructor for the CircularLinkedList class.
     */
    
    public CircularLinkedList() {
        size = 0;
    }

    /**
     * Retrieves a node object.
     *
     * @param the index of the desired Node
     * @return a node object
     */
    public Node<E> getNode(int index ) {
	// This exception throws if the index out of bounds for the
	// list.
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(Integer.toString(index + 1));
        }
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    /**
     * Adds an item to the end of the list. Gets handled by the
     * 2 argument add method.
     *
     * @param the content of the new node
     */
    public void add(E item) {
        add(size, item);
    }

    /**
     * This add method does a lot of heavy lifting. While implementing
     * this I considered making a double-linked circular list, but
     * considering how the list items are referenced by index anyway
     * there didn't seem to be a point so there is no prev variable.
     *
     * @param where to insert the new node
     * @param the content of the new node
     */
    public void add(int index, E item) {
	// Handling cases wherein the node will be put first.
        if (index == 0) {
            if (size == 0) {
		// The new node is not assigned a next because it is the
		// only item in the list.
                head = new Node<>(item);
            } else {
                Node<E> node = new Node<>(item);
		// Assigns the new node the next value of the previous
		// head, then makes the new node the current head.
                node.next = head;
                head = node;
            }
        } else {
            Node<E> newNode = new Node<>(item);
	    // This pulls the node that will precede the new one, then
	    // assigns next values based on that relationship. Also
	    // responsible for redirecting out of bounds indices.
            Node<E> node = getNode(index - 1);
            newNode.next = node.next;
            node.next = newNode;
        }
        size++;
        if (index == size - 1) {
	    // The tail must be reassigned if a node is added to the end
	    // of the list.
            tail = getNode(index);
        }
        tail.next = head;
    }

    /**
     * Removes the node at the index.
     *
     * @param the index of the node to be removed
     * @return the data from the removed node
     */
    public E remove(int index) {
        // This if statement increments the index size if it's just
	// past the last list element so that I don't have to make
	// another exception statement, as it will now trigger the
	// one in getNode().
        if (index == size) {
            index++;
        }
	// toBeRemoved holds the item so that it can be returned,
	// whereas node is there to help reassign next values.
        Node<E> node;
        Node<E> toBeRemoved;
        if (index == 0 && size > 0) {
	    // For when there's only one item in the list.
            if (size == 1) {
                head = null;
                tail = null;
                size--;
                return null;
            }
            toBeRemoved = head;
            head = head.next;
            tail.next = head;
            size--;
            return toBeRemoved.item;
        }
	// getNode handles the out of bounds indices here again.
        node = getNode(index - 1);
        toBeRemoved = node.next;
        node.next = node.next.next;
        size--;
        return toBeRemoved.item;
    }

    /**
     * Overrides Object.toString() with a more relevant function.
     *
     * @return the current list plus arrows
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (size == 0) {
            result.append("The list is empty.");
        } else {
            Node<E> node = head;
	    // This is appended first in case there's only one item in
	    // the list, as the head would be self referencing.
            result.append(head.item + "==>");
            while (node.next != head) {
                result.append(node.next.item + "==>");
                node = node.next;
            }
        }
        return result.toString();
    }

    /**
     * Implements a new iterator.
     *
     * @return an iterator of sub ListIterator<E>
     */
    public Iterator<E> iterator() {
        return new ListIterator<E>();
    }

    /**
     * The ListIterator class.
     */
    private class ListIterator<E> implements Iterator<E> {
        Node<E> nextItem;
        int index;

        @SuppressWarnings("unchecked")
	/**
	 * Constructor for ListIterator that puts it at the head of a
	 * list.
	 */
        public ListIterator() {
            nextItem = (Node<E>) head;
            index = 0;
        }

	/**
	 * Checks to see whether or not there is a next value. Hopefully
	 * redundant in a circular list.
	 *
	 * @return boolean based on presence of null value
	 */
        public boolean hasNext() {
            return nextItem.next != null;
        }

	/**
	 * Advances the iterator one node.
	 *
	 * @return returns value of current node
	 */
        public E next() {
            if (nextItem == head) {
                index = 0;
            } else {
                index++;
            }
	    // temp stores the value to be returned.
            Node<E> temp = nextItem;
            nextItem = nextItem.next;
            return temp.item;
        }

	/**
	 * Removes the node that was just passed using next.
	 */
        public void remove() {
	    // Index must be used in order to take advantage of
	    // CircularLinkedList's remove() function.
            CircularLinkedList.this.remove(index);
            index--;
        }
    }

    /**
     * The node class.
     */
    public static class Node<E>{
        E item;
        Node<E> next;

	/**
	 * Node constructor.
	 *
	 * @param value with which the node will be initialized
	 */
        public Node(E item) {
            this.item = item;
        }

    }

    public static void main(String args[]) {
        int numOfSoldier = 13;
        int count = 2;

        CircularLinkedList list = new CircularLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(6);
        System.out.println(list);
        list.add(4, 5);
        System.out.println(list);
        list.add(0, 9);
        System.out.println(list);

        list.remove(0);
        System.out.println("List after removing the head:");
        System.out.println(list);

        list.remove(5);
        System.out.println("List after removing the tail:");
        System.out.println(list);

        list.remove(2);
        System.out.println("List after removing one item in the middle:");
        System.out.println(list);

        System.out.println("Item at index 3: " + list.getNode(3).item);

        System.out.println("\nOutput for game:");

        list = new CircularLinkedList();
        for (int i = 1; i <= numOfSoldier; i++) {
            list.add(i);
        }
        System.out.println(list);

        Iterator iterator = list.iterator();

	// Since the list is circular, evaluating iterator.hasNext() is
	// needless but I wanted to be thorough.
        while (list.size > count && iterator.hasNext()) {
            iterator.next();
            iterator.next();
            iterator.remove();

            System.out.println(list);
        }
    }
}

