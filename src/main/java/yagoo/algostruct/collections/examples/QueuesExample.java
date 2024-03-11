package yagoo.algostruct.collections.examples;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueuesExample {

	public static void main(String[] args) {
		//
		Queue<Integer> q = new LinkedList<>();
		for (int i = 5; i > 0; i--) q.add(i);
		System.out.println(q);
		while (!q.isEmpty()) System.out.printf("--> %d ", q.poll());
		
		System.out.println();
		
		Queue<Integer> pq = new PriorityQueue<>();
		for (int i = 5; i > 0; i--) pq.add(i);
		System.out.println(pq);
		while (!pq.isEmpty()) System.out.printf("--> %d ", pq.poll());
		
		System.out.println();
		
		Comparator<Integer> cmp = new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				if ((o1 % 2) != 0 && (o2 % 2) == 0) {
					return 1;
				}
				if ((o1 % 2) == 0 && (o2 % 2) != 0) {
					return -1;
				}
				if ((o1 % 2) == 0 && (o2 % 2) == 0) {
					return (o1 == o2) ? 0 : (o1 < o2) ? -1 : 1;
				}
				else {
					return (o1 == o2) ? 0 : (o1 < o2) ? -1 : 1;
				}
			}
		};
		
		Queue<Integer> pq2 = new PriorityQueue<>(cmp);
		pq2.add(5);
		pq2.add(2);
		pq2.add(1);
		pq2.add(4);
		while (!pq2.isEmpty()) System.out.printf("--> %d ", pq2.poll());
	}

}
