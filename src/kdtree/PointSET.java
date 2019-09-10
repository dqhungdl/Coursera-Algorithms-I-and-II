package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private final Set<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        pointSet.add(new Point2D(p.x(), p.y()));
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setScale(0, 1);
        StdDraw.setPenRadius(0.02);
        for (Point2D point2D : pointSet)
            point2D.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> pointInRect = new ArrayList<>();
        for (Point2D point2D : pointSet)
            if (rect.contains(point2D))
                pointInRect.add(new Point2D(point2D.x(), point2D.y()));
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return pointInRect.iterator();
            }
        };
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        double minDist = -1;
        Point2D nearestPoint = null;
        for (Point2D point2D : pointSet) {
            double dist = p.distanceSquaredTo(point2D);
            if (minDist == -1 || minDist > dist) {
                minDist = dist;
                nearestPoint = point2D;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        System.out.println("Run KdTreeVisualizer instead");
    }
}