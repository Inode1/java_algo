import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Iterator;
import edu.princeton.cs.algs4.In;

public class Solver {
    private int movesResult = -1;
    private SolutionResult solverResult;
    private MinPQ<QueueData> priorityQueue = new MinPQ<QueueData>();
    private ArrayList<DataStruct> arrayAllData = new ArrayList<DataStruct>();
    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        arrayAllData.add(new DataStruct(initial, null, 0));
        priorityQueue.insert(new QueueData(arrayAllData.get(arrayAllData.size() - 1)));
        
        QueueData tempResult;
        while (true)
        {
            tempResult = priorityQueue.delMin();
            // we solve task?
            if (tempResult.refToDataStruct.value.isGoal())
            {
                break;
            }

            for (Board step: tempResult.refToDataStruct.value.neighbors())
            {
                if (!step.equals(tempResult.refToDataStruct.privious))
                {
                    arrayAllData.add(new DataStruct(step, tempResult.refToDataStruct, tempResult.refToDataStruct.moves + 1));
                    priorityQueue.insert(new QueueData(arrayAllData.get(arrayAllData.size() - 1)));
                }
            }
        }
        System.out.println("Find moves: " + tempResult.refToDataStruct.moves);

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
    private class DataStruct {
        private Board value;
        private DataStruct privious;
        private int moves;
        private DataStruct(Board value, DataStruct privious, int moves) {
            this.value = value;
            this.privious = privious;
            this.moves = moves;
        }
    }

    // data in priority queue
    private class QueueData implements Comparable<QueueData> {
        private DataStruct refToDataStruct;
        private QueueData(DataStruct refToDataStruct) {
            this.refToDataStruct = refToDataStruct;
        }
        public int compareTo(QueueData that) {
            return refToDataStruct.moves + refToDataStruct.value.hamming() - that.refToDataStruct.moves - that.refToDataStruct.value.hamming();
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