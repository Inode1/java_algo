import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import java.lang.System;

public class FastCollinearPoints {
    private Point[] pointsCopy;
    private ArrayList<LineStruct> lineSegments = new ArrayList<LineStruct>();
    private static final Comparator<LineStruct> BY_SLOPE = new LineStruct.ComapareBySlope();
    private static final Comparator<LineStruct> BY_POINT = new LineStruct.ComapareByPoint();
    private static class LineStruct {
        private final Point biggest;
        private final Point little;
        public LineStruct(Point little, Point biggest) {
            this.biggest = biggest;
            this.little = little;
        }
        private static class ComapareBySlope implements Comparator<LineStruct> {
            public int compare(LineStruct lhs, LineStruct rhs) {
                double leftAngle = lhs.little.slopeTo(lhs.biggest);
                double rightAngle = rhs.little.slopeTo(rhs.biggest);
                if (leftAngle == rightAngle) {
                    return 0;
                }
                if (leftAngle >= 0 && rightAngle < 0) {
                    return 1;
                }
                if (rightAngle >= 0 && leftAngle < 0) {
                    return -1;
                }
                if ((leftAngle - rightAngle) < 0) {
                    return -1;
                }
                return 1;   
            }
        }

        private static class ComapareByPoint implements Comparator<LineStruct> {
            public int compare(LineStruct lhs, LineStruct rhs) {
                return lhs.biggest.compareTo(rhs.biggest);
            }
        }

    }
            private int compareTo(double leftAngle, double rightAngle) {
            if (leftAngle == rightAngle) {
                return 0;
            }
            if (leftAngle >= 0 && rightAngle < 0) {
                return 1;
            }
            if (rightAngle >= 0 && leftAngle < 0) {
                return -1;
            }
            if ((leftAngle - rightAngle) < 0) {
                return -1;
            }
            return 1;              
        }

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
                    long start = System.nanoTime();
                    if (!isExist(pointsCopy[i - 1], pointsCopy[z], slope)) {
                    }
                    System.out.println("wfwef" + (System.nanoTime() - start));

                    //Collections.sort(lineSegments);                              
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
        int i = 0;
        LineSegment[] temp = new LineSegment[lineSegments.size()];
        for (LineStruct value: lineSegments) {
            temp[i++] = new LineSegment(value.little, value.biggest);
        }
        return temp;
    }

    private boolean isExist(Point little, Point biggest, double slope) {
        int position = 0;
        LineStruct element = new LineStruct(little, biggest);
        long start = System.nanoTime();
        position = returnLeftPosistions(element, slope);
        if (position == lineSegments.size()) {
            lineSegments.add(element);
            return false;
        }
/*        if ((position = Collections.binarySearch(lineSegments, 
                        element, BY_SLOPE)) < 0) {
            lineSegments.add(element);
            Collections.sort(lineSegments, BY_POINT);
            Collections.sort(lineSegments, BY_SLOPE);
            return false;
        }*/
        System.out.println(System.nanoTime() - start);
        System.out.println("Element count: " + lineSegments.size());
        int result = biggest.compareTo(lineSegments.get(position).biggest);
        while (result < 0) {
            //|| slope != lineSegments.get(position).little.slopeTo(lineSegments.get(position).biggest)
            --position;
            if (position < 0) {
                if (position < 0) {
                    lineSegments.add(0, element);
                    return false;
                }
                lineSegments.add(position, element);
                return false;
            }
            result = biggest.compareTo(lineSegments.get(position).biggest);
            if (result == 0) {
                return true;
            }
            if (result > 0) {
                lineSegments.add(position, element);
                return false;
            }
        }
//|| slope != lineSegments.get(position).little.slopeTo(lineSegments.get(position).biggest)
        while (result > 0) {
            ++position;
            if (position == lineSegments.size() ) {
                if (position == lineSegments.size()) {
                    lineSegments.add(element);
                    return false;
                }
                lineSegments.add(position, element);
                return false;
            }
            result = biggest.compareTo(lineSegments.get(position).biggest);
            if (result == 0) {
                return true;
            }
            if (result < 0) {
                lineSegments.add(position, element);
                return false;
            }       
        }

        return true;
    }
    private int returnLeftPosistions(LineStruct element, double slopeFirst) {
        int lo = 0;
        int hi = lineSegments.size() - 1;
        int result = 0;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            result = compareTo(slopeFirst, lineSegments.get(mid).little.slopeTo(lineSegments.get(mid).biggest);
            if (result < 0) hi = mid - 1;
            else if (result > 0) lo = mid + 1;
            else return mid;
        }
        return mid;
    }
}