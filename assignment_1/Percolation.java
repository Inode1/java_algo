import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    //private int[] array;
    private boolean[] openFlag;
    private boolean[] fullFlag;
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
        fullFlag = new boolean[count];
        fullFlag[0] = true;
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
        if (openFlag[element]) {
            return;
        }        
        runOpen(i, j);
        openFlag[element] = true;
    }
    
    private void runOpen(int i, int j) {   
        int element = elementNumber(i, j);
        int my = unionFind.find(element);
        int temp;
        // next element left
        if (j != 1)
        {
            if (openFlag[element - 1]) {
                temp = unionFind.find(element - 1);
                fullFlag[temp] = fullFlag[my] || fullFlag[temp];
                unionFind.union(element, element - 1);
                if (my != temp) {
                    my = temp;
                }
            }
        }
        // rigth
        if (j != size)
        {
            if (openFlag[element + 1]) {
                temp = unionFind.find(element + 1);
                fullFlag[temp] = fullFlag[my] || fullFlag[temp];
                unionFind.union(element, element + 1);
                if (my != temp) {
                    my = temp;
                }
            }
        }
               
        // element -N
        if (i == 1) {
            fullFlag[my] = true;
            unionFind.union(0, element);
            temp = unionFind.find(element);
            if (my != temp) {
                my = temp;
            }
        } else {
            if (openFlag[element - size]) {
                temp = unionFind.find(element - size);
                fullFlag[temp] = fullFlag[my] || fullFlag[temp];
                unionFind.union(element, element - size);
                if (my != temp) {
                    my = temp;
                }
            }
        }
        
        // element +N
        if (i == size) {
                temp = unionFind.find(1);
                fullFlag[temp] = fullFlag[my] || fullFlag[temp];
                unionFind.union(element, 1);
                if (my != temp) {
                    my = temp;
                }
            //unionFind.union(element, 1);
        } else {
            if (openFlag[element + size]) {
                temp = unionFind.find(element + size);
                fullFlag[temp] = fullFlag[my] || fullFlag[temp];
                unionFind.union(element, element + size);
                if (my != temp) {
                    my = temp;
                }
            }            
        }
    }
    
        
    public boolean isOpen(int i, int j) {     // is site (row i, column j) open?
        return openFlag[elementNumber(i, j)];
    }
    
    public boolean isFull(int i, int j) {     // is site (row i, column j) full?
        return fullFlag[unionFind.find(elementNumber(i, j))];
    }
    
    public boolean percolates() {             // does the system percolate?
        return fullFlag[1];
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
        System.out.println(false || true);
    }
}