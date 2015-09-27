import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arrayReference = (Item[]) new Object[1];
    private int size;
    
    public RandomizedQueue() {                 // construct an empty randomized queue
    
    }
        
    public boolean isEmpty() {                // is the queue empty?
        if (size == 0) {
            return true;
        }
        return false;
    }
    public int size() {                        // return the number of items on the queue
        return size;
    }
    
    public void enqueue(Item item) {           // add the item
        checkEnterData(item);
        
        if (arrayReference.length == size) {
            resizeArray(size * 2);
        }
        arrayReference[size++] = item;       
    }
    
    public Item dequeue() {                    // remove and return a random item
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int i = StdRandom.uniform(size);
        
        Item item = arrayReference[i];
        
        arrayReference[i] = arrayReference[--size];
        arrayReference[size] = null;
        
        if (size > 0 && arrayReference.length/4 == size) {
            resizeArray(size * 2);
        }
        
        return item;    
    }
    public Item sample() {                     // return (but do not remove) a random item
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        
        return arrayReference[StdRandom.uniform(size)];
    }
    public Iterator<Item> iterator() {         // return an independent iterator over items in random order
        return new RandomIterator();
    }
    
    private void checkEnterData(Item item) {
        if (item == null)
        {
            throw new java.lang.NullPointerException();
        }
    }
    
    private void resizeArray(int resize) {
        Item[] temp = (Item[]) new Object[resize];
        for (int i = 0; i < size; ++i) {
            temp[i] = arrayReference[i];
        }
        arrayReference = temp;
    }
    
    private class RandomIterator implements Iterator<Item> {
        private int[] arrayOfIndex = new int[size];
        private int nextIndex;
        public RandomIterator() {
            for (int i = 0; i < arrayOfIndex.length; ++i) {
                arrayOfIndex[i] = i;
            }
            
            for (int i = 1; i < arrayOfIndex.length; ++i) {
                
                int random = StdRandom.uniform(i + 1);
                int temp = arrayOfIndex[i];
                arrayOfIndex[i] = arrayOfIndex[random];
                arrayOfIndex[random] = temp;
            }
        }
        public boolean hasNext() {
            if (nextIndex == arrayOfIndex.length) {
                return false;
            }
            return true;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return arrayReference[arrayOfIndex[nextIndex++]];   
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
    
    public static void main(String[] args) {  // unit testing
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>(); 
        for (int i = 0; i < 10; ++i) {
            queue.enqueue(i);
        }
        
        for (int i = 0; i < 10; ++i) {
            queue.dequeue();
        }
        
        for (int i = 0; i < 10; ++i) {
            queue.enqueue(i);
        }
 
        System.out.println(queue.size());
        for (Integer i: queue) {
            System.out.println(i);
        }
    }
}