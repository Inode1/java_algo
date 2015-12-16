import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final char ALPH = 256;
    private static char[] characters = new char[ALPH];
    private static char[] indexs = new char[ALPH];
    private static char lastNullElement = 0;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        initStruct();

        while (!BinaryStdIn.isEmpty()) {
            char symbol = BinaryStdIn.readChar();
            //System.out.println(symbol);
            BinaryStdOut.write(indexs[symbol]);
            shift(symbol);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        initStruct();

        while (!BinaryStdIn.isEmpty()) {
            char symbol = BinaryStdIn.readChar();
            BinaryStdOut.write(characters[symbol]);
            shift(characters[symbol]);
                    /*for (char i = 0; i < ALPH; ++i) {
            System.out.print(" " + (int)(characters[i]));
        }
        	System.out.print("   " + (int)(symbol) + "\n");*/
        }

        BinaryStdOut.close();
    }
    
    private static void initStruct() {
        for (char i = 0; i < ALPH; ++i) {
            indexs[i] = i;
            characters[i] = i;
        }
    }

    private static void shift(char symbol) {
        /*if (lastNullElement > indexs[symbol])
        {
            ++indexs[lastNullElement];
        }*/

        for (char i = 0; i < indexs[symbol]; ++i) {
            ++indexs[characters[i]];
        } 
        for (char i = indexs[symbol]; i > 0; --i) {
        	characters[i] = characters[i - 1];
        }
        characters[0] = symbol;
        indexs[symbol] = 0;
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");	
    }
}