import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;
        
    private class Node<Item> {
        private Item item;
        private Node<Item> previous;
        private Node<Item> next;
        public Node(Item item) {
            this.item = item;
        }
    }
    public Deque() {
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
        
        Node<Item> newNode = new Node<Item>(item);
        newNode.next = first;
        if (first != null) {
            first.previous = newNode;
        }
        else
        {
            last = newNode;
        }
        first = newNode;
        ++size;
    }
    
    public void addLast(Item item) {           // add the item to the end
        checkEnterData(item);
        
        Node<Item> newNode = new Node<Item>(item);
        newNode.previous = last;
        if (last != null) {
            last.next = newNode;
        }
        else {
            first = newNode;
        }
        last = newNode;
        ++size;
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
        else {
            first.previous = null;
        }
        return newNode.item;
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
        else {
            last.next = null;
        }
        return newNode.item;       
    }
    
    private void checkEnterData(Item item) {
        if (item == null)
        {
            throw new java.lang.NullPointerException();
        }
    }
    
    public Iterator<Item> iterator() {         // return an iterator over items in order from front to end
        return new DequeIterator(first);
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node<Item> firstIteration;
        private DequeIterator(Node<Item> first) {
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
                firstIteration = firstIteration.next;
                return temp.item;
            }
            throw new java.util.NoSuchElementException();
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
    
    public static void main(String[] args) {   // unit testing
        Deque<Integer> test = new Deque<Integer>();
          //test.addLast(1);
          //test.removeLast();
          test.addFirst(3);
          //System.out.println(test.size());
          test.addFirst(4);
          //System.out.println(test.size());
           System.out.println(test.removeLast());
        for (int a: test) {
            System.out.println(a);
        }
    }
}
