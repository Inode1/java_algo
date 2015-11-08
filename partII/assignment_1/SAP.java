import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayDeque;

public class SAP {
    private Digraph graph;
    private boolean[] marks;
    private int[] pathsCost;
    private int path;
    private int ancestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        graph = new Digraph(G);
        marks = new boolean[graph.V()];
        pathsCost = new int[graph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateData(v);
        validateData(w);
        bfsMark(v);
        bfsSearch(w);
        return path;

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateData(v);
        validateData(w);
        bfsMark(v);
        bfsSearch(w);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        //validateData(v);
        //validateData(w);
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return -1;
    }

    private void bfsMark(Iterable<Integer> v) {
        path = -1;
        ancestor = -1;
        for (int i = 0; i < marks.length; ++i) {
            marks[i] = false;
            pathsCost[i] = Integer.MAX_VALUE;
        }

        for (int noCost: v){
            pathsCost[i] = 0;
        }

        int pathCost = 0;
        ArrayDeque<Integer> q = new ArrayDeque<Integer>();
        q.add(v);
        pathsCost[v] = 0;
        while (!q.isEmpty()) {
            v = q.remove();
            pathCost = pathsCost[v] + 1;
            marks[v] = true;
            for (int vertix: graph.adj(v)) {
                if (!marks[vertix]) {
                    q.add(vertix);
                    pathsCost[vertix] = pathCost;
                }
            }
        }
    }

    private void bfsSearch(int w) {
        if (marks[w]) {
            ancestor = w;
            path     = pathsCost[w];
            return;
        }

        int pathCost = 0;
        ArrayDeque<Integer> q = new ArrayDeque<Integer>();
        q.add(w);
        pathsCost[w] = 0;

        while (!q.isEmpty()) {
            w = q.remove();
            pathCost = pathsCost[w] + 1;
            for (int vertix: graph.adj(w)) {
                if (marks[vertix]) {
                    ancestor = vertix;
                    path     =  pathsCost[vertix] + pathCost;
                    return;
                }
                else {
                    q.add(vertix);
                    pathsCost[vertix] = pathCost;
                }
            }
        } 
    }

    private void validateData(int i) {
        if (i < 0 || i > marks.length - 1) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
