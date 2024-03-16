package yagoo.threads.parallel2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DivideAndConquerDemo {
    
    static class RecursiveSum extends RecursiveTask<Long> {
        
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
            // Base case threshold
            if (hi - lo <= 100_000) {
                for (long k = lo; k <= hi; k++) result += k;
            } else {
                long mid = ((hi - lo) >> 1) + lo; // middle index for split
                RecursiveSum leftSum = new RecursiveSum(lo, mid);
                RecursiveSum rightSum = new RecursiveSum(mid + 1, hi);
                leftSum.fork(); // forked thread computes left half
                result = rightSum.compute() + leftSum.join(); // current thread computes right half
            }
            return result;
        }
        
    }
    
    public static void main(String args[]) {
        ForkJoinPool fjPool = ForkJoinPool.commonPool();
        Long total = fjPool.invoke(new RecursiveSum(0, 1_000_000_000));
        fjPool.shutdown();
        System.out.println("Total sum is " + total);
    }

}
