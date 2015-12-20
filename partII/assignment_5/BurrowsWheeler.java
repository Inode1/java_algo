import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Huffman;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray example = new CircularSuffixArray(s);
        for (int i = 0; i < example.length(); ++i) {
            if (example.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < example.length(); ++i) {
            if (example.index(i) == 0) {
                BinaryStdOut.write(s.charAt(s.length() - 1));
                continue;    
            }
            BinaryStdOut.write(s.charAt(example.index(i) - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        bucket(BinaryStdIn.readString(), first);
    }

    private static void bucket(String s, int first){
        int length = s.length();
        int[] next = new int[length];

        char lowerBound = 0xffff;
        char upperBound = 0;
        char sybvol;
        int[] radix = new int[257];
        // sort radix
        for (int i = 0; i < length; ++i) {
            sybvol = s.charAt(i); 
            ++radix[sybvol + 1];
            if (sybvol < lowerBound) {
                lowerBound = sybvol;
            }

            if (sybvol > upperBound) {
                upperBound = sybvol;
            }
        }

        for (char i = lowerBound; i <= upperBound; ++i) { 
            radix[i + 1] += radix[i];
        }

        for (int i = 0; i < length; ++i) {
            next[radix[s.charAt(i)]++] = i;
        }

        first = next[first];
        for (int i = 0; i < length; ++i) {
            BinaryStdOut.write(s.charAt(first));
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) {
            BurrowsWheeler.encode();
        }
        else if (args[0].equals("+")) {
            BurrowsWheeler.decode();
        }
        else throw new IllegalArgumentException("Illegal command line argument");   
    }
}
