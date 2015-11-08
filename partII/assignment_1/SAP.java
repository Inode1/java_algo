import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.ArrayDeque;
import java.util.Arrays;

public class SAP {
    private Digraph graph;
    private boolean[] marksFirst;
    private int[] pathsCost;
    private int path;
    private int ancestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle()) {
            throw new java.lang.IllegalArgumentException();
        }
        graph = new Digraph(G);
        if (!oneRoot()) {
            throw new java.lang.IllegalArgumentException();
        }
        marksFirst = new boolean[graph.V()];
        pathsCost = new int[graph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    // length of shortest ancestral path betMath.ween any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        bfsMark(v);
        bfsSearch(w);
        if (path == Integer.MAX_VALUE) {
            return -1;
        }
        return path;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        bfsMark(v);
        bfsSearch(w);
        if (ancestor == Integer.MAX_VALUE) {
            return -1;
        }
        return ancestor;
    }

    private void bfsMark(Iterable<Integer> vertixIterable) {
        path = Integer.MAX_VALUE;
        ancestor = Integer.MAX_VALUE;
        for (int i = 0; i < marksFirst.length; ++i) {
            marksFirst[i] = false;
            pathsCost[i] = Integer.MAX_VALUE;
        }

        for (int noCost: vertixIterable) {
            //System.out.println(noCost);
            validateData(noCost);
            pathsCost[noCost] = 0;
        }

        ArrayDeque<Integer> q = new ArrayDeque<Integer>();
        for (int v: vertixIterable) {
            if (marksFirst[v]) {
                continue;
            }

            q.add(v);
            while (!q.isEmpty()) {
                v = q.remove();
                marksFirst[v] = true;
                for (int vertix: graph.adj(v)) {
                    if (!marksFirst[vertix]) {
                        q.add(vertix);
                        pathsCost[vertix] = Math.min(pathsCost[v] + 1, pathsCost[vertix]);
                    }
                }
            }
        }

    }

    private boolean oneRoot() {
        int state = 0;
        for (int i = 0; i < graph.V(); ++i) {

            if (graph.outdegree(i) == 0 && graph.indegree(i) != 0) {
                
                //System.out.println(i);
                ++state;
            }
        }
        if (state > 1) {
            return false;
        }
        return true;
    }
/*
    // depth first search from v
    private void dfs() {
        count++;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }*/

    private void bfsSearch(Iterable<Integer> vertixIterable) {
        /*if (marks[w]) {
            ancestor = w;
            path     = pathsCost[w];
            return;
        }*/
        for (int noCost: vertixIterable) {
            if (marksFirst[noCost]) {
                if (pathsCost[noCost] < path) {
                    ancestor = noCost;
                    path     = pathsCost[noCost];
                } 
            }
            pathsCost[noCost] = 0;            
        }

        //int pathCost = 0;
        
        for (int w: vertixIterable) {
            ArrayDeque<Integer> q = new ArrayDeque<Integer>();
            q.add(w);

            while (!q.isEmpty()) {
                w = q.remove();
                for (int vertix: graph.adj(w)) {
                    if (marksFirst[vertix]) {
                        int newPath = pathsCost[vertix] + pathsCost[w] + 1;
                        if (newPath < path) {
                            ancestor = vertix;
                            path     = newPath;
                        } 
                    }
                    else {
                        q.add(vertix);
                        pathsCost[vertix] = Math.min(pathsCost[w] + 1, pathsCost[vertix]);
                    }
                }
            }
        }

    }

    private void validateData(int i) {
        if (i < 0 || i > marksFirst.length - 1) {
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
