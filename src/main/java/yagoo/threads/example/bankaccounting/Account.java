package yagoo.threads.example.bankaccounting;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    
	private volatile int balance;
	private final String id;
	public Lock lock;
	public boolean prepared;
	private AtomicInteger failLock = new AtomicInteger(0);
	
	public Account(int balance, String id) {
		this.balance = balance;
		this.id = id;
		lock = new ReentrantLock();
	}
	
	static final void balance(Account ... accounts) {
		for (Account acc : accounts)
			System.out.printf("%s -> balance: %d -> fails: %d%n", acc.getId(), acc.getBalance(), acc.getFailLock().get());
	}
	
	public void withdraw(int amount) {
		balance -= amount;
	}
	
	public void deposit(int amount) {
		balance += amount;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public String getId() {
		return id;
	}
	
	public AtomicInteger getFailLock() {
		return failLock;
	}
	
}
