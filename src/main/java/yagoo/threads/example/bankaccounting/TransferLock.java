package yagoo.threads.example.bankaccounting;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class InsufficientFoundsException extends Exception {
    
	private static final long serialVersionUID = 4862367435925186455L;
	
	public InsufficientFoundsException(String message) {
		super(message);
	}
}

public class TransferLock {

	private static final Logger LOG = Logger.getLogger(TransferLock.class.getName());
	private static final TimeUnit TU = TimeUnit.MILLISECONDS;
	private static final long TIME = 500;
	static {
		LOG.setLevel(Level.ALL);
	}
	
	public static void main(String[] args) {
		final Account a = new Account(1000, "A");
		final Account b = new Account(2000, "B");
		Account.balance(a, b);
		
		// New thread transfer 
		Thread t = new Thread(
			() -> {
				try {
					transfer(a, b, 500);
				} catch (InsufficientFoundsException e) {
					LOG.severe(e.getMessage());
					return;
				}
			}
		);
		t.start();
		
		// Main thread transfer
		try {
			transfer(b, a, 300);
		} catch (InsufficientFoundsException e) {
			LOG.severe(e.getMessage());
			return;
		}
		
		try {
			t.join();
		} catch (InterruptedException e) { /* NOP */ }
		
		Account.balance(a, b);
	}

	static void transfer(Account from, Account to, int amount) throws InsufficientFoundsException {
		boolean transfered = false;
		try {
			while (!transfered) {
				if (from.lock.tryLock(TIME, TU)) {
					try {
						if (from.getBalance() < amount) {
							throw new InsufficientFoundsException(String.format("Not enough money on %s: %d < %d", from.getId(), from.getBalance(), amount));
						}
						LOG.info(String.format("%s locked account --> %s", Thread.currentThread().getName(), from.getId()));
						
						// sleep
						Thread.sleep(1000);
						
						// lock to
						if (to.lock.tryLock(TIME, TU)) {
							try {
								LOG.info(String.format("%s locked account --> %s", Thread.currentThread().getName(), to.getId()));
								// operations
								from.withdraw(amount);
								to.deposit(amount);
							} finally {
								to.lock.unlock();
								transfered = true;
								LOG.info(String.format("%s unlocked account --> %s", Thread.currentThread().getName(), to.getId()));
							}
						}
						else {
							to.getFailLock().incrementAndGet();
						}
					} finally {
						from.lock.unlock();
						LOG.info(String.format("%s unlocked account --> %s", Thread.currentThread().getName(), from.getId()));
					}
				}
				else {
					from.getFailLock().incrementAndGet();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		LOG.info(String.format("%s [ %s: %d | %s: %d ] -> -%d", Thread.currentThread().getName(), from.getId(), from.getBalance(), to.getId(), to.getBalance(), amount));
	}
	
}
