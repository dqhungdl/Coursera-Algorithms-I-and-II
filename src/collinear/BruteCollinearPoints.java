package collinear;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

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

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] tempPoints) {
        if (isNullPointer(tempPoints) || isDuplicate(tempPoints))
            throw new IllegalArgumentException();
        Point[] points = Arrays.copyOf(tempPoints, tempPoints.length);
        Arrays.sort(points);
        for (int a = 0; a < points.length; a++)
            for (int b = a + 1; b < points.length; b++)
                for (int c = b + 1; c < points.length; c++)
                    for (int d = c + 1; d < points.length; d++) {
                        double slope1 = points[a].slopeTo(points[b]);
                        double slope2 = points[a].slopeTo(points[c]);
                        double slope3 = points[a].slopeTo(points[d]);
                        if (Math.abs(slope1 - slope2) < EPS && Math.abs(slope1 - slope3) < EPS)
                            lineSegments.add(new LineSegment(points[a], points[d]));
                        else if (slope1 == Double.POSITIVE_INFINITY && slope2 == Double.POSITIVE_INFINITY && slope3 == Double.POSITIVE_INFINITY)
                            lineSegments.add(new LineSegment(points[a], points[d]));
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
}
