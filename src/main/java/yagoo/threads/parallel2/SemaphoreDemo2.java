package yagoo.threads.parallel2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class SemaphoreDemo2 {

    public static void main(String[] args) {
        //
        Semaphore charger = new Semaphore(1);
        for (int i = 0; i < 10; i++) new Thread(new CellPhone(charger, "Phone-" + i)).start();
    }
    
    static class CellPhone implements Runnable {
        //
        private Semaphore charger;
        private String name;
        
        public CellPhone(Semaphore charger, String name) {
            this.charger = charger;
            this.name = name;
        }
        
        @Override
        public void run() {
            //
            try {
                charger.acquire();
                int timeout = ThreadLocalRandom.current().nextInt(1000, 2000);
                System.out.format("%s is charging for %d...%n", getName(), timeout);
                Thread.sleep(timeout);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println(getName() + " is done charging!");
                charger.release();
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
    }

}
