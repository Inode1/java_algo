import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    private Digraph graph;
    private HashMap<String, Vector<Integer>> hashmap = new HashMap<String, Vector<Integer>>();
    private HashMap<Integer, Vector<String>> nouns = new HashMap<Integer, Vector<String>>();
    private SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In synsetFile = new In(synsets);
        String line;
        while (synsetFile.hasNextLine()) {
            line = synsetFile.readLine();
            String[] array = line.split(",");
            int vertix = Integer.parseInt(array[0]);
            String[] noun = array[1].split(" ");
            for (int i = 0; i < noun.length; ++i) {
                if (!hashmap.containsKey(noun[i])) {
                    hashmap.put(noun[i], new Vector<Integer>());
                }
                if (!nouns.containsKey(vertix)) {
                    nouns.put(vertix, new Vector<String>());
                }
                hashmap.get(noun[i]).add(vertix);
                nouns.get(vertix).add(noun[i]);
            }
        }
        graph = new Digraph(nouns.size());
        In hypernymsFile = new In(hypernyms);
        int state = 0;
        while (hypernymsFile.hasNextLine()) {
            line = hypernymsFile.readLine();
            String[] array = line.split(",");
            int first = Integer.parseInt(array[0]);
            for (int i = 1; i < array.length; ++i) {
                graph.addEdge(first, Integer.parseInt(array[i]));
            }
        }
        for (int i = 0; i < graph.V(); ++i) {
            if (graph.outdegree(i) == 0 && graph.indegree(i) != 0) {
                ++state;
            }
        }
        if (state > 1) {
            throw new java.lang.IllegalArgumentException();
        }

        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle()) {
            throw new java.lang.IllegalArgumentException();
        }
        sap = new SAP(graph);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        Vector<String> ret = new Vector<String>();
        Iterator it = hashmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ret.add((String)pair.getKey());
        }
        return ret;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (hashmap.get(word) == null) {
            return false;
        }
        return true;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Vector<Integer> a = hashmap.get(nounA);
        Vector<Integer> b = hashmap.get(nounB);
        if (a == null || b == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return sap.length(hashmap.get(nounA), hashmap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return nouns.get(distance(nounA, nounB)).elementAt(0);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet word = new WordNet(args[0], args[1]);
    }
}