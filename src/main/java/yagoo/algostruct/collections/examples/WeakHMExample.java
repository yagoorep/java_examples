package yagoo.algostruct.collections.examples;

import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakHMExample {

	public static void main(String[] args) {
		//
		Map<Date, String> map = new WeakHashMap<>();
		Date now = new Date();
		map.put(now, "current");
		now = null;
		System.gc(); // Call garbage collector
		
		for (int i = 1; i < 10000; i++) {
			if (map.isEmpty()) {
				System.out.println("Empty!");
				break;
			}
			else {
				System.out.println(i);
			}
		}
	}

}
