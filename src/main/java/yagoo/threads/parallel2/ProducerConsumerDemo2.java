package yagoo.threads.parallel2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerDemo2 {

    public static void main(String[] args) {
        //
        BlockingQueue<String> servingLine = new ArrayBlockingQueue<>(5);
        for (int i = 2; i > 0; i--) new Thread(new SoupConsumer2(servingLine)).start();
        new Thread(new SoupProducer2(servingLine)).start();
    }

}

class SoupConsumer2 implements Runnable {
    //
    private BlockingQueue<String> servingLine;
    
    public SoupConsumer2(BlockingQueue<String> servingLine) {
        this.servingLine = servingLine;
    }
    
    //
    @Override
    public void run() {
        //
        while (true) {
            try {
                String bowl = servingLine.take();
                if (bowl == "no soup") break;
                System.out.format("Ate %s%n", bowl);
                Thread.sleep(300); // time to eat a bowl of soup
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.format("Stop consumer: %s%n", Thread.currentThread().getName());
    }

}

class SoupProducer2 implements Runnable {
    //
    private BlockingQueue<String> servingLine;
    
    public SoupProducer2(BlockingQueue<String> servingLine) {
        this.servingLine = servingLine;
    }
    
    //
    @Override
    public void run() {
        //
        for (int i = 0; i < 20; i++) { // serve 20 bowls of soup
            try {
                servingLine.add("Bowl #" + i);
                System.out.format("Served Bowl #%d - remaining capacity: %d%n", i, servingLine.remainingCapacity());
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 2; i > 0; i--) servingLine.add("no soup");
    }
    
}