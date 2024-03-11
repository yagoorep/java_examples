package yagoo.threads.example.execution;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownMain {

	public static void main(String[] args) throws InterruptedException {
	    //
		CountDownLatch latch = new CountDownLatch(2);
		
		// Thread implementation
		Runnable r = () -> {
			for (int i = 0; i < 10; i++) {
				System.out.printf("%d:My name is: %s\n", i, Thread.currentThread().getName());
				try {
					latch.await();
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					System.err.printf("Thread %s is interrupted\n", Thread.currentThread().getName());
					return;
				}
			}
		};
		
		new Thread(r, "A").start();
		new Thread(r, "B").start();
		new Thread(r, "C").start();
		new Thread(r, "D").start();
		new Thread(r, "E").start();
		new Thread(r, "F").start();
		latch.countDown();
		Thread.sleep(2000);
		latch.countDown();
		
	}

}
