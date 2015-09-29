import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private boolean[] isConnected;
    private int length;
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();   
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; ++i) {
            pointsCopy[i] = points[i];
        } 
        Arrays.sort(pointsCopy);

        for (int i = 1; i < pointsCopy.length; ++i) {
            if (pointsCopy[i-1].compareTo(pointsCopy[i]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        length = pointsCopy.length;
        isConnected = new boolean[length * length];

        int collinearNumber = 0;
        double slope;
        for (int i = 1; i < pointsCopy.length; ++i) {
            Arrays.sort(pointsCopy, i, pointsCopy.length, pointsCopy[i - 1].slopeOrder());
            int j = i + 1;
            while (j < pointsCopy.length) {
                if (isConnected(useLoop(points, pointsCopy[i - 1]), useLoop(points, pointsCopy[j - 1]))) {
                        ++j;
                        continue;
                }
                slope = pointsCopy[i - 1].slopeTo(pointsCopy[j - 1]);
                while (j < pointsCopy.length && slope == pointsCopy[j - 1].slopeTo(pointsCopy[j])) {
                    if (isConnected(useLoop(points, pointsCopy[j - 1]),useLoop(points, pointsCopy[j]))) {
                        break;
                    }
                    ++collinearNumber;
                    ++j;
                }
                if (collinearNumber >= 2) {
                    int z = j - 1;    
                    lineSegments.add(new LineSegment(pointsCopy[i - 1], pointsCopy[z]));
                    int last = 0;
                    int first = useLoop(points, pointsCopy[z]);
                    for (; collinearNumber-- != 0; ) {                        
                        last = useLoop(points, pointsCopy[--z]);
                        isConnected[last*length + first] = true;
                        first = last;
                    }
                    first = useLoop(points, pointsCopy[i - 1]);
                    isConnected[first*length + last] = true;                                    
                }
                collinearNumber = 0;
                ++j;
            }
            Arrays.sort(pointsCopy, i, pointsCopy.length);
        }
    }

    public int numberOfSegments() {        // the number of line segments
        return lineSegments.size();
    }
    
    public LineSegment[] segments() {               // the line segments
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    private int useLoop(Point[] arr, Point targetValue) {
        //Arrays.binarySearch(pointsCopy, firstElement, length, targetValue, pointsCopy[firstElement - 1].slopeOrder());

        for(int i = 0; i < arr.length; ++i){
            if(arr[i].compareTo(targetValue) == 0)
            {
                return i;
            }
        }
        return 0;   
    }

    private boolean isConnected(int lhs, int rhs) {
        return isConnected[lhs * length + rhs];
    }
}