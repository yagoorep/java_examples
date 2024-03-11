package yagoo.threads.example.execution;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyLock implements Lock {

	private volatile long threadID;
	private volatile boolean[] flag = new boolean[Thread.activeCount()];
	
	@Override
	public void lock() {
		int i = (int) (Thread.activeCount() - Thread.currentThread().getId() - 1);
		flag[i] = true;
		threadID = Thread.currentThread().getId();
		while (flag[i] && threadID == Thread.currentThread().getId()) { /* NOP */};
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unlock() {
		int i = (int) (Thread.activeCount() - Thread.currentThread().getId() - 1);
		flag[i] = false;
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
