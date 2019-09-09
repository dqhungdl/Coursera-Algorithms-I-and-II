package queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int queueSize;

    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queueSize = 0;
        items = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return queueSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return queueSize;
    }

    // resize items array
    private void resize(int length) {
        Item[] tempItems = (Item[]) new Object[length];
        for (int i = 0; i < size(); i++)
            tempItems[i] = items[i];
        items = tempItems;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size() == items.length)
            resize(2 * size());
        items[queueSize++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        int index = StdRandom.uniform(size());
        Item item = items[index];
        items[index] = items[size() - 1];
        items[--queueSize] = null;
        if (size() > 0 && size() == items.length / 4)
            resize(items.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        return items[StdRandom.uniform(size())];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // iterator implements
    private class RandomizedQueueIterator implements Iterator<Item> {

        private int currentPos;
        private Item[] tempItems;

        public RandomizedQueueIterator() {
            currentPos = 0;
            tempItems = (Item[]) new Object[queueSize];
            for (int i = 0; i < size(); i++)
                tempItems[i] = items[i];
            StdRandom.shuffle(tempItems);
        }

        public boolean hasNext() {
            return currentPos < size();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return tempItems[currentPos++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        System.out.println("Run Permutation instead");
    }
}