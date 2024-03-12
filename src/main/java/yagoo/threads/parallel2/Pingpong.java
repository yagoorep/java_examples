package yagoo.threads.parallel2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pingpong {
    
    public static void main(String[] args) {
        Ball ball = new Ball(7, new ReentrantLock());
        Player p1 = new Player("Mich", ball);
        Player p2 = new Player("Yagoo", ball);
        System.out.println(p1);
        System.out.println(p2);
        System.out.println("- - - - -");
        new Thread(p1).start();
        new Thread(p2).start();
    }

}

class Player implements Runnable {
    
    private static AtomicInteger NUMBER = new AtomicInteger(0);
    private String person;
    private Ball ball;
    private int id;
    
    public Player(String person, Ball ball) {
        this.person = person;
        this.ball = ball;
        this.id = NUMBER.getAndIncrement();
    }

    @Override
    public void run() {
        //
        while (ball.getParty() > 0) {
            //
            ball.getLock().lock();
            try {
                while (ball.getParty() % 2 != id && ball.getParty() > 0) {
                    System.out.format("%s waiting ball%n", person);
                    ball.getCondition().await();
                }
                if (ball.getParty() > 0) {
                    ball.hit(person);
                    ball.getCondition().signal();
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ball.getLock().unlock();
            }
        }
        System.out.format("Person %s finish game%n", person);
    }

    @Override
    public String toString() {
        return "Player [person=" + person + ", ball=" + ball + ", id=" + id + "]";
    }
    
    
}

class Ball {
    //
    private int times;
    private Lock lock;
    private Condition condition;
    private String person;
    
    public Ball(int party, Lock lock) {
        if (party < 1) throw new RuntimeException("Parties cannot be less than 1");
        this.times = party;
        this.lock = lock;
        this.condition = lock.newCondition();
    }

    public int getParty() {
        return times;
    }

    public void setParty(int party) {
        if (party < 0) throw new RuntimeException("Parties cannot be negative");
        this.times = party;
    }

    public Condition getCondition() {
        return condition;
    }
    
    public Lock getLock() {
        return lock;
    }
    
    public String getPerson() {
        return person;
    }
    
    public void hit(String person) {
        this.person = person;
        System.out.format("%s -> hits the ball times: %d%n", person, times);
        times--;
    }
    
}
