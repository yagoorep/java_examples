package yagoo.threads.parallel2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class HungryPerson2 extends Thread {

    private int personID;
    private static int SERVINGS = 11;
    private static Lock SLOW_COOKER_LID = new ReentrantLock();
    private static Condition SOUP_TAKEN = SLOW_COOKER_LID.newCondition();
    
    public HungryPerson2(int personID) {
        this.personID = personID;
    }
    
    public void run() {
        while (SERVINGS > 0) {
            SLOW_COOKER_LID.lock();
            try {
                while ((personID != SERVINGS % 5) && SERVINGS > 0) { // check if it's not your turn
                    System.out.format("Person %d checked... then put the lid back.\n", personID);
                    SOUP_TAKEN.await();
                }
                if (SERVINGS > 0) {
                    SERVINGS--; // it's your turn - take some soup!
                    System.out.format("--> Person %d took some soup! Servings left: %d\n", personID, SERVINGS);
                    SOUP_TAKEN.signalAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SLOW_COOKER_LID.unlock();
            }
        }
    }
}

public class ConditionVariableDemo2 {

    public static void main(String args[]) {
        //
        for (int i = 0; i < 5; i++) {
            new HungryPerson2(i).start();
        }
    }

}
