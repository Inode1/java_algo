public class Board {
    private int[][] board;
    private int dimension;
    private int emptyBlock = 0;
    public Board(int[][] blocks) {           // construct a board from an N-by-N array of blocks
        if (blocks = null) {
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

    }
                                            // (where blocks[i][j] = block in row i, column j)
    public int dimension(){                 // board dimension N
        return dimension;
    }
    public int hamming() {                  // number of blocks out of place
        int hamming = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (board[i][j] != 0 && board[i][j] != j + dimension * i + 1) {
                    ++hamming;
                }   
            }
        }
    }

    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int manhattan = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (board[i][j] != 0) {
                    int row = (board[i][j]) / dimension;
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
        return hamming() == 0;
    }

    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
/*        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                
            }
        }*/
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
        int iterationSize;
        private Neighbors {
            int row = emptyBlock % dimension;
            int column = emptyBlock / dimension;
            if (row == 0 || row == dimension - 1) {
                if (column == 0 || column == dimension - 1) {
                    return 
                }
            }
        }
        boolean hasNext(){

        }

        Board next() {
            
        }
    }

    public String toString() {               // string representation of this board (in the output format specified below)
        String result;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                string += board[i][j] + " ";
            }
            string += "\n";
        }        

    }
    //public static void main(String[] args) // unit tests (not graded)
}