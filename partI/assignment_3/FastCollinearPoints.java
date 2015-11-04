import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FastCollinearPoints {
    private Point[] pointsCopy;
    private ArrayList<LineStruct> lineSegments = new ArrayList<LineStruct>();
    private ArrayList<LineSegment> result = new ArrayList<LineSegment>();
    private class LineStruct implements Comparable<LineStruct> {
        private final Point biggest;
        private final ArrayList<Point> little = new ArrayList<Point>();
        public LineStruct(Point little, Point biggest) {
            this.biggest = biggest;
            this.little.add(little);
        }

        public int compareTo(LineStruct rhs)
        {
            return this.biggest.compareTo(rhs.biggest);
        }
    }

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        pointsCopy = new Point[points.length];
        for (int i = 0; i < pointsCopy.length; ++i) {
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
        Point lastElement;
        //long sortTime = 0;
        //long findTime = 0;
        //int goodElement = 0;
        //Arrays.sort(pointsCopy, 1, pointsCopy.length, pointsCopy[0].slopeOrder());
        for (int i = 1; i < pointsCopy.length; ++i) {
            //long start = System.nanoTime();
            //System.out.println(goodElement + "and" + i);
            //Arrays.sort(pointsCopy, goodElement > i ? goodElement : i, pointsCopy.length, pointsCopy[i-1].slopeOrder());
            Arrays.sort(pointsCopy, i, pointsCopy.length, pointsCopy[i-1].slopeOrder());
            //sortTime += System.nanoTime() - start;
            //System.out.println("Sort time" + "="+ (System.nanoTime() - start));
            //int j = goodElement > i ? goodElement : i;
            int j = i;
            //start = System.nanoTime();
            while (j < pointsCopy.length) {
                lastElement = pointsCopy[j];
                slope = pointsCopy[i - 1].slopeTo(pointsCopy[j++]);
                while (j < pointsCopy.length && slope == pointsCopy[i - 1].slopeTo(pointsCopy[j])) {
                    //if (slope == 0) {
                        //goodElement = j + 1;
                    //}
                    if (pointsCopy[j].compareTo(lastElement) > 0) {
                        lastElement = pointsCopy[j];
                    }                    
                    ++collinearNumber;
                    ++j;
                }
                if (collinearNumber >= 2) {
                    isExist(pointsCopy[i - 1], lastElement);                  
                }
                collinearNumber = 0;
            }
            //Arrays.sort(pointsCopy, i, pointsCopy.length);
            //if (goodElement <= i) {
                findMinElement(i);
            //}          
            //findTime += System.nanoTime() - start;
        }
        //System.out.println("Sort time" + sortTime);
        //System.out.println("Find time" + findTime);
    }

    public int numberOfSegments() {        // the number of line segments
        return result.size();
    }
    
    public LineSegment[] segments() {               // the line segments
        return result.toArray(new LineSegment[result.size()]);
    }
    private void isExist(Point little, Point biggest)
    {
        int position = 0;
        //++size;
        //isSorted();
        LineStruct element = new LineStruct(little, biggest);
        if ((position = Collections.binarySearch(lineSegments, 
                        element)) < 0) {
            lineSegments.add(~position, element);
            result.add(new LineSegment(little, biggest));
        } else 
        {
            int position2 = 0;
            if ((position2 = Collections.binarySearch(lineSegments.get(position).little, 
                            little, biggest.slopeOrder())) < 0) {
                lineSegments.get(position).little.add(~position2, little);
                result.add(new LineSegment(little, biggest));
            }
        }
        
    } 
    private void findMinElement(int i) {
        Point firstElement = pointsCopy[i];
        int j = i;
        for (int k = i + 1; k < pointsCopy.length; ++k) {
            if (pointsCopy[k].compareTo(firstElement) < 0) {
                firstElement = pointsCopy[k];
                j = k;
            }
        }
        if (i == j) {
            return;
        }
        pointsCopy[j] = pointsCopy[i];
        pointsCopy[i] = firstElement;
    }
/*    private void isSorted() {
        for (LineStruct value: lineSegments) {
            for (int i = 1; i < value.slope.size(); i++) {
                if (value.slope.get(i - 1) > value.slope.get(i)) {
                    System.out.println("Fail");
                    break;
                }
            }
        }
    }*/
}