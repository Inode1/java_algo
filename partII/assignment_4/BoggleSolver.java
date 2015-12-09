import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver
{
    private Node root = new Node();
    private Stack<String> dict = new Stack<String>();
    private HashSet<String> result = new HashSet<String>();
    //private Stack<String> allFindPrefix = new Stack<String>();
    private int[] scourePoints = {0, 0, 0, 1, 1, 2, 3, 5, 11};
    private int[][] precomputeArray;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        int index = 0;
        for (String word: dictionary) {
            Node iterator = root;
            for (int i = 0; i < word.length(); ++i) {
                if (iterator.next[(int) word.charAt(i) - 65] == null) {
                    iterator.next[(int) word.charAt(i) - 65] = new Node();
                }
                iterator = iterator.next[(int) word.charAt(i) - 65];
            }
            iterator.value = ++index; 
        }
        //getAllWord(root, "");
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        result.clear();
        precomputeArray = new int[board.rows() * board.cols()][9];
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                int p = 0;
                boolean isGood = false;
                int number = i * board.cols() + j;
                if (i > 0) {
                    precomputeArray[number][p++] = (i - 1) * board.cols() + j;
                    if (i + 1 < board.rows()) {
                        precomputeArray[number][p++] = (i + 1) * board.cols() + j;
                        isGood = true;
                    }
                    if (j > 0) {
                       precomputeArray[number][p++] = i * board.cols() + j - 1;
                       if (isGood) {
                        precomputeArray[number][p++] = (i + 1) * board.cols() + j - 1; 
                       } 
                       precomputeArray[number][p++] = (i - 1) * board.cols() + j - 1; 
                    }
                    if (j + 1 < board.cols()) {
                       precomputeArray[number][p++] = i * board.cols() + j + 1;
                       if (isGood) {
                        precomputeArray[number][p++] = (i + 1) * board.cols() + j + 1; 
                       } 
                       precomputeArray[number][p++] = (i - 1) * board.cols() + j + 1; 
                    }
                }
                else {
                    if (i + 1 < board.rows()) {
                        precomputeArray[number][p++] = (i + 1) * board.cols() + j;
                        isGood = true;
                    }
                    if (j > 0) {
                       precomputeArray[number][p++] = i * board.cols() + j - 1;
                       if (isGood) {
                        precomputeArray[number][p++] = (i + 1) * board.cols() + j - 1; 
                       }
                    }
                    if (j + 1 < board.cols()) {
                       precomputeArray[number][p++] = i * board.cols() + j + 1;
                       if (isGood) {
                        precomputeArray[number][p++] = (i + 1) * board.cols() + j + 1; 
                       } 
                    }
                }
                precomputeArray[number][p] = -1;
            }
        }
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                deepFirstSearch(root, i * board.cols() + j, board, new StringBuffer(board.rows() * board.cols()), new boolean[board.rows() * board.cols()]);
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
        public Object value;
        public Node[] next = new Node[RADIX]; 
    }

    private void getAllWord(Node position, String prefix) {
        if (position.value != null) {
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
            if (position == null) {
                return false;
            }
        }
        if (position.value == null) {
            return false;
        }
        // need prefix push in all valid word
        return true;
    }

    private Node notInDict(Node position, int i) {
        return position.next[i];
    }

    private void deepFirstSearch(Node position, int i, BoggleBoard board, StringBuffer prefix, boolean[] alreadyBeing) {
        alreadyBeing[i] = true;
        position = notInDict(position, board.getLetter(i / board.rows(), i % board.rows()) - 65);
        if (position == null) {
            return;
        }
        prefix.append(board.getLetter(i / board.rows(), i % board.rows()));
        if (board.getLetter(i / board.rows(), i % board.rows()) == 'Q') {
            position = notInDict(position, (int) 'U' - 65);
            if (position == null) {
                return;
            }
            prefix.append("U");
        }
        
        if (position.value != null && prefix.length() >= 3) {
            //dict.push(prefix.toString());
            result.add(prefix.toString());
        }

        for (int k = 0; k < 8 && precomputeArray[i][k] != -1; ++k) {
            int temp = precomputeArray[i][k];
            if (alreadyBeing[temp] == true) {
                continue;
            }
            deepFirstSearch(position, temp ,board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        }
/*        deepFirstSearch(position, i - 1, j - 1, board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(position, i - 1, j, board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(position, i - 1, j + 1, board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(position, i, j - 1, board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(position, i, j + 1, board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(position, i + 1, j - 1, board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(position, i + 1, j, board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(position, i + 1, j + 1, board, new StringBuffer(prefix), Arrays.copyOf(alreadyBeing, alreadyBeing.length));*/

    }

    public static void main(String[] args) {
        double start = System.nanoTime();
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int count = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
            ++count;
        }
        StdOut.println("Score = " + score + " Count = " + count);
        System.out.printf("Elapsed time: %f\n", System.nanoTime() - start);
    }
}