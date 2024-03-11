package yagoo.threads.example.bankaccounting;

public class TransferSynch {

	public static void main(String[] args) throws InterruptedException {
	    
		// Accounts
		final Account a = new Account(1000, "A");
		final Account b = new Account(2000, "B");
		Account.balance(a, b);
		
		Thread th1 = new Thread(() -> transfer(a, b, 500), "t1");
		Thread th2 = new Thread(() -> transfer(b, a, 100), "t2");
		th1.start();
		th2.start();
		transfer(b, a, 300);
		th1.join();
		th2.join();
		Account.balance(a, b);
	}
	
	public static void transfer(Account from, Account to, int amount) {
		boolean transfered = false;
		while(!transfered) {
			if (!from.prepared && !to.prepared) {
				synchronized(from) {
					from.prepared = to.prepared = true;
					/*if (from.getBalance() < amount) {
						throw new InsufficientFoundsException(String.format("Not enough money on %s: %d < %d", from.getId(), from.getBalance(), amount));
					}*/
					System.out.printf("%s synchronized account: %s\n", Thread.currentThread().getName(), from.getId());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) { /* NOP */ }
					synchronized(to) {
						System.out.printf("%s synchronized account: %s\n", Thread.currentThread().getName(), to.getId());
						from.withdraw(amount);
						to.deposit(amount);
						transfered = true;
					}
					from.prepared = to.prepared = false;
				}
			} else {
				from.getFailLock().incrementAndGet();
			}
		}
	}

}
