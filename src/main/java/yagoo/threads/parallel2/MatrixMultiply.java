package yagoo.threads.parallel2;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixMultiply {
    // Rounds
    final static int NUM_EVAL_RUNS = 5;
    final static boolean SHOW_MATRIX = true;
    
    private static void showMatrix(int[][] matrix) {
        Arrays.stream(matrix).map(e -> Arrays.toString(e))
        .forEach(System.out::println);
    }
    
    public static void main(String[] args) {
        // matrices
        int[][] matrixA = generateMatrix(500, 500);
        if (SHOW_MATRIX) showMatrix(matrixA);
        System.out.println();
        int[][] matrixB = generateMatrix(500, 500);
        if (SHOW_MATRIX) showMatrix(matrixB);
        System.out.println();
        
        // Sequential
        System.out.println("Evaluating Sequential Implementation...");
        var smm = new SequentialMatrixMultiplier(matrixA, matrixB);
        int[][] sequentialResult = smm.computeProduct(); // "Warm up"
        double sequentialTime = 0;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            smm.computeProduct();
            sequentialTime += System.currentTimeMillis() - start;
        }
        sequentialTime /= NUM_EVAL_RUNS;
        
        // Parallel
        System.out.println("Evaluating Parallel Implementation...");
        var pmm = new ParallelMatrixMultiplier(matrixA, matrixB);
        int[][] parallelResult = pmm.computeProduct();
        double parallelTime = 0;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            pmm.computeProduct();
            parallelTime += System.currentTimeMillis() - start;
        }
        parallelTime /= NUM_EVAL_RUNS;
        
        // Display sequential and parallel results for comparison
        if (!Arrays.deepEquals(sequentialResult, parallelResult))
            throw new Error("ERROR: sequentialResult and parallelResult do not match!");
        if (SHOW_MATRIX) showMatrix(sequentialResult);
        System.out.println();
        System.out.format("Average Sequential Time: %.1f ms%n", sequentialTime);
        System.out.format("Average Parallel Time: %.1f ms%n", parallelTime);
        System.out.format("Speedup: %.2f %n", sequentialTime / parallelTime);
        System.out.format("Efficiency: %.2f%%%n", 100 * (sequentialTime / parallelTime) / Runtime.getRuntime().availableProcessors());
    }
    
    // Helper function to generate MxN matrix of random integers
    private static int[][] generateMatrix(int rows, int cols) {
        int[][] result = new int[rows][cols];
        System.out.format("Generating random %d x %d matrix...\n", rows, cols);
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++) {
                result[i][k] = r.nextInt(10, 100);
            }
        }
        return result;
    }
    
    static class SequentialMatrixMultiplier {
        //
        private int[][] matrixA;
        private int[][] matrixB;
        private int numRowsA, numColsA, numRowsB, numColsB;
        
        SequentialMatrixMultiplier(int[][] A, int[][] B) {
            if (A[0] == null || B[0] == null) 
                throw new Error("Cols array is null");
            this.matrixA = A;
            this.matrixB = B;
            this.numRowsA = matrixA.length;
            this.numColsA = matrixA[0].length;
            this.numRowsB = matrixB.length;
            this.numColsB = matrixB[0].length;
            if (numColsA != numRowsB) 
                throw new Error(String.format("Invalid dimensions; Cannot multiply %dx%d*%dx%d%n", numRowsA, numRowsB, numColsA, numColsB));
        }
        
        // returns matrix product result = matrixA x matrixB
        public int[][] computeProduct() {
            int[][] result = new int[numRowsA][numColsB];
            for (int i = 0; i < numRowsA; i++) {
                for (int k = 0; k < numColsB; k++) {
                    int sum = 0;
                    for (int j = 0; j < numColsA; j++) {
                        sum += matrixA[i][j] * matrixB[j][k];
                    }
                    result[i][k] = sum;
                }
            }
            return result;
        }
        
    }
    
    static class ParallelMatrixMultiplier {
        //
        private int[][] matrixA;
        private int[][] matrixB;
        private int numRowsA, numColsA, numRowsB, numColsB;
        
        ParallelMatrixMultiplier(int[][] A, int[][] B) {
            if (A[0] == null || B[0] == null) 
                throw new Error("Cols array is null");
            this.matrixA = A;
            this.matrixB = B;
            this.numRowsA = matrixA.length;
            this.numColsA = matrixA[0].length;
            this.numRowsB = matrixB.length;
            this.numColsB = matrixB[0].length;
            if (numColsA != numRowsB) 
                throw new Error(String.format("Invalid dimensions; Cannot multiply %dx%d*%dx%d%n", numRowsA, numRowsB, numColsA, numColsB));
        }
        
        // returns matrix product result = matrixA x matrixB
        public int[][] computeProduct() {
            // create thread pool
            int numWorkers = Runtime.getRuntime().availableProcessors();
            ExecutorService pool = Executors.newFixedThreadPool(numWorkers);
            
            // submit tasks to calculate partial results
            int chunkSize = (int) Math.ceil((double) numRowsA / numWorkers);
            Future<int[][]>[] futures = new Future[numWorkers];
            for (int w = 0; w < numWorkers; w++) {
                int start = Math.min(chunkSize * w, numRowsA);
                int end = Math.min(chunkSize * (w + 1), numRowsA);
                futures[w] = pool.submit(new ParallelWorker(start, end));
            }
            
            // merge partial results
            int[][] result = new int[numRowsA][numColsB];
            try {
                for (int w = 0; w < numWorkers; w++) {
                    // retrieve value from future
                    int[][] partial = futures[w].get();
                    for (int i = 0; i < partial.length; i++) {
                        for (int j = 0; j < numColsB; j++)
                            result[w * chunkSize + i][j] = partial[i][j];
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            pool.shutdown();
            return result;
        }
        
        // worker calculates result for subset of rows in result matrix
        private class ParallelWorker implements Callable<int[][]> {
            
            private int startRow, endRow;
            
            ParallelWorker(int startRow, int endRow) {
                this.startRow = startRow;
                this.endRow = endRow;
            }
            
            @Override
            public int[][] call() {
                //
                int[][] result = new int[endRow - startRow][numColsB];
                for (int i = 0; i < endRow - startRow; i++) {
                    for (int k = 0; k < numColsB; k++) {
                        int sum = 0;
                        for (int j = 0; j < numColsA; j++) {
                            sum += matrixA[startRow + i][j] * matrixB[j][k];
                        }
                        result[i][k] = sum;
                    }
                }
                return result;
            }
            
        }
        
    }

}
