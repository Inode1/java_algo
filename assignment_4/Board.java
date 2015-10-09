import java.util.Iterator;
import edu.princeton.cs.algs4.In;

public class Board {
    private int[] board;
    private int dimension;
    private int emptyBlock = 0;
    private int hamming = 0;
    private int manhattan = 0;
    public Board(int[][] blocks) {           // construct a board from an N-by-N array of blocks
        if (blocks == null) {
            throw new java.lang.NullPointerException();
        }
        dimension = blocks.length;
        board = new int[dimension*dimension];
        int k = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                board[k] = blocks[i][j];
                if (board[k] == 0) {
                    emptyBlock = k;
                }
                ++k;
            }
        }
        hammingCalculate();
        manhattanCalculate();

    }
    private Board(Board that) {
        dimension = that.dimension();
        board = new int[that.board.length];
        for (int i = 0; i < that.board.length; ++i)
        {
            board[i] = that.board[i];
            if (board[i] == 0)
            {
                emptyBlock = i;
            }
        }
        hammingCalculate();
        manhattanCalculate();
    }
                                            // (where blocks[i][j] = block in row i, column j)
    public int dimension(){                 // board dimension N
        return dimension;
    }
    public int hamming() {                  // number of blocks out of place
        return hamming;
    }

    private void hammingCalculate() {
        for (int i = 0; i < board.length; ++i) {
            if (board[i] != 0 && board[i] != i + 1) {
                ++hamming;
            }   
        }
    }

    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        return manhattan;
   
    }

    private void manhattanCalculate() {
        for (int i = 0; i < board.length; ++i) {
            if (board[i] != 0 && board[i] != i + 1) {
                int row = (board[i] - 1) / dimension;
                int column = (board[i] - 1) % dimension;
                row -= i / dimension;
                if (row < 0) {
                    row = -row;
                }
                column -= i % dimension;
                if (column < 0) {
                    column = -column;
                }
                manhattan += row + column;
            }   
        }
        
    }

    public boolean isGoal() {                // is this board the goal board?
        return hamming == 0;
    }

    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        Board temp = new Board(this);
        int j = -1;
        for (int i = 0; i < board.length; ++i)
        {
            if (board[i] != 0) 
            {
                if (j != -1)
                {
                    int swap = temp.board[i];
                    temp.board[i] = temp.board[j];
                    temp.board[j] = swap;
                    break;
                }
                j = i;
            }
        }
        return temp;
    }

    public boolean equals(Object y) {       // does this board equal y?
        if (this == y) {
            return true;
        }

        if (y == null || this.getClass() != y.getClass()){
            return false;
        }

        Board that = (Board) y;
        if (dimension != that.dimension())
        {
            return false;
        }
        for (int i = 0; i < board.length; ++i) {
            if (board[i] != that.board[i]) {
                return false;
            }    
        }
        return true;       
    }

    public Iterable<Board> neighbors() {    // all neighboring boards
        return new MyCollection();
    }

    private class MyCollection implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new Neighbors();
        }
    }

    private class Neighbors implements Iterator<Board> {
        private int iterationSize = 0;
        private int row = emptyBlock / dimension;
        private int column = emptyBlock % dimension;
        private Board[] vectorBoardElement;
        private Neighbors() {
            vectorBoardElement = new Board[4];
            // left element
            if (column > 0) {
                board[emptyBlock] = board[emptyBlock - 1];
                board[emptyBlock - 1] = 0;
                vectorBoardElement[iterationSize] = new Board(Board.this);
                board[emptyBlock - 1] = board[emptyBlock]; 
                board[emptyBlock] = 0;
                ++iterationSize;
            }
            // top element
            if (row > 0) {
                board[emptyBlock] = board[emptyBlock - dimension];
                board[emptyBlock - dimension] = 0;
                vectorBoardElement[iterationSize] = new Board(Board.this);
                board[emptyBlock - dimension] = board[emptyBlock];
                board[emptyBlock] = 0;
                ++iterationSize;
            }
            // right element
            if (column < dimension - 1) {
                board[emptyBlock] = board[emptyBlock + 1];
                board[emptyBlock + 1] = 0;
                vectorBoardElement[iterationSize] = new Board(Board.this);
                board[emptyBlock + 1] = board[emptyBlock];
                board[emptyBlock] = 0;                
                ++iterationSize;
            }
            // bottom element
            if (row < dimension - 1) {
                board[emptyBlock] = board[emptyBlock + dimension];
                board[emptyBlock + dimension] = 0;
                vectorBoardElement[iterationSize] = new Board(Board.this);
                board[emptyBlock + dimension] = board[emptyBlock];
                board[emptyBlock] = 0;
                ++iterationSize;
            }
        }
        public boolean hasNext() {
            return iterationSize != 0;
        }

        public Board next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            --iterationSize;
            return vectorBoardElement[iterationSize];
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", board[i*dimension + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    public static void main(String[] args) { // unit tests (not graded)
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println("Hamming: " + initial.hamming() + " Manhattan: " + initial.manhattan());
        System.out.println(initial);
        for (Board step: initial.neighbors())
        {
            System.out.println(step + " and " + step.hamming() + " Manhattan: " + step.manhattan());
            if (step.hamming() == 1) 
            {
                System.out.println("efwefwe");
                for (Board stepic: step.neighbors()) {
                    System.out.println(stepic + " and " + stepic.hamming() + " Manhattan: " + stepic.manhattan());
                }
            break;
            } 
        }
        System.out.println(initial);
    }
}   