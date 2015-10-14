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
        Node start = root;
        boolean vertical = true;
        while (start != null) {
            GoNext(start, vertical);
        }
        start = new Node(new Point2D(p.x(), p.y()));
        ++size;       
    }
    
    public boolean contains(Point2D p) {            // does the set contain point p? 
        checkPoint(p);
        Node start = root;

        while (start != null) 
        {
            if (start.point.equals(p))
            {
                break;
            }
            GoNext(start, vertical);
        }
        return start != null;
    }

    private void GoNext(Node next, boolean vertical) {
        if (vertical)
        {
            if (start.compareForVertical(p) < 0) {
                start = start.left;
            }
            else 
            {
                start = start.right;
            }
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
    
    public void draw() {                         // draw all points to standard draw 
        Node start = root;
        StdDraw.setPenRadius(.01);
        draw(start, true)
        
    }

    private void draw(Node next, boolean vertical) {
        if (next == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        next.point.draw();
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line
        }
        draw(next.left, !vertical);
        draw(next.right, !vertical);
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
        public Node(Point2D point) {
            this.point = point;
        }
        public int compareForVertical(Point2D point) {
            if (this.point.x() < point.x()) {
                return -1;
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
    
    }
}
