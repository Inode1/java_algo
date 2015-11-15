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
    //private ArrayList<Vector<Integer>> graph;

    private int[] graphVertixArray;
    private int[] graphEdgesArray;
    private ArrayDeque<Integer> vertixDeque;
    private ArrayDeque<Boolean> marksDeque;

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
        graphVertixArray = new int[G.V()];
        graphEdgesArray = new int[G.E()];
        int totalEdgeSize = 0;
        for (int v = 0, e = 0; v < G.V(); ++v) {
            graphVertixArray[v] = totalEdgeSize;
            for (int edge: G.adj(v)) {
                graphEdgesArray[e] = edge; 
                ++e;
            }
            totalEdgeSize += G.outdegree(v); 
        }
        marks = new boolean[G.V()];
        nonMarks = new boolean[G.V()];
        pathsCost = new int[G.V()];
        vertixDeque = new ArrayDeque<Integer>(G.V());
        marksDeque = new ArrayDeque<Boolean>(G.V());
    }

    private int[] getEdges(int vertixPosition) {
        int to = 0;
        // last element to == total size
        if (vertixPosition == graphVertixArray.length - 1) {
            to = graphEdgesArray.length; 
        } else {
            to = graphVertixArray[vertixPosition + 1];
        }
        return Arrays.copyOfRange(graphEdgesArray, graphVertixArray[vertixPosition], to);
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
        HashData first = new HashData(v);
        HashData second = new HashData(w);
        Cache temp = checkCache(first, second); 
        if (temp != null) {
           return temp.distance;
        }
        asyncSearch(v, w);
        if (path == Integer.MAX_VALUE) {
            path = -1;
        }
        if (ancestor == Integer.MAX_VALUE) {
            ancestor = -1;
        }
        setCache(first, second, new Cache(path, ancestor));
        return path;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        HashData first = new HashData(v);
        HashData second = new HashData(w);
        Cache temp = checkCache(first, second); 
        if (temp != null) {
            return temp.ancestor;
        }
        asyncSearch(v, w);
        if (path == Integer.MAX_VALUE) {
            path = -1;
        }
        if (ancestor == Integer.MAX_VALUE) {
            ancestor = -1;
        }
        setCache(first, second, new Cache(path, ancestor));
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
        }
        needReset.clear();

        // deque for first vertix
        marksDeque.clear();
        vertixDeque.clear();
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
            pathsCost[noCost] = 0;
            nonMarks[noCost] = true; 
            vertixDeque.add(noCost);
            marksDeque.add(false);
            needReset.add(noCost);
                //System.out.println("Add: activeVertix: " + noCost + " activeMark: " + false);
        }

        boolean activeMark = true;
        int     activeVertix; 
        int     activePath;
        int elementInDeque = vertixDeque.size();
        int bestLength = 1;
        while (!vertixDeque.isEmpty()) {
            if (elementInDeque == 0) {
                // check best
                ++bestLength;
                //System.out.println("Change");
                elementInDeque = vertixDeque.size();
            }
            if (path <= bestLength) {
                return;
            }
            --elementInDeque;
            activeVertix = vertixDeque.remove();
            activeMark  = marksDeque.remove();
            //System.out.println("remove: activeVertix: " + activeVertix + " activeMark: " + activeMark);    

            for (int w: getEdges(activeVertix)) {
                activePath = pathsCost[w] + bestLength;
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
                        if (nonMarks[w] && pathsCost[w] <= bestLength) {
                            //System.out.println("Continue" + activePath);
                            continue;
                        }
                        marks[w]     = activeMark;
                        pathsCost[w] = bestLength;
                        vertixDeque.add(w);
                        needReset.add(w);                        
                        marksDeque.add(activeMark);
                        //System.out.println("Add: activeVertix: " + w + " activeMark: " + activeMark);
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
                        pathsCost[w] = bestLength;
                        vertixDeque.add(w);
                        needReset.add(w);
                        marksDeque.add(activeMark);
                    }
                    //System.out.println("Add: activeVertix: " + w + " activeMark: " + activeMark);
                }
            }
        }
    }


