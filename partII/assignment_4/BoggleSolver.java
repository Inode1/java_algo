import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.lang.StringBuilder;
import java.util.Vector;

public class BoggleSolver
{
    private Node root = new Node(null);
    private String[] dict;
    private HashSet<String> result = new HashSet<String>(300);
    //private Stack<String> allFindPrefix = new Stack<String>();
    private int[] scourePoints = {0, 0, 0, 1, 1, 2, 3, 5, 11};
    //private Adjacent adj; 
    //private HashMap<String, Adjacent> prec = new HashMap<String, Adjacent>();
    private int[] boardMinus = new int[100];
    private int boardSize;
    private boolean[] alreadyBeing = new boolean[100];
    private int[][] adj;
    private int[] capacity;
    private int[] state = new int[100];
    private Vector<String> myArrayResult = new Vector<String>(500);
    private Vector<Node> alreadyNodeBeing = new Vector<Node>(500);
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root.back = root;
        int count = 0;
        dict = new String[dictionary.length];
        for (String word: dictionary) {
            if (word.length() < 3) {
                continue;
            }
            Node iterator = root;
            boolean flag = true;
            for (int i = 0; i < word.length(); ++i) {
                int symb = (int) word.charAt(i) - 65;
                // Q
                if (symb == 16) {
                    if (i + 1 < word.length() && (int) word.charAt(i + 1) - 65 == 20) {
                        i += 1;
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (iterator.next[symb] == null) {
                    iterator.next[symb] = new Node(iterator);
                }
                iterator = iterator.next[symb];
            }
            if (flag) {
                iterator.value = count;
                dict[count++] = word;
            }
        }
/*        for (int i = 1; i < 11; ++i) {
            for (int j = 1; j < 11; ++j) {
                String temp = "" + i + "x" + j; 
                prec.put(temp, new Adjacent(i, j));
            }
        }*/
        calculateAdjacent(4, 4);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
/*        for (Node n: alreadyNodeBeing) {
            n.being = false;
        }
        alreadyNodeBeing.clear();*/
        myArrayResult.clear();
/*        String temp = "" + board.rows() + "x" + board.cols();
        adj = prec.get(temp);
        if (adj == null) {
            System.out.println(temp);
            adj = new Adjacent(board.rows(), board.cols());
            prec.put(temp, adj);
        }*/


        boardSize = 0;
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                boardMinus[boardSize] = board.getLetter(i, j) - 65;
                ++boardSize;
            }
        }
/*        Node position;
        for (int i = 0; i < boardSize; ++i) {
                if ((position = root.next[boardMinus[i]]) == null) {
                    continue;
                }
                alreadyBeing[i] = true;
                if (this.board[i] == 'Q') {
                    deepFirstSearch(position, i, "" + this.board[i] + "U");
                }
                else
                {
                }
                    deepFirstSearch(position, i);
                alreadyBeing[i] = false;
        }*/
        nonRecur();
        return myArrayResult;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.) 
    public int scoreOf(String word) {
        int len = word.length();
        if (!inDictionary(word)) {
            len = 0;
        }
        if (len > 8) {
            return 11;
        }
        return scourePoints[len];
    }

    private class Node {
        public static final int RADIX = 26;
        public int value = -1;
        public Node[] next = new Node[RADIX];
        public int privCell = 0;
        public Node back;
        public boolean being;
        public Node(Node back) {
            this.back = back;
        }
    }

    private void calculateAdjacent(int row, int column) {
        adj = new int[row * column][8];
        capacity = new int[row * column];
        for (int i = 0; i < row - 1; ++i) {
            for (int j = 0; j < column; ++j) {
                int temp = i * column + j;
                add(temp, (i + 1) * column + j);
                if (j + 1 < column) {
                    add(temp, temp + 1);
                    add(temp, (i + 1) * column + j + 1);
                }
                if (j > 0) {
                    add(temp, (i + 1) * column + j - 1);
                }
            }
        }
        for (int i = row - 1; i < row; ++i) {
            for (int j = 0; j < column - 1; ++j) {
                add(i * column + j, i * column + j + 1);
            }
        }
    }

    private void add(int index, int value) {
            adj[index][capacity[index]++] = value;
            adj[value][capacity[value]++] = index;
    }

