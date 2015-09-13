import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    //private int[] array;
    private boolean[] openFlag;
    //private int[] range;
    
    private int size;
    // use quick algo
    private WeightedQuickUnionUF unionFind;
    //    
        
    public Percolation(int N) {
        if (N <= 0)
        {
            throw new java.lang.IllegalArgumentException();
        }
        
        size = N;
        
        int count = N*N + 2;
        
        unionFind = new WeightedQuickUnionUF(count);
        
        openFlag = new boolean[count];
                     
        // my realization
        //array = new int[count];
        //range = new int[count];
        /* for (int i = 0; i < array.length; ++i) {
                array[i] = i;
                openFlag[i] = false;
        }*/
    }
        
    public void open(int i, int j) {   
        int element = elementNumber(i, j);
        // next element left
        if (j != 1)
        {
            if (openFlag[element - 1]) {
                unionFind.union(element, element - 1);
            }  
        }
        // rigth
        if (j != size)
        {
            if (openFlag[element + 1]) {
                unionFind.union(element, element + 1);
            }  
        }
               
        // element -N
        if (i == 1) {
            unionFind.union(0, element);
        } else {
            if (openFlag[element - size]) {
                unionFind.union(element, element - size);
            }            
        }
        
        // element +N
        if (i == size) {
            if (!openFlag[1]) {
                unionFind.union(1, element);
                openFlag[1] = true;
            }          
        } else {
            if (openFlag[element + size]) {
                unionFind.union(element, element + size);
            }            
        }
        openFlag[element] = true;
    }
    
    public boolean isOpen(int i, int j) {     // is site (row i, column j) open?
        return openFlag[elementNumber(i, j)];
    }
    
    public boolean isFull(int i, int j) {     // is site (row i, column j) full?
        return unionFind.connected(0, elementNumber(i, j));
    }
    
    public boolean percolates() {             // does the system percolate?
        for (int i = size*size + 2 - size; i < size*size + 2; ++i) {
            if (unionFind.connected(0, i)) {
                return true;
            }
        }
        return false;
    }
    
    private int elementNumber(int i, int j) {
        if (i == 0 || j == 0 || size < i || size < j)
        {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        return (i - 1) * size + j + 1;
    }
    
    // my union find
    /*
    private boolean connected(int lhs, int rhs) {
        if (find(lhs) == find(rhs))
        {
            return true;
        }
        
        return false;
    }
    
    private int find(int element) {
        int tmp = element;
        while (array[tmp] != tmp)
        {
            array[tmp] = array[array[tmp]];
            tmp = array[tmp];
        }
        return tmp;
    }
    
    private void union(int lhs, int rhs) {
        int lhsRoot = find(lhs);
        int rhsRoot = find(rhs);
        if (lhsRoot == rhsRoot) {
            return;
        }
        
        if (range[lhsRoot] > range[rhsRoot]) {
            array[rhsRoot] = lhsRoot;
        }
        else if (range[lhsRoot] < range[rhsRoot]) {
            array[lhsRoot] = rhsRoot;
        }
        else {
            array[rhsRoot] = lhsRoot;
            ++range[lhs];
        }
        
    }*/
           
    public static void main(String[] args) {  // test client (optional)
    }
}