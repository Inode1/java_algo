public class Percolation {
    private int[] array;
    private boolean[] openFlag;
    private int[] range;
    
    private int size;
    
    private boolean isConnected;
        
    public Percolation(int N) {
        if (N <= 0)
        {
            throw new java.lang.IllegalArgumentException();
        }
        
        size = N;
        
        int count = N*N + 2;
        
        array = new int[count];
        openFlag = new boolean[count];
        range = new int[count];
                
        for (int i = 0; i < array.length; ++i) {
                array[i] = i;
                openFlag[i] = false;
        }
    }
        
    public void open(int i, int j) {   
        int element = elementNumber(i, j);
        // next element left
        if (j != 1)
        {
            if (openFlag[element - 1]) {
                union(element, element - 1);
            }  
        }
        // rigth
        if (j != size)
        {
            if (openFlag[element + 1]) {
                union(element, element + 1);
            }  
        }
               
        // element -N
        if (i == 1) {
            union(0, element);
        } else {
            if (openFlag[element - size]) {
                union(element, element - size);
            }            
        }
        
        // element +N
        if (i == size) {
            if (!openFlag[1]) {
                union(1, element);
                openFlag[1] = true;
            }           
        } else {
            if (openFlag[element + size]) {
                union(element, element + size);
            }            
        }
        openFlag[element] = true;
    }
    
    public boolean isOpen(int i, int j) {     // is site (row i, column j) open?
        return openFlag[elementNumber(i, j)];
    }
    
    public boolean isFull(int i, int j) {     // is site (row i, column j) full?
        return connected(0, elementNumber(i, j));
    }
    
    public boolean percolates() {             // does the system percolate?
        return isConnected;
    }
    
    private int elementNumber(int i, int j) {
        if (i == 0 || j == 0 || size < i || size < j)
        {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        return (i - 1) * size + j + 1;
    }
    
    // union find
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
        
    }
           
    public static void main(String[] args) {  // test client (optional)
    }
}