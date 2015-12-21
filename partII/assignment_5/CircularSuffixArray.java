public class CircularSuffixArray {
    // circular suffix array of 
    private int[] index;
    private int[] aux;
    private int length;
    private static final int CUTOFF =  15;
    private char lowerBound = 0xffff;
    private char upperBound = 0;
    public CircularSuffixArray(String s) {
        length = s.length();
        index = new int[length];
        aux   = new int[length];
        for (int i = 0; i < length; ++i) {
            index[i] = i;
        }
        sort(s, 0, length, 0, false);
    }

    // length of s  
    public int length() {                   
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {  
        if (i < 0 || i >= length) throw new IndexOutOfBoundsException();             
        return index[i];
    }

    private void sort(String s, int lo, int hi, int d, boolean state){
        if (hi <= lo + CUTOFF) {
            insertion(s, lo, hi, d);
            return;
        }

        char sybvol;
        // sort radix
        int[] radix = new int[258];  
        for (int i = lo; i < hi; ++i) {
            sybvol = s.charAt((index[i] + d) % length); 
            ++radix[sybvol + 2];
            if (!state) {
                if (sybvol < lowerBound) {
                    lowerBound = sybvol;
                }

                if (sybvol > upperBound) {
                    upperBound = sybvol;
                }
            }
        }

        for (char i = lowerBound; i <= upperBound; ++i) { 
            radix[i + 1] += radix[i];
        }
/*        System.out.println((int) lowerBound);
        System.out.println((int) upperBound);
        for (char i = lowerBound; i <= upperBound; ++i) {
            System.out.print(radix[i] + " ");
        }*/

        for (int i = lo; i < hi; ++i) {
            aux[radix[s.charAt((index[i] + d) % length) + 1]++] = index[i];
        }

/*for (char i = lowerBound; i <= upperBound + 1; ++i) {
            System.out.print(radix[i] + " ");
        }

        for (int i = lo; i < hi; ++i) {
            System.out.print(aux[i] + " ");
        }*/

        for (int i = lo; i < hi; ++i) {
            index[i] = aux[i - lo];
        }

        if (lowerBound == upperBound) {
            return;
        }

        //int k = 0;
        for (char i = lowerBound; i <= upperBound; ++i) {
            if (radix[i + 1] - radix[i] > 1 && d != length - 1) {
                //System.out.println((lo + radix[i + 1]) + " "  + (lo + radix[i]) + "k:" + (++k));
                sort(s, lo + radix[i], lo + radix[i + 1], d + 1, true);
            }
        }
    }

    private void insertion(String s, int lo, int hi, int d) {
        for (int i = lo; i < hi; i++)
            for (int j = i; j > lo && less(s, index[j], index[j-1], d); j--)
                exch(j, j-1);
    }

    private boolean less(String s, int k, int j, int d) {
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < length; i++) {
            if (s.charAt((i + k) % length) < s.charAt((i + j) % length)) return true;
            if (s.charAt((i + k) % length) > s.charAt((i + j) % length)) return false;
        }
        return true;
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String s = "BAAABBBABB";
        //String s = "AAAAAAAAAAAA";
        CircularSuffixArray example = new CircularSuffixArray(s);
        for (int i = 0; i < example.length(); ++i) {
            System.out.println(s.substring(example.index(i)) + " index: " + example.index(i));
        }
    }
}