import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double meanValue;
    private double stddevValue;
    private double confidenceLoValue;
    private double confidenceHiValue;
    
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) 
        {
            throw new java.lang.IllegalArgumentException();
        }
        double[] mean = new double[T];
        Percolation statistics;
        double addValue;
        for (int i = 0; i < T; ++i) {
            statistics = new Percolation(N);
            addValue = 0;
            int k, j;
            while (!statistics.percolates()) {               
                do {
                    k = StdRandom.uniform(N) + 1;
                    j = StdRandom.uniform(N) + 1;
                } while(statistics.isOpen(k, j));
               
                ++addValue;
                statistics.open(k, j);                
            }
            mean[i] = addValue/(N*N);
        }
        meanCalculate(mean);
        stddevCalculate(mean);
        addValue = 1.96 * stddevValue/Math.sqrt(T);
        confidenceLoValue = meanValue - addValue;
        confidenceHiValue = meanValue + addValue;        
    }
    public double mean() { // sample mean of percolation threshold
        return meanValue;
    }
    public double stddev() { // sample standard deviation of percolation threshold
        return stddevValue;
    }
    public double confidenceLo() { // low  endpoint of 95% confidence interval
        return confidenceLoValue;
    }
    public double confidenceHi() {    // high endpoint of 95% confidence interval
        return confidenceHiValue;
    }
    
    private void meanCalculate(double[] mean) {
        meanValue = StdStats.mean(mean);
    }
    
    private void stddevCalculate(double[] mean) {
        for (int i = 0; i < mean.length; ++i) {
            mean[i] = mean[i] - meanValue;
        }
        stddevValue = StdStats.stddev(mean);
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