/*    private void asyncSearch(Iterable<Integer> vIterable, Iterable<Integer> wIterable) {
        // preinit
        path = Integer.MAX_VALUE;
        ancestor = Integer.MAX_VALUE;
        deque.clear();
        for (int i: needReset) {
            marks[i] = false;
            nonMarks[i] = false;
        }

        needReset.clear();
        int bestLength = 1;
        int elementInDeque = 0;
        // input marks vertix
        for (int noCost: vIterable) {
            validateData(noCost);
            marks[noCost] = true;
            pathsCost[noCost] = 0;
            needReset.add(noCost);
            deque.add(noCost);
        }
        // input marks vertix 
        for (int noCost: wIterable) {
            if (marks[noCost]) {
                path = 0;
                ancestor = noCost;
                return;
            }
            validateData(noCost);
            nonMarks[noCost] = true;
            pathsCost[noCost] = 0;
            needReset.add(noCost);
            deque.add(noCost);
        }
        elementInDeque = deque.size();
        int activeElement = 0;
        while (!deque.isEmpty()) {
            if (elementInDeque == 0) {
                // check best
                ++bestLength;
                //System.out.println("Change");
                elementInDeque = deque.size();
            }
            if (path <= bestLength) {
                return;
            }
            activeElement = deque.remove();
            --elementInDeque;

            for (int w: getEdges(activeElement)) {
                int distance = bestLength + pathsCost[w];
                //System.out.println(pathsCost[activeElement] + " "+ bestLength + " vertix: " + activeElement);
                if (marks[activeElement] && nonMarks[activeElement]) {
                    if (marks[w] != nonMarks[w]) {
                        nonMarks[w] = true;
                        marks[w] = true;
                        if (distance < path) {
                            path = distance;
                            ancestor = w; 
                            if (path == bestLength) {
                                return;
                            }
                        }

                        if (pathsCost[w] > bestLength) {
                            //System.out.println("Change1: " + bestLength + " vertix: " + w);
                            pathsCost[w] = bestLength;
                            needReset.add(w);
                            deque.add(w);
                        }
                    } else if (!marks[w] && !nonMarks[w]) {
                        nonMarks[w] = true;
                        marks[w] = true;              
                        //System.out.println("Change2: " + bestLength + " vertix: " + w);                  
                        pathsCost[w] = bestLength;
                        needReset.add(w);
                        deque.add(w);
                    }
                } else {
                    //System.out.println("mark " + marks[activeElement] + " " + nonMarks[activeElement]);
                    //System.out.println(marks[w] + " " + nonMarks[w]);
                    if (marks[w] || nonMarks[w]) {
                        if (marks[w] && nonMarks[activeElement]
                            || marks[activeElement] && nonMarks[w]) {
                            if (distance < path) {
                                path = distance;
                                ancestor = w; 
                                if (path == bestLength) {
                                    return;
                                }
                            }
                        }
                        if (pathsCost[w] >= bestLength) {
                            marks[w] = true;
                            nonMarks[w] = true;
                            pathsCost[w] = bestLength;
                            needReset.add(w);
                            deque.add(w);
                        }
                    } else {
                        if (marks[activeElement]) {
                            marks[w] = true;
                        } else {
                            nonMarks[w] = true;
                        }
                        //System.out.println("Change4: " + bestLength + " vertix: " + w);
                        pathsCost[w] = bestLength;
                        needReset.add(w);
                        deque.add(w);
                    }                 
                }
            }
        }
    }*/

