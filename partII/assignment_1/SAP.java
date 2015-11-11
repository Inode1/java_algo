import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.HashMap;

public class SAP {
    private ArrayList<Vector<Integer>> graph;
    private boolean[] marks;
    private boolean[] nonMarks;
    private int[] pathsCost;
    private int path;
    private int ancestor;
    private HashMap<Integer, HashMap<Integer, Cache>> cache;
    private ArrayDeque<Integer> needReset = new ArrayDeque<Integer>();
    // constructor takes a digraph (not necessarily a DAG)
    private class Cache {
        private int distance;
        private int ancestor;
        public Cache(int dist, int ancestor) {
            this.distance = dist;
            this.ancestor = ancestor;
        }
    }
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        int state = 0;
        //graph = (Vector<Integer>[]) new Object[G.V()];
        graph = new ArrayList<Vector<Integer>>(G.V());
        for (int i = 0; i < G.V(); ++i) {
            Vector<Integer> temp = new Vector<Integer>();
            for (int vertix: G.adj(i)) {
                temp.add(vertix);
            }
            graph.add(temp);
        }
        //System.out.println(pathsCost.length);

        /*DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle()) {
            throw new java.lang.IllegalArgumentException();
        }
        graph = new Digraph(G);*/
        cache = new HashMap<Integer, HashMap<Integer, Cache>>(graph.size());
        marks = new boolean[graph.size()];
        nonMarks = new boolean[graph.size()];
        pathsCost = new int[graph.size()];
        for (int i = 0; i < pathsCost.length; ++i) {
            pathsCost[i] = Integer.MAX_VALUE;
        }
        
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        //System.out.println(v);
        //System.out.println(w);
        Cache temp = checkCache(v, w); 
        if (temp != null) {
            return temp.distance;
        }
        asyncSearch(Arrays.asList(v), Arrays.asList(w));
        if (path == Integer.MAX_VALUE) {
            path = -1;
        }
        if (ancestor == Integer.MAX_VALUE) {
            ancestor = -1;
        }
        setCache(v, w, new Cache(path, ancestor));
        return path;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Cache temp = checkCache(v, w); 
        if (temp != null) {
            return temp.ancestor;
        }
        asyncSearch(Arrays.asList(v), Arrays.asList(w));
        if (path == Integer.MAX_VALUE) {
            path = -1;
        }
        if (ancestor == Integer.MAX_VALUE) {
            ancestor = -1;
        }
        setCache(v, w, new Cache(path, ancestor));
        return ancestor;
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

    private Cache checkCache(int v, int w) {
        int min = v;
        if (w < v) {
            min = w;
            w = v;
        }
        if (cache.containsKey(min)) {
            Cache temp = cache.get(min).get(w);
            if (temp != null) {
                return temp;
            }
        } else {
            cache.put(min, new HashMap<Integer, Cache>());            
        }
        return null;
    }

    private void setCache(int v, int w, Cache data) {
        int min = v;
        if (w < v) {
            min = w;
            w   = v;
        }
        cache.get(min).put(w, data);
    }

    private void asyncSearch(Iterable<Integer> vIterable, Iterable<Integer> wIterable) {
        path = Integer.MAX_VALUE;
        ancestor = Integer.MAX_VALUE;
        // check not empty
        if (vIterable == null || wIterable == null) {
            throw new java.lang.NullPointerException();
        }
        if (!vIterable.iterator().hasNext() || !wIterable.iterator().hasNext()) {
            return;
        }
        for (int i: needReset) {
            marks[i] = false;
            nonMarks[i] = false;
            pathsCost[i] = Integer.MAX_VALUE;
        }
        needReset.clear();

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
                needReset.add(noCost);
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
                nonMarks[noCost] = true; 
                vertixDeque.add(noCost);
                marksDeque.add(false);
                needReset.add(noCost);
                //System.out.println("Add: activeVertix: " + noCost + " activeMark: " + false);
            }
        }

        boolean activeMark = true;
        int     activeVertix; 
        int     activePath;
        while (!vertixDeque.isEmpty()) {

            activeVertix = vertixDeque.remove();
            activeMark  = marksDeque.remove();
            //System.out.println("remove: activeVertix: " + activeVertix + " activeMark: " + activeMark);    

            for (int w: graph.get(activeVertix)) {
                activePath = pathsCost[w] + pathsCost[activeVertix] + 1;
                if (activeMark) {
                    if (!marks[w]) {
                        if (nonMarks[w] && activePath < path) {
                            //System.out.println("NonMark" + activePath);
                            path = activePath;
                            ancestor = w;
                            if (pathsCost[w] == 0) {
                                return;
                            }
                        }
                        if (pathsCost[w] <= pathsCost[activeVertix] + 1) {
                            //System.out.println("Continue" + activePath);
                            continue;
                        }
                        marks[w]     = activeMark;
                        pathsCost[w] = pathsCost[activeVertix] + 1;
                        vertixDeque.add(w);
                        needReset.add(w);                        
                        marksDeque.add(activeMark);
                        //System.out.println("Add: activeVertix: " + w + " activeMark: " + activeMark);
                    } else {
                        if (pathsCost[w] > pathsCost[activeVertix] + 1) {
                            pathsCost[w] = pathsCost[activeVertix] + 1;
                            vertixDeque.add(w);
                            needReset.add(w);
                            marksDeque.add(activeMark);       
                        }
                    }

                } else {
                    if (!nonMarks[w]) {
                        if (marks[w]) {
                            if (activePath < path) {
                                //System.out.println("Mark" + activePath);
                                path = activePath;
                                ancestor = w;
                                if (pathsCost[w] == 0) {
                                    return;
                                }
                            }
                        }
                        nonMarks[w] = true;
                        pathsCost[w] = pathsCost[activeVertix] + 1;
                        vertixDeque.add(w);
                        needReset.add(w);
                        marksDeque.add(activeMark);
                    } else {
                        if (pathsCost[w] > pathsCost[activeVertix] + 1) {
                            pathsCost[w] = pathsCost[activeVertix] + 1;
                            vertixDeque.add(w);
                            needReset.add(w);
                            marksDeque.add(activeMark);       
                        }
                    }
                    //System.out.println("Add: activeVertix: " + w + " activeMark: " + activeMark);
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
