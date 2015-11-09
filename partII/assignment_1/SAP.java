import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.ArrayDeque;
import java.util.Arrays;

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
        DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle()) {
            throw new java.lang.IllegalArgumentException();
        }
        graph = new Digraph(G);
        if (!oneRoot()) {
            throw new java.lang.IllegalArgumentException();
        }
        marks = new boolean[graph.V()];
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
        //bfsMark(v);
        asyncSearch(v, w);
        if (path == Integer.MAX_VALUE) {
            return -1;
        }
        return path;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        //bfsMark(v);
        asyncSearch(v, w);
        if (ancestor == Integer.MAX_VALUE) {
            return -1;
        }
        return ancestor;
    }

    private void asyncSearch(Iterable<Integer> vIterable, Iterable<Integer> wIterable) {
        path = Integer.MAX_VALUE;
        ancestor = Integer.MAX_VALUE;
        // check not empty
        if (!vIterable.iterator().hasNext() || !wIterable.iterator().hasNext()) {
            return;
        }
        for (int i = 0; i < marks.length; ++i) {
            marks[i] = false;
            pathsCost[i] = Integer.MAX_VALUE;
        }

        // deque for first vertix
        ArrayDeque<Boolean> marksDeque = new ArrayDeque<Boolean>();
        ArrayDeque<Integer> vertixDeque = new ArrayDeque<Integer>();
        for (int noCost: vIterable) {
            validateData(noCost);
            if (!marks[noCost]) {
                marks[noCost] = true;
                vertixDeque.add(noCost);
                marksDeque.add(true);
                pathsCost[noCost] = 0;
                //System.out.println("Add: activeVertix: " + noCost + " activeMark: " + true);
            }
        }

        for (int noCost: wIterable) {
            validateData(noCost);
            if (marks[noCost]) {
                path = 0;
                ancestor = noCost;
                return;
            }
            // we previous get this element
            // no need put in deque
            if (pathsCost[noCost] != 0) {
                pathsCost[noCost] = 0;
                vertixDeque.add(noCost);
                marksDeque.add(false);
                //System.out.println("Add: activeVertix: " + noCost + " activeMark: " + false);
            }
        }

        boolean activeMark = true;
        int     activeVertix; 
        int     activePath;
        while (!vertixDeque.isEmpty()) {

            activeVertix = vertixDeque.remove();
            if (ancestor != Integer.MAX_VALUE && activeMark != marksDeque.getFirst()) {
                return;
            }
            activeMark  = marksDeque.remove();
            //System.out.println("remove: activeVertix: " + activeVertix + " activeMark: " + activeMark);    


            for (int w: graph.adj(activeVertix)) {
                activePath = pathsCost[w] + pathsCost[activeVertix] + 1;
                if (activeMark) {
                    if (!marks[w]) {
                        if (pathsCost[w] != Integer.MAX_VALUE) {
                            if (activePath < path) {
                                //System.out.println("NonMark" + activePath);
                                path = activePath;
                                ancestor = w;
                                continue;
                            }
                        }
                        marks[w]     = activeMark;
                        if (pathsCost[w] == Integer.MAX_VALUE) {
                            pathsCost[w] = pathsCost[activeVertix] + 1;
                        }
                        if (ancestor == Integer.MAX_VALUE) {
                            vertixDeque.add(w);
                        }
                        
                        marksDeque.add(activeMark);
                        //System.out.println("Add: activeVertix: " + w + " activeMark: " + activeMark);
                    }

                } else {
                    if (marks[w]) {
                        if (activePath < path) {
                            //System.out.println("Mark" + activePath);
                            path = activePath;
                            ancestor = w;
                            continue;
                        }
                    }
                    if (pathsCost[w] == Integer.MAX_VALUE) {
                        pathsCost[w] = pathsCost[activeVertix] + 1;
                        if (ancestor == Integer.MAX_VALUE) {
                            vertixDeque.add(w);
                        }
                        marksDeque.add(activeMark);
                        //System.out.println("Add: activeVertix: " + w + " activeMark: " + activeMark);
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
            if (marks[noCost]) {
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
                    if (marks[vertix]) {
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
