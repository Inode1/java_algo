import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private boolean[] isPointInclude;
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        Arrays.sort(points);

        for (int i = 1; i < points.length; ++i) {
            if (points[i-1].compareTo(points[i]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        boolean find;
        isPointInclude = new boolean[points.length];
        double slope;
        for (int i = 0; i < points.length; ++i) {
            find = false;
            for (int j = i + 1; j < points.length && !find; ++j) {
                if (isPointInclude[i] && isPointInclude[j]) {
                    continue;
                }

                slope = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length && !find; ++k) {
                    if (slope != points[j].slopeTo(points[k]) || (isPointInclude[j] && isPointInclude[k])) {
                        continue;
                    }
                    for (int z = k + 1; z < points.length && !find; ++z) {
                        if (slope != points[k].slopeTo(points[z]) || (isPointInclude[k] && isPointInclude[z])) {
                            continue;
                        }
                        isPointInclude[i] = true;
                        isPointInclude[j] = true;
                        isPointInclude[k] = true;
                        isPointInclude[z] = true;

                        lineSegments.add(new LineSegment(points[i], points[z]));
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