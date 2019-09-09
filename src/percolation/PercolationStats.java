package percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double RATIO = 1.96;
    private double mean, stddev;
    private final double[] fractions;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        mean = stddev = -1;
        fractions = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!percolation.isOpen(row, col))
                    percolation.open(row, col);
            }
            fractions[i] = (double) percolation.numberOfOpenSites() / (double) (n * n);
        }
    }

    // calculate mean of percolation threshold
    public double mean() {
        if (mean == -1)
            mean = StdStats.mean(fractions);
        return mean;
    }

    // calculate standard deviation of percolation threshold
    public double stddev() {
        if (stddev == -1)
            stddev = StdStats.stddev(fractions);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - RATIO * stddev() / Math.sqrt(fractions.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + RATIO * stddev() / Math.sqrt(fractions.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}