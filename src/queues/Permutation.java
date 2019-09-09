package queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    // main method
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty())
            randomizedQueue.enqueue(StdIn.readString());
        int n = Integer.parseInt(args[0]);
        while (n-- > 0)
            StdOut.println(randomizedQueue.dequeue());
    }
}