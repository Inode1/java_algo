public class Outcast {
    private WordNet net;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }
    // given an array of WordNet nouns, return an outcast         
    public String outcast(String[] nouns) {
        int[] array = new int[(nouns.length*nouns.length - nouns.length)/2];
        int i = 1;
        int j = 0;
        for (int start = 0; i < array.length; ++i) {
            int distance = net.distance(nouns[j], nouns[i]);
            array[j] += distance;
            array[i] += distance;
            if (i == nouns.length - 1) {
                ++j;
                i = j + 1;
            }
        }


        return "st";
    }
    // see test client below   
    public static void main(String[] args) {

    }  
}
