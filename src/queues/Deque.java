package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    // node object
    private class Node {
        private Item item;
        private Node prev, next;

        private Node(Item item) {
            this.item = item;
            this.prev = this.next = null;
        }

        private Item getItem() {
            return item;
        }

        private Node getPrev() {
            return prev;
        }

        private void setPrev(Node prev) {
            this.prev = prev;
        }

        private Node getNext() {
            return next;
        }

        private void setNext(Node next) {
            this.next = next;
        }
    }

    // iterator implements
    private class DequeIterator implements Iterator<Item> {

        private Node currentPos = head;

        public boolean hasNext() {
            return currentPos != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = currentPos.getItem();
            currentPos = currentPos.getNext();
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private int dequeSize;
    private Node head, tail;

    // construct an empty deque
    public Deque() {
        dequeSize = 0;
        head = tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return dequeSize == 0;
    }

    // return the number of items on the deque
    public int size() {
        return dequeSize;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size() == 0)
            head = tail = new Node(item);
        else {
            Node newNode = new Node(item);
            head.setPrev(newNode);
            newNode.setNext(head);
            head = head.getPrev();
        }
        dequeSize++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size() == 0)
            head = tail = new Node(item);
        else {
            Node newNode = new Node(item);
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = tail.getNext();
        }
        dequeSize++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = head.getItem();
        if (size() == 1)
            head = tail = null;
        else {
            head = head.getNext();
            head.getPrev().setNext(null);
            head.setPrev(null);
        }
        dequeSize--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = tail.getItem();
        if (size() == 1)
            head = tail = null;
        else {
            tail = tail.getPrev();
            tail.getNext().setPrev(null);
            tail.setNext(null);
        }
        dequeSize--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        System.out.println("Run Permutation instead");
    }
}