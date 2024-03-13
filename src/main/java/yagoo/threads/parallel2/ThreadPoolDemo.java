package yagoo.threads.parallel2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {

    public static void main(String[] args) {
        //
        int pUnits = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(pUnits);
        for (int i = 0; i < 100; i++) pool.submit(new VegetableChopper(i));
        pool.shutdown();
    }
    
    static class VegetableChopper implements Runnable {
        
        private int chopperId;
        
        public VegetableChopper(int i) {
            this.chopperId = i;
        }
        
        @Override
        public void run() {
            //
            System.out.format("%s chopped a vegetable number %d!%n", Thread.currentThread().getName(), chopperId);
        }
        
    }

}
