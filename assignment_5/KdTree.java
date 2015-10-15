import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Stack;

public class KdTree {
    private Node root;
    private int size;
    public KdTree() {                               // construct an empty set of points 
    
    }

    public boolean isEmpty() {                     // is the set empty? 
        return size == 0;
    }

    public int size() {                        // number of points in the set 
        return size;
    }
    
    public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)
        checkPoint(p);
        if (root == null) {
            root = new Node(new Point2D(p.x(), p.y()));
            ++size;
            return;
        }
        Node start = root;
        boolean vertical = true;
        
        while (start != null) {
            start = GoNext(start, p, vertical);
            vertical = !vertical;
        }
        start = new Node(new Point2D(p.x(), p.y()));
        System.out.println(size);
        ++size;       
    }

    private Node GoNext(Node start, Point2D p, boolean vertical) {
                    if (vertical)
            {
                if (start.compareForVertical(p) < 0) {
                    return start.left;
                }
                else 
                {
                    return start.right;
                }
            }
            else 
            {
                if (start.compareForHorizontal(p) < 0) {
                    return start.left;
                }
                else 
                {
                    return start.right;
                } 
            }   

    }
    
    public boolean contains(Point2D p) {            // does the set contain point p? 
        checkPoint(p);
        Node start = root;
        boolean vertical = true;

        while (start != null) 
        {
            if (start.point.equals(p))
            {
                break;
            }
                    if (vertical)
        {
            System.out.println("print");
            System.out.println(start.point);
            if (start.compareForVertical(p) < 0) {
                start = start.left;
            }
            else 
            {
                start = start.right;
            }
            System.out.println(start.point);
        }
        else 
        {
            if (start.compareForHorizontal(p) < 0) {
                start = start.left;
            }
            else 
            {
                start = start.right;
            } 
        }
        vertical = !vertical;   
        }
        return start != null;
    }
    
    public void draw() {                         // draw all points to standard draw 
        Node start = root;
        StdDraw.setPenRadius(.01);
        // upper side, left side
        Point2D leftUpperAngel = new Point2D(0.0, 1.0);
        // right side, down side
        Point2D rightDownAngel = new Point2D(1.0, 0.0);
        System.out.println("draw1");
        draw(start, leftUpperAngel, rightDownAngel, true);
        System.out.println("draw2");
    }

    private void draw(Node next, Point2D leftUpperAngel, Point2D rightDownAngel, boolean vertical) {
        if (next == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        next.point.draw();

        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(next.point.x(), leftUpperAngel.y(), next.point.x(), rightDownAngel.y());
            System.out.println("draw");
            draw(next.left, leftUpperAngel, new Point2D(next.point.x(), rightDownAngel.y()), !vertical);
            draw(next.right, new Point2D(next.point.x(), leftUpperAngel.y()), rightDownAngel, !vertical);
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(leftUpperAngel.x(), next.point.y(), rightDownAngel.x(), next.point.y());
            draw(next.left, new Point2D(leftUpperAngel.x(), next.point.y()), rightDownAngel, !vertical);
            draw(next.right, leftUpperAngel, new Point2D(rightDownAngel.x(), next.point.y()), !vertical);
        }
    }
    
    public Iterable<Point2D> range() {             // all points that are inside the rectangle 
        Stack<Point2D> result = new Stack<Point2D>();
        Node start = root;
        range(result, start);
        return result;
    }

    private void range(Stack<Point2D> result, Node next) {
        if (next == null) return;
        result.push(next.point);
        range(result, next.left);
        range(result, next.right);
    }
    
    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        return null;
    }

    private class Node {
        private Point2D point;
        private int maxLeftBorder;
        private Node right;
        private Node left;
        public Node(Point2D point) {
            this.point = point;
        }
        public int compareForVertical(Point2D point) {
            if (this.point.x() < point.x()) {
                return -1;
            }
            return 1;
        }

        public int compareForHorizontal(Point2D point) {
            if (this.point.y() < point.y()) {
                return -1;
            }
            return 1;
        }
    } 

    private void checkPoint(Point2D check) {
        if (check == null) {
            throw new java.lang.NullPointerException();
        }
    }


    
    public static void main(String[] args) {                  // unit testing of the methods (optional) 
        KdTree tree = new KdTree();
        for (int i = 0; i < 5; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            System.out.printf("%8.6f %8.6f\n", x, y);
            tree.insert(new Point2D(x, y));
        }
        for (Point2D f: tree.range()) {
            System.out.println(f);
        }
    
    }
}
