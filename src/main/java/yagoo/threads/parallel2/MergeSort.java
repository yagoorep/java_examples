package yagoo.threads.parallel2;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class MergeSort {
    // Options
    final static int NUM_EVAL_RUNS = 5; // Rounds
    final static boolean VERBOSE = false; // Verbose output
    final static int NUMBER = 1_000_000; // Number array elements
    final static int BOUND_MIN = -1000; // Generate numbers from
    final static int BOUND_MAX = 1000; // Generate numbers to
    
    // Evaluate performance of sequential and parallel implementations
    public static void main(String[] args) {
        // Generate array
        final int[] input = generateRandomArray(NUMBER);
        if (VERBOSE) showArray(input);
        
        // Sequential
        System.out.println("Evaluating Sequential Implementation...");
        SequentialMergeSorter sms = new SequentialMergeSorter(Arrays.copyOf(input, input.length));
        int[] sequentialResult = sms.sort();
        if (VERBOSE) showArray(sequentialResult);
        double sequentialTime = 0D;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            sms = new SequentialMergeSorter(Arrays.copyOf(input, input.length));
            long start = System.currentTimeMillis();
            sms.sort();
            sequentialTime += System.currentTimeMillis() - start;
        }
        sequentialTime /= NUM_EVAL_RUNS;
        
        // Parallel
        System.out.println("Evaluating Parallel Implementation...");
        ParallelMergeSorter pms = new ParallelMergeSorter(Arrays.copyOf(input, input.length));
        int[] parallelResult = pms.sort();
        if (VERBOSE) showArray(parallelResult);
        double parallelTime = 0D;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            pms = new ParallelMergeSorter(Arrays.copyOf(input, input.length));
            long start = System.currentTimeMillis();
            pms.sort();
            parallelTime += System.currentTimeMillis() - start;
        }
        parallelTime /= NUM_EVAL_RUNS;
        
        // Display sequential and parallel results for comparison
        if (!Arrays.equals(sequentialResult, parallelResult))
            throw new Error("ERROR: sequentialResult and parallelResult do not match!");
        System.out.format("Average Sequential Time: %.1f ms%n", sequentialTime);
        System.out.format("Average Parallel Time: %.1f ms%n", parallelTime);
        System.out.format("Speedup: %.2f %n", sequentialTime / parallelTime);
        System.out.format("Efficiency: %.2f%%%n", 100 * (sequentialTime / parallelTime) / Runtime.getRuntime().availableProcessors());
    }
    
    // Helper function to generate array of random integers
    public static int[] generateRandomArray(int length) {
        System.out.format("Generating random array int[%d]...\n", length);
        return ThreadLocalRandom.current().ints(length, BOUND_MIN, BOUND_MAX).toArray();
    }
    
    private static void showArray(int[] array) {
        Arrays.stream(array).forEach(i -> System.out.format("%d  ", i));
        System.out.println();
    }
    
    // Sequential implementation of merge sort
    static class SequentialMergeSorter {
        //
        private int[] array;

        public SequentialMergeSorter(int[] array) {
            this.array = array;
        }
        
        // Returns sorted array
        public int[] sort() {
            sort(0, array.length - 1);
            return array;
        }
        
        // Helper method that gets called recursively
        private void sort(int left, int right) {
            if (left < right) {
                int mid = ((right - left) >> 1) + left; // Find the middle point
                sort(left, mid); // Sort the left part of array
                sort(mid + 1, right); // Sort the right part of array
                merge(left, mid, right); // Merge sorted results
            }
        }
        
        // Helper method to merge two sorted subarrays array[l..m] and array[m+1..r] into array
        private void merge(int left, int mid, int right) {
            // Copy data to temp subarrays to be merged
            int[] leftTempArray = Arrays.copyOfRange(array, left, mid + 1);
            int[] rightTempArray = Arrays.copyOfRange(array, mid + 1, right + 1);
            
            // Initial indexes for left, right, and merged subarrays
            int lindx = 0, rindx = 0, tindx = left;
            
            // Merge temp arrays into original
            while (lindx < mid - left + 1 || rindx < right - mid) {
                if (lindx < mid - left + 1 && rindx < right - mid) {
                    if (leftTempArray[lindx] <= rightTempArray[rindx]) {
                        array[tindx] = leftTempArray[lindx];
                        lindx++;
                    } else {
                        array[tindx] = rightTempArray[rindx];
                        rindx++;
                    }
                } else if (lindx < mid - left + 1) { // Copy any remaining on left side
                    array[tindx] = leftTempArray[lindx];
                    lindx++;
                } else if (rindx < right - mid) { // Copy any remaining on right side
                    array[tindx] = rightTempArray[rindx];
                    rindx++;
                }
                tindx++;
            }
        }
    }
    
    // Parallel implementation of merge sort
    static class ParallelMergeSorter {
        //
        private int[] array;

        public ParallelMergeSorter(int[] array) {
            this.array = array;
        }
        
        // Returns sorted array
        public int[] sort() {
            int workersNumber = Runtime.getRuntime().availableProcessors();
            ForkJoinPool fjp = new ForkJoinPool(workersNumber);
            fjp.invoke(new ParallelWorker(0, array.length - 1));
            fjp.shutdown();
            fjp.close();
            return array;
        }
        
        // Worker that gets called recursively
        private class ParallelWorker extends RecursiveAction {
            //
            private static final long serialVersionUID = 1L;
            
            // Variables
            private int left, right;
            
            ParallelWorker(int left, int right) {
                this.left = left;
                this.right = right;
            }

            @Override
            protected void compute() {
                if (left < right) {
                    int mid = ((right - left) >> 1) + left; // Find the middle point
                    ParallelWorker leftWorker = new ParallelWorker(left, mid);
                    ParallelWorker rightWorker = new ParallelWorker(mid + 1, right);
                    invokeAll(leftWorker, rightWorker);
                    merge(left, mid, right); // Merge the two sorted halves
                }
            }
            
        }
        
        // Helper method to merge two sorted subarrays array[l..m] and array[m+1..r] into array
        private void merge(int left, int mid, int right) {
            // Copy data to temp subarrays to be merged
            int[] leftTempArray = Arrays.copyOfRange(array, left, mid + 1);
            int[] rightTempArray = Arrays.copyOfRange(array, mid + 1, right + 1);
            
            // Initial indexes for left, right, and merged subarrays
            int lindx = 0, rindx = 0, tindx = left;
            
            // Merge temp arrays into original
            while (lindx < mid - left + 1 || rindx < right - mid) {
                if (lindx < mid - left + 1 && rindx < right - mid) {
                    if (leftTempArray[lindx] <= rightTempArray[rindx]) {
                        array[tindx] = leftTempArray[lindx];
                        lindx++;
                    } else {
                        array[tindx] = rightTempArray[rindx];
                        rindx++;
                    }
                } else if (lindx < mid - left + 1) { // Copy any remaining on left side
                    array[tindx] = leftTempArray[lindx];
                    lindx++;
                } else if (rindx < right - mid) { // Copy any remaining on right side
                    array[tindx] = rightTempArray[rindx];
                    rindx++;
                }
                tindx++;
            }
        }
        
    }
}
