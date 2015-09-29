import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
    private boolean[] isConnected;
    private int length;
    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
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
        boolean find;
        double slope;
        for (int i = 0; i < pointsCopy.length; ++i) {
            for (int j = i + 1; j < pointsCopy.length; ++j) {
                if (isConnected(i, j)) {
                    continue;
                }
                find = false;

                slope = pointsCopy[i].slopeTo(pointsCopy[j]);
                for (int k = j + 1; k < pointsCopy.length && !find; ++k) {
                    if (slope != pointsCopy[i].slopeTo(pointsCopy[k]) || isConnected(j, k)) {
                        continue;
                    }
                    for (int z = k + 1; z < pointsCopy.length && !find; ++z) {
                        if (slope != pointsCopy[i].slopeTo(pointsCopy[z]) || isConnected(k, z)) {
                              continue;
                        }
                        isConnected[i*length + j] = true;

                        isConnected[j*length + k] = true;

                        isConnected[k*length + z] = true;

                        lineSegments.add(new LineSegment(pointsCopy[i], pointsCopy[z]));
                        find = true;
                    }                    
                }
            }
        }
    }
    
    public int numberOfSegments() {        // the number of line segments
        return lineSegments.size();
    }
    
    public LineSegment[] segments() {               // the line segments
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    private void checkPointIsNull(Point point) {
        if (point == null) {
            throw new java.lang.NullPointerException();
        }
    }

    private boolean isConnected(int lhs, int rhs) {
        if (isConnected[lhs * length + rhs]) {
            return true;
        }
        return false;
    }
}