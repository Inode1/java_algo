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
    private HashMap<HashData, HashMap<HashData, Cache>> cache = 
            new HashMap<HashData, HashMap<HashData, Cache>>();
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

    private class HashData {
        private int hashCode;
        public HashData(Iterable<Integer> data) {
            for (int i: data) {
                hashCode = 31 * hashCode + i; 
            }
        }

        public int hashCode() {
            return hashCode;
        }
     
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            HashData other = (HashData) obj;
            if (hashCode != other.hashCode) {
                return false;
            }
            return true;
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
        if (v == w) {
            return 0;
        }
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v == w) {
            return v;
        }
        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    // length of shortest ancestral path betMath.ween any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        //HashData first = new HashData(v);
        //HashData second = new HashData(w);
        //Cache temp = checkCache(first, second); 
        //if (temp != null) {
        //   return temp.distance;
        //}
        asyncSearch(v, w);
        if (path == Integer.MAX_VALUE) {
            path = -1;
        }
        //if (ancestor == Integer.MAX_VALUE) {
        //    ancestor = -1;
        //}
        //setCache(first, second, new Cache(path, ancestor));
        return path;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        //HashData first = new HashData(v);
        //HashData second = new HashData(w);
        //Cache temp = checkCache(first, second); 
        //if (temp != null) {
        //    return temp.ancestor;
        //}
        asyncSearch(v, w);
        //if (path == Integer.MAX_VALUE) {
        //    path = -1;
        //}
        if (ancestor == Integer.MAX_VALUE) {
            ancestor = -1;
        }
        //setCache(first, second, new Cache(path, ancestor));
        return ancestor;
    }

    private Cache checkCache(HashData v, HashData w) {
        if (w.hashCode < v.hashCode) {
            HashData temp = v;
            v = w;
            w = temp;
        }  
        if (cache.containsKey(v)) {
            Cache temp = cache.get(v).get(w);
            if (temp != null) {
                //System.out.println("Cache");
                return temp;
            }
        } else {
            cache.put(v, new HashMap<HashData, Cache>());         
        }
        return null;
    }

    private void setCache(HashData v, HashData w, Cache data) {
        if (w.hashCode < v.hashCode) {
            HashData temp = v;
            v = w;
            w = temp;
        }       
        cache.get(v).put(w, data);
    }

    private void asyncSearch(Iterable<Integer> vIterable, Iterable<Integer> wIterable) {
        path = Integer.MAX_VALUE;
        ancestor = Integer.MAX_VALUE;
        
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
        ArrayList<Integer> first = new ArrayList<Integer>();
        first.add(1);
        first.add(2);
        first.add(3);
        ArrayList<Integer> second = new ArrayList<Integer>();
        second.add(4);
        second.add(5);
        second.add(6);
        //while (!StdIn.isEmpty()) {
/*              int v = StdIn.readInt();
            int w = StdIn.readInt();*/
            int length   = sap.length(1, 5);
            int ancestor = sap.ancestor(5, 1);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }

}