/*                for (int w: getEdges(activeElement)) {
                int distance = pathsCost[activeElement] + 1 + pathsCost[w];
                if (marks[activeElement] && nonMarks[activeElement]) {
                    nonMarks[w] = true;
                    marks[w] = true;
                    if (marks[w] != nonMarks[w]) {
                        if (distance < path) {
                            path = distance;
                            ancestor = w; 
                        }
                        if (pathsCost[w] != bestLength) {
                            pathsCost[w] = bestLength;
                            needReset.add(w);
                            deque.add(w);
                        }
                    } else if (!marks[w] && !nonMarks[w]) {
                        pathsCost[w] = bestLength;
                        needReset.add(w);
                        deque.add(w);
                    }
                } else {
                    if (marks[w] != nonMarks[w]) {
                        if (marks[w] && nonMarks[activeElement]
                            || nonMarks[w] && marks[activeElement]) {
                            nonMarks[w] = true;
                            marks[w] = true;
                            if (marks[w] != nonMarks[w]) {
                                if (distance < path) {
                                    path = distance;
                                    ancestor = w; 
                                }
                                if (pathsCost[w] != bestLength) {
                                    pathsCost[w] = bestLength;
                                    needReset.add(w);
                                    deque.add(w);
                                }
                            }
                        }
                    } else if (!marks[w] && !nonMarks[w]) {
                        if (marks[activeElement]) {
                            marks[w] = true;
                        } else {
                            nonMarks[w] = true;
                        }
                        pathsCost[w] = bestLength;
                        needReset.add(w);
                        deque.add(w);
                    } 
                }
            }*/



            /*                int distance = pathsCost[activeElement] + 1 + pathsCost[w];
                if (marks[activeElement] && nonMarks[activeElement]) {
                    if (marks[w] && nonMarks[w]) {
                    } else if (marks[w] || nonMarks[w]) {
                        nonMarks[w] = true;
                        marks[w] = true;
                        if (distance < path) {
                            path = distance;
                            ancestor = w; 
                        }
                        if (pathsCost[w] == bestLength) {
                        } else {
                            pathsCost[w] = bestLength;
                            needReset.add(w);
                            deque.add(w);
                        }
                    } else {
                        nonMarks[w] = true;
                        marks[w] = true;
                        pathsCost[w] = bestLength;
                        needReset.add(w);
                        deque.add(w);
                    }
                } else {
                    if (marks[w] && nonMarks[w]) {
                    } else if (marks[w] || nonMarks[w]) {
                        if (marks[w]) {
                            if (marks[activeElement]) {
                            } else {
                                nonMarks[w] = true;
                                marks[w] = true;
                                if (distance < path) {
                                    path = distance;
                                    ancestor = w; 
                                }
                                if (pathsCost[w] == bestLength) {
                                    //System.out.println("ewfwef4");
                                } else {
                                    pathsCost[w] = bestLength;
                                    needReset.add(w);
                                    deque.add(w);
                                }
                            }
                        } else {
                            if (marks[activeElement]) {
                                nonMarks[w] = true;
                                marks[w] = true;
                                if (distance < path) {
                                    path = distance;
                                    ancestor = w; 
                                }
                                if (pathsCost[w] == bestLength) {
                                } else {
                                    pathsCost[w] = bestLength;
                                    needReset.add(w);
                                    deque.add(w);
                                }
                            } else {
                            }
                        }
                    } else {
                        if (marks[activeElement]) {
                            marks[w] = true;
                        } else {
                            nonMarks[w] = true;
                        }
                        pathsCost[w] = bestLength;
                        needReset.add(w);
                        deque.add(w);
                    }
                }*/

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
        first.add(12002);
        first.add(55025);
        //first.add(2);
        //first.add(3);
        ArrayList<Integer> second = new ArrayList<Integer>();
        second.add(55854);
        second.add(82187);
        //second.add(6);
        //while (!StdIn.isEmpty()) {
            //int v = StdIn.readInt();
            //int w = StdIn.readInt();
            int length   = sap.length(first, second);
            int ancestor = sap.ancestor(first, second);
            //int length   = sap.length(v, w);
            //int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            //length   = sap.length(2, 5);
            //ancestor = sap.ancestor(2, 1);
            //StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        //}
    }

}
