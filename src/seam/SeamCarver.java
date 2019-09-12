package seam;

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private int width, height;
    private int[][] color;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        width = picture.width();
        height = picture.height();
        color = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                color[i][j] = picture.get(i, j).getRGB();
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                picture.set(i, j, new Color(color[i][j]));
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    private double squareDistX(int x, int y) {
        Color c1 = new Color(color[x - 1][y]);
        Color c2 = new Color(color[x + 1][y]);
        double distR = c1.getRed() - c2.getRed();
        double distG = c1.getGreen() - c2.getGreen();
        double distB = c1.getBlue() - c2.getBlue();
        return distR * distR + distG * distG + distB * distB;
    }

    private double squareDistY(int x, int y) {
        Color c1 = new Color(color[x][y - 1]);
        Color c2 = new Color(color[x][y + 1]);
        double distR = c1.getRed() - c2.getRed();
        double distG = c1.getGreen() - c2.getGreen();
        double distB = c1.getBlue() - c2.getBlue();
        return distR * distR + distG * distG + distB * distB;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000.0;
        return Math.sqrt(squareDistX(x, y) + squareDistY(x, y));
    }

    private double[][] getEnergy(int width, int height, boolean isRotated) {
        double[][] rs = new double[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (isRotated)
                    rs[i][j] = energy(j, i);
                else
                    rs[i][j] = energy(i, j);
        return rs;
    }

    private int getId(int x, int y, int width) {
        return y * width + x;
    }

    private int[] findSeam(double[][] energy) {
        int width = energy.length, height = energy[0].length;
        double[][] dp = new double[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (j == 0)
                    dp[i][j] = 1000.0;
                else
                    dp[i][j] = Double.POSITIVE_INFINITY;
        int[] seam = new int[height];
        int[][] trace = new int[width][height];
        for (int j = 0; j < height - 1; j++)
            for (int i = 0; i < width; i++)
                for (int k = i - 1; k <= i + 1; k++)
                    if (0 <= k && k < width && dp[k][j + 1] > dp[i][j] + energy[k][j + 1]) {
                        dp[k][j + 1] = dp[i][j] + energy[k][j + 1];
                        trace[k][j + 1] = getId(i, j, width);
                    }
        // find minimum of last row
        int id = -1;
        double rs = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width; i++)
            if (rs > dp[i][height - 1]) {
                rs = dp[i][height - 1];
                id = i;
            }
        seam[height - 1] = id;
        // trace back
        for (int i = height - 1; i > 0; i--)
            seam[i - 1] = trace[seam[i]][i] % width;
        return seam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] energy2D = getEnergy(height, width, true);
        return findSeam(energy2D);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy2D = getEnergy(width, height, false);
        return findSeam(energy2D);
    }

    private void checkSeam(int[] seam) {
        for (int i = 1; i < seam.length; ++i) {
            if (Math.abs(seam[i - 1] - seam[i]) > 1)
                throw new IllegalArgumentException();
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width || height <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++)
            if (seam[i] < 0 || seam[i] >= height)
                throw new IllegalArgumentException();
        checkSeam(seam);
        int[][] copy = new int[width][height - 1];
        for (int i = 0; i < width; i++) {
            System.arraycopy(color[i], 0, copy[i], 0, seam[i]);
            System.arraycopy(color[i], seam[i] + 1, copy[i], seam[i], height - seam[i] - 1);
        }
        height--;
        color = copy;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height || width <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++)
            if (seam[i] < 0 || seam[i] >= width)
                throw new IllegalArgumentException();
        checkSeam(seam);
        int[][] copy = new int[width - 1][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (i < seam[j])
                    copy[i][j] = color[i][j];
                else if (i > seam[j])
                    copy[i - 1][j] = color[i][j];
        width--;
        color = copy;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        System.out.println("Unit testing");
    }
}
