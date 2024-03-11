package yagoo.algostruct.collections.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadingList {
	
	static class Task implements Callable<Long> {
		private int startRange;
		private int endRange;
		private List<Integer> list;
		private CountDownLatch latch;
		
		public Task(int startRange, int endRange, List<Integer> list, CountDownLatch latch) {
			this.startRange = startRange;
			this.endRange = endRange;
			this.list = list;
			this.latch = latch;
		}
		
		@Override
		public Long call() throws Exception {
			latch.await();
			Long startTime = System.nanoTime();
			for (int i = startRange; i < endRange; i++) {
				list.get(i);
				//if (i % 9 == 0) list.add(-1);
			}
			return System.nanoTime() - startTime;
		}
	}

	public static void main(String[] args) {
		//
		List<Integer> sl = Collections.synchronizedList(new ArrayList<Integer>(100));
		List<Integer> cl = new CopyOnWriteArrayList<>();
		fillList(sl, 100);
		fillList(cl, 100);
		
		System.out.println("Collections.synchronizedList:");
		checkList(sl);
		
		System.out.println("CopyOnWriteArrayList:");
		checkList(cl);
	}
	
	public static void fillList(List<Integer> list, int i) {
		for (int k = 1; k <= i; k++) {
			list.add(k);
		}
	}
	
	public static void checkList(List<Integer> list) {
		CountDownLatch latch = new CountDownLatch(1);
		ExecutorService es = Executors.newFixedThreadPool(2);
		Future<Long> f1 = es.submit(new Task(0, 50, list, latch));
		Future<Long> f2 = es.submit(new Task(50, 100, list, latch));
		latch.countDown();
		try {
			System.out.printf("Thread 1: %d\n", f1.get()/1000);
			System.out.printf("Thread 2: %d\n", f2.get()/1000);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		es.shutdown();
	}
	
}
