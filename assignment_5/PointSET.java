import java.util.TreeSet;
import java.util.Stack;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private TreeSet<Point2D> tree = new TreeSet<Point2D>(Point2D.Y_ORDER);/*
    public         PointSET() {                               // construct an empty set of points 
    }
*/
    public boolean isEmpty() {                     // is the set empty? 
        return tree.isEmpty();
    }

    public int size() {                        // number of points in the set 
        return tree.size();
    }
    public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)
        checkPoint(p);
        tree.add(p);
    }
    public boolean contains(Point2D p) {            // does the set contain point p? 
        checkPoint(p);
        return tree.contains(p);
    }
    public void draw() {                         // draw all points to standard draw 
        for (Point2D p: tree) {
            p.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle 
        Stack<Point2D> range = new Stack<Point2D>();
        for (Point2D point: tree) {
            if (rect.contains(point)){
                range.push(point);
            }
        }
        return range;
    }
    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        checkPoint(p);
        double min = Double.POSITIVE_INFINITY;
        Point2D result = null;
        for (Point2D point: tree) {
            double distance = point.distanceTo(p);  
            if (distance < min) {
                min = distance;
                result = point;
            }
        }
        return result;
    }

    private void checkPoint(Point2D check) {
        if (check == null) {
            throw new java.lang.NullPointerException();
        }
    }

    public static void main(String[] args) {                  // unit testing of the methods (optional) 
    }
}
