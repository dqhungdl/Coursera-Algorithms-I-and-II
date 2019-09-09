package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FastCollinearPoints {

    private static final double EPS = 1e-9;

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // check null pointer exception
    private boolean isNullPointer(Point[] points) {
        if (points == null)
            return true;
        for (Point point : points)
            if (point == null)
                return true;
        return false;
    }

    // check duplicate exception
    private boolean isDuplicate(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    return true;
        }
        return false;
    }

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] tempPoints) {
        if (isNullPointer(tempPoints) || isDuplicate(tempPoints))
            throw new IllegalArgumentException();
        Point[] points = Arrays.copyOf(tempPoints, tempPoints.length);
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(points, tempPoints[i].slopeOrder());
            int preSlopeId = 0;
            for (int j = 1; j < points.length; j++) {
                double slope1 = tempPoints[i].slopeTo(points[j - 1]);
                double slope2 = tempPoints[i].slopeTo(points[j]);
                if (Math.abs(slope1 - slope2) < EPS)
                    continue;
                if (slope1 == Double.POSITIVE_INFINITY && slope2 == Double.POSITIVE_INFINITY)
                    continue;
                if (j - preSlopeId >= 3) {
                    Point minPoint = tempPoints[i], maxPoint = tempPoints[i];
                    for (int t = preSlopeId; t < j; t++) {
                        if (minPoint.compareTo(points[t]) > 0)
                            minPoint = points[t];
                        if (maxPoint.compareTo(points[t]) < 0)
                            maxPoint = points[t];
                    }
                    if (tempPoints[i].equals(minPoint))
                        lineSegments.add(new LineSegment(tempPoints[i], maxPoint));
                }
                preSlopeId = j;
            }
            if (points.length - preSlopeId >= 3) {
                Point minPoint = tempPoints[i], maxPoint = tempPoints[i];
                for (int t = preSlopeId; t < points.length; t++) {
                    if (minPoint.compareTo(points[t]) > 0)
                        minPoint = points[t];
                    if (maxPoint.compareTo(points[t]) < 0)
                        maxPoint = points[t];
                }
                if (tempPoints[i].equals(minPoint))
                    lineSegments.add(new LineSegment(tempPoints[i], maxPoint));
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[numberOfSegments()]);
    }

    // test unit (optional)
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt(), y = scanner.nextInt();
            points[i] = new Point(x, y);
        }
        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        System.out.println("Brute collinear points");
        System.out.println(bruteCollinearPoints.numberOfSegments());
        LineSegment[] lineSegments1 = bruteCollinearPoints.segments();
        for (int i = 0; i < lineSegments1.length; i++)
            System.out.println(lineSegments1[i].toString());
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        System.out.println("Fast collinear points");
        System.out.println(fastCollinearPoints.numberOfSegments());
        LineSegment[] lineSegments2 = fastCollinearPoints.segments();
        for (int i = 0; i < lineSegments2.length; i++)
            System.out.println(lineSegments2[i].toString());
    }
}
