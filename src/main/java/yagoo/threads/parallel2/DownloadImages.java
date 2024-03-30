package yagoo.threads.parallel2;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.net.URI;

public class DownloadImages {
    // Options
    final static int NUM_EVAL_RUNS = 3; // Rounds
    final static boolean VERBOSE = true; // Verbose output
    
    // Evaluate performance of sequential and parallel implementations
    public static void main(String[] args) {
        //
        final int[] IMAGE_NUMS = IntStream.rangeClosed(1, 200).toArray();
        if (VERBOSE) System.out.println(Arrays.toString(IMAGE_NUMS));
        
        // Sequential
        System.out.println("Evaluating Sequential Implementation...");
        SequentialImageDownloader sid = new SequentialImageDownloader(IMAGE_NUMS);
        int sequentialResult = sid.downloadAll();
        double sequentialTime = 0D;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            sid.downloadAll();
            sequentialTime = System.currentTimeMillis() - start;
        }
        sequentialTime /= NUM_EVAL_RUNS;
        
        // Parallel
        System.out.println("Evaluating Parallel Implementation...");
        ParallelImageDownloader pid = new ParallelImageDownloader(IMAGE_NUMS);
        int parallelResult = pid.downloadAll();
        double parallelTime = 0D;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            pid.downloadAll();
            parallelTime = System.currentTimeMillis() - start;
        }
        parallelTime /= NUM_EVAL_RUNS;
        
        // Display sequential and parallel results for comparison
        if (sequentialResult != parallelResult)
            throw new Error("ERROR: sequentialResult and parallelResult do not match!");
        System.out.format("Average Sequential Time: %.1f ms%n", sequentialTime);
        System.out.format("Average Parallel Time: %.1f ms%n", parallelTime);
        System.out.format("Speedup: %.2f %n", sequentialTime / parallelTime);
        System.out.format("Efficiency: %.2f%%%n", 100 * (sequentialTime / parallelTime) / Runtime.getRuntime().availableProcessors());
    }
    
    static class SequentialImageDownloader {
        
        private int[] imgNumbers;
        
        SequentialImageDownloader(int[] imgNumbers) {
            this.imgNumbers = imgNumbers;
        }
        
        // Returns total bytes from downloading all images in imageNumbers array
        public int downloadAll() {
            int result = 0;
            for (int i : imgNumbers) result += downloadImage(i);
            return result;
        }
        
        // Returns total bytes from downloading all images in imageNumbers array
        private int downloadImage(int imgNum) {
            imgNum = (Math.abs(imgNum) % 50) + 1; // force number between 1 and 50
            int totalBytes = 0, bytesRead = 0;
            try {
                URL photoURL = URI.create(String.format("http://699340.youcanlearnit.net/image%03d.jpg", imgNum)).toURL();
                BufferedInputStream bis = new BufferedInputStream(photoURL.openStream());
                byte[] buffer = new byte[1024];
                while ((bytesRead = bis.read(buffer, 0, 1024)) != -1) {
                    totalBytes += bytesRead;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return totalBytes;
        }
        
    }
    
    static class ParallelImageDownloader {
        
        private int[] imgNumbers;
        
        ParallelImageDownloader(int[] imgNumbers) {
            this.imgNumbers = imgNumbers;
        }
        
        // Returns total bytes from downloading all images in imageNumbers array
        public int downloadAll() {
            // Create threadpool
            int numWorkers = Runtime.getRuntime().availableProcessors();
            ExecutorService pool = Executors.newFixedThreadPool(numWorkers);
            
            // Submit callable futures to threadpool
            List<Future<Integer>> futures = new ArrayList<>();
            for (final int imgNum : imgNumbers) {
                futures.add(pool.submit(() -> { return downloadImage(imgNum); }));
            }
            
            // Retrieve and accumulate results from futures
            int result = 0;
            try {
                for (Future<Integer> f: futures) result += f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            pool.shutdown();
            return result;
        }
        
        // Returns total bytes from downloading all images in imageNumbers array
        private int downloadImage(int imgNum) {
            imgNum = (Math.abs(imgNum) % 50) + 1; // force number between 1 and 50
            int totalBytes = 0, bytesRead = 0;
            try {
                URL photoURL = URI.create(String.format("http://699340.youcanlearnit.net/image%03d.jpg", imgNum)).toURL();
                BufferedInputStream bis = new BufferedInputStream(photoURL.openStream());
                byte[] buffer = new byte[1024];
                while ((bytesRead = bis.read(buffer, 0, 1024)) != -1) {
                    totalBytes += bytesRead;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return totalBytes;
        }
        
    }

}
