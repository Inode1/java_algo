import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] mean;
    private double[] stddev;
    private double meanValue;
    private double stddevValue;
    
    
    public PercolationStats(int N, int T) {
        mean = new double[T];
        stddev = new double[T];
        double count;
        for (int i = 0; i < T; ++i) {
            Percolation statistics = new Percolation(N);
            count = 0;
            int k, j;
            while (!statistics.percolates()) {               
                do {
                    k = StdRandom.uniform(N) + 1;
                    j = StdRandom.uniform(N) + 1;
                } while(statistics.isOpen(k, j));
               
                ++count;
                statistics.open(k, j);                
            }
            mean[i] = count/(N*N);
        }
    }
    public double mean() { // sample mean of percolation threshold
        meanValue = StdStats.mean(mean);
        return meanValue;
    }
    public double stddev() { // sample standard deviation of percolation threshold
        for (int i = 0; i < mean.length; ++i) {
            stddev[0] = mean[0] - meanValue;
        }
        stddevValue = StdStats.stddev(stddev);
        return stddevValue;
    }
    public double confidenceLo() { // low  endpoint of 95% confidence interval
        return meanValue - 1.96 * stddevValue/Math.sqrt(mean.length);
    }
    public double confidenceHi() {    // high endpoint of 95% confidence interval
        return meanValue + 1.96 * stddevValue/Math.sqrt(mean.length);
    }
    public static void main(String[] args) { // test client (described below)
        if (args.length != 2) 
        {
            throw new java.lang.IllegalArgumentException();
        }
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
       
        if (N <= 0 || T <= 0) 
        {
            throw new java.lang.IllegalArgumentException();
        }
                
        PercolationStats stat = new PercolationStats(N, T);
        System.out.println("mean: " + stat.mean());
        System.out.println("stddev: " + stat.stddev());
        System.out.println("95% confidence interval: " + stat.confidenceLo()
                           + ", " + stat.confidenceHi());
    }
}
