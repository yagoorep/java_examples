package yagoo.threads.example.execution;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskMain {

	static Boolean result = false;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//
		Runnable r = () -> {try {
								Thread.sleep(3000L);
							} catch (Exception e) { /* NOP */ }
							System.out.println("calculate");
							result = true;
							};
		
		FutureTask<Boolean> ft = new FutureTask<Boolean>(r, result);
		new Thread(ft).start();
		while (!ft.isDone()) System.out.println("working");
		System.out.println(ft.get());
	}
	
}
