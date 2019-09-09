package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n, topEdge, botEdge;
    private int cntOpened;
    private final WeightedQuickUnionUF topDSU, normalDSU;
    private final boolean[] isOpened;

    // creates n-by-n dsu, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        this.n = n;
        this.topEdge = 0;
        this.botEdge = n * n + 1;
        this.cntOpened = 0;
        isOpened = new boolean[n * n + 2];
        isOpened[topEdge] = isOpened[botEdge] = true;
        topDSU = new WeightedQuickUnionUF(n * n + 1);
        normalDSU = new WeightedQuickUnionUF(n * n + 2);
    }

    // convert 2-d index to 1-d index
    private int getId(int row, int col) {
        return (row - 1) * n + col;
    }

    // check opened site and union
    private void tryUnion(int id1, int id2) {
        if (isOpened[id2]) {
            topDSU.union(id1, id2);
            normalDSU.union(id1, id2);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        if (isOpen(row, col))
            return;
        isOpened[getId(row, col)] = true;
        cntOpened++;
        // Check top and bottom
        if (row == 1)
            tryUnion(getId(row, col), topEdge);
        if (row == n)
            normalDSU.union(getId(row, col), botEdge);
        // Check adjacent
        if (row > 1)
            tryUnion(getId(row, col), getId(row - 1, col));
        if (row < n)
            tryUnion(getId(row, col), getId(row + 1, col));
        if (col > 1)
            tryUnion(getId(row, col), getId(row, col - 1));
        if (col < n)
            tryUnion(getId(row, col), getId(row, col + 1));
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        return isOpened[getId(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        return topDSU.connected(topEdge, getId(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return cntOpened;
    }

    // does the system percolate?
    public boolean percolates() {
        return normalDSU.connected(topEdge, botEdge);
    }
}