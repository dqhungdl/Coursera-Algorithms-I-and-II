package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KdTree {

    private static final RectHV BORDER_RECT = new RectHV(0, 0, 1, 1);

    private class Node {

        private final double x, y;

        private Node left, right;

        private final boolean isVertical;

        public Node(double x, double y, Node left, Node right, boolean isVertical) {
            this.x = x;
            this.y = y;
            this.left = left;
            this.right = right;
            this.isVertical = isVertical;
        }
    }

    private int treeSize = 0;

    private Node root = null;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return treeSize == 0;
    }

    // number of points in the set
    public int size() {
        return treeSize;
    }

    // insert implements
    private Node dfsInsert(Node node, Point2D point, boolean isVertical) {
        if (node == null) {
            treeSize++;
            return new Node(point.x(), point.y(), null, null, isVertical);
        }
        if (node.x == point.x() && node.y == point.y())
            return node;
        if ((isVertical && point.x() <= node.x) || (!isVertical && point.y() <= node.y))
            node.left = dfsInsert(node.left, point, !isVertical);
        else
            node.right = dfsInsert(node.right, point, !isVertical);
        return node;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = dfsInsert(root, p, true);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Node node = root;
        while (node != null) {
            if (node.x == p.x() && node.y == p.y())
                return true;
            if ((node.isVertical && p.x() <= node.x) || (!node.isVertical && p.y() <= node.y))
                node = node.left;
            else
                node = node.right;
        }
        return false;
    }

    // get left child rectangle
    private RectHV getLeftRect(Node node, RectHV rect) {
        if (node.isVertical)
            return new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());
        return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);
    }

    // get right child rectangle
    private RectHV getRightRect(Node node, RectHV rect) {
        if (node.isVertical)
            return new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());
        return new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());
    }

    // draw implements
    private void dfsDraw(Node node, RectHV rect) {
        if (node == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        new Point2D(node.x, node.y).draw();
        StdDraw.setPenRadius();
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            new Point2D(node.x, rect.ymin()).drawTo(new Point2D(node.x, rect.ymax()));
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            new Point2D(rect.xmin(), node.y).drawTo(new Point2D(rect.xmax(), node.y));
        }
        dfsDraw(node.left, getLeftRect(node, rect));
        dfsDraw(node.right, getRightRect(node, rect));
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setScale(0, 1);
        dfsDraw(root, BORDER_RECT);
    }

    // range implements
    private void dfsRange(Node node, RectHV border, RectHV rect, List<Point2D> pointInRect) {
        if (node == null)
            return;
        if (rect.intersects(border)) {
            Point2D point = new Point2D(node.x, node.y);
            if (rect.contains(point))
                pointInRect.add(point);
            dfsRange(node.left, getLeftRect(node, border), rect, pointInRect);
            dfsRange(node.right, getRightRect(node, border), rect, pointInRect);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> pointInRect = new ArrayList<>();
        dfsRange(root, BORDER_RECT, rect, pointInRect);
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return pointInRect.iterator();
            }
        };
    }

    // nearest implements
    private Point2D dfsNearest(Node node, RectHV rect, double x, double y, Point2D potentialPoint) {
        Point2D nearestPoint = potentialPoint;
        if (node == null)
            return nearestPoint;
        Point2D queryPoint = new Point2D(x, y);
        if (nearestPoint == null || queryPoint.distanceSquaredTo(nearestPoint) > rect.distanceSquaredTo(queryPoint)) {
            Point2D tempPoint = new Point2D(node.x, node.y);
            if (nearestPoint == null || queryPoint.distanceSquaredTo(nearestPoint) > queryPoint.distanceSquaredTo(tempPoint))
                nearestPoint = tempPoint;
            if ((node.isVertical && x <= node.x) || (!node.isVertical && y <= node.y)) {
                nearestPoint = dfsNearest(node.left, getLeftRect(node, rect), x, y, nearestPoint);
                nearestPoint = dfsNearest(node.right, getRightRect(node, rect), x, y, nearestPoint);
            } else {
                nearestPoint = dfsNearest(node.right, getRightRect(node, rect), x, y, nearestPoint);
                nearestPoint = dfsNearest(node.left, getLeftRect(node, rect), x, y, nearestPoint);
            }
        }
        return nearestPoint;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return dfsNearest(root, BORDER_RECT, p.x(), p.y(), null);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        System.out.println("Run KdTreeVisualizer instead");
    }
}
