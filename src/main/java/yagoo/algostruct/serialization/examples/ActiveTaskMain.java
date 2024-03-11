package yagoo.algostruct.serialization.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

class ActiveTask implements Serializable, Runnable {
	//
	private static final long serialVersionUID = -908860324704506284L;
	private boolean run;
	private int count;
	private long rate;
	private String message;
	private transient Thread thread;
	
	//
	public ActiveTask(int count, long rate, String message) {
		this.run = false;
		this.count = count;
		this.rate = rate;
		this.message = message;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public void stop () {
		this.run = false;
	}
	
	private synchronized void pause() {
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void start() {
		this.run = true;
		this.notify();
	}
	
	@Override
	public void run() {
		synchronized (this) {
			for (int i = count; i > 0; i--) {
				if (!run) pause();
				System.out.println(message);
				try {
					TimeUnit.SECONDS.sleep(rate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void save(final File file) {
		stop();
		thread.interrupt();
		try (OutputStream output = new FileOutputStream(file); 
		        ObjectOutput objOut = new ObjectOutputStream(output);)
		{
			System.out.println(this.toString());
			objOut.writeObject(this);
			objOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ActiveTask load(final File file) {
		ActiveTask task = null;
		try (InputStream input = new FileInputStream(file);
		        ObjectInput objInput = new ObjectInputStream(input);)
		{
			task = (ActiveTask) objInput.readObject();
			task.thread = new Thread(task);
			task.thread.start();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(task.toString());
		return task;
	}
	
	@Override
	public String toString() {
		return String.format("%s [count: %d; message: %s; rate: %d; run: %b]%n", this.getClass().getName(), this.count, this.message, this.rate, this.run);
	}

}

public class ActiveTaskMain {

	public static void main(String[] args) {
		// Create new active task
		ActiveTask at = new ActiveTask(5, 1, "How're you?");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Start active task
		at.start();
		//
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Stop active task
		at.stop();
		// Save active task to file
		File file = new File(String.format("..%1$sActiveTask.bin", File.separator));
		at.save(file);
		// Load and start saved active task
		ActiveTask at2 = ActiveTask.load(file);
		at2.start();
	}

}
