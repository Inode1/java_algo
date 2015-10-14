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
        while (root != null) {
            if (root.)
        }        
    }
    
    public boolean contains(Point2D p) {            // does the set contain point p? 
    
    }
    
    public void draw() {                         // draw all points to standard draw 
        
    }
    
    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle 
    
    }
    
    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
    
    }

    private Node {
        private Point2D point;
        private int maxLeftBorder;
        private Node right;
        private Node left;
        public int compareForVertical(Point2D point) {
            if (point.x() < this.point.x()) {
                return -1;
            }
            if (point.x() > this.point.x()) {
                return 1;
            }
            return -1;
        }

        public int compareForHorizontal(Point2D point) {
            if (point.x() < this.point.x()) {
                return -1;
            }
            if (point.x() > this.point.x()) {
                return 1;
            }
            return -1;
        }
    } 

    private void checkPoint(Point2D check) {
        if (check == null) {
            throw new java.lang.NullPointerException();
        }
    }


    
    public static void main(String[] args) {                  // unit testing of the methods (optional) 
    
    }
}
