import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;
import java.util.Arrays;

public class BoggleSolver
{
    private Node root = new Node();
    private Stack<String> dict = new Stack<String>();
    private Stack<String> allFindPrefix = new Stack<String>();
    private int[] scourePoints = {0, 0, 0, 1, 1, 2, 3, 5, 11};
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
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                deepFirstSearch(i, j, board, "", new boolean[board.rows() * board.cols()]);
            }
        }

        for (String word: allFindPrefix) {
            if (word.length() < 3) {
                continue;
            }
            if (inDictionary(word)) {
                dict.push(word);
            }
        }
        return dict;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.) 
    public int scoreOf(String word) {
        int len = word.length();
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

    private boolean notInDict(String prefix) {
        Node position = root;
        for (int i = 0; i < prefix.length(); ++i) {
            position = position.next[prefix.charAt(i) - 65];
            if (position == null) {
                return false;
            }
        }
        return true;
    }

    private void deepFirstSearch(int i, int j, BoggleBoard board, String prefix, boolean[] alreadyBeing) {
        if (i < 0 || i >= board.rows()) {
            return;
        }
        if (j < 0 || j >= board.cols()) {
            return;
        }
        if (alreadyBeing[i * board.cols() + j] == false) {
            alreadyBeing[i * board.cols() + j] = true;
        }
        else
        {
            return;
        }
        prefix += board.getLetter(i, j);
        if (!notInDict(prefix)) {
            return;
        }
        allFindPrefix.push(prefix);
        deepFirstSearch(i - 1, j - 1, board, prefix, Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(i - 1, j, board, prefix, Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(i - 1, j + 1, board, prefix, Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(i, j - 1, board, prefix, Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(i, j + 1, board, prefix, Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(i + 1, j - 1, board, prefix, Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(i + 1, j, board, prefix, Arrays.copyOf(alreadyBeing, alreadyBeing.length));
        deepFirstSearch(i + 1, j + 1, board, prefix, Arrays.copyOf(alreadyBeing, alreadyBeing.length));

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}