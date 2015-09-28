import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private boolean[] isPointInclude;
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();   
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        Arrays.sort(points);

        for (int i = 1; i < points.length; ++i) {
            if (points[i-1].compareTo(points[i]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        double slope;
        int collinearNumber = 0;
        isPointInclude = new boolean[points.length];
        Arrays.sort(points, points[0].slopeOrder());
        for (int i = 0; i < points.length - 3; ++i) {
            for (int k = i + 1; k < points.length; ++k) {
                if (isPointInclude[i] && isPointInclude[k]) {
                    continue;
                }
                slope = points[i].slopeTo(points[k]);
                while (k < points.length - 1) {
                    System.out.println(slope + "==" + points[k].slopeTo(points[k + 1]));
                    if (slope == points[k].slopeTo(points[k + 1]) && (!isPointInclude[k] || !isPointInclude[k + 1])) {
                        System.out.println("Find");
                        ++collinearNumber;
                        ++k;
                    } else {
                        break;
                    }
                }

                if (collinearNumber >= 3) {
                    System.out.println("Line");
                    for (;collinearNumber == 0;) {
                        isPointInclude[k - collinearNumber] = true;
                        --collinearNumber;
                    }
                    isPointInclude[i] = true;
                    lineSegments.add(new LineSegment(points[i], points[k-1]));
                }
                collinearNumber = 0;
            }
            Arrays.sort(points, points[i + 1].slopeOrder());
        }
        System.out.println("End");
    }

    public int numberOfSegments() {        // the number of line segments
        return lineSegments.size();
    }
    
    public LineSegment[] segments() {               // the line segments
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }
}