import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private boolean[] isPointInclude;
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
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
        boolean find;
        isPointInclude = new boolean[pointsCopy.length];
        double slope;
        for (int i = 0; i < pointsCopy.length; ++i) {
            find = false;
            for (int j = i + 1; j < pointsCopy.length && !find; ++j) {
                if (isPointInclude[i] && isPointInclude[j]) {
                    continue;
                }

                slope = pointsCopy[i].slopeTo(pointsCopy[j]);
                for (int k = j + 1; k < pointsCopy.length && !find; ++k) {
                    if (slope != pointsCopy[j].slopeTo(pointsCopy[k]) || (isPointInclude[j] && isPointInclude[k])) {
                        continue;
                    }
                    for (int z = k + 1; z < pointsCopy.length && !find; ++z) {
                        if (slope != pointsCopy[k].slopeTo(pointsCopy[z]) || (isPointInclude[k] && isPointInclude[z])) {
                            continue;
                        }
                        isPointInclude[i] = true;
                        isPointInclude[j] = true;
                        isPointInclude[k] = true;
                        isPointInclude[z] = true;

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
}