import java.util.Vector;
import java.util.HashMap;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private Digraph graph;
    private HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
    private Vector<Vector<String>> nouns = new Vector<Vector<String>>();
    private SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In synsetFile = new In(synsets);
        String line;
        while (synsetFile.hasNextLine()) {
            line = synsetFile.readLine();
            String[] array = line.split(",");
            int vertix = Integer.parseInt(array[0]);
            nouns.add(new Vector<String>());
            String[] noun = array[1].split(" ");
            for (int i = 0; i < noun.length; ++i) {
                hashmap.put(noun[i], vertix);
                nouns.elementAt(vertix).add(noun[i]);
            }
        }
       /* for (Vector<String> vec: nouns) {
            for (String st: vec) {
                System.out.println(st);
            }
        }*/
        graph = new Digraph(nouns.size());
        In hypernymsFile = new In(hypernyms);
        while (hypernymsFile.hasNextLine()) {
            line = hypernymsFile.readLine();
            String[] array = line.split(",");
            int first = Integer.parseInt(array[0]);
            for (int i = 1; i < array.length; ++i) {
                graph.addEdge(first, Integer.parseInt(array[i]));
            }
        }
        sap = new SAP(graph);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        Vector<String> ret = new Vector<String>();
        for (Vector<String> vec: nouns) {
            for (String st: vec) {
                ret.add(st);
            }
        }
        return ret;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (hashmap.get(word) == null) {
            return false;
        }
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Integer a = new Integer(hashmap.get(nounA));
        Integer b = new Integer(hashmap.get(nounB));
        if (a == null || b == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return sap.length(hashmap.get(nounA), hashmap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return nouns.elementAt(distance(nounA, nounB)).elementAt(0);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet word = new WordNet(args[0], args[1]);
    }
}