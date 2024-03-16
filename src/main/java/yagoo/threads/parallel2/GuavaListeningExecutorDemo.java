package yagoo.threads.parallel2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class GuavaListeningExecutorDemo {
    //
    static class Calculation implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            //
            int timeout = ThreadLocalRandom.current().nextInt(200, 1000);
            System.out.format("Thread %s is calculating for %d%n", Thread.currentThread().getName(), timeout);
            Thread.sleep(timeout);
            return timeout;
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //
        ExecutorService es = Executors.newCachedThreadPool();
        // Guava listening decorator for ExecutorService
        ListeningExecutorService listeningEs = MoreExecutors.listeningDecorator(es);
        // Listenable future
        List<ListenableFuture<Integer>> futuresList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            futuresList.add(listeningEs.submit(new Calculation()));
        }
        ListenableFuture<List<Integer>> result = Futures.allAsList(futuresList);
        result.addListener(() -> { 
            try {
                System.out.format("Total sum is: %s", result.get().stream().mapToLong(Integer::longValue).sum());
             } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }, listeningEs);
        //
        while (!result.isDone()) {
            System.out.format("%s doing some work while tasks are not finished%n", Thread.currentThread().getName());
            Thread.sleep(100);
        }
        //System.out.format("Total sum is: %s", result.get().stream().mapToLong(Integer::longValue).sum());
        listeningEs.shutdown();
    }

}
