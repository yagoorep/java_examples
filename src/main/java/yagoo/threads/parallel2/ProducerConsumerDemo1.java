package yagoo.threads.parallel2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerDemo1 {

    public static void main(String[] args) {
        //
        BlockingQueue<String> servingLine = new ArrayBlockingQueue<>(5);
        new Thread(new SoupConsumer1(servingLine)).start();
        new Thread(new SoupProducer1(servingLine)).start();
    }

}

class SoupConsumer1 implements Runnable {
    //
    private BlockingQueue<String> servingLine;
    
    public SoupConsumer1(BlockingQueue<String> servingLine) {
        this.servingLine = servingLine;
    }
    
    //
    @Override
    public void run() {
        while (true) {
            try {
                String bowl = servingLine.take();
                System.out.format("Ate %s%n", bowl);
                Thread.sleep(300); // time to eat a bowl of soup
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}

class SoupProducer1 implements Runnable {
    //
    private BlockingQueue<String> servingLine;
    
    public SoupProducer1(BlockingQueue<String> servingLine) {
        this.servingLine = servingLine;
    }
    
    //
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) { // serve 20 bowls of soup
            try {
                servingLine.add("Bowl #" + i);
                System.out.format("Served Bowl #%d - remaining capacity: %d%n", i, servingLine.remainingCapacity());
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.format("iteration i = %d%n", i);
            }
        }
        System.out.format("Thread %s is finished%n", Thread.currentThread().getName());
    }
    
}
