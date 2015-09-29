import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {
    private Point[] pointsCopy;
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();   
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; ++i) {
            pointsCopy[i] = points[i];
        } 
        Arrays.sort(pointsCopy);

        for (int i = 1; i < pointsCopy.length; ++i) {
            if (pointsCopy[i-1].compareTo(pointsCopy[i]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        int collinearNumber = 0;
        double slope;
        for (int i = 1; i < pointsCopy.length; ++i) {
            Arrays.sort(pointsCopy, i, pointsCopy.length, pointsCopy[i - 1].slopeOrder());
            int j = i + 1;
            while (j < pointsCopy.length) {
                slope = pointsCopy[i - 1].slopeTo(pointsCopy[j - 1]);
                while (j < pointsCopy.length && slope == pointsCopy[j - 1].slopeTo(pointsCopy[j])) {
                    ++collinearNumber;
                    ++j;
                }
                if (collinearNumber >= 2) {
                    int z = j - 1; 
/*                    if (Collections.binarySearch(lineSegments, slope) < 0) {
                        lineSegments.add(new LineSegment(pointsCopy[i - 1], pointsCopy[z]));
                    } */                   
                    Collections.sort(lineSegments);                              
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
}