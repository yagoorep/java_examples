package yagoo.threads.parallel2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class HungryPerson1 extends Thread {
    
    private int personID;
    private static int SERVINGS = 11;
    private static Lock SLOW_COOKER_LID = new ReentrantLock();
    
    public HungryPerson1(int personID) {
        this.personID = personID;
    }
    
    public void run() {
        while (SERVINGS > 0) {
            SLOW_COOKER_LID.lock();
            try {
                if ((personID == SERVINGS % 2) && SERVINGS > 0) { // check if it's your turn
                    SERVINGS--; // it's your turn - take some soup!
                    System.out.format("--> Person %d took some soup! Servings left: %d\n", personID, SERVINGS);
                } else {  // not your turn - put the lid back
                    System.out.format("Person %d checked... then put the lid back.\n", personID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SLOW_COOKER_LID.unlock();
            }
        }
    }
}

public class ConditionVariableDemo1 {

    public static void main(String[] args) {
        //
        for (int i = 0; i < 2; i++) {
            new HungryPerson1(i).start();
        }
    }

}
