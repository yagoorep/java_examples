package yagoo.threads.example.bankaccounting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class TransferCall implements Callable<Boolean> {
	
	private Account from;
	private Account to;
	private int amount;
	private static final TimeUnit TU = TimeUnit.MILLISECONDS;
	private static final long TIME = 500;

	public TransferCall(Account from, Account to, int amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}
	
	@Override
	public Boolean call() throws Exception {
		// 
		if (//!from.prepared && !to.prepared && 
			from.lock.tryLock(TIME, TU)) {
			try {
				//from.prepared = to.prepared = true;
				if (from.getBalance() < amount) {
					throw new InsufficientFoundsException(String.format("Not enough money on %s: %d < %d", from.getId(), from.getBalance(), amount));
				}
				
				//Thread.sleep(1000);
				
				if (to.lock.tryLock(TIME, TU)) {
					try {
						from.withdraw(amount);
						to.deposit(amount);
						System.out.printf("%s: %s -> %s -> %d\n", Thread.currentThread().getName(), from.getId(), to.getId(), amount);
						Thread.sleep(1000);
						return true;
					} finally {
						to.lock.unlock();
					}
					
				}
				else {
					to.getFailLock().incrementAndGet();
					return false;
				}
			} finally {
				//from.prepared = to.prepared = false;
				from.lock.unlock();
			}
		}
		else {
			from.getFailLock().incrementAndGet();
			return false;
		}
	}

	public Account getFrom() {
		return from;
	}

	public Account getTo() {
		return to;
	}

	public int getAmount() {
		return amount;
	}
}


public class TransferCallable {

	public static void main(String[] args) {
		final Account a = new Account(1000, "A");
		final Account b = new Account(2000, "B");
		Random rnd = new Random();
		
		Account.balance(a, b);
		
		List<Callable<Boolean>> tasks = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			tasks.add(new TransferCall(a, b, rnd.nextInt(400)));
			tasks.add(new TransferCall(b, a, rnd.nextInt(400)));
		}
		
		ExecutorService es = Executors.newFixedThreadPool(3);
		List<Future<Boolean>> futures = null;
		try {
			futures = es.invokeAll(tasks);
		} catch (InterruptedException e) { /* NOP */ }
		
		es.shutdown();
		
		for (int i = 0; i < futures.size(); i++) {
			Future<Boolean> f = futures.get(i);
			TransferCall tc = (TransferCall) tasks.get(i);
			try {
				System.out.printf("from %s to %s amount %d result %b\n", tc.getFrom().getId(), tc.getTo().getId(), tc.getAmount(), f.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		Account.balance(a, b);
	}

}
