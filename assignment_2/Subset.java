import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    
    private static void shuffle(String[] temp, int count) {
        for (int i = 0; i < count; ++i) {
            String swapString = temp[i];
            int random = StdRandom.uniform(temp.length);
            temp[i] = temp[random];
            temp[random] = swapString;
        }
    }
    
    public static void main(String[] args) {
        int firstArg = Integer.parseInt(args[0]);
                
        String[] string = StdIn.readAllStrings();
        RandomizedQueue<String> queue = new RandomizedQueue<String>(); 
        shuffle(string, firstArg);
        for (int i = 0; i < firstArg; ++i) {
            queue.enqueue(string[i]);
        }
        
        for (int i = 0; i < firstArg; ++i) {
            System.out.println(queue.dequeue());
        }
        
    }
}