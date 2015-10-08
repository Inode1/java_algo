import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Iterator;
import edu.princeton.cs.algs4.In;
import java.util.Collections;

public class Solver {
    private int movesResult = -1;
    private SolutionResult solverResult;
    private MinPQ<QueueData> priorityQueue = new MinPQ<QueueData>();
    private ArrayList<DataStruct> arrayAllData = new ArrayList<DataStruct>();
    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        long start = System.nanoTime();
        arrayAllData.add(new DataStruct(initial, null, 0));
        int count = 0;
        priorityQueue.insert(new QueueData(arrayAllData.get(arrayAllData.size() - 1)));
        boolean notFind = true;
        QueueData tempResult = null;
        while (notFind)
        {
            //System.out.println(count++);

            tempResult = priorityQueue.delMin();
            // we solve task?
            if (tempResult.refToDataStruct.value.isGoal())
            {
                break;
            }
            //System.out.println(tempResult.refToDataStruct.value.hamming() + " and " + tempResult.refToDataStruct.value.manhattan());
            //System.out.println("Value");
            for (Board step: tempResult.refToDataStruct.value.neighbors())
            {
                if (step.equals(tempResult.refToDataStruct.privious)) {
                    continue;
                }
                //System.out.print(step);
                //System.out.println(step.hamming() + " and " + step.manhattan());
                int position = 0;
                DataStruct currentElement = new DataStruct(step, tempResult.refToDataStruct, tempResult.refToDataStruct.moves + 1);
                if ((position = Collections.binarySearch(arrayAllData, 
                                            currentElement)) < 0) {
                    arrayAllData.add(~position, currentElement);
                }

                
                if (step.isGoal())
                {
                    notFind = false;
                    tempResult = new QueueData(currentElement);
                    break;
                }

                priorityQueue.insert(new QueueData(currentElement));
            }
        }
        System.out.println("Find moves: " + tempResult.refToDataStruct.moves);
        System.out.println("Time: " + (System.nanoTime() - start));

    }
    public boolean isSolvable() {           // is the initial board solvable?
        return movesResult != -1;
    }

    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        return movesResult;
    }

    
    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
        return solverResult;
    }

    // for array of all result
    private class DataStruct implements Comparable<DataStruct> {
        private Board value;
        private DataStruct privious;
        private int moves;
        private DataStruct(Board value, DataStruct privious, int moves) {
            this.value = value;
            this.privious = privious;
            this.moves = moves;
        }
        public int compareTo(DataStruct that) {
            if (value.hamming() == that.value.hamming() && value.manhattan() == that.value.manhattan()) {
                //System.out.println("value:");
                //System.out.println(value);
                //System.out.println(that.value);
                if (value.equals(that.value)) {
                    //System.out.println("equals");
                    return 0;
                }
                if (moves == that.moves) {
                    //System.out.println("Alarm");
                    return 0;
                }
                return moves - that.moves;
            }
            if (value.hamming() == that.value.hamming()){
                return value.manhattan() - that.value.manhattan();
            }
            return value.hamming() - that.value.hamming();
        }
    }

    // data in priority queue
    private class QueueData implements Comparable<QueueData> {
        private DataStruct refToDataStruct;
        private QueueData(DataStruct refToDataStruct) {
            this.refToDataStruct = refToDataStruct;
        }
        public int compareTo(QueueData that) {
            //return refToDataStruct.moves + refToDataStruct.value.hamming() + refToDataStruct.value.manhattan() - that.refToDataStruct.moves - that.refToDataStruct.value.manhattan() - that.refToDataStruct.value.hamming();
            if (refToDataStruct.moves + refToDataStruct.value.manhattan() == that.refToDataStruct.moves + that.refToDataStruct.value.manhattan()) {
                return refToDataStruct.value.hamming() - that.refToDataStruct.value.hamming();
            }
            return refToDataStruct.moves + refToDataStruct.value.manhattan() - that.refToDataStruct.moves - that.refToDataStruct.value.manhattan();
        }
    }

    private class SolutionResult implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new Result();
        }
    }

    private class Result implements Iterator<Board> {
        public boolean hasNext() {
            return true;
        }

        public Board next() {
            return null;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
    
    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);
/*
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }*/

    }
}