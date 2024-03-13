package yagoo.threads.parallel2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {

    public static void main(String[] args) {
        //
        int units = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(units);
        for (int i = 0; i < 100; i++) pool.submit(new VegetableChopper());
        pool.shutdown();
    }
    
    static class VegetableChopper implements Runnable {

        @Override
        public void run() {
            //
            System.out.println(Thread.currentThread().getName() + " chopped a vegetable!");
        }
        
    }

}
