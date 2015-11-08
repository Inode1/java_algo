public class Outcast {
    private WordNet net;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }
    // given an array of WordNet nouns, return an outcast         
    public String outcast(String[] nouns) {
        int[] array = new int[nouns.length*nouns.length];
        int i = 0;
        for (int j = 0; j < array.length; ++j) {
            if (i == j % nouns.length) {
                array[j] = 0;
            }
            else if (i > j) {
                //int dst = net.distance(i, j % array.length);

                if (j % nouns.length == nouns.length - 1) {
                    ++i;
                }
            }
        }
        return "st";
    }
    // see test client below   
    public static void main(String[] args) {

    }  
}
