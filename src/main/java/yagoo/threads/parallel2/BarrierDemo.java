package yagoo.threads.parallel2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierDemo {

    public static void main(String[] args) throws InterruptedException {
        //
        // create 10 shoppers: Barron-0...4 and Olivia-0...4
        int number = 10;
        Thread[] shoppers = new Thread[10];
        for (int i = 0; i < number / 2; i++) {
            Shopper s1 = new Shopper("Barron-" + i);
            shoppers[2 * i] = new Thread(s1);
            Shopper s2 = new Shopper("Olivia-" + i);
            shoppers[2 * i + 1] = new Thread(s2);
            System.out.format(" %s - %s%n", s1, s2);
        }
        for (var s : shoppers) s.start();
        for (var s : shoppers) s.join();
        System.out.println("We need to buy " + Shopper.BAGS_OF_CHIPS + " bags of chips.");
    }
    
    static class Shopper implements Runnable {
        //
        public static int BAGS_OF_CHIPS = 1; // start with one on the list
        private static Lock PENCIL = new ReentrantLock();
        private static CyclicBarrier FIRST_BUMP = new CyclicBarrier(10);
        
        private String name;
        
        public Shopper(String name) {
            this.name = name;
        }
        
        //
        @Override
        public void run() {
            //
            if (getName().contains("Olivia")) {
                //
                PENCIL.lock();
                try {
                    BAGS_OF_CHIPS += 3;
                    System.out.println(this.getName() + " ADDED three bags of chips.");
                } finally {
                    PENCIL.unlock();
                }
                // Synchronization point
                try {
                    FIRST_BUMP.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            } else { // "Barron"
                // Synchronization point
                try {
                    FIRST_BUMP.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                //
                PENCIL.lock();
                try {
                    BAGS_OF_CHIPS *= 2;
                    System.out.println(this.getName() + " DOUBLED the bags of chips.");
                } finally {
                    PENCIL.unlock();
                }
            }
        }
        
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Shopper [name=" + name + "]";
        }
    }

}
