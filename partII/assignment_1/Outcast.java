import edu.princeton.cs.algs4.In;

public class Outcast {
    private WordNet net;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }
    // given an array of WordNet nouns, return an outcast         
    public String outcast(String[] nouns) {
        int[] array = new int[nouns.length];
        int i = 1;
        int j = 0;  
        for (int start = 0; start < (nouns.length*nouns.length - nouns.length) / 2; ++start, ++i) {
            int distance = net.distance(nouns[j], nouns[i]);
            array[j] += distance;
            array[i] += distance;
            if (i == nouns.length - 1) {
                ++j;
                i = j;
            }
        }
        int position = 0;
        int max = array[0];
        for (int it = 1; it < array.length; it++) {
            if (array[it] > max) {
                max = array[it];
                position = it;
            }
        }
        return nouns[position];
    }
    // see test client below   
    public static void main(String[] args) {
        WordNet word = new WordNet(args[0], args[1]);
        Outcast wild = new Outcast(word);
        System.out.println(args[2]);
        In fileile = new In(args[2]);
        String[] temp = fileile.readAllStrings();
        System.out.println("Sap" + wild.outcast(temp));
    }  
}
