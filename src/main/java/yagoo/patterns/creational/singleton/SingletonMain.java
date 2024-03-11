package yagoo.patterns.creational.singleton;

// Lazy singleton with double check
final class LazySingletonDoubleCheck {
	
	private static volatile LazySingletonDoubleCheck INSTANCE;
	private String name;
	
	private LazySingletonDoubleCheck() { /* Private constructor */ }
	
	public static LazySingletonDoubleCheck getInstance() {
		if (INSTANCE == null) {
			synchronized (LazySingletonDoubleCheck.class) {
				if (INSTANCE == null) {
					INSTANCE = new LazySingletonDoubleCheck();
				}
			}
		}
		return INSTANCE;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

// Lazy singleton via holder
final class LazySingletonHolder {
	
	private String name;
	
	private LazySingletonHolder() { /* Private constructor */ }
	
	private static class InstanceHolder {
		private static final LazySingletonHolder INSTANCE = new LazySingletonHolder();
	}
	
	public static LazySingletonHolder getInstance() {
		return InstanceHolder.INSTANCE;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

public class SingletonMain {

	private static LazySingletonDoubleCheck sl = LazySingletonDoubleCheck.getInstance();
	private static LazySingletonHolder sh = LazySingletonHolder.getInstance();
	
	public static void main(String[] args) throws InterruptedException {
		sl.setName("LazySingletonDoubleCheck: YAGOO");
		sh.setName("LazySingletonHolder: YAGOO");
		System.out.println(Thread.currentThread().getName() + " : " + sl.getName());
		System.out.println(Thread.currentThread().getName() + " : " + sh.getName());
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				sl = LazySingletonDoubleCheck.getInstance();
				if (Thread.currentThread().getName().equalsIgnoreCase("thread-c")) {
					sl.setName("LazySingletonDoubleCheck: MICH");
					sh.setName("LazySingletonHolder: MICH");
				}
				System.out.println(Thread.currentThread().getName() + " : " + sl.getName());
				System.out.println(Thread.currentThread().getName() + " : " + sh.getName());
			}
		};
		
		Thread t1 = new Thread(r, "Thread-A");
		Thread t2 = new Thread(r, "Thread-B");
		Thread t3 = new Thread(r, "Thread-C");
		Thread t4 = new Thread(r, "Thread-D");
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		System.out.println(Thread.currentThread().getName() + " : " + sl.getName());
		System.out.println(Thread.currentThread().getName() + " : " + sh.getName());
	}

}
