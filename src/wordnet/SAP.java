package wordnet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {

    private final Digraph G;

    // common ancestor
    private class LCA {
        private final int vertices;
        private final BreadthFirstDirectedPaths v, w;
        private int ancestor;
        private int length;

        public LCA(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {
            vertices = G.V();
            this.v = new BreadthFirstDirectedPaths(G, v);
            this.w = new BreadthFirstDirectedPaths(G, w);
            ancestor = length = -1;
            for (int a = 0; a < vertices; a++)
                if (isAncestor(a)) {
                    int tempLength = getLength(a);
                    if (length == -1 || length > tempLength) {
                        length = tempLength;
                        ancestor = a;
                    }
                }
        }

        public int getLength() {
            return length;
        }

        public int getAncestor() {
            return ancestor;
        }

        public boolean isAncestor(int a) {
            return v.hasPathTo(a) && w.hasPathTo(a);
        }

        public int getLength(int a) {
            return v.distTo(a) + w.distTo(a);
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // check valid range
    private void checkValid(int x) {
        if (x < 0 || x >= G.V())
            throw new IllegalArgumentException();
    }

    // check valid range and check null
    private void checkValid(Iterable<Integer> list) {
        if (list == null)
            throw new IllegalArgumentException();
        for (Object x : list) {
            if (x == null)
                throw new IllegalArgumentException();
            checkValid((int) x);
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkValid(v);
        checkValid(w);
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkValid(v);
        checkValid(w);
        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkValid(v);
        checkValid(w);
        return new LCA(G, v, w).getLength();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkValid(v);
        checkValid(w);
        return new LCA(G, v, w).getAncestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}