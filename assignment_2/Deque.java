import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;
        
    private class Node<Item> {
        private Item data;
        private Node<Item> previous;
        private Node<Item> next;
    }
    public Deque() {
        first = null;
        last = null;
    }                           // construct an empty deque

    public boolean isEmpty() {  // is the deque empty?
        if (size == 0) {
            return true;
        }
        return false;
    }    
    public int size() { // return the number of items on the deque
        return size;
    }
    
    public void addFirst(Item item) {         // add the item to the front
        checkEnterData(item);
        
        Node<Item> newNode = new Node<Item>();
        newNode.data = item;
        newNode.next = first;
        newNode.previous = null;
        first = newNode;
        ++size;
   
        if (isEmpty()) {
            newNode.next = null;
            last  = newNode;
        }
    }
    
    public void addLast(Item item) {           // add the item to the end
        checkEnterData(item);
        
        Node<Item> newNode = new Node<Item>();
        newNode.data = item;
        newNode.next = null;
        newNode.previous = last;
        last = newNode;
        ++size;
        
        if (isEmpty()) {
            first = newNode;
            newNode.previous = null;
        }
    }
    
    public Item removeFirst() {                // remove and return the item from the front
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Node<Item> newNode = first;
        first = first.next;
        --size;
        if (first == null) {
            last = null;
        }
        return newNode.data;
    }
    
    public Item removeLast() {                 // remove and return the item from the end
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Node<Item> newNode = last;
        last = last.previous;
        --size;
        if (last == null) {
            first = null;
        }
        return newNode.data;       
    }
    
    private void checkEnterData(Item item) {
        if (item == null)
        {
            throw new java.lang.NullPointerException();
        }
    }
    
    public Iterator<Item> iterator() {         // return an iterator over items in order from front to end
        return new DequeIterator<Item>(first);
    }
    
    public class DequeIterator<Item> implements Iterator<Item> {
        Node<Item> firstIteration;
        public DequeIterator(Node<Item> first) {
            firstIteration = first;
        }
        
        public boolean hasNext() {
            if (firstIteration == null) {
                return false;
            }
            return true;
        }

        public Item next() {
            if (firstIteration != null) {
                
                Node<Item> temp = firstIteration;
                //System.out.println(temp.data);
                firstIteration = firstIteration.next;
                return temp.data;
            }
            throw new java.util.NoSuchElementException();
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
    
    public static void main(String[] args) {   // unit testing
        Deque<Integer> test = new Deque<Integer>();
        test.addFirst(2);
        test.addFirst(20);
        test.addLast(55);
        test.addLast(43);
        for (int a: test) {
            System.out.println(a);
        }
    }
}
