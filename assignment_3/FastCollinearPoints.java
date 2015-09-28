import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private boolean[] isPointInclude;
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();   
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; ++i) {
            pointsCopy[i] = pointsp[i];
        } 
        Arrays.sort(pointsCopy);

        for (int i = 1; i < pointsCopy.length; ++i) {
            if (pointsCopy[i-1].compareTo(pointsCopy[i]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        double slope;
        int collinearNumber = 0;
        isPointInclude = new boolean[pointsCopy.length];
        for (int i = 1; i < pointsCopy.length - 3; ++i) {
            Arrays.sort(pointsCopy, i, pointsCopy.length, pointsCopy[i - 1].slopeOrder());
            for (int k = i; k < pointsCopy.length; ++k) {
                if (isPointInclude[i - 1] && isPointInclude[k]) {
                    continue;
                }
                slope = pointsCopy[i - 1].slopeTo(pointsCopy[k++]);
                while (k < pointsCopy.length) {
                    if (slope == pointsCopy[i - 1].slopeTo(pointsCopy[k]) && (!isPointInclude[k - 1] || !isPointInclude[k])) {
                        ++collinearNumber;
                        ++k;
                    } else {
                        break;
                    }
                }

                if (collinearNumber >= 2) {
                    for ( ;collinearNumber == 0; ) {
                        isPointInclude[k - collinearNumber] = true;
                        --collinearNumber;
                    }
                    isPointInclude[i] = true;
                    lineSegments.add(new LineSegment(pointsCopy[i - 1], pointsCopy[k-1]));
                } else {
                    --k;
                }
                collinearNumber = 0;
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
}