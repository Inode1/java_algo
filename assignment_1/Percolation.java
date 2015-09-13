public class Percolation {
    private int[] array;
    private boolean[] isOpen;
    private int[] range;
    
    private int size;
    // count distjoin set
    private int count;
        
    public Percolation(int N) {
        if (N <= 0)
        {
            throw new java.lang.IllegalArgumentException();
        }
        
        size = N;
        
        count = N*N;
        
        array = new int[count + 2];
        isOpen = new boolean[count + 2];
        range = new int[count + 2];
        
        // plus two element for bottom and tom
        for (int i = 0; i < array.length; ++i) {
            array[i] = i;
        }
    }
        
    public void open(int i, int j) {   
        int element = elementNumber(i, j);
        // next element
        if (element + 1  >= array.length)
        {
            union(1, element); 
        }
        else {
            if (isOpen[element + 1]) {
                union(element, element + 1);
            }            
        }
        // previous element
        if (element <= 2) {
            union(0, element); 
        }
        else {
            if (isOpen[element - 1]) {
                union(element, element - 1);
            }            
        }
        
        
        // element -N
        if (element - size <= 1) {
            union(0, element);
        } else {
            if (isOpen[element - size]) {
                union(element, element - size);
            }            
        }
        
        // element +N
        if (element + size >= array.length) {
            union(1, element);
        } else {
            if (isOpen[element + size]) {
                union(element, element + size);
            }            
        }        
        isOpen[element] = true;
    }
    
    public boolean isOpen(int i, int j) {     // is site (row i, column j) open?
        
        return isOpen[elementNumber(i, j)];
    }
    
    public boolean isFull(int i, int j) {     // is site (row i, column j) full?
        return !isOpen(i, j);
    }
    
    public boolean percolates() {             // does the system percolate?
        return find(0, 1);
    }
    
    private int elementNumber(int i, int j) {
        if (i == 0 || j == 0 || size < i || size < j)
        {
            throw new java.lang.IllegalArgumentException();
        }
        
        return (i - 1)*size + j + 1;
    }
    
    // union find
    private boolean find(int lhs, int rhs) {
        if (root(lhs) == root(rhs))
        {
            return true;
        }
        
        return false;
    }
    
    private int root(int element) {
        while (array[element] != element)
        {
            array[element] = array[array[element]];
            element = array[element];
        }
        return element;
    }
    
    private void union(int lhs, int rhs) {
        int lhsRoot = root(lhs);
        int rhsRoot = root(rhs);
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
        
    }
    
       
    public static void main(String[] args) {  // test client (optional)
    }
}