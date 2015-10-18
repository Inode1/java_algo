import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;
import java.util.Stack;

public class KdTree {
    private Node root;
    private int size;
    private Point2D neigh;
    private double distanceNeigh;
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
        //System.out.println(p);
        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1.0, 1.0));
            ++size;
            //System.out.println("Size: " + size);
            return;
        }
        root = GoNext(root, p, true);
        ++size;
        //System.out.println("Size2: " + size);       
    }

    private Node GoNext(Node root, Point2D point, boolean vertical) {
            //int cmp = ;
            if (root.point.equals(point)) {
                --size;
                return root;
            }

            if (root.compare(point, vertical) < 0) {
                if (root.left == null) {
                    if (vertical) {
                        root.left = new Node(point, new RectHV(root.rect.xmin(), root.rect.ymin(),
                                                               root.point.x(), root.rect.ymax()));
                    } else {
                        root.left = new Node(point, new RectHV(root.rect.xmin(), root.rect.ymin(),
                                                               root.rect.xmax(), root.point.y()));
                    }
                } else {
                    root.left = GoNext(root.left, point, !vertical);                    
                }
            } else {
                if (root.right == null) {
                    if (vertical) {
                        root.right = new Node(point, new RectHV(root.point.x(), root.rect.ymin(),
                                                           root.rect.xmax(), root.rect.ymax())); 
                    } else {
                        root.right = new Node(point, new RectHV(root.rect.xmin(), root.point.y(),
                                                           root.rect.xmax(), root.rect.ymax())); 
                    }
                }
                else {
                    root.right = GoNext(root.right, point, !vertical);
                }
            }
            return root;
    }
    
    public boolean contains(Point2D p) {            // does the set contain point p? 
        checkPoint(p);
        Node start = root;
        boolean vertical = true;
        while (start != null) 
        { 
            if (start.point.equals(p)) {
                return true;
            }
            if (start.compare(p, vertical) < 0) {
                start = start.left;
            } 
            else {
                start = start.right;
            }
            vertical = !vertical;
        }
        return false;
    }
    
    public void draw() {                         // draw all points to standard draw 
        StdDraw.setPenRadius(.005);
        draw(root, true);
    }

    private void draw(Node next, boolean vertical) {
        if (next == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        next.point.draw();

        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(next.point.x(), next.rect.ymin(), next.point.x(), next.rect.ymax());
            draw(next.left, false);
            draw(next.right, false);
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(next.rect.xmin(), next.point.y(), next.rect.xmax(), next.point.y());
            draw(next.left, true);
            draw(next.right, true);
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle 
        Stack<Point2D> result = new Stack<Point2D>();
        range(result, root, rect, true);
        return result;
    }

    private void range(Stack<Point2D> result, Node next, RectHV rect, boolean vertical) {
        if (next == null) return;
        if (rect.contains(next.point)) {
            result.push(next.point);
        }
        if ((vertical && next.point.x() >= rect.xmin()) ||
            (!vertical && next.point.y() >= rect.ymin())) {
            range(result, next.left, rect, !vertical);
        }
        if ((vertical && next.point.x() <= rect.xmax()) ||
            (!vertical && next.point.y() <= rect.ymax())) {
            range(result, next.right, rect, !vertical);
        }
               
    }
    
    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        checkPoint(p);
        if (root == null) {
            return null;
        }
        //neigh = root.point;
        distanceNeigh = Double.MAX_VALUE;
        nearest(root, p);

        return neigh;
    }

    private void nearest(Node start, Point2D p) {
        if (start == null){
            return;
        }

        double distance = start.point.distanceSquaredTo(p);
        if (distance < distanceNeigh) {
            distanceNeigh = distance;
            neigh = start.point;
        }

        if (start.left != null && start.right != null) {
            double left, right;
            
            left = start.left.rect.distanceSquaredTo(p);
            right = start.right.rect.distanceSquaredTo(p);
            
            if (left < right) {
                nearest(start.left, p);
                
                if (right < distanceNeigh) {
                    nearest(start.right, p);
                }
                
            } else {
                nearest(start.right, p);
                
                if (left < distanceNeigh) {
                    nearest(start.left, p);
                }
            }
            
            return;
        }


        if (start.left != null && start.left.rect.distanceSquaredTo(p) < distanceNeigh) {
            nearest(start.left, p);
        }
                
        if (start.right != null && start.right.rect.distanceSquaredTo(p) < distanceNeigh) {
            nearest(start.right, p);
        }
    }

    private class Node {
        private Point2D point;
        private RectHV rect;
        //private Point2D axis;
        private Node right;
        private Node left;
        public Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }

        public int compare(Point2D point, boolean vertical) {
            if ((vertical && point.x() < this.point.x()) ||
                (!vertical && point.y() < this.point.y()))
            {
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
        String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the two data structures with point from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            //System.out.println(p);
            kdtree.insert(p);
        }
        Point2D nei = new Point2D(1, 0.5);
        System.out.println("Start");

        long start = 0;
        System.out.println("Nearest" + kdtree.nearest(nei));

        System.out.println("Sort time" + "="+ (System.nanoTime() - start));
        //kdtree.insert(nei);

        System.out.println(kdtree.size());
    
    }
}
