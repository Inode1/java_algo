import java.util.Iterator;

public class Board {
    private int[][] board;
    private int dimension;
    private int emptyBlock = 0;
    private int hamming = 0;
    private int manhattan = 0;
    public Board(int[][] blocks) {           // construct a board from an N-by-N array of blocks
        if (blocks == null) {
            throw new java.lang.NullPointerException();
        }
        dimension = blocks.length;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                board[i][j] = blocks[i][j];
                if (board[i][j] == 0) {
                    emptyBlock = j + dimension * i;
                }
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
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (board[i][j] != 0 && board[i][j] != j + dimension * i + 1) {
                    ++hamming;
                }   
            }
        }
    }

    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        return manhattan;
   
    }

    private void manhattanCalculate() {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (board[i][j] != 0) {
                    int row = board[i][j] / dimension;
                    int column = (board[i][j] - 1) % dimension;
                    row -= i;
                    if (row < 0) {
                        row = -row;
                    }
                    column -= j;
                    if (column < 0) {
                        column = -column;
                    }
                    manhattan += row + column;
                }   
            }
        }
    }

    public boolean isGoal() {                // is this board the goal board?
        return hamming == 0;
    }

    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        Board temp = new Board(board);
        int i = 0;
        int j = 0;
        while (true) {
            if (temp.board[0][i] != 0) {
                if (j != 0) {
                    int element = temp.board[0][i];
                    temp.board[0][i] = temp.board[0][j];
                    temp.board[0][j] = element;
                    break;
                }
                j = i;
            }
            ++i;
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
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (board[i][j] != that.board[i][j]) {
                    return false;
                }    
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
                vectorBoardElement[iterationSize] = new Board(board);
                vectorBoardElement[iterationSize].board[row][column] = board[row][column - 1];
                vectorBoardElement[iterationSize].board[row][column - 1] = 0;
                ++iterationSize;
            }
            // top element
            if (row > 0) {
                vectorBoardElement[iterationSize] = new Board(board);
                vectorBoardElement[iterationSize].board[row][column] = board[row - 1][column];
                vectorBoardElement[iterationSize].board[row - 1][column] = 0;
                ++iterationSize;
            }
            // right element
            if (column < dimension - 1) {
                vectorBoardElement[iterationSize] = new Board(board);
                vectorBoardElement[iterationSize].board[row][column] = board[row][column + 1];
                vectorBoardElement[iterationSize].board[row][column + 1] = 0;
                ++iterationSize;
            }
            // bottom element
            if (row < dimension - 1) {
                vectorBoardElement[iterationSize] = new Board(board);
                vectorBoardElement[iterationSize].board[row][column] = board[row + 1][column];
                vectorBoardElement[iterationSize].board[row + 1][column] = 0;
                ++iterationSize;
            }
        }
        public boolean hasNext(){
            return iterationSize != 0;
        }

        public Board next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            --iterationSize;
            return vectorBoardElement[iterationSize];
        }
    }

    public String toString() {               // string representation of this board (in the output format specified below)
        String result = "";
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                result += board[i][j] + " ";
            }
            result += "\n";
        }        
        return result;
    }
    //public static void main(String[] args) // unit tests (not graded)
}