package yagoo.threads.example.execution;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrierMain {

	public static void main(String[] args) {
		//
		CountDownLatch latch = new CountDownLatch(1);
		CyclicBarrier barrier = new CyclicBarrier(3);
		AtomicInteger amount = new AtomicInteger(0);
		
		Runnable r = () -> {
			try {
				latch.await();
			} catch (InterruptedException e) {
				System.err.println("Interrupted...");
			}
			for (int i = 0; ; i++) {
				try {
					barrier.await();
				} catch (InterruptedException e) {
					break;
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				for (int k = 0; k < 3; k++) System.out.printf("%s count is: %d\n", Thread.currentThread().getName(), i);
				amount.incrementAndGet();
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					break;
				}
			}
		};
		
		Thread[] th = {new Thread(r, "Aa"), new Thread(r, "Bb"), new Thread(r, "Cc"), new Thread(r, "Dd"), new Thread(r, "Ee")};
		for (Thread t: th) t.start();
		latch.countDown();
		
		while (amount.get() < 1000) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (Thread t: th) t.interrupt();
		
		for (Thread t: th) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
