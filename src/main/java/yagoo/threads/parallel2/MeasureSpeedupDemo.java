package yagoo.threads.parallel2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MeasureSpeedupDemo {
    
    final static int NUM_EVAL_RUNS = 10; // rounds
    final static long SUM_VALUE = 1_000_000_000L;
    
    // Parallel sum task implementation
    static class RecursiveSum extends RecursiveTask<Long> {
        //
        private static final long serialVersionUID = 1L;
        private long lo;
        private long hi;
        
        RecursiveSum(long lo, long hi) {
            this.lo = lo;
            this.hi = hi;
        }
        
        @Override
        protected Long compute() {
            long result = 0;
            if (hi - lo <  100_000L) { // base case threshold
                result = sequentialSum(lo, hi);
            } else {
                long middle = ((hi - lo) >> 1) + lo; // middle index for split
                RecursiveSum left = new RecursiveSum(lo, middle);
                RecursiveSum right = new RecursiveSum(middle + 1, hi);
                // forked thread computes left half
                left.fork();
                // current thread computes right half
                result = right.compute() + left.join();
            }
            return result;
        }
        
    }
    
    // Sequential sum implementation
    private static long sequentialSum(long lo, long hi) {
        long result = 0L;
        for (long k = lo; k <= hi; k++) result += k;
        return result;
    }
    
    public static void main(String[] args) {
        
        // Sequential implementation
        System.out.println("Evaluating Sequential Implementation...");
        // "Warm up"
        long sequentalResult = sequentialSum(0, SUM_VALUE);
        double sequentialTime = 0D;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            sequentialSum(0, SUM_VALUE);
            sequentialTime = System.currentTimeMillis() - start;
        }
        sequentialTime /= NUM_EVAL_RUNS;
        
        // Parallel implementation
        System.out.println("Evaluating Parallel Implementation...");
        // "Warm up"
        ForkJoinPool pool = ForkJoinPool.commonPool();
        long parallelResult = pool.invoke(new RecursiveSum(0, SUM_VALUE));
        pool.shutdown();
        double parallelTime = 0D;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            pool = ForkJoinPool.commonPool();
            pool.invoke(new RecursiveSum(0, SUM_VALUE));
            pool.shutdown();
            parallelTime += System.currentTimeMillis() - start; 
        }
        parallelTime /= NUM_EVAL_RUNS;
        
        // display sequential and parallel results for comparison
        if (sequentalResult != parallelResult) 
            throw new Error("ERROR: sequentialResult and parallelResult do not match!");
        System.out.format("Average Sequential Time: %.1f ms%n", sequentialTime);
        System.out.format("Average Parallel Time: %.1f ms%n", parallelTime);
        System.out.format("Speedup: %.2f%n", sequentialTime / parallelTime);
        System.out.format("Efficiency: %.2f%%%n", 100 * (sequentialTime / parallelTime) / Runtime.getRuntime().availableProcessors());
    }

}