/*    private class Adjacent {
        public int[][] adj;
        public int[] capacity;
        public int[] position;
        public Adjacent(int row, int column) {
            adj = new int[row * column][8];
            capacity = new int[row * column];
            position = new int[row * column];
            for (int i = 0; i < row - 1; ++i) {
                for (int j = 0; j < column; ++j) {
                    int temp = i * column + j;
                    add(temp, (i + 1) * column + j);
                    if (j + 1 < column) {
                        add(temp, temp + 1);
                        add(temp, (i + 1) * column + j + 1);
                    }
                    if (j > 0) {
                        add(temp, (i + 1) * column + j - 1);
                    }
                }
            }
            for (int i = row - 1; i < row; ++i) {
                for (int j = 0; j < column - 1; ++j) {
                    add(i * column + j, i * column + j + 1);
                }
            }
        }

        

        // return -1 if non more elements
        public int get(int index, int pos) {
            return adj[index][pos];
        }

        public void clear(int index) {
            position[index] = 0;
        }

    }*/

    private void getAllWord(Node position, String prefix) {
        if (position.value != -1) {
            //dict.push(prefix);
        }
        for (int i = 0; i < Node.RADIX; ++i) {
            if (position.next[i] == null) {
                continue;
            } 
            getAllWord(position.next[i], prefix + (char) (i + 65));
        }   
    }

    private boolean inDictionary(String prefix) {
        Node position = root;
        for (int i = 0; i < prefix.length(); ++i) {
            position = position.next[prefix.charAt(i) - 65];
            if (prefix.charAt(i) == 'Q') {
                ++i;    
            }
            if (position == null) {
                return false;
            }
        }
        return position.value != -1; 
    }

    private void nonRecur() {
        Node position = root;
        Node next;
        int temp;
        for (int i = 0; i < boardSize; ++i) {
            if ((position = root.next[boardMinus[i]]) == null) {
                continue;
            }
            position.privCell = i;
            alreadyBeing[i] = true;

            // go throw all node
            while (position != root) {
                while (state[position.privCell] < capacity[position.privCell]) {
                    temp = adj[position.privCell][state[position.privCell]];
                    ++state[position.privCell];
                    if (position.next[boardMinus[temp]] == null) {
                        continue;
                    }
                    if (alreadyBeing[temp] == true) {
                        //System.out.println(k++);
                        continue;
                    }
                    position = position.next[boardMinus[temp]];
                    if (position.value != -1 && !position.being) {
                        myArrayResult.add(dict[position.value]);
                        //position.being = true;
                        //alreadyNodeBeing.add(position);
                    }
                    //System.out.println(prefix);
                    position.privCell = temp;
                    alreadyBeing[temp] = true;

                }
                alreadyBeing[position.privCell] = false;
                state[position.privCell] = 0;
                position = position.back;
        
                //}
            }

        }
    }
    private int counter = 0;
    private void deepFirstSearch(Node position, int i) {
        /*if (position.value && prefix.length() >= 3) {
                        result.add(prefix);
        }*/

        int left = capacity[i];
        for (int k = 0; k < left; ++k) {
            int temp = adj[i][k];
            Node next;
            if (alreadyBeing[temp] == true) {
                //System.out.println(counter++);
                continue;
            }
            if ((next = position.next[boardMinus[temp]]) == null){
                continue;
            }
            alreadyBeing[temp] = true;
            /*if (board[temp] == 'Q') {
                deepFirstSearch(next, temp, prefix + board[temp] + "U");
            } else
            {
            }*/
                deepFirstSearch(next, temp);
            
            
            alreadyBeing[temp] = false;
        }
    }

    public static void main(String[] args) {
        In in = new In("/home/ivan/workspace/Algorithm/partII/assignment_4/boggle/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        long start = System.nanoTime();
        BoggleSolver solver = new BoggleSolver(dictionary);
        start = System.nanoTime() - start;
        System.out.println("Elapsed time: " + start);
        start = System.nanoTime();
        BoggleBoard board = new BoggleBoard("/home/ivan/workspace/Algorithm/partII/assignment_4/boggle/board4x4.txt");
        int score = 0;
        int count = 0;
        //for (int i = 0; i < 30; ++i){
            for (String word : solver.getAllValidWords(board))
            {
                System.out.println(word);
                score += solver.scoreOf(word);
                ++count;
            }

        
        StdOut.println("Score = " + score + " Count = " + count);
        System.out.println("Elapsed time: " + (System.nanoTime() - start)/30);
    }
}