package yagoo.threads.parallel2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class FutureDemo {
    //
    static class HowManyVegetables implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            //
            System.out.println("Olivia is counting vegetables...");
            int timeout = ThreadLocalRandom.current().nextInt(3000, 5000);
            Thread.sleep(timeout);
            return timeout;
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //
        System.out.println("Barron asks Olivia how many vegetables are in the pantry.");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        var result = executor.submit(new HowManyVegetables());
        int number = 0;
        while (!result.isCancelled() && !result.isDone()) {
            System.out.println("Barron can do other things while he waits for the result... Thing number " + ++number);
            Thread.sleep(250);
        }
        System.out.println("Olivia responded with " + result.get());
        executor.shutdown();
    }

}
