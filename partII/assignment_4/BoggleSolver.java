import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver
{
    private Node root = new Node(null);
    private Stack<String> dict = new Stack<String>();
    private HashSet<String> result = new HashSet<String>();
    //private Stack<String> allFindPrefix = new Stack<String>();
    private int[] scourePoints = {0, 0, 0, 1, 1, 2, 3, 5, 11};
    private int[][] precomputeArray;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root.back = root;
        for (String word: dictionary) {
            Node iterator = root;
            for (int i = 0; i < word.length(); ++i) {
                if (iterator.next[(int) word.charAt(i) - 65] == null) {
                    iterator.next[(int) word.charAt(i) - 65] = new Node(iterator);
                }
                iterator = iterator.next[(int) word.charAt(i) - 65];
            }
            iterator.value = true; 
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

/*        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                for (int k = 0; k < 8 && precomputeArray[i * board.cols() + j][k] != -1; ++k) {
                    System.out.print(precomputeArray[i * board.cols() + j][k] + " ");
                }
                System.out.println();
            }
        }*/
        deepFirstSearch(board);
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
        private Node(Node back) {
            this.back = back;
        }
        private boolean value;
        private Node[] next = new Node[RADIX];
        private Node back; 
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
            if (position == null) {
                return false;
            }
        }
        return position.value; 
    }

    private void deepFirstSearch(BoggleBoard board) {
        Stack<String> prefixString = new Stack<String>(); 
        Stack<Integer> alreadyBeingUpdate = new Stack<Integer>();

        boolean[] alreadyBeing = new boolean[board.rows() * board.cols()];
        int count = 0;
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
                            //System.out.println("True");
                            }
                            continue;
                        }
                        position = position.next['U' - 65];
                        prefix += 'U';
                    } 

                    if (position.value && prefix.length() >= 3) {
                        result.add(prefix);
                    }

                    for (int k = 0; k < 8 && precomputeArray[start][k] != -1; ++k) {
                        int temp = precomputeArray[start][k];
                        if (alreadyBeing[temp] == true) {
                            continue;
                        }
                        //alreadyBeing[start] = true;
                        alreadyBeingUpdate.push(temp);
                        prefixString.push(prefix + board.getLetter(temp / board.cols(), temp % board.cols()));
                    }
                    int count1 = 0;
                    while (!prefixString.isEmpty() && prefixString.peek() == "") {
                        alreadyBeing[alreadyBeingUpdate.pop()] = false;
                        prefixString.pop();
                            if (count1 > 0)
                            {
                                position = position.back;
                            }
                            ++count1;
                    }

                }
                for (boolean value: alreadyBeing) {
                        System.out.print(value);
                }
                System.out.println();
            }
        }

    }

/*    private void deepFirstSearch(Node position, int i, BoggleBoard board, String prefix, boolean[] alreadyBeing) {
        alreadyBeing[i] = true;
        int j = i / board.cols();
        int z = i % board.cols();
        position = position.next[board.getLetter(j, z) - 65];
        if (position == null) {
            return;
        }
        prefix += board.getLetter(j, z);
        if (board.getLetter(j, z) == 'Q') {
            position = position.next[(int) 'U' - 65];
            if (position == null) {
                return;
            }
            prefix += "U";
        }
        
        if (position.value && prefix.length() >= 3) {
            //dict.push(prefix.toString());
            result.add(prefix);
        }

        for (int k = 0; k < 8 && precomputeArray[i][k] != -1; ++k) {
            int temp = precomputeArray[i][k];
            if (alreadyBeing[temp] == true) {
                continue;
            }
            deepFirstSearch(position, temp, board, prefix, alreadyBeing);
            alreadyBeing[temp] = false;
        }
    }*/

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
            StdOut.println(word);
            score += solver.scoreOf(word);
            StdOut.println(score);
            ++count;
        }
        StdOut.println("Score = " + score + " Count = " + count);
        System.out.println("Elapsed time: " + (System.nanoTime() - start));
    }
}