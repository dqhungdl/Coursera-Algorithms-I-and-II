package collinear;

import edu.princeton.cs.algs4.StdDraw;

public class LineSegment {

    private final Point p, q;

    // constructs the line segment between points p and q
    public LineSegment(Point p, Point q) {
        this.p = p;
        this.q = q;
    }

    // draws this line segment
    public void draw() {
        p.drawTo(q);
    }

    // string representation
    public String toString() {
        return p.toString() + " -> " + q.toString();
    }
}
