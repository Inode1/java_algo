import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BoggleSolver
{
    private Node root = new Node();
    private Stack<String> dict = new Stack<String>();
    private HashSet<String> result = new HashSet<String>();
    //private Stack<String> allFindPrefix = new Stack<String>();
    private int[] scourePoints = {0, 0, 0, 1, 1, 2, 3, 5, 11};
    private Adjacent adj; 
    private HashMap<String, Adjacent> prec = new HashMap<String, Adjacent>();
    private String onlyOne = "";
    private BoggleBoard board;
    private boolean[] alreadyBeing;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word: dictionary) {
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
                    iterator.next[symb] = new Node();
                }
                iterator = iterator.next[symb];
            }
            if (flag) {
                iterator.value = true; 
            }
        }
        for (int i = 1; i < 11; ++i) {
            for (int j = 1; j < 11; ++j) {
                String temp = "" + i + "x" + j; 
                prec.put(temp, new Adjacent(i, j));
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        result.clear();
        String temp = "" + board.rows() + "x" + board.cols();
        adj = prec.get(temp);
        if (adj == null) {
            System.out.println(temp);
            adj = new Adjacent(board.rows(), board.cols());
            prec.put(temp, adj);
        }

        this.board = board; 
        Node position;
        alreadyBeing = new boolean[board.rows() * board.cols()];
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                position = root.next[board.getLetter(i, j) - 65];
                if (position == null) {
                    continue;
                }
                int p = i * board.cols() + j;
                alreadyBeing[p] = true;
                /*if (board.getLetter(i, j) == 'Q') {
                    deepFirstSearch(position, i * board.cols() + j, "" + board.getLetter(i, j) + "U", alreadyBeing);
                }
                else
                {
                }*/
                    deepFirstSearch(position, p);
                alreadyBeing[p] = false;
            }
        }
        return result;
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
        private static final int RADIX = 26;
        private boolean value;
        private Node[] next = new Node[RADIX];
    }

    private class Adjacent {
        private int[][] adj;
        private int[] capacity;
        private int[] position;
        private Adjacent(int row, int column) {
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

        private void add(int index, int value) {
            adj[index][capacity[index]++] = value;
            adj[value][capacity[value]++] = index;
        }

        // return -1 if non more elements
        private int get(int index, int pos) {
            /*if (capacity[index] == position[index]) {
                return -1;
            }*/
            if (pos == capacity[index]) {
                return -1;
            }
            return adj[index][pos];
        }

        private void clear(int index) {
            position[index] = 0;
        }

    }

    private void getAllWord(Node position, String prefix) {
        if (position.value) {
            dict.push(prefix);
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
        return position.value; 
    }

/*    private void deepFirstSearch(BoggleBoard board) {
        boolean[] alreadyBeing = new boolean[board.rows() * board.cols()];
        //StringBuffer prefix = new StringBuffer(20);
        
        Stack<Integer> alreadyBeingUpdate = new Stack<Integer>();
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                //String prefix = "";
                int start = i * board.cols() + j;
                char symb = board.getLetter(i, j);
                Node nodePositon = root.next[symb - 65];
                    /*for (int z = 0; z < board.rows() * board.cols(); ++z) {
                        //adj.clear(z);
                        System.out.println(alreadyBeingUpdate.size());
                        //alreadyBeing[z] = false;
                    }*/

                                    /*alreadyBeing[start] = true;
                alreadyBeingUpdate.push(start);

                if (nodePositon == null) {
                    continue;
                }
                //prefix.append(symb);
                /*prefix += symb;
                if (symb == 'Q') {
                    //prefix.append('U');
                    prefix += 'U';
                }
                alreadyBeing[start] = true;
                alreadyBeingUpdate.push(start);

                while (nodePositon != root) {
                    start = adj.get(start);
                    if (start == -1) {
                        start = alreadyBeingUpdate.pop();
                        alreadyBeing[start] = false;
                        nodePositon = nodePositon.back;
                        adj.clear(start);
                        if (nodePositon == root) {
                            break;
                        }
                        /*if (board.getLetter(start / board.cols(), start % board.cols()) == 'Q') {
                            //prefix.setLength(prefix.length() - 2);
                            prefix = prefix.substring(0, prefix.length() - 2);
                        } else
                        {
                            //prefix.setLength(prefix.length() - 1);
                             prefix = prefix.substring(0, prefix.length() - 1);
                        }
                        start = alreadyBeingUpdate.peek();
                        continue;
                    }
                    symb = board.getLetter(start / board.cols(), start % board.cols());
                    if (alreadyBeing[start] == true || nodePositon.next[symb - 65] == null) {
                        start = alreadyBeingUpdate.peek();
                        continue;
                    }
                    if (nodePositon.next[symb - 65] == null) {
                        start = alreadyBeingUpdate.peek();
                        continue;
                    }
                    nodePositon = nodePositon.next[symb - 65];
                    //prefix.append(symb);
                    /*prefix += symb;
                    if (symb == 'Q') {
                        //prefix.append('U');
                        prefix += 'U';
                    }
                    alreadyBeing[start] = true;
                    alreadyBeingUpdate.push(start);
                    /*if (nodePositon.value && prefix.length() >= 3) {
                        result.add(prefix);
                    }
                }  
            }
        }
    }
*/

/*    private void deepFirstSearch(BoggleBoard board) {
        Stack<String> prefixString = new Stack<String>();
        prefixString.ensureCapacity(1000); 
        Stack<Integer> alreadyBeingUpdate = new Stack<Integer>();
        alreadyBeingUpdate.ensureCapacity(1000); 

        boolean[] alreadyBeing = new boolean[board.rows() * board.cols()];
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                int start = i * board.cols() + j;
                Node position = root;
                
                alreadyBeingUpdate.push(start);
                
                prefixString.push("" + board.getLetter(i, j));
                while (!prefixString.isEmpty()) {

                    String prefix = prefixString.pop();
                    prefixString.push("");
                    char sybvols  = prefix.charAt(prefix.length() - 1);
                    start = alreadyBeingUpdate.peek();
                    alreadyBeing[start] = true;

                    if (position.next[sybvols - 65] == null) {
                        int count1 = 0;
                        while (!prefixString.isEmpty() && prefixString.peek() == "") {
                            alreadyBeing[alreadyBeingUpdate.pop()] = false;
                            prefixString.pop();
                            if (count1 > 0)
                            {
                                position = position.back;
                            }
                            count1++;
                        }
                        continue;
                    }
                    position = position.next[sybvols - 65];

                    
                    if (sybvols == 'Q') {
                        if (position.next['U' - 65] == null) {
                            int count1 = 0;
                            while (!prefixString.isEmpty() && prefixString.peek() == "") {
                                alreadyBeing[alreadyBeingUpdate.pop()] = false;
                                prefixString.pop();
                                if (count1 > 0)
                                {
                                    position = position.back;
                                }
                                count1++;
                            }
                            continue;
                        }
                        position = position.next['U' - 65];
                        prefix += 'U';
                    }

                    if (position.value && prefix.length() >= 3) {
                        result.add(prefix);
                    }

                    //for (int k = 0; k < 8 && precomputeArray[start][k] != -1; ++k) {
                    for (int temp: precompute.get(start)) {
                        //int temp = precomputeArray[start][k];
                        if (alreadyBeing[temp] == true) {
                            continue;
                        }
                        //alreadyBeing[start] = true;
                        alreadyBeingUpdate.push(temp);
                        prefixString.push(prefix + board.getLetter(temp / board.cols(), temp % board.cols()));
                    }
                    while (!prefixString.isEmpty() && prefixString.peek() == "") {
                        alreadyBeing[alreadyBeingUpdate.pop()] = false;
                        prefixString.pop();
                            position = position.back;
                    }

                }
            }
        }

    }*/

    private void deepFirstSearch(Node position, int i) {
        /*if (position.value && prefix.length() >= 3) {
                        result.add(prefix);
        }*/


        for (int k = 0; k < 8 && adj.get(i, k) != -1; ++k) {
            int temp = adj.get(i, k);
            int j = temp / board.cols();
            int z = temp % board.cols();
            Node next;
            if (alreadyBeing[temp] == true || (next = position.next[board.getLetter(j, z) - 65]) == null) {
                continue;
            }
            alreadyBeing[temp] = true;
            /*if (board.getLetter(j, z) == 'Q') {
                deepFirstSearch(position.next[board.getLetter(j, z) - 65], temp, prefix + board.getLetter(j, z) + "U", alreadyBeing);
            } else
            {
            }*/
                deepFirstSearch(next, temp);
            
            
            alreadyBeing[temp] = false;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        long start = System.nanoTime();
        BoggleSolver solver = new BoggleSolver(dictionary);
        start = System.nanoTime() - start;
        System.out.println("Elapsed time: " + start);
        start = System.nanoTime();
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int count = 0;
        for (String word : solver.getAllValidWords(board))
        {
            //System.out.println(word);
            score += solver.scoreOf(word);
            ++count;
        }
        StdOut.println("Score = " + score + " Count = " + count);
        System.out.println("Elapsed time: " + (System.nanoTime() - start));
    }
}