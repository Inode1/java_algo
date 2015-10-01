import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import java.lang.System;
import edu.princeton.cs.algs4.StdRandom;

public class FastCollinearPoints {
    private Point[] pointsCopy;
    private ArrayList<LineStruct> lineSegments = new ArrayList<LineStruct>();
    private LineSegment[] temp = null;
    private static final Comparator<LineStruct> BY_SLOPE = new LineStruct.ComapareBySlope();
    private static class LineStruct {
        private final Point biggest;
        private final Point little;
        private final double slope;
        public LineStruct(Point little, Point biggest, double slope) {
            this.biggest = biggest;
            this.little = little;
            this.slope = slope;
        }

        private static class ComapareBySlope implements Comparator<LineStruct> {
            public int compare(LineStruct lhs, LineStruct rhs) {
                if (lhs.slope == rhs.slope) {
                    return 0;
                }
                if (lhs.slope < 0 && rhs.slope >= 0) {
                    return 1;
                }
                if (lhs.slope >= 0 && rhs.slope < 0) {
                    return -1;
                }
                if (lhs.slope > rhs.slope) {
                    return 1;
                }
                return -1;   
            }
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
        long sortTime = 0;
        long findTime = 0;
        int goodElement = 0;
        //Arrays.sort(pointsCopy, 1, pointsCopy.length, pointsCopy[0].slopeOrder());
        for (int i = 1; i < pointsCopy.length - 1; ++i) {
            //long start = System.nanoTime();
            //System.out.println(goodElement + "and" + i);
            Arrays.sort(pointsCopy, goodElement > i ? goodElement : i, pointsCopy.length, pointsCopy[i-1].slopeOrder());
            //sortTime += System.nanoTime() - start;
            //System.out.println("Sort time" + "="+ (System.nanoTime() - start));
            int j = goodElement > i ? goodElement : i;
            //start = System.nanoTime();
            while (j < pointsCopy.length) {
                lastElement = pointsCopy[j];
                slope = pointsCopy[i - 1].slopeTo(pointsCopy[j++]);
                while (j < pointsCopy.length && slope == pointsCopy[i - 1].slopeTo(pointsCopy[j])) {
                    if (slope == 0) {
                        goodElement = j + 1;
                    }
                    if (pointsCopy[j].compareTo(lastElement) > 0) {
                        lastElement = pointsCopy[j];
                    }                    
                    ++collinearNumber;
                    ++j;
                }
                if (collinearNumber >= 2) {
                    isExist(pointsCopy[i - 1], lastElement, slope);                  
                }
                collinearNumber = 0;
            }
            if (goodElement <= i) {
                findMinElement(i);
            }            
            //findTime += System.nanoTime() - start;
        }
        //System.out.println("Sort time" + sortTime);
       // System.out.println("Find time" + findTime);
    }

    public int numberOfSegments() {        // the number of line segments
        return lineSegments.size();
    }
    
    public LineSegment[] segments() {               // the line segments
        if (temp == null) {
            int i = 0;
            if (lineSegments != null){
                temp = new LineSegment[lineSegments.size()];
                for (LineStruct value: lineSegments) {
                    temp[i++] = new LineSegment(value.little, value.biggest);
                }
            }
        }
        return temp;
    }

    private void isExist(Point little, Point biggest, double slope) {
        int position = 0;
        LineStruct element = new LineStruct(little, biggest, slope);
        long start = System.nanoTime();
        if ((position = Collections.binarySearch(lineSegments, 
                        element, BY_SLOPE)) < 0) {
            lineSegments.add(- (1 + position), element);
            return;
        }
        //System.out.println(System.nanoTime() - start);
        //System.out.println("Element count: " + lineSegments.size());
        int result = biggest.compareTo(lineSegments.get(position).biggest);
        while (result < 0) {
            //
            --position;
            if (position < 0 || slope != lineSegments.get(position).slope) {
                if (position < 0) {
                    lineSegments.add(0, element);
                    return;
                }
                lineSegments.add(position + 1, element);
                return;
            }
            result = biggest.compareTo(lineSegments.get(position).biggest);
            if (result == 0) {
                return;
            }
            if (result > 0) {
                lineSegments.add(position + 1, element);
                return;
            }
        }

        while (result > 0) {
            ++position;
            if (position == lineSegments.size() || slope != lineSegments.get(position).slope) {
                if (position == lineSegments.size()) {
                    lineSegments.add(element);
                    return;
                }
                lineSegments.add(position, element);
                return;
            }
            result = biggest.compareTo(lineSegments.get(position).biggest);
            if (result == 0) {
                return;
            }
            if (result < 0) {
                lineSegments.add(position, element);
                return;
            }       
        }

        return;
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
}