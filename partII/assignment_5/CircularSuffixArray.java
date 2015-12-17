public class CircularSuffixArray {
	// circular suffix array of s
    private int length;
    public CircularSuffixArray(String s) {  
	
	}

    // length of s	
    public int length() {                   
        return length;
    }

	// returns index of ith sorted suffix
    public int index(int i) {               
        if (i < 0 || i >= length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return ;
    }

	// unit testing of the methods (optional)
    public static void main(String[] args) {
	
	}
}